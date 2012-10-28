(ns returnev.models.shared_connection
  (:import com.mchange.v2.c3p0.ComboPooledDataSource))

(defn pool []
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass "org.postgresql.Driver")
               (.setJdbcUrl (System/getenv "DATABASE_URL"))
               (.setUser (System/getenv "DATABASE_USER"))
               (.setPassword (System/getenv "DATABASE_PASSWORD"))
               (.setMaxIdleTimeExcessConnections (* 30 60))
               (.setMinPoolSize 5)
               (.setMaxPoolSize 10)
               (.setMaxIdleTime (* 3 60 60)))]
    {:datasource cpds}))

(def pooled-db (delay (pool)))

(defn db-connection [] @pooled-db)
