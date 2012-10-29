(ns returnev.schedule
  (:use [clojurewerkz.quartzite.jobs :only [defjob]]
        [clojurewerkz.quartzite.schedule.calendar-interval :only [schedule with-interval-in-days]]
        [clojure.tools.logging :only [info]]
        [returnev.ev_calculator :as calc]
        [returnev.card_list :as card-list])
  (:require [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.triggers :as t]))

(defjob FetchPrices [ctx]
  (info "Fetching prices")
  (calc/ev-for-pack (card-list/all-rtr-cards)))

(defn start-schedule []
  (qs/initialize)
  (qs/start)
  (let [job (j/build
             (j/of-type FetchPrices)
             (j/with-identity (j/key "jobs.fetch_prices.1")))
        trigger (t/build
                 (t/with-identity (t/key "daily.1"))
                 (t/start-now)
                 (t/with-schedule (schedule
                                   (with-interval-in-days 1))))]
    (qs/schedule job trigger)))
