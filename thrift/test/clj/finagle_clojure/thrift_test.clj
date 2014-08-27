(ns finagle-clojure.thrift-test
  (:import test.DogBreedService)
  (:require [finagle-clojure.thrift :as thrift]
            [finagle-clojure.futures :as f]
            [midje.sweet :refer :all]))

;; set *warn-on-reflection* after loading midje to skip its reflection warnings
(set! *warn-on-reflection* true)

(fact "finagle-interface"
  (thrift/finagle-interface 'some.Service) => 'some.Service$ServiceIface
  (thrift/finagle-interface 'some.Service$ServiceIface) => 'some.Service$ServiceIface
  (thrift/finagle-interface 'test.DogBreedService) => 'test.DogBreedService$ServiceIface
  (thrift/finagle-interface test.DogBreedService) => 'test.DogBreedService$ServiceIface
  (thrift/finagle-interface 'DogBreedService) => 'test.DogBreedService$ServiceIface
  (thrift/finagle-interface 'DogBreedService$ServiceIface) => 'DogBreedService$ServiceIface
  (thrift/finagle-interface DogBreedService) => 'test.DogBreedService$ServiceIface)

;;; This is a high level integration test of finagle-clojure/thrift
;;; See the Thrift service definition in test/resources/service.thrift
;;; It has been compiled into finagled Java classes at test/java/
;;; To regenerate the compiled Java classes run scrooge against the thrift definition:
;;;     lein finagle-clojure scrooge

(def dog-breed-service
  (thrift/service DogBreedService
    (breedInfo [breed-name]
      (if (not= breed-name "pomeranian")
        (f/value (test.BreedInfoResponse. breed-name true))
        (f/value (test.BreedInfoResponse. breed-name false))))))

(def dog-breed-server (thrift/serve ":9999" dog-breed-service))

(def dog-breed-client (thrift/client "localhost:9999" test.DogBreedService))

(defn beautiful-dog?
  "Is `dog-breed` beautiful?"
  [dog-breed]
  (-> dog-breed-client
      (.breedInfo dog-breed)
      (f/map [breed-info] (.beautiful breed-info))))

(fact "this all works"
  (f/await (beautiful-dog? "pit bull")) =>  true
  (f/await (beautiful-dog? "pomeranian")) =>  false)

;; shut down the thrift server so midje :autorun will work
(f/await (.close dog-breed-server))
