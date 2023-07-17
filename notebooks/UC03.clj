(ns UC03)





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

;create user schema
(def user-schema
  [{:db/ident       :user/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :user/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/many}
   {:db/ident       :user/password
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data user-schema})
(def db (d/db conn))                                        ;;refresh database


(defn add-new-user [user-id username password-string]
  (d/transact conn {:tx-data [{:user/id       user-id
                               :user/name     username
                               :user/password password-string}
                              ]})
  (def db (d/db conn))
  )



;create supplier schema
(def supplier-schema
  [{:db/ident       :supplier/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :supplier/label
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :supplier/email
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :supplier/phonenumber
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/many}
   {:db/ident       :supplier/responsibleperson
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/many}
   {:db/ident       :supplier/category
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}])
(d/transact conn {:tx-data supplier-schema})
(def db (d/db conn))                                        ;;refresh database


(defn add-new-supplier [id label email phonenumber responsibleperson category]
  (d/transact conn {:tx-data [{:supplier/id                id
                               :supplier/label             label
                               :supplier/email             email
                               :supplier/phonenumber       phonenumber
                               :supplier/responsibleperson responsibleperson
                               :supplier/category          category
                               }
                              ]})
  (def db (d/db conn))
  )



;project general informations schema
(def pgi-schema
  [{:db/ident       :pgi/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :pgi/username
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :pgi/clientname
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :pgi/phone
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :pgi/projectname
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :pgi/projectstartingdate
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :pgi/projectfinishingdate
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :pgi/documents
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/many}
   ])
(d/transact conn {:tx-data pgi-schema})
(def db (d/db conn))

(defn add-pgi-info [id username clientname clientphonenumber projectname projectstartingdate projectfinishingdate documents]
  (d/transact conn {:tx-data [{:pgi/id                   id
                               :pgi/username             username
                               :pgi/clientname           clientname
                               :pgi/phone                clientphonenumber
                               :pgi/projectname          projectname
                               :pgi/projectstartingdate  projectstartingdate
                               :pgi/projectfinishingdate projectfinishingdate
                               :pgi/documents            documents
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
   ])
(d/transact conn {:tx-data orderinformation-schema})
(def db (d/db conn))

(defn add-order-info [number category name explanation]
  (d/transact conn {:tx-data [{:rfp/id          number
                               :rfp/category    category
                               :rfp/name        name
                               :rfp/explanation explanation
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

(defn add-selected-supplier-info [supplierid suppliername supplierphonenumber supplieremail category]
  (d/transact conn {:tx-data [{:supplier/id           supplierid
                               :supplier/name         suppliername
                               :supplier/phone-number supplierphonenumber
                               :supplier/email        supplieremail
                               :supplier/category     category
                               }
                              ]})
  (def db (d/db conn))
  )


;order informations schema
(def offer-schema
  [{:db/ident       :proposal/supplier
    :db/valueType   :db.type/ref
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :proposal/price
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   ])
(d/transact conn {:tx-data offer-schema})
(def db (d/db conn))

(defn add-supplier-offer [supplierid supplieroffer]
  (d/transact conn {:tx-data [{:proposal/supplier supplierid
                               :proposal/price    supplieroffer
                               }
                              ]})
  (def db (d/db conn))
  )

;1-Satın alma uzmanı, ''şirket çalışanları için seyahat ve konaklama'' projesi oluşturmak için kullanıcı adı ve şifresiyle sisteme giriş yapar.

;1a; kullanıcı oluştur
(add-new-user 1 "buse" "123456")

;1b; add new suppliers

(add-new-supplier 1 "kayıtlı tedarikci 01" "kt1@gmail.com" "123456789000" "someone" [:category/seyahat])
(add-new-supplier 2 "kayıtlı tedarikçi 02" "kt2@gmail.com" "123456789000" "someone" [:category/konaklama])



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

(add-order-info 1 [:category/seyahat :category/konaklama] "seyahat, konaklama desteği" "50 adet oda, 50 adet uçak bileti")

;8-Satın alma uzmanı, ileri butonuna tıklar ve sonraki aşamaya geçer.
;
;9-Sistem, aplikasyonunda kayıtlı olan bütün tedarikçileri sıralar. Satın alma uzmanı bu tedarikçileri seçebilir ve kendi tedarikçi ekleyebilir. Tedarikçi eklerken şu bilgileri girer:
;-Tedarikçi numarası (Örn: 1)
;-Tedarikçi ismi (Örn: Hilton otel, Sueno otel, Garden otel, Türk Hava Yolları, Pegasus, Anadolu Jet, msa taşımacılık, Ekol lojistik, Borusan Lojistik)
;-Tedarikçi telefon numarası (05534532113)
;-Tedarikçi emaili (hilton@gmail.com, sueno@gmail.com, garden@gmail.com, thy@gmail.com, pegasus@gmail.com, anadolujet@gmail.com)
;-Yetkili kişi (Özlem hanım, Funda hanım, Orhan bey, Dilek hanım)

(def get-free-supplier-id (+ 1 (ffirst (take 1 (reverse (->> (d/q
                                                               '[:find ?e
                                                                 :where
                                                                 [_ :supplier/id ?e]]
                                                               db)
                                                             (sort-by last)
                                                             )
                                                        )
                                             )
                                       )
                             )
  )


;9-
(d/q
  '[:find (pull ?e [*])
    :where
    [?e :supplier/label _]]
  db)


;10-
(d/q
  '[:find (pull ?e [*])
    :where
    [?e :pgi/id _]]
  db)

(d/q
  '[:find (pull ?e [*])
    :where
    [?e :rfp/id _]]
  db)

(d/q
  '[:find (pull ?e [*])
    :where
    [?e :supplier/id _]]
  db)

(d/q
  '[:find (pull ?e [*])
    :where
    [?e :supplier/label _]]
  db)

(add-new-supplier get-free-supplier-id "hilton otel" "hilton@gmail.com" "05534532113" "someone" [:category/konaklama])
(add-new-supplier get-free-supplier-id "sueno otel" "sueno@gmail.com" "05534532113" "someone" [:category/konaklama])
(add-new-supplier get-free-supplier-id "garden otel" "garden@gmail.com" "05534532113" "someone" [:category/konaklama])
(add-new-supplier get-free-supplier-id "Türk Hava Yolları" "thy@gmail.com" "05534532113" "someone" [:category/seyahat])
(add-new-supplier get-free-supplier-id "pegasus" "thy@gmail.com" "05534532113" "someone" [:category/seyahat])
(add-new-supplier get-free-supplier-id "anadolu jet" "anadolujet@gmail.com" "05534532113" "someone" [:category/seyahat])

(add-supplier-offer [:supplier/id 1] 150000)
(add-supplier-offer [:supplier/id 2] 170000)
(add-supplier-offer [:supplier/id 3] 140000)


;10-Satın alma uzmanı, uygun bulduğu tedarikçileri seçer ve kaydet butonuna tıklar.
;
;12-Sistem, kullanıcının proje hakkında girdiği tüm bilgileri ''ön izleme ekranında'' gösterir.
;4.adımdaki genel bilgiler
;7.adımdaki sipariş bilgileri
;10.adımdaki tedarikçi bilgileri

;12-Sistem, kullanıcının proje hakkında girdiği tüm bilgileri ''ön izleme
;ekranında'' gösterir.




 (->> (d/q
               '[:find ?si ?p
                 :where
                 [?e :proposal/supplier ?s]
                 [?s :supplier/label ?si]
                 [?e :proposal/price ?p]]
               db)
             (sort-by last)
             )


