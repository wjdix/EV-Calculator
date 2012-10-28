(ns returnev.group_by_two)

(defn- groupsOf2WithAcc [list acc]
  (if (empty? list) acc
    (let [head (take 2 list)]
      (recur (drop 2 list) (cons head acc)))))

(defn inGroupsOf2 [list]
  (groupsOf2WithAcc list []))
