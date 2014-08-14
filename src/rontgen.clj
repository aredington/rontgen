(ns rontgen
  (:import (java.lang.reflect Modifier Field)))

(def ^:private instance-read-strategies (atom {}))
(def ^:private class-read-strategies (atom {}))
(def ^:private class-write-strategies (atom {}))

(defn instance-fields
  [^Class class static-fn]
  (let [fields (.getDeclaredFields class)
        instance-fields (static-fn #(Modifier/isStatic (.getModifiers ^Field %)) (seq fields))
        public-fields (doall (filter #(.isAccessible ^Field %) instance-fields))
        private-fields (doall (remove #(.isAccessible ^Field %) instance-fields))
        _ (doseq [^Field field private-fields] (.setAccessible field true))]
    (concat public-fields private-fields)))

(defn- read-strategy
  [instance]
  (let [^Class class (class instance)
        [cache static-fn] (if (= class Class)
                            [class-read-strategies filter]
                            [instance-read-strategies remove])]
    (if (find @cache class)
      (@cache class)
      (let [fields (instance-fields class static-fn)
            strategy (fn [obj]
                       (locking obj
                         (into {} (for [^Field field fields]
                                    [(keyword (.getName field)) (.get field obj)]))))]
        ((swap! cache assoc class strategy) class)))))

(defn- write-strategy
  [instance]
  (let [^Class class (class instance)]
    (when (= class Class)
      (throw (ex-info "Cannot bash a class" {:instance instance})))
    (if (find @class-write-strategies class)
      (@class-write-strategies class)
      (let [fields (instance-fields class remove)
            field-accessors (into {} (for [^Field field fields]
                                       [(.getName field) field]))
            strategy (fn [obj map]
                       (locking obj
                         (doseq [key (keys map)]
                           (let [name (name key)
                                 ^Field field (field-accessors name)
                                 newval (map key)]
                             (.set field obj newval)))))]
        ((swap! class-write-strategies assoc class strategy) class)))))

(defn peer
  "Returns a map in which the keys correspond to all of the declared
  fields of instance, and the values are the present values of those
  fields. Obtains a lock on instance prior to reading any fields."
  [instance]
  (let [strategy (read-strategy instance)]
    (strategy instance)))

(defn bash
  "Installs the corresponding values into map for all of the keys in
  map which have corresponding fields in instance."
  [instance map]
  (let [strategy (write-strategy instance)]
    (strategy instance map)
    instance))
