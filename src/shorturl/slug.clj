(ns shorturl.slug)

(defn generate-slug []
  (->> (repeatedly #(rand-nth "ABCDEFGHIJKLMNOPQRSTUVWXYZ"))
       (take 4)
       (apply str)))