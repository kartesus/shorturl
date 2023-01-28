(ns shorturl.env
  (:require [clojure.edn :as edn]))

(def vars (edn/read-string (slurp "env.edn")))
(defn env [key]
  (or (get vars key)
      (System/getenv (name key))))