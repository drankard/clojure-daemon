(ns clojure-daemon.test-daemon
  (:use clojure-daemon.core))


(defcommand "hello"
  (prn "Hello, World"))

(defn -main[& args]
  (start-server 5000))