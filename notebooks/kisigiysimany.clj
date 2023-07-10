(ns kisigiysimany)


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
    :db/cardinality :db.cardinality/many}
   {:db/ident :person/dress
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many}])
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
    }
   {:cloth/id 2
    :cloth/type "shirt"
    :cloth/color :color/green
    :cloth/season :season/summer
    }
   {:cloth/id 3
    :cloth/type "shirt"
    :cloth/color :color/blue
    :cloth/season :season/spring
    }
   {:cloth/id 4
    :cloth/type "pants"
    :cloth/color :color/red
    :cloth/season :season/spring
    }
   {:cloth/id 5
    :cloth/type "pants"
    :cloth/color :color/green
    :cloth/season :season/summer
    }
   {:cloth/id 6
    :cloth/type "pants"
    :cloth/color :color/blue
    :cloth/season :season/winter
    }
   {:cloth/id 7
    :cloth/type "jacket"
    :cloth/color :color/red
    :cloth/season :season/winter
    }
   {:cloth/id 8
    :cloth/type "jacket"
    :cloth/color :color/green
    :cloth/season :season/spring
    }
   {:cloth/id 9
    :cloth/type "jacket"
    :cloth/color :color/blue
    :cloth/season :season/fall
    }
   {:cloth/id 10
    :cloth/type "jacket"
    :cloth/color :color/red
    :cloth/season :season/spring
    }])
(d/transact conn {:tx-data cloth01})
(def db (d/db conn))

(def person01
  [{:person/id 1
    :person/name "baris"
    :person/dress [[:cloth/id 1][:cloth/id 3][:cloth/id 5][:cloth/id 7][:cloth/id 9]]
    }])
(d/transact conn {:tx-data person01})
(def db (d/db conn))
(def person02
  [{:person/id 2
    :person/name "doruk"
    :person/dress [[:cloth/id 2][:cloth/id 4][:cloth/id 6][:cloth/id 8][:cloth/id 10] ]
    }])
(d/transact conn {:tx-data person02})
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