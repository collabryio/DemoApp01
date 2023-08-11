(ns UC01c_20230809)
;usecase --->  UC 1.Yyeni downloads

(require '[datomic.client.api :as d])
(def client (d/client {:server-type :dev-local
                       :storage-dir :mem
                       :system      "ci"}))
(d/create-database client {:db-name "db01"})
(def conn (d/connect client {:db-name "db01"}))
(def db (d/db conn))                                        ;;refresh database

(d/transact
  conn
  {:tx-data [{:db/ident :service/seyahat}
             {:db/ident :service/konaklama}
             {:db/ident :service/lojistik}
             {:db/ident :service/bilgisayar}
             {:db/ident :service/kalem}
             {:db/ident :service/mobilya}]})
(def db (d/db conn))                                        ;;refresh database

(d/transact
  conn
  {:tx-data [{:db/ident :customer/supplier}
             {:db/ident :customer/client}
             ]})
(def db (d/db conn))                                        ;;refresh database

(d/transact
  conn
  {:tx-data [{:db/ident :order-status/ongoing}
             {:db/ident :order-status/done}
             {:db/ident :order-status/waiting}
             {:db/ident :order-status/declined}
             {:db/ident :order-status/timeout}
             ]})
(def db (d/db conn))


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
    :db/cardinality :db.cardinality/one}
   {:db/ident       :user/formal-name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :user/formal-surname
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :user/company-id
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data user-schema})
(def db (d/db conn))                                        ;;refresh database


