(ns mncmd.library
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc])
  (:gen-class))

(defn- create-db! [db]
  (try
    (jdbc/db-do-commands db (jdbc/create-table-ddl :composers [[:id :integer :primary :key :autoincrement]
                                                               [:name "varchar(256)"]]))
    (jdbc/db-do-commands db (jdbc/create-table-ddl :scores [[:id :integer :primary :key :autoincrement]
                                                            [:composer_id :integer]
                                                            [:title "varchar(512)"]
                                                            [:path "varchar(2048)"]
                                                            ["FOREIGN KEY(composer_id) REFERENCES composers(id)"]]))
    (catch Exception e
      (println "Failed to initialized library db: " e))))

(defn init-library! [path]
  (let [db-path (.getCanonicalPath (io/file path ".mncmd-library.db"))
        db {:classname   "org.sqlite.JDBC"
            :subprotocol "sqlite"
            :subname     db-path}]
    (io/make-parents db-path)
    (create-db! db)))

(defn create-library [args]
  (let [path (first (:_arguments args))]
    (init-library! path)))
