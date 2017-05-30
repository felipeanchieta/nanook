(defproject nanook "0.1.0-SNAPSHOT"
  :description "Account manager for a new bank written in Clojure"
  :url "http://github.com/felipeanchieta/nanook"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-time "0.13.0"]
                 [compojure "1.6.0"]
                 [ring/ring-jetty-adapter "1.6.1"]
                 [ring/ring-core "1.6.1"]
                 [ring/ring-json "0.5.0-beta1"]]
  :plugins [[lein-ring "0.12.0"]
            [lein-cljfmt "0.5.6"]
            [jonase/eastwood "0.2.4"]]
  :ring {:handler nanook.handler/app}
  :main nanook.handler
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
