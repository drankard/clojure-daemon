(ns clojure-daemon.core
  (:use [clojure.java.io :only [reader writer]]
        [server.socket :only [create-server]]
        [clojure.tools.macro :only (name-with-attributes)]
        [clojure.data.json])
  (:require [clojure.string :as string]))


(defmacro defcommand [command func]
  `(do
     (defn ~(symbol (str *ns* "/" command)) [] ~func)))


(defcommand "ping"
  "pong")


(def prompt ":>> ")

(defn- handle-client [in out]
  (binding [*in* (reader in)
            *out* (writer out)
            *err* (writer System/err)]    
    (print "\nWelcome.. ")
    (print prompt)
    (flush)   
    (loop [input (string/trim (read-line))]
      (prn "input-> " input)        
      (when-not (= "exit" input)          
        (do 
          (let [fn (str "(" *ns* "/" input ")")
                _ (prn fn)
                resp (load-string fn)]
            (prn "resp-> " resp))
          (.flush *err*)
          (print prompt)
          (flush)
          (recur (read-line)))))))


(defn start-server [port]
  (create-server (Integer. port) handle-client))
