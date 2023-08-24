(ns UC01d-20230818)
;rfr: 20230818-UC1D.pdf

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
  {:tx-data [{:db/ident :order-status/done}
             {:db/ident :order-status/waiting}
             {:db/ident :order-status/declined}
             {:db/ident :order-status/timeout}
             ]})
(def db (d/db conn))

(d/transact
  conn
  {:tx-data [{:db/ident :order-approval-status/accepted}
             {:db/ident :order-approval-status/declined}
             ]})
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



;proposal-status schema
(def proposal-status
  [{:db/ident       :ps/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :ps/proposal
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :ps/proposal-status
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   ])
(d/transact conn {:tx-data proposal-status})
(def db (d/db conn))

(defn add-new-proposal-status [map]
  (d/transact conn {:tx-data [{:ps/id              (get map :id)
                               :ps/proposal        (get map :proposal)
                               :ps/proposal-status (get map :proposal-status)
                               }
                              ]})
  (def db (d/db conn))
  )




;id proposal proposal-status  accepted-person


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
           [?ps :ps/proposal-status ?os]
           [?ps :ps/proposal ?e]
           [?e :proposal/client-company ?cid]
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
;8. Sistem, sipariş bilgilerini veri tabanına kaydeder, sonraki aşamaya (tedarikçi bilgileri formu)
;geçer ve kayıtlı tüm tedarikçileri isme göre sıralar. (A’dan Z’ye doğru)

(->>
  (d/q
    '[:find ?e
      :where
      [_ :company/brand-name ?e]]
    db)
  (sort-by str)
  )
;=> (["APPLE"] ["HP"] ["IBM"] ["TEI"])

;9. Satın alma uzmanı, fiyat isteyeceği tedarikçileri kayıtlı tedarikçi listesinden seçer
;10. Satın alma uzmanının ekleyeceği diğer tedarikçi, kayıtlı tedarikçi listesinde bulunmamaktadır.
;Satın alma uzmanı, "tedarikçi ekle" talimatını verir
(add-new-company {:id 5 :brand-name "ZYCCELL" :email "ZYCCELL@company.com" :phonenumber "1234567890" :category [:service/bilgisayar]})
;11. Sistem, yeni bir tablo sunar ve satın alma uzmanı bilgileri girer, sistem veri tabanına kaydeder
;12. Sistem, seçilen tedarikçileri veri tabanına kaydeder ve uzmana "ön izleme ekranında" gösterir



;veri tabanına kayıt edilen bütün tedarikçileri gösterir
(d/q
  '[:find ?e
    :where
    [_ :company/brand-name ?e]]
  db)
;=> [["APPLE"] ["IBM"] ["TEI"] ["HP"] ["ZYCCELL"]]


;13. Satın alma uzmanı, ön izleme ekranında bilgileri kontrol eder ve "proje başlat" talimatını verir.
;15. Sistem, tedarikçileri "teklif giriş ekranına" yönlendirir ve tedarikçiler, "teklif gir" talimatını
;verir, tekliflerini girerler.


(add-new-proposal {:id 1 :supplier-id [:company/id 2] :amount 139000 :project-id [:project/id 1] :item-amount 20 :order-status :order-status/waiting :client-company [:company/id 1] :client-person [:user/id 1]})
(add-new-proposal {:id 2 :supplier-id [:company/id 3] :amount 124000 :project-id [:project/id 1] :item-amount 20 :order-status :order-status/waiting :client-company [:company/id 1] :client-person [:user/id 1]})
(add-new-proposal {:id 3 :supplier-id [:company/id 4] :amount 135000 :project-id [:project/id 1] :item-amount 20 :order-status :order-status/waiting :client-company [:company/id 1] :client-person [:user/id 1]})

;16. Sistem, tedarikçilerin girdiği teklifleri veri tabanına kaydeder, satın alma uzmanına [bildirim
;gönderir].
;17. Satın alma uzmanı projeye girdiğinde, sistem "teklif kıyaslama ekranında" tedarikçilerin
;tekliflerini en iyi tekliften en kötü teklife doğru gösterir.


(->> (d/q
       '[:find ?si ?ia ?os ?p ?up
         :where
         [?e :proposal/id _]
         [?e :proposal/supplier-id ?s]
         [?e :proposal/item-amount ?ia]
         [?e :proposal/amount ?p]
         [?e :proposal/order-status ?ps]
         [?s :company/brand-name ?si]
         [?ps :db/ident ?os]
         [(UC01d-20230818/unit-price ?p ?ia) ?up]
         ]
       db)
     (sort-by last)
     )

;=>
;(["HP" 20 :order-status/waiting 124000 6200]
; ["APPLE" 20 :order-status/waiting 135000 6750]
; ["IBM" 20 :order-status/waiting 139000 6950])


;18. Satın alma uzmanı, teklif kıyaslama ekranında uygun bulduğu tedarikçileri onaylar, sistem
;tedarikçilere [bildirim gönderir] ve proje sonlanır.
(add-new-proposal-status {:id 1 :proposal [:proposal/id 1] :proposal-status :order-approval-status/accepted})

(->> (d/q
       '[:find ?si ?ia ?p ?up
         :where
         [?ps :ps/proposal-status :order-approval-status/accepted]
         [?ps :ps/proposal ?e]
         [?e :proposal/supplier-id ?s]
         [?e :proposal/item-amount ?ia]
         [?e :proposal/amount ?p]
         [?s :company/brand-name ?si]
         [(UC01d-20230818/unit-price ?p ?ia) ?up]
         ]
       db)
     (sort-by last)
     )
;=> (["IBM" 20 139000 6950])

;19. Sistem, tüm paydaşlara tedarikçilerin hazırladığı siparişlerin durumlarını takip etmesi için
;"sipariş ekranı" sunar ve [bildirim gönderir]. Gerçekleşen her bir güncelleme, siparişler teslim
;edilene kadar ekranda gösterilir.

(proposal-by-companyid-and-status 1 :order-approval-status/accepted)

(->> (d/q
       '[:find ?bn ?ia ?os ?p
         :in $ ?cid ?os
         :where
         [?ps :ps/proposal-status ?os]
         [?ps :ps/proposal ?e]
         [?e :proposal/client-company ?cid]
         [?e :proposal/supplier-id ?si]
         [?e :proposal/item-amount ?ia]
         [?e :proposal/amount ?p]
         [?si :company/brand-name ?bn]
         [?ps :db/ident ?os]
         ]
       db (get-company-entity-id-by-id 1) :order-approval-status/accepted)
     (sort-by last)
     )