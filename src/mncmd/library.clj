(ns mncmd.library
  (:import [java.nio.file Paths])
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as str]
            [mncmd.score :as score])
  (:gen-class))

(defn- create-db! [db]
  (try
    (io/make-parents (:subname db))
    (jdbc/db-do-commands db (jdbc/create-table-ddl :composers [[:id :integer :primary :key :autoincrement]
                                                               [:name "varchar(256)"]]))
    (jdbc/db-do-commands db (jdbc/create-table-ddl :scores [[:id :integer :primary :key :autoincrement]
                                                            [:composer_id :integer]
                                                            [:title "varchar(512)"]
                                                            [:path "varchar(2048)"]
                                                            ["FOREIGN KEY(composer_id) REFERENCES composers(id)"]]))
    (catch Exception e
      (println "Failed to initialized library db: " e))))

(defn- path->db [path]
  (let [db-path (.getCanonicalPath (io/file path ".mncmd-library.db"))]
    {:classname   "org.sqlite.JDBC"
     :subprotocol "sqlite"
     :subname     db-path}))

(defn- init-library! [path]
  (let [db (path->db path)]
    (create-db! db)))

(defn- mxl-extension [file-path]
  (let [split-parts (str/split file-path #"\.")
        orig-extension (last split-parts)]
    (if (= "mxl" orig-extension)
      ".mxl"
      ".musicxml")))

(defn- insert-composer [db composer]
  (let [last-insert (first (jdbc/insert! db :composers {:name composer}))]
    ((keyword "last_insert_rowid()") last-insert)))

(defn- insert-to-tables [db composer title lib-score-path]
  (let [existing-composer (first (jdbc/query db ["SELECT id FROM composers WHERE name = ?" composer]))
        composer-id (if (empty? existing-composer)
                      (insert-composer db composer)
                      (:id existing-composer))]
    (jdbc/insert! db :scores {:title title :composer_id composer-id :path lib-score-path})))

(defn- get-composer [score-attributes path]
  (let [composer-attr (::score/composer score-attributes)]
    (if (empty? composer-attr)
      (do (println "Give composer for" path ": ")
          (flush)
          (read-line))
      composer-attr)))

(defn- get-title [score-attributes path]
  (let [title-attr (::score/title score-attributes)]
    (if (empty? title-attr)
      (do (println "Give title for" path ": ")
          (flush)
          (read-line))
      title-attr)))

(defn- add-to-lib [lib-path score-path]
  (let [attributes (-> score-path score/read-score score/score-attributes)
        composer (get-composer attributes score-path)
        title (get-title attributes score-path)
        lib-score-path (.getCanonicalPath (io/file lib-path composer (str title (mxl-extension score-path))))
        db (path->db lib-path)]
    (try
      (io/make-parents lib-score-path)
      (io/copy (io/file score-path) (io/file lib-score-path))
      (insert-to-tables db composer title lib-score-path)
      (catch Exception e
        (println "Failed to add " score-path " to library " lib-path " due to:" e)))))

(defn- remove-from-lib [lib-path score-path]
  (let [db (path->db lib-path)
        path-obj (Paths/get score-path (into-array [""]))
        abs-score-path (.toAbsolutePath path-obj)]
    (try
      (jdbc/delete! db :scores ["path = ?" abs-score-path])
      (io/delete-file score-path)
      (catch Exception e
        (println "Failed to remove " abs-score-path " from library " lib-path " due to:" e)))))

(defn create-library [args]
  (let [path (first (:_arguments args))]
    (init-library! path)))

(defn add-to-library [args]
  (let [lib-path (first (:_arguments args))
        score-paths (rest (:_arguments args))]
    (run! #(add-to-lib lib-path %) score-paths)))

(defn remove-from-library [args]
  (let [lib-path (first (:_arguments args))
        score-paths (rest (:_arguments args))]
    (run! #(remove-from-lib lib-path %) score-paths)))
