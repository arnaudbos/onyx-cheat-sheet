(ns onyx-cheat-sheet.dev
  (:require [environ.core :refer [env]]
            [net.cgrand.enlive-html :refer [set-attr prepend append html]]
            [cemerick.piggieback :as piggieback]
            [weasel.repl.websocket :as weasel]
            [figwheel-sidecar.repl-api :as fig]
            ;[figwheel-sidecar.auto-builder :as fig-auto]
            ;[figwheel-sidecar.core :as fig]
            [clojure.java.shell :refer [sh]]))

(def is-dev? (env :is-dev))

(def inject-devmode-html
  (comp
     (set-attr :class "is-dev")
     #_(prepend (html [:script {:type "text/javascript" :src "/js/out/goog/base.js"}]))
     #_(append  (html [:script {:type "text/javascript"} "goog.require('onyx_cheat_sheet.main')"]))))

(defn browser-repl []
  (let [repl-env (weasel/repl-env :ip "0.0.0.0" :port 9001)]
    (piggieback/cljs-repl :repl-env repl-env)))

(defn start-figwheel []
  (fig/start-figwheel!)
  #_(let [server (fig/start-server { :css-dirs ["resources/public/css"] })
        config {:builds [{:id "dev"
                          :source-paths ["src/cljs" "env/dev/cljs"]
                          :compiler {:output-to            "resources/public/js/app.js"
                                     :output-dir           "resources/public/js/out"
                                     :source-map           true
                                     :optimizations        :none
                                     :source-map-timestamp true
                                     :preamble             ["react/react.min.js"]}}]
                :figwheel-server server}]
    (fig-auto/autobuild* config)))
