(ns UC04)

(require '[datomic.client.api :as d])
(def client (d/client {:server-type :dev-local
                       :storage-dir :mem
                       :system      "ci"}))
(d/create-database client {:db-name "db01"})
(def conn (d/connect client {:db-name "db01"}))
(def db (d/db conn))                                        ;;refresh database

(d/transact
  conn
  {:tx-data [{:db/ident :category/seyahat}
             {:db/ident :category/konaklama}
             {:db/ident :category/lojistik}
             {:db/ident :category/bilgisayar}
             {:db/ident :category/kalem}
             {:db/ident :category/mobilya}]})
(def db (d/db conn))                                        ;;refresh database

(d/transact
  conn
  {:tx-data [{:db/ident :category/supplier}
             {:db/ident :category/client}]})

;create user schema
(def user-schema
  [{:db/ident       :user/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :user/username
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :user/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :user/password
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :user/company-id
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data user-schema})
(def db (d/db conn))                                        ;;refresh database


(defn add-new-user [user-id name username password-string company-id]
  (d/transact conn {:tx-data [{:user/id         user-id
                               :user/username   username
                               :user/name       name
                               :user/password   password-string
                               :user/company-id company-id}
                              ]})
  (def db (d/db conn))
  )



;create supplier schema
(def company-schema
  [{:db/ident       :company/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :company/brand-name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :company/company-name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :company/responsible-person-name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :company/email
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :company/phone-number
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/many}
   {:db/ident       :company/category
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}
   {:db/ident       :company/type
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}])
(d/transact conn {:tx-data company-schema})
(def db (d/db conn))                                        ;;refresh database


(defn add-new-supplier [id brand-name company-name responsible-person-name email phone-number category type]
  (d/transact conn {:tx-data [{:company/id                      id
                               :company/brand-name              brand-name
                               :company/company-name            company-name
                               :company/responsible-person-name responsible-person-name
                               :company/email                   email
                               :company/phone-number            phone-number
                               :company/category                category
                               :company/type                    type
                               }
                              ]})
  (def db (d/db conn))
  )



;project general informations schema
(def project-schema
  [{:db/ident       :project/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one
    :db/doc         "project general information"}
   {:db/ident       :project/responsible-person-name
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc         "project general information"}
   {:db/ident       :project/client-id
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "project general information"}
   {:db/ident       :project/phone
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "project general information"}
   {:db/ident       :project/project-title
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "project general information"}
   {:db/ident       :project/start-date
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "project general information"}
   {:db/ident       :project/finish-date
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "project general information"}
   {:db/ident       :project/documents
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/many
    :db/doc         "project general information"}
   ])
(d/transact conn {:tx-data project-schema})
(def db (d/db conn))

(defn add-pgi-info [id username clientname clientphonenumber projecttitle projectstartingdate projectfinishingdate documents]
  (d/transact conn {:tx-data [{:project/id                      id
                               :project/responsible-person-name username
                               :project/client-id               clientname
                               :project/phone                   clientphonenumber
                               :project/project-title           projecttitle
                               :project/start-date              projectstartingdate
                               :project/finish-date             projectfinishingdate
                               :project/documents               documents
                               }
                              ]})
  (def db (d/db conn))
  )

;order informations schema

(def orderinformation-schema
  [{:db/ident       :rfp/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :rfp/category
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}
   {:db/ident       :rfp/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :rfp/explanation
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :rfp/client-id
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   ])
(d/transact conn {:tx-data orderinformation-schema})
(def db (d/db conn))

(defn add-order-info [number category name explanation client-id]
  (d/transact conn {:tx-data [{:rfp/id          number
                               :rfp/category    category
                               :rfp/name        name
                               :rfp/explanation explanation
                               :rfp/client-id   client-id
                               }
                              ]})
  (def db (d/db conn))
  )


;supplier informations schema
(def selected-supplierinformation-schema
  [{:db/ident       :supplier/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :supplier/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :supplier/phone-number
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :supplier/email
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :supplier/category
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}
   ])
(d/transact conn {:tx-data selected-supplierinformation-schema})
(def db (d/db conn))

;offer informations schema
(def offer-schema
  [{:db/ident       :proposal/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :proposal/supplier
    :db/valueType   :db.type/ref
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :proposal/price
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident       :proposal/rfp
    :db/valueType   :db.type/ref
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/many}
   ])
(d/transact conn {:tx-data offer-schema})
(def db (d/db conn))

(defn add-supplier-offer [offerid supplierid supplieroffer orderid]
  (d/transact conn {:tx-data [{:proposal/id       offerid
                               :proposal/supplier supplierid
                               :proposal/price    supplieroffer
                               :proposal/rfp      orderid
                               }
                              ]})
  (def db (d/db conn))
  )

;1-Satın alma uzmanı, ''şirket çalışanları için seyahat ve konaklama'' projesi oluşturmak için kullanıcı adı ve şifresiyle sisteme giriş yapar.

;1a; kullanıcı oluştur
(add-new-user 1 "buse" "saubuse" "123456" 1)
(add-new-user 1 "buse" "saubuse" "123456" 2)
(add-new-user 1 "buse" "saubuse" "123456" 5)


;2-Satın alma uzmanı, ''proje oluştur'' butonuna tıklar.
;3-Sistem, proje hakkında girilmesi gereken ''genel bilgiler'' ekranını
;sunar.


;4-Satın alma uzmanı, proje genel bilgilerini doldurur:
;-Satın alma uzmanı ismi (Örn: Buse)
;-Müşteri şirket ismi (Örn: Koç Holding)
;-Müşteri şirket telefon numarası (Örn:5314567834)
;-Proje ismi (Örn: seyahat ve konaklama satın alma projesi)
;-Proje başlangıç tarihi (Örn:10.06.2023)
;-Proje bitiş tarihi (Örn:10.07.2023)
;-Belgeler (Örn:sözleşme belgesi Örn:iletişim kuralları belgesi)

(add-pgi-info 1 [:user/id 1] "Koç Holding" "5314567834" "seyahat ve konaklama satın alma projesi" "10.06.2023" "10.07.2023" "iletişim kuralları belgesi")

;5-Kullanıcı, ileri butonuna tıklar ve sonraki aşamaya geçer.
;6-Sistem, proje hakkında girilmesi gereken ''sipariş bilgileri'' ekranını
;sunar.

;7-Satın alma uzmanı, hizmet bilgilerini doldurur:
;-Hizmet numarası (Örn:1)
;-Hizmet kategorisi (Örn: seyahat, konaklama, lojistik, bilgisayar, kalem, mobilya)
;-Hizmet İsmi (Örn: seyahat, konaklama desteği, global lojistik desteği)
;-Hizmet açıklaması (Örn: 50 adet oda, 50 adet uçak bileti, 50 adet karayolu lojistik aracı)

(add-order-info 1 [:category/seyahat] "konaklama desteği" "50 adet uçak bileti" 1)
(add-order-info 2 [:category/konaklama] "seyehat desteği" "50 adet oda" 2)

;8-Satın alma uzmanı, ileri butonuna tıklar ve sonraki aşamaya geçer.


;1b; add new suppliers
(add-new-supplier 1 "A brand" "A seyahat" "responsible person" "kt1@gmail.com" "123456789000" [:category/seyahat] [:category/supplier])
(add-new-supplier 2 "B brand" "B seyahat" "responsible person" "kt1@gmail.com" "123456789000" [:category/seyahat] [:category/supplier])
(add-new-supplier 3 "C brand" "C seyahat" "responsible person" "kt1@gmail.com" "123456789000" [:category/seyahat] [:category/supplier])
(add-new-supplier 4 "D brand" "D konaklama" "responsible person" "kt2@gmail.com" "123456789000" [:category/konaklama] [:category/supplier])
(add-new-supplier 5 "E brand" "E konaklama" "responsible person" "kt2@gmail.com" "123456789000" [:category/konaklama] [:category/supplier])
(add-new-supplier 6 "F brand" "F konaklama" "responsible person" "kt2@gmail.com" "123456789000" [:category/konaklama] [:category/supplier])


(add-supplier-offer 1 [:supplier/id 1] 150000 [:rfp/id 1])
(add-supplier-offer 2 [:supplier/id 2] 170000 [:rfp/id 1])
(add-supplier-offer 3 [:supplier/id 3] 140000 [:rfp/id 1])
(add-supplier-offer 4 [:supplier/id 4] 140300 [:rfp/id 2])


(d/q
  '[:find ?e
    :where
    [?e :proposal/rfp _]
    ]
  db)

(d/q
  '[:find (pull ?e [*])
    :where
    [?e :proposal/rfp _]
    ]
  db)



(->> (d/q
       '[:find ?si ?p
         :where
         [?e :proposal/supplier ?s]
         [?s :supplier/label ?si]
         [?e :proposal/price ?p]]
       db)
     (sort-by last)
     )


