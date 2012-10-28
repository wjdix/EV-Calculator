(ns returnev.models.card_price
  (:require [clojure.java.jdbc :as sql]
            [clj-time.core :as time]
            [clj-time.local :as local]
            [clj-time.coerce :as coerce]))

(defmacro connected [op]
  `(sql/with-connection (System/getenv "DATABASE_URL")
     ~op))

(defn day-ago [] (time/minus (time/from-now (time/secs 0)) (time/hours 24)))

(defn price-for-card-today [card-name]
  (sql/with-connection (System/getenv "DATABASE_URL")
   (sql/with-query-results results
     ["select price from card_prices where sanitized_card_name = ? and created_at > ?"
      card-name
      (coerce/to-timestamp (day-ago))]
     (:price (first results)))))

(defn record-price-for-today [card-name price]
  (or (price-for-card-today card-name)
   (connected (sql/insert-values :card_prices [:sanitized_card_name :price] [card-name price]))))
