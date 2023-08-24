(ns UC01a_20230804)

;rfr: 20230806-UC1a.pdf

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
   {:db/ident       :rfp/explanation
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   ])
(d/transact conn {:tx-data order-schema})
(def db (d/db conn))

(defn  add-new-order [number category name explanation]
  (d/transact conn {:tx-data [{:rfp/id          number
                               :rfp/category    category
                               :rfp/name        name
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
   ])
(d/transact conn {:tx-data proposal-schema})
(def db (d/db conn))

(defn add-new-proposal [id supplier-id amount project-id]
  (d/transact conn {:tx-data [{:proposal/id          id
                               :proposal/supplier-id supplier-id
                               :proposal/amount      amount
                               :proposal/project-id  project-id
                               }
                              ]})
  (def db (d/db conn))
  )


;; example
(add-new-company 1 "TEI" "info.tei@tei.com" "1234567890" [:service/konaklama])
(add-new-company 2 "IBM" "info.ibm@ibm.com" "1234567890" [:service/bilgisayar])
(add-new-company 3 "HP" "info.hp@hp.com" "1234567890" [:service/bilgisayar])
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
;7. Satın alma uzmanı, sipariş bilgileri formunu doldurur: - Sipariş numarası
;- Sipariş kategorisi
;- Sipariş ismi
;- Sipariş açıklaması
(add-new-order 1 [:service/bilgisayar] "notebook" "10 adet, gri renk")
;8. Sistem, aplikasyonda kayıtlı olan tedarikçilere bildirim gönderir.
;
(add-new-proposal 1 [:company/id 2] 150000 [:project/id 1])
(add-new-proposal 2 [:company/id 3] 139000 [:project/id 1])
;9. Sistem, tedarikçilerin girdiği teklifleri veri tabanına kaydeder ve en iyi tekliften en kötü teklife doğru satın alma uzmanına sıralayıp "teklif kıyaslama ekranında" gösterir.
;- Tedarikçi numarası
;- Tedarikçi ismi
;- Tedarikçi telefon numarası -Tedarikçi e maili
;-Tedarikçi teklifi
;10. Satın alma uzmanı, tedarikçi kıyaslama ekranından uygun gördüğü tedarikçiyi seçer ve sistem tedarikçiye bildirir.
(->> (d/q
       '[:find ?bn ?p
         :where
         [?e :proposal/supplier-id ?s]
         [?s :company/brand-name ?bn]
         [?e :proposal/amount ?p]]
       db)
     (sort-by last)
     )
;=> (["HP" 139000])


