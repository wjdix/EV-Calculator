(ns returnev.models.shared_connection
  (:import com.mchange.v2.c3p0.ComboPooledDataSource))

(defn pool []
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass "org.postgresql.Driver")
               (.setJdbcUrl (System/getenv "DATABASE_URL"))
               (.setMaxIdleTimeExcessConnections (* 30 60))
               (.setMaxIdleTime (* 3 60 60)))]
    {:datasource cpds}))

(def pooled-db (delay (pool)))

(defn db-connection [] @pooled-db)
