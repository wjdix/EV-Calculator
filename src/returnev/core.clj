(ns returnev.core
  (:use [returnev.ev_calculator :as calc]
        [returnev.card_list :as card-list]
        [returnev.schedule :as schedule]
        [compojure.core :only [defroutes GET]]
        [ring.middleware.stacktrace :as st])
  (:require [ring.adapter.jetty :as ring]))


(defroutes routes
  (GET "/" [] (str "<h1>" (calc/ev-for-pack card-list/all-rtr-cards) "</h1>")))

(def app
  (-> (var routes)
      (st/wrap-stacktrace)))

(defn start [port]
  (ring/run-jetty #'app {:port (or port 8080) :join? false}))

(defn -main []
  (schedule/start)
  (let [port (Integer. (System/getenv "PORT"))]
    (start port)))
