(ns kisivegiysi)


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

(d/transact
  conn
  {:tx-data [{:db/ident :season/spring}
             {:db/ident :season/summer}
               {:db/ident :season/fall}
             {:db/ident :season/winter}]})
(def db (d/db conn))  ;;refresh database

;product schema
(def db-schema
  [{:db/ident :person/id
    :db/valueType :db.type/long
    :db/unique :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident :person/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :person/dress
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data db-schema})
(def db (d/db conn))

(def clothing-schema
  [{:db/ident :cloth/id
    :db/valueType :db.type/long
    :db/unique :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident :cloth/type
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :cloth/color
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :cloth/season
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}]
  )
(d/transact conn {:tx-data clothing-schema})
(def db (d/db conn))

(def cloth01 "creating a person"
  [{:cloth/id 1
    :cloth/type "shirt"
    :cloth/color :color/red
    :cloth/season :season/spring
    }])
(d/transact conn {:tx-data cloth01})
(def db (d/db conn))

(def person01
  [{:person/id 1
    :person/name "baris"
    :person/dress [:cloth/id 1]
    }])
(d/transact conn {:tx-data person01})
(def db (d/db conn))

(d/q
  '[:find (pull ?e [*])
    :where
    [?e :person/name _]]
  db)
(d/q
  '[:find (pull ?e [*])
    :where
    [?e :cloth/type _]]
  db)