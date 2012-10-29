(ns returnev.card_list
  (:use [[net.cgrand.enlive-html :as html]
         [returnev.group_by_two :as group]]))

(def rtr-url
  (java.net.URL.
   "http://gatherer.wizards.com/Pages/Search/Default.aspx?output=checklist&action=advanced&set=%5b%22Return+to+Ravnica%22%5d"))

(def all-rtr-cards
  (map (fn [[card rarity]] {:name  (first (:content (first (:content card)))) :rarity  (first (:content rarity))})
       (group/inGroupsOf2
        (html/select (html/html-resource rtr-url) [:tr.cardItem #{:td.name :td.rarity}]))))
