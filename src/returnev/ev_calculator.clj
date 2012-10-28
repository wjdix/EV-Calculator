(ns returnev.ev_calculator
  (:use [returnev.card_price :as price]))

(defn- cards-by-rarity [cards rarity]
  (filter (fn [x] (= rarity (:rarity x))) cards))

(defn- avg-price-for-rarity [cards rarity]
  (let [filtered (cards-by-rarity cards rarity)
        n (count filtered)
        prices (pmap (fn [c] (:price (price/enrich-card c))) filtered)
        sum (apply + prices)]
    (/ sum n)))

(defn ev-for-pack [cards]
  (let [common-avg   (avg-price-for-rarity cards "C")
        uncommon-avg (avg-price-for-rarity cards "U")
        rare-avg     (avg-price-for-rarity cards "R")
        mythic-avg   (avg-price-for-rarity cards "M")]
    (+ (* 10 common-avg) (* 3 uncommon-avg) (* 0.875 rare-avg) (* 0.125 mythic-avg))))
