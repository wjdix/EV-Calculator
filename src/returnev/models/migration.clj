(ns returnev.models.migration
  (:require [clojure.java.jdbc :as sql]))

(defn create-card-prices []
  (sql/with-connection (System/getenv "DATABASE_URL")
    (sql/create-table :card_prices
                      [:id :serial "PRIMARY KEY"]
                      [:price :real "NOT NULL"]
                      [:sanitized_card_name :varchar "NOT NULL"]
                      [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"])))

(defn -main []
  (print "Migrating database")
  (flush)
  (create-card-prices)
  (println "DONE"))
