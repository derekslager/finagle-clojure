(defproject finagle-clojure/thrift "0.1.0"
  :description "A light wrapper around finagle-thrift for Clojure"
  :url "https://github.com/crashlytics/finagle-clojure"
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.6.0"]
                                  [midje "1.5.1" :exclusions [org.clojure/clojure]]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}}
  :repositories [["twitter" {:url "http://maven.twttr.com/" :checksum :warn}]]
  :dependencies [[com.twitter/finagle-thrift "6.6.2"]
                 [org.apache.thrift/libthrift "0.5.0-1"]])