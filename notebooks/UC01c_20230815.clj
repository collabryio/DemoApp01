(ns UC01c_20230815)
;rfr: 20230815-UC1c.pdf

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
  {:tx-data [{:db/ident :order-status/waiting}
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


(defn add-new-user [map]
  (d/transact conn {:tx-data [{:user/id             (get map :id)
                               :user/name           (get map :username)
                               :user/password       (get map :password)
                               :user/formal-name    (get map :formal-name)
                               :user/formal-surname (get map :formal-surname)
                               :user/company-id     (get map :company)}
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


(defn add-new-company [map]
  (d/transact conn {:tx-data [{:company/id          (get map :id)
                               :company/brand-name  (get map :brand-name)
                               :company/email       (get map :email)
                               :company/phonenumber (get map :phonenumber)
                               :company/category    (get map :category)
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

(defn add-new-project [map]
  (d/transact conn {:tx-data [{:project/id            (get map :id)
                               :project/owner-id      (get map :owner-id)
                               :project/client-id     (get map :client-id)
                               :project/phonenumber   (get map :clientphonenumber)
                               :project/project-title (get map :project-title)
                               :project/project-sd    (get map :projectstartingdate)
                               :project/project-fd    (get map :projectfinishingdate)
                               :project/documents     (get map :documents)
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

(defn add-new-order [map]
  (d/transact conn {:tx-data [{:rfp/id          (get map :id)
                               :rfp/category    (get map :category)
                               :rfp/name        (get map :name)
                               :rfp/item-amount (get map :item-amount)
                               :rfp/explanation (get map :explanation)
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
   {:db/ident       :proposal/client-company
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :proposal/client-person
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   ])
(d/transact conn {:tx-data proposal-schema})
(def db (d/db conn))

(defn add-new-proposal [map]
  (d/transact conn {:tx-data [{:proposal/id             (get map :id)
                               :proposal/supplier-id    (get map :supplier-id)
                               :proposal/amount         (get map :amount)
                               :proposal/project-id     (get map :project-id)
                               :proposal/item-amount    (get map :item-amount)
                               :proposal/order-status   (get map :order-status)
                               :proposal/client-company (get map :client-company)
                               :proposal/client-person  (get map :client-person)
                               }
                              ]})
  (def db (d/db conn))
  )


(defn unit-price [total-price unit-amount]
  (long (/ total-price unit-amount))
  )
;[(UC01c_20230815/unit-price ?p ?ia) ?up]


(defn get-user-entity-id-by-id [id]
  (ffirst (d/q
            '[:find ?e
              :in $ ?id
              :where
              [?e :user/id ?id]
              ]
            db id)
          ))




(defn get-company-entity-id-by-id [id]
  (ffirst (d/q
            '[:find ?e
              :in $ ?id
              :where
              [?e :company/id ?id]
              ]
            db id)
          ))



(defn proposal-by-user-id [user-id]
  (->> (d/q
         '[:find ?bn ?ia ?os ?am
           :in $ ?userid
           :where
           [?e :proposal/id _]
           [?e :proposal/client-person ?userid]
           [?e :proposal/supplier-id ?si]
           [?e :proposal/item-amount ?ia]
           [?e :proposal/amount ?am]
           [?e :proposal/order-status ?ps]
           [?si :company/brand-name ?bn]
           [?ps :db/ident ?os]
           ]
         db (get-user-entity-id-by-id user-id))
       (sort-by last)
       )
  )

(defn proposal-by-company-id [company-id]
  (->> (d/q
         '[:find ?bn ?ia ?os ?am
           :in $ ?cid
           :where
           [?e :proposal/id _]
           [?e :proposal/client-company ?cid]
           [?e :proposal/supplier-id ?si]
           [?e :proposal/item-amount ?ia]
           [?e :proposal/amount ?am]
           [?e :proposal/order-status ?ps]
           [?si :company/brand-name ?bn]
           [?ps :db/ident ?os]
           ]
         db (get-company-entity-id-by-id company-id))
       (sort-by last)
       )
  )

(defn proposal-by-companyid-and-status [company-id order-status]
  (->> (d/q
         '[:find ?bn ?ia ?os ?p
           :in $ ?cid ?os
           :where
           [?e :proposal/id _]
           [?e :proposal/client-company ?cid]
           [?e :proposal/order-status ?os]
           [?e :proposal/supplier-id ?si]
           [?e :proposal/item-amount ?ia]
           [?e :proposal/amount ?p]
           [?si :company/brand-name ?bn]
           [?ps :db/ident ?os]
           ]
         db (get-company-entity-id-by-id company-id) order-status)
       (sort-by last)
       )
  )

(defn proposal-by-userid-and-status [user-id order-status]
  (->> (d/q
         '[:find ?bn ?ia ?os ?p
           :in $ ?cid ?os
           :where
           [?e :proposal/id _]
           [?e :proposal/client-person ?cid]
           [?e :proposal/order-status ?os]
           [?e :proposal/supplier-id ?si]
           [?e :proposal/item-amount ?ia]
           [?e :proposal/amount ?p]
           [?si :company/brand-name ?bn]
           [?ps :db/ident ?os]
           ]
         db (get-user-entity-id-by-id user-id) order-status)
       (sort-by last)
       )
  )


(add-new-company {:id 1 :brand-name "TEI" :email "info.tei@tei.com" :phonenumber "1234567890" :category [:service/konaklama]})
(add-new-company {:id 2 :brand-name "IBM" :email "info.ibm@ibm.com" :phonenumber "1234567890" :category [:service/bilgisayar]})
(add-new-company {:id 3 :brand-name "HP" :email "info.hp@hp.com" :phonenumber "1234567890" :category [:service/bilgisayar]})
(add-new-company {:id 4 :brand-name "APPLE" :email "apple@company.com" :phonenumber "1234567890" :category [:service/bilgisayar]})



(add-new-user {:id 1 :username "user1" :password "123456" :formal-name "Beyza" :formal-surname "Polatlı" :company [:company/id 1]})
(add-new-user {:id 2 :username "user2" :password "123456" :formal-name "Barış" :formal-surname "Can" :company [:company/id 2]})
(add-new-user {:id 3 :username "user3" :password "123456" :formal-name "Elif" :formal-surname "Iğdırlı" :company [:company/id 3]})
(add-new-user {:id 4 :username "user4" :password "123456" :formal-name "kerem" :formal-surname "kalafat" :company [:company/id 4]})
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

(add-new-project {:id 1 :owner-id [:user/id 1] :client-id [:company/id 1] :clientphonenumber "1234567890" :project-title "bilgisayar satın alma projesi" :projectstartingdate "10.06.2023" :projectfinishingdate "10.07.2023" :documents "docs"})
(add-new-project {:id 2 :owner-id [:user/id 4] :client-id [:company/id 4] :clientphonenumber "1234567890" :project-title "ipad 10 adet" :projectstartingdate "10.06.2023" :projectfinishingdate "10.07.2023" :documents "docs"})
;6. Sistem, genel bilgileri veri tabanına kaydeder ve sonraki aşamaya (sipariş bilgileri formu) geçer.
;7. Satın alma uzmanı, sipariş bilgileri formunu doldurur:
;- Sipariş numarası
;- Sipariş kategorisi
;- Sipariş ismi
;- Sipariş açıklaması

(add-new-order {:id 1 :category [:service/bilgisayar] :name "notebook" :item-amount 20 :explanation "20 adet, gri renk"})
(add-new-order {:id 2 :category [:service/bilgisayar] :name "tablet" :item-amount 15 :explanation "15 adet, uzay gri renk"})
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
(add-new-proposal {:id 1 :supplier-id [:company/id 2] :amount 110000 :project-id [:project/id 1] :item-amount 10 :order-status :order-status/waiting :client-company [:company/id 1] :client-person [:user/id 1]})
(add-new-proposal {:id 2 :supplier-id [:company/id 3] :amount 119000 :project-id [:project/id 1] :item-amount 10 :order-status :order-status/waiting :client-company [:company/id 1] :client-person [:user/id 1]})
(add-new-proposal {:id 3 :supplier-id [:company/id 4] :amount 239000 :project-id [:project/id 1] :item-amount 20 :order-status :order-status/waiting :client-company [:company/id 1] :client-person [:user/id 1]})
(add-new-proposal {:id 4 :supplier-id [:company/id 1] :amount 239000 :project-id [:project/id 2] :item-amount 15 :order-status :order-status/waiting :client-company [:company/id 4] :client-person [:user/id 4]})





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
(add-new-proposal {:id 1 :supplier-id [:company/id 2] :amount 110000 :project-id [:project/id 1] :item-amount 10 :order-status :order-status/waiting :client-company [:company/id 1] :client-person [:user/id 1]})
(add-new-proposal {:id 2 :supplier-id [:company/id 3] :amount 119000 :project-id [:project/id 1] :item-amount 10 :order-status :order-status/waiting :client-company [:company/id 1] :client-person [:user/id 1]})


(proposal-by-companyid-and-status 1 :order-status/waiting)
;=> (["IBM" 10 :order-status/ongoing 110000 11000] ["HP" 10 :order-status/ongoing 119000 11900])
(proposal-by-userid-and-status 1 :order-status/waiting)
;=> (["apple" 20 :order-status/waiting 239000])
(proposal-by-userid-and-status 1 :order-status/waiting)
;=> (["IBM" 10 :order-status/ongoing 110000] ["HP" 10 :order-status/ongoing 119000])
(proposal-by-userid-and-status 4 :order-status/waiting)
;=> (["TEI" 15 :order-status/waiting 239000])
(proposal-by-companyid-and-status 1 :order-status/waiting)
;=> (["apple" 20 :order-status/waiting 239000])
(proposal-by-user-id 1)
;=>
;(["IBM" 10 :order-status/ongoing 110000]
; ["HP" 10 :order-status/ongoing 119000]
; ["apple" 20 :order-status/waiting 239000])





(sort-by last (mapv (fn [[_ x _ y :as row]] (conj row :derived (/ y x))) (proposal-by-companyid-and-status 1 :order-status/waiting)))
;=> (["IBM" 10 :order-status/ongoing 110000 :derived 11000] ["HP" 10 :order-status/ongoing 119000 :derived 11900])

(map (fn [arghs] (zipmap [:supplier :amount :order-status :totalprice :unit-price] arghs)) (map (fn [x] (let [[supplier amount order-status price] x]
                                                                                                          (conj [supplier amount order-status price] (long (/ price amount))))) (->> (d/q '[:find ?si ?ia ?os ?p
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
                                                                                                                                                                                     )))
;=>
;({:supplier "IBM", :amount 10, :order-status :order-status/ongoing, :totalprice 110000, :unit-price 11000}
; {:supplier "HP", :amount 10, :order-status :order-status/ongoing, :totalprice 119000, :unit-price 11900}
; {:supplier "apple", :amount 20, :order-status :order-status/waiting, :totalprice 239000, :unit-price 11950}
; {:supplier "TEI", :amount 15, :order-status :order-status/waiting, :totalprice 239000, :unit-price 15933})

(map (fn [x] (let [[supplier amount order-status price] x]
               (conj [supplier amount order-status price] (long (/ price amount))))) (->> (d/q '[:find ?si ?ia ?os ?p
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
                                                                                          ))
;=>
;(["IBM" 10 :order-status/ongoing 110000 11000]
; ["HP" 10 :order-status/ongoing 119000 11900]
; ["apple" 20 :order-status/waiting 239000 11950]
; ["TEI" 15 :order-status/waiting 239000 15933])

;;;---------------- bitti
