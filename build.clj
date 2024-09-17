(ns build
  (:require [clojure.tools.build.api :as b]
            [clojure.java.shell :refer [sh]]))

(def project 'project/mncmd)
(def version "0.2.3")
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def uber-file (format "target/%s-%s-standalone.jar" (name project) version))
(def native-build-res-path "./native-build/")

(defn clean [_]
  (b/delete {:path "target"}))

(defn uberjar [_]
  (clean nil)
  (b/copy-dir {:src-dirs ["src"]
               :target-dir class-dir})
  (b/copy-dir {:src-dirs [native-build-res-path]
               :target-dir (str class-dir "/META-INF/native-image/mncmd")})
  (b/compile-clj {:basis basis
                  :src-dirs ["src"]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis basis
           :main 'mncmd.core}))

(defn- run-with-agent-lib [& mncmd-args]
  (let [args (into ["java" (str "-agentlib:native-image-agent=config-merge-dir=" native-build-res-path)
                    "-jar" uber-file]
                   mncmd-args)]
    (apply sh args)))

(defn graal-config-files
  "Requires java to be GraalVM"
  [_]
  (uberjar nil)
  (println (:out
            (run-with-agent-lib
             "stat" "./test/resources/multistaff_test_file.musicxml"
             "--counts" "--parts" "--ambitus" "--key-signature" "--time-signature" "--chroma" "--chroma-plot")))
  (println (:out
            (run-with-agent-lib
             "lib" "create" "./target/tmp-lib")))
  (println (:out
            (run-with-agent-lib
             "lib" "add" "./target/tmp-lib" "./test/resources/multistaff_test_file.musicxml")))

  (println (:out
            (run-with-agent-lib
             "lib" "remove" "./target/tmp-lib" "./target/tmp-lib/TestFile Composer/Multistaff test file.musicxml"))))

(defn native-image
  "Requires GraalVM native-image to be installed and in path."
  [_]
  (uberjar nil)
  (let [output (sh "native-image"
                   "--features=clj_easy.graal_build_time.InitClojureClasses"
                   "--initialize-at-build-time=org.wmn4j,org.slf4j,org.apache.commons.math3,org.clojars.rorokimdim"
                   "--initialize-at-build-time=potemkin__init,clj_tuple$hash_map,clj_tuple$vector"
                   "--initialize-at-run-time=clojure.java.jdbc__init"
                   "--no-fallback"
                   "--verbose"
                   "-jar" uber-file
                   "-H:+UnlockExperimentalVMOptions"
                   "-H:Name=./target/mncmd")]
    (println
     (:out output)
     (:err output))))
