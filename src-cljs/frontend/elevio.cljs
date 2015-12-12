(ns frontend.elevio
  (:require [frontend.utils :as utils]
            [goog.dom :as gdom]
            [goog.dom.classlist :as class-list]))

(def account-id "5639122987b91")

(defn get-root []
  (gdom/getElement "elevio-widget"))

(defn disable! []
  (when-let [el (get-root)]
    (gdom/removeNode el))
  (aset js/window "_elev" #js {}))

(defn enable! []
  (class-list/add js/document.body "circle-elevio")
  (aset js/window "_elev" (or (aget js/window "_elev") #js {}))
  ;; Make sure there are non-whitespace characters in the last_name field to get around Elevio form validation
  (let [last-name (aget js/elevSettings "last_name")]
    (when-not (re-find #"\S+" last-name)
      (aset js/elevSettings "last_name" "<nolastname>")))
  (let [set-elev! (partial aset (aget js/window "_elev"))]
    (set-elev! "account_id" account-id)
    (set-elev! "user" (aget js/window "elevSettings"))
    (set-elev! "translations"
          #js {"loading"
               #js {"loading_ticket" "Loading support request"
                    "loading_tickets" "Loading support requests"
                    "reloading_ticket" "Reloading support request"},
               "modules"
               #js {"support"
                    #js {"create_new_ticket" "Create new support request"
                         "submit" "Submit support request"
                         "reply_placeholder" "Write your reply here"
                         "no_tickets" "Currently no existing support requests"
                         "back_to_tickets" "Back to your support requests"
                         "deflect" "Before you submit a support request, please check to see if your question has already been answered on <a target=\"_blank\" href=\"https://discuss.circleci.com/\">Discuss</a>."}}})))
