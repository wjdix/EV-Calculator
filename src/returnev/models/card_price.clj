(ns returnev.models.card_price
  (:require [clojure.java.jdbc :as sql]
            [clj-time.core :as time]
            [clj-time.local :as local]
            [returnev.models.shared_connection :as shared-conn]
            [clj-time.coerce :as coerce]))

(defmacro connected [op]
  `(sql/with-connection (shared-conn/db-connection)
     ~op))

(defn today []
  (coerce/to-timestamp (coerce/to-date (time/from-now (time/secs 0)))))

(defn price-for-card-today [card-name]
  (connected
   (sql/with-query-results results
     ["select price from card_prices where sanitized_card_name = ? and created_at::date = ?"
      card-name
      (today)]
     (:price (first results)))))

(defn record-price-for-today [card-name price]
  (or (price-for-card-today card-name)
   (connected (sql/insert-values :card_prices [:sanitized_card_name :price] [card-name price]))))
