(ns shorturl.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as r]
            [reitit.ring :as ring]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :refer [format-middleware]]
            [shorturl.db :as db]
            [shorturl.slug :refer [generate-slug]]))

(defn redirect [request]
  (let [slug (get-in request [:path-params :slug])
        url (db/get-url slug)]
    (if url
      (r/redirect url)
      (r/not-found "No URL found"))))

(defn create-redirect [request]
  (let [slug (generate-slug)
        url (get-in request [:body-params :url])]
    (db/add-redirect! slug url)
    (r/response "Redirect created")))

(def app (-> (ring/router
              ["/"
               [":slug/" {:get redirect}]
               ["api/"
                ["redirects/" {:post create-redirect}]]]
              {:data {:muuntaja m/instance
                      :middleware [format-middleware]}})
             ring/ring-handler))

(defn start-server []
  (jetty/run-jetty #'app {:port 4000 :join? false}))

(def server (start-server))

(.stop server)