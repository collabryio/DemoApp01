(ns demoapp)



(require '[datomic.client.api :as d])
(def client (d/client {:server-type :dev-local
                       :storage-dir :mem
                       :system      "ci"}))
(d/create-database client {:db-name "db01"})
(def conn (d/connect client {:db-name "db01"}))
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
    :db/valueType   :db.type/string
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
  [{:db/ident       :rfpinformation/number
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :rfpinformation/category
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :rfpinformation/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :rfpinformation/explanation
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   ])
(d/transact conn {:tx-data orderinformation-schema})
(def db (d/db conn))

(defn add-order-info [number category name explanation]
  (d/transact conn {:tx-data [{:rfpinformation/number      number
                               :rfpinformation/category    category
                               :rfpinformation/name        name
                               :rfpinformation/explanation explanation
                               }
                              ]})
  (def db (d/db conn))
  )


;supplier informations schema
(def selected-supplierinformation-schema
  [{:db/ident       :supplierinformation/supplierid
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :supplierinformation/suppliername
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :supplierinformation/supplierphonenumber
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :supplierinformation/supplieremail
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   ])
(d/transact conn {:tx-data selected-supplierinformation-schema})
(def db (d/db conn))

(defn add-selected-supplier-info [supplierid suppliername supplierphonenumber supplieremail]
  (d/transact conn {:tx-data [{:supplierinformation/supplierid          supplierid
                               :supplierinformation/suppliername        suppliername
                               :supplierinformation/supplierphonenumber supplierphonenumber
                               :supplierinformation/supplieremail       supplieremail
                               }
                              ]})
  (def db (d/db conn))
  )


;order informations schema
(def offer-schema
  [{:db/ident       :proposal/supplierid
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
  (d/transact conn {:tx-data [{:proposal/supplierid supplierid
                               :proposal/price      supplieroffer
                               }
                              ]})
  (def db (d/db conn))
  )

;1-Satın alma uzmanı, ''bilgisayar satın alma'' projesi oluşturmak için kullanıcı adı ve şifresiyle sisteme giriş yapar. @mockup: poms/M01
;(Örn: sau ismi: Beyza) (Örn: sau şifresi: 1234)

;1a; kullanıcı oluştur
(add-new-user 1 "baris" "123456")

;1b; add new suppliers

(add-new-supplier 1 "lenovo" "lenovo@gmail.com" "123456789000" "someone" "bilgisayar")
(add-new-supplier 2 "monster" "monster@gmail.com" "123456789000" "someone" "bilgisayar")
(add-new-supplier 3 "hp" "hp@gmail.com" "123456789000" "someone" "bilgisayar")
(add-new-supplier 4 "asus" "asus@gmail.com" "123456789000" "someone" "bilgisayar")


;2-Kullanıcı, ''proje oluştur'' butonuna tıklar.
;3-Sistem, proje hakkında girilmesi gereken ''genel bilgiler'' ekranını
;sunar.


;4-Satın alma uzmanı, proje genel bilgilerini doldurur: @mockup: poms/M02
;-Satın alma uzmanı ismi (Örn:Beyza)
;-Müşteri şirket ismi (Örn:TEİ)
;-Müşteri şirket telefon numarası (Örn:5314567834)
;-Proje ismi (Örn:bilgisayar satın alma projesi)
;-Proje başlangıç tarihi (Örn:10.06.2023)
;-Proje bitiş tarihi (Örn:10.06.2023)
;-Belgeler (Örn:sözleşme belgesi Örn:iletişim kuralları belgesi)

(add-pgi-info 1 [:user/id 1] "TEI" "5314567834" "bilgisayar satın alma projesi" "10.06.2023" "10.06.2023" "sözleşme belgesi")

;5-Kullanıcı, ileri butonuna tıklar ve sonraki aşamaya geçer.
;6-Sistem, proje hakkında girilmesi gereken ''sipariş bilgileri'' ekranını
;sunar.

;7-Satın alma uzmanı, sipariş bilgilerini doldurur: @mockup: poms/M03
;-Sipariş numarası (Örn:1)
;-Sipariş kategorisi (Örn:bilgisayar)
;-Sipariş İsmi (Örn:notebook)
;-Sipariş açıklaması (Örn:10adet,gri renk)

(add-order-info 1 "bilgisayar" "notebook" "10adet,gri renk")

;8-Kullanıcı, ileri butonuna tıklar ve sonraki aşamaya geçer.
;9-Sistem, aplikasyonunda kayıtlı olan bilgisayar tedarikçilerini sıralar.

(d/q
  '[:find (pull ?e [*])
    :where
    [?e :supplier/category "bilgisayar"]]
  db)




;10-Satın alma uzmanı, uygun bulduğu tedarikçileri seçer ve kaydet butonuna tıklar.
;-Tedarikçi numarası
;-Tedarikçi isimi
;-Tedarikçi telefon numarası
;-Tedarikçi e-maili

(add-selected-supplier-info 1 "lenovo" "12345678900" "lenovo@gmail.com")

;11-Kullanıcı, kaydet butonuna tıklar.

(add-supplier-offer [:supplier/id 1] 150000)
(add-supplier-offer [:supplier/id 2] 170000)
(add-supplier-offer [:supplier/id 3] 165000)
(add-supplier-offer [:supplier/id 4] 189000)

;12-Sistem, kullanıcının proje hakkında girdiği tüm bilgileri ''ön izleme
;ekranında'' gösterir.

(d/q
  '[:find (pull ?e [*])
    :where
    [?e :pgi/id _]]
  db)

(d/q
  '[:find (pull ?e [*])
    :where
    [?e :rfpinformation/number _]]
  db)

(d/q
  '[:find (pull ?e [*])
    :where
    [?e :supplier/id _]]
  db)



(take 1 (->> (d/q
               '[:find ?si ?p
                 :where
                 [?e :proposal/supplierid ?s]
                 [?s :supplier/label ?si]
                 [?e :proposal/price ?p]]
               db)
             (sort-by last)
             )
      )




