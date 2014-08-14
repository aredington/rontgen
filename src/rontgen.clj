(ns rontgen
  (:import java.lang.reflect.Modifier))

(def ^:private class-read-strategies (atom {}))
(def ^:private class-write-strategies (atom {}))

(defn instance-fields
  [class]
  (let [fields (.getDeclaredFields class)
        instance-fields (remove #(Modifier/isStatic (.getModifiers %)) (seq fields))
        public-fields (doall (filter #(.isAccessible %) instance-fields))
        private-fields (doall (remove #(.isAccessible %) instance-fields))
        _ (doseq [field private-fields] (.setAccessible field true))]
    (concat public-fields private-fields)))

(defn- class-read-strategy
  [instance]
  (let [^Class class (class instance)]
    (if (find @class-read-strategies class)
      (@class-read-strategies class)
      (let [fields (instance-fields class)
            strategy (fn [obj]
                       (locking obj
                         (into {} (for [field fields]
                                    [(keyword (.getName field)) (.get field obj)]))))]
        ((swap! class-read-strategies assoc class strategy) class)))))

(defn- class-write-strategy
  [instance]
  (let [^Class class (class instance)]
    (if (find @class-write-strategies class)
      (@class-write-strategies class)
      (let [fields (instance-fields class)
            field-accessors (into {} (for [field fields]
                                       [(.getName field) field]))
            strategy (fn [obj map]
                       (locking obj
                         (doseq [key (keys map)]
                           (let [name (name key)
                                 field (field-accessors name)
                                 newval (map key)]
                             (.set field obj newval)))))]
        ((swap! class-write-strategies assoc class strategy) class)))))

(defn peer
  "Returns a map in which the keys correspond to all of the declared
  fields of instance, and the values are the present values of those
  fields. Obtains a lock on instance prior to reading any fields."
  [instance]
  (let [strategy (class-read-strategy instance)]
    (strategy instance)))

(defn bash
  "Installs the corresponding values into map for all of the keys in
  map which have corresponding fields in instance."
  [instance map]
  (let [strategy (class-write-strategy instance)]
    (strategy instance map)
    instance))
