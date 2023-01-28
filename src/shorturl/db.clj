(ns shorturl.db
  (:require [clojure.java.jdbc :as j]
            [honey.sql :as sql]
            [honey.sql.helpers :refer [select from insert-into columns values where] :as h]
            [shorturl.env :refer [env]]))

(def db {:dbtype "mysql"
         :host (env :HOST)
         :dbname (env :DATABASE)
         :user (env :USER)
         :password (env :PASSWORD)})

(defn query [q] (j/query db q))

(defn insert! [q] (j/db-do-prepared db q))

(defn add-redirect! [slug url]
  (insert! (-> (insert-into :redirects)
               (columns :slug :url)
               (values [[slug url]])
               (sql/format))))

(defn get-url [slug]
  (-> (query (-> (select :url)
                 (from :redirects)
                 (where [:= :slug slug])
                 (sql/format)))
      first
      :url))

(comment
  (query (-> (select :*)
             (from :redirects)
             (sql/format)))

  (insert! (-> (insert-into :redirects)
               (columns :slug :url)
               (values [["abc" "https://www.google.com"]])
               (sql/format)))

  (add-redirect! "oto" "https://chat.openai.com/chat")

  (get-url "abc"))

