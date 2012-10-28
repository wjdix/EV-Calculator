(ns returnev.card_price
  (:require [net.cgrand.enlive-html :as html]
            [returnev.models.card_price :as card-price]
            [clojure.string :as string]))

(defn- parse-money [money]
  (if (nil? money) 0
      (Float/parseFloat (clojure.string/replace money "$" ""))))

(def tcg-player-url "http://store.tcgplayer.com/magic/return-to-ravnica/")

(defn sanitize-name [card-name]
  (clojure.string/replace
    (clojure.string/replace (.toLowerCase card-name)
                            "'" "")
    #"\W" "-"))

(defn- url-for-card [card-name]
  (java.net.URL.
   (str tcg-player-url (sanitize-name card-name))))

(defn- price-from-web [card-name]
  (parse-money (first (:content (first (html/select (html/html-resource (url-for-card card-name)) [:td.avg]))))))

(defn avg-for-card [card-name] (or (card-price/price-for-card-today card-name) (price-from-web card-name)))

(defn enrich-card [card]
  (let [name (sanitize-name (:name card))
        price  (avg-for-card name)]
    (card-price/record-price-for-today name price)
    (assoc card :price price)))