(defn add-new-user [user-id username password-string formal-name formal-surname company]
  (d/transact conn {:tx-data [{:user/id             user-id
                               :user/name           username
                               :user/password       password-string
                               :user/formal-name    formal-name
                               :user/formal-surname formal-surname
                               :user/company-id     company}
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
   {:db/ident       :company/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :company/email
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :company/phonenumber
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/many}
   {:db/ident       :company/category
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}])
(d/transact conn {:tx-data company-schema})
(def db (d/db conn))                                        ;;refresh database


(defn add-new-company [id brand-name email phonenumber category]
  (d/transact conn {:tx-data [{:company/id          id
                               :company/brand-name  brand-name
                               :company/email       email
                               :company/phonenumber phonenumber
                               :company/category    category
                               }
                              ]})
  (def db (d/db conn))
  )



;project general informations schema
(def project-schema
  [{:db/ident       :project/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :project/owner-id
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :project/client-id
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :project/phonenumber
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :project/project-title
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :project/project-sd
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :project/project-fd
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :project/documents
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/many}
   ])
(d/transact conn {:tx-data project-schema})
(def db (d/db conn))

(defn add-new-project [id owner-id client-id clientphonenumber project-title projectstartingdate projectfinishingdate documents]
  (d/transact conn {:tx-data [{:project/id            id
                               :project/owner-id      owner-id
                               :project/client-id     client-id
                               :project/phonenumber   clientphonenumber
                               :project/project-title project-title
                               :project/project-sd    projectstartingdate
                               :project/project-fd    projectfinishingdate
                               :project/documents     documents
                               }
                              ]})
  (def db (d/db conn))
  )

;order informations schema
(def order-schema
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
   {:db/ident       :rfp/item-amount
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident       :rfp/explanation
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   ])
(d/transact conn {:tx-data order-schema})
(def db (d/db conn))

(defn add-new-order [number category name item-amount explanation]
  (d/transact conn {:tx-data [{:rfp/id          number
                               :rfp/category    category
                               :rfp/name        name
                               :rfp/item-amount item-amount
                               :rfp/explanation explanation
                               }
                              ]})
  (def db (d/db conn))
  )



;order informations schema
(def proposal-schema
  [{:db/ident       :proposal/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :proposal/supplier-id
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :proposal/amount
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident       :proposal/project-id
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :proposal/item-amount
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident       :proposal/order-status
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   ])
(d/transact conn {:tx-data proposal-schema})
(def db (d/db conn))

(defn add-new-proposal [id supplier-id amount project-id item-amount order-status]
  (d/transact conn {:tx-data [{:proposal/id           id
                               :proposal/supplier-id  supplier-id
                               :proposal/amount       amount
                               :proposal/project-id   project-id
                               :proposal/item-amount  item-amount
                               :proposal/order-status order-status
                               }
                              ]})
  (def db (d/db conn))
  )


;; example
(add-new-company 1 "TEI" "info.tei@tei.com" "1234567890" [:service/konaklama])
(add-new-company 2 "IBM" "info.ibm@ibm.com" "1234567890" [:service/bilgisayar])
(add-new-company 3 "HP" "info.hp@hp.com" "1234567890" [:service/bilgisayar])
(add-new-company 4 "Beyza yeni şirket" "beyzayeni@company.com" "1234567890" [:service/bilgisayar])

(add-new-user 1 "user1" "123456" "Beyza" "Polatlı" [:company/id 1])
(add-new-user 2 "user2" "123456" "Barış" "Can" [:company/id 2])
(add-new-user 3 "user3" "123456" "Elif" "Iğdırlı" [:company/id 3])
;1. Satın alma uzmanı, kullanıcı adı ve şifresiyle sisteme giriş yapar.
;2. Sistem, bilgileri doğrular ve profili sunar.
;3. Satın alma uzmanı, "proje oluştur" butonuna tıklar.
;4. Sistem, proje hakkında girilmesi gereken "genel bilgiler" ekranını sunar.
;5. Satın alma uzmanı, genel bilgilerini doldurur: - Satın alma uzmanı ismi
;- Müşteri şirket ismi
;- Müşteri şirket telefon numarası
;- Proje ismi
;- Proje başlangıç tarihi - Proje bitiş tarihi
;- Belgeler
;6. Sistem, genel bilgileri veri tabanına kaydeder ve sonraki aşamaya (sipariş bilgileri formu) geçer.
(add-new-project 1 [:user/id 1] [:company/id 1] "1234567890" "bilgisayar satın alma projesi" "10.06.2023" "10.07.2023" "docs")
;6. Sistem, genel bilgileri veri tabanına kaydeder ve sonraki aşamaya (sipariş bilgileri formu) geçer.
;7. Satın alma uzmanı, sipariş bilgileri formunu doldurur:
;- Sipariş numarası
;- Sipariş kategorisi
;- Sipariş ismi
;- Sipariş açıklaması


(add-new-order 1 [:service/bilgisayar] "notebook" 20 "20 adet, gri renk")
;8. Sistem, sipariş bilgilerini veri tabanına kaydeder ve sonraki aşamaya (tedarikçi bilgileri formu) geçer.
;Aplikasyonunda kayıtlı olan tedarikçileri sıralar.

(d/q
  '[:find (pull ?e [*])
    :where
    [?e :company/brand-name _]]
  db)
;=>
;[[{:db/id 79164837199974,
;   :company/id 2,
;   :company/brand-name "IBM",
;   :company/email "info.ibm@ibm.com",
;   :company/phonenumber ["1234567890"],
;   :company/category [#:db{:id 79164837199948, :ident :service/bilgisayar}]}]
; [{:db/id 83562883711079,
;   :company/id 3,
;   :company/brand-name "HP",
;   :company/email "info.hp@hp.com",
;   :company/phonenumber ["1234567890"],
;   :company/category [#:db{:id 79164837199948, :ident :service/bilgisayar}]}]
; [{:db/id 101155069755493,
;   :company/id 1,
;   :company/brand-name "TEI",
;   :company/email "info.tei@tei.com",
;   :company/phonenumber ["1234567890"],
;   :company/category [#:db{:id 79164837199946, :ident :service/konaklama}]}]]


;Örnek:
;- Tedarikçi şirket ismi
;- Tedarikçi telefon numarası
;- E-posta



;10. Sistem, seçilen tedarikçileri veri tabanına kaydeder, satın alma uzmanının proje hakkında girdiği tüm bilgileri doğrular ve uzmana "ön izleme ekranında" gösterir:
;5. adımdaki genel bilgiler 7. adımdaki sipariş bilgiler 8. adımdaki tedarikçiler

;veri tabanına kayıt edilen bütün tedarikçileri gösterir
; 8. adım
(d/q
  '[:find (pull ?e [*])
    :where
    [?e :company/brand-name _]]
  db)
;=>
;[[{:db/id 79164837199974,
;   :company/id 2,
;   :company/brand-name "IBM",
;   :company/email "info.ibm@ibm.com",
;   :company/phonenumber ["1234567890"],
;   :company/category [#:db{:id 79164837199948, :ident :service/bilgisayar}]}]
; [{:db/id 79164837200014,
;   :company/id 4,
;   :company/brand-name "Beyza yeni şirket",
;   :company/email "beyzayeni@company.com",
;   :company/phonenumber ["1234567890"],
;   :company/category [#:db{:id 79164837199948, :ident :service/bilgisayar}]}]
; [{:db/id 83562883711079,
;   :company/id 3,
;   :company/brand-name "HP",
;   :company/email "info.hp@hp.com",
;   :company/phonenumber ["1234567890"],
;   :company/category [#:db{:id 79164837199948, :ident :service/bilgisayar}]}]
; [{:db/id 101155069755493,
;   :company/id 1,
;   :company/brand-name "TEI",
;   :company/email "info.tei@tei.com",
;   :company/phonenumber ["1234567890"],
;   :company/category [#:db{:id 79164837199946, :ident :service/konaklama}]}]]

;proje bilgilerini gösterir
; 5. adım
(d/q
  '[:find (pull ?e [*])
    :where
    [?e :project/id _]]
  db)

;proje (rfp) bilgilerini gösterir
; 7. adım
(d/q
  '[:find (pull ?e [*])
    :where
    [?e :rfp/id _]]
  db)


;11. Satın alma uzmanı, ön izleme ekranında bilgileri kontrol eder ve "proje başlat" butonuna tıklar.


;12. Sistem, bildirim ekranı sunar. Satın alma uzmanı, bildirim mesaj içeriğini ve
; bildirim gönderilecek birimleri seçer. Sistem, gönderir.
;(bu adımı yapamıyoruz.)
;12-b. Satın alma uzmanı, tedarikçilerden fiyat ister.


;13. Sistem, tedarikçileri "teklif giriş ekranına" yönlendirir. Tedarikçiler, tekliflerini girerler. [sistem, bildirim gönderir]
(add-new-proposal 1 [:company/id 2] 110000 [:project/id 1] 10 :order-status/waiting)
(add-new-proposal 2 [:company/id 3] 119000 [:project/id 1] 10 :order-status/waiting)
(add-new-proposal 3 [:company/id 4] 239000 [:project/id 1] 20 :order-status/waiting)


;14. Sistem, tedarikçilerin girdiği teklifleri veri tabanına kaydeder ve en iyi tekliften en kötü teklife doğru
; satın alma uzmanına sıralayıp "teklif kıyaslama ekranında" gösterir.
;- Tedarikçi ismi
;- Tedarikçi teklifi
(->> (d/q
       '[:find ?si ?ia ?os ?p
         :where
         [?e :proposal/id _]
         [?e :proposal/supplier-id ?s]
         [?e :proposal/item-amount ?ia]
         [?e :proposal/amount ?p]
         [?e :proposal/order-status ?ps]
         [?s :company/brand-name ?si]
         [?ps :db/ident ?os]
         ]
       db)
     (sort-by last)
     )
;=>
;(["IBM" 10 :order-status/waiting 110000]
; ["HP" 10 :order-status/waiting 119000]
; ["Beyza yeni şirket" 20 :order-status/waiting 239000])




;15. Satın alma uzmanı, uygun bulduğu iki tedarikçiyle çalışmaya başlar ve
;sistem sipariş durumlarını takip etmesi için "sipariş ekranı" sunar.
;;;;;;;;;Burada satınalma uzman bir teklifi kabul etmeli
(add-new-proposal 1 [:company/id 2] 110000 [:project/id 1] 10 :order-status/ongoing)
(add-new-proposal 2 [:company/id 3] 119000 [:project/id 1] 10 :order-status/ongoing)


(->> (d/q
       '[:find ?si ?ia ?os ?p
         :where
         [?e :proposal/id _]
         [?e :proposal/supplier-id ?s]
         [?e :proposal/item-amount ?ia]
         [?e :proposal/amount ?p]
         [?e :proposal/order-status ?ps]
         [?s :company/brand-name ?si]
         [?ps :db/ident ?os]
         ]
       db)
     (sort-by last)
     )
;=>
;(["IBM" 10 :order-status/ongoing 110000]    ----> teklif kabul edildi
; ["HP" 10 :order-status/ongoing 119000]     ----> teklif kabul edildi
; ["Beyza yeni şirket" 20 :order-status/waiting 239000])




;;;---------------- bitti



