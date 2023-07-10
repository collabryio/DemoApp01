(ns pencilnotebookdemoapp)


(require '[datomic.client.api :as d])
;; Memory storage
(def client (d/client {:server-type :dev-local
                       :storage-dir :mem
                       :system "ci"}))
(d/create-database client {:db-name "db04"})
(def conn (d/connect client {:db-name "db04"}))

;eğer db ident ile tanımlandıysa bir enum direkt olarak ona keyword ile referans verebiliriz
(d/transact
  conn
  {:tx-data [{:db/ident :color/red}
             {:db/ident :color/green}
             {:db/ident :color/blue}]})
(def db (d/db conn))  ;;refresh database

;product schema
(def db-schema
  [{:db/ident :product/id
    :db/valueType :db.type/long
    :db/unique :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident :product/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :product/color
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data db-schema})
(def db (d/db conn))  ;;refresh database

;order schema
(def order-schema
  [{:db/ident :order/product
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :order/size
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data order-schema})
(def db (d/db conn))  ;;refresh database

;product list -string/ref
(def product-list
  [{:product/id 1
    :product/name "Kalem"
    :product/color :color/red}
   {:product/id 2
    :product/name "Kalem"
    :product/color :color/blue}
   {:product/id 3
    :product/name "Defter"
    :product/color :color/red}
   {:product/id 4
    :product/name "Defter"
    :product/color :color/green}])
(d/transact conn {:tx-data product-list})
(def db (d/db conn))  ;;refresh database



(def order-list-1
  [{:order/product [:product/id 1]
    :order/size 2}
   {:order/product [:product/id 2]
    :order/size 4}
   {:order/product [:product/id 3]
    :order/size 5}
   {:order/product [:product/id 4]
    :order/size 6}])
(d/transact conn {:tx-data order-list-1})
(def db (d/db conn))  ;;refresh database



(d/q
  '[:find (pull ?e [*])
    :where
    [?e :product/name _]]
  db)

