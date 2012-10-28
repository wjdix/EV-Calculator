(ns returnev.core
  (:use [returnev.group_by_two :as group]
        [returnev.ev_calculator :as calc]
        [net.cgrand.enlive-html :as html]
        [compojure.core :only [defroutes GET]]
        [ring.middleware.stacktrace :as st])
  (:require [ring.adapter.jetty :as ring]))

(def rtr-url
  (java.net.URL.
   "http://gatherer.wizards.com/Pages/Search/Default.aspx?output=checklist&action=advanced&set=%5b%22Return+to+Ravnica%22%5d"))

(def all-rtr-cards
  (map (fn [[card rarity]] {:name  (first (:content (first (:content card)))) :rarity  (first (:content rarity))})
       (group/inGroupsOf2
        (html/select (html/html-resource rtr-url) [:tr.cardItem #{:td.name :td.rarity}]))))

(defroutes routes
  (GET "/" [] (str "<h1>" (calc/ev-for-pack all-rtr-cards) "</h1>")))

(def app
  (-> (var routes)
      (st/wrap-stacktrace)))

(defn start [port]
  (ring/run-jetty #'app {:port (or port 8080) :join? false}))

(defn -main []
  (let [port (Integer. (System/getenv "PORT"))]
    (start port)))
