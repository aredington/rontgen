(ns rontgen
  (:require [clojure.core.cache :as cache])
  (:import (java.lang.reflect Modifier Field)))

(def ^:private instance-read-strategies (cache/lirs-cache-factory {}))
(def ^:private class-read-strategies (cache/lirs-cache-factory {}))
(def ^:private instance-write-strategies (cache/lirs-cache-factory {}))

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
        [target static-fn] (if (= class Class) [instance filter] [class remove])
        fields (instance-fields target static-fn)]
    (fn [obj]
      (locking obj
        (into {} (for [^Field field fields]
                   [(keyword (.getName field)) (.get field obj)]))))))

(defn- write-strategy
  [instance]
  (let [^Class class (class instance)]
    (when (= class Class)
      (throw (ex-info "Cannot bash a class" {:instance instance})))
    (let [fields (instance-fields class remove)
          field-accessors (into {} (for [^Field field fields]
                                     [(.getName field) field]))]
      (fn [obj map]
                     (locking obj
                       (doseq [key (keys map)]
                         (let [name (name key)
                               ^Field field (field-accessors name)
                               newval (map key)]
                           (.set field obj newval))))))))

(defn find-strategy-in-cache
  [cache strategy strategy-factory]
  (cache/lookup
   (if (cache/has? cache class)
     (cache/hit cache class)
     (cache/miss cache class (strategy-factory)))
   class))

(defn peer
  "Returns a map in which the keys correspond to all of the declared
  fields of instance, and the values are the present values of those
  fields. Obtains a lock on instance prior to reading any fields."
  [instance]
  (let [class (class instance)
        cache (if (= Class class)
                class-read-strategies
                instance-read-strategies)
        strategy (find-strategy-in-cache cache
                                         class
                                         #(read-strategy instance))]
    (strategy instance)))

(defn bash
  "Installs the corresponding values into map for all of the keys in
  map which have corresponding fields in instance."
  [instance map]
  (let [class (class instance)
        strategy (find-strategy-in-cache instance-write-strategies
                                         class
                                         #(write-strategy instance))]
    (strategy instance map)
    instance))
