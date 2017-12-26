package com.gelerion.learning.rx.v2.connect;

import rx.Observable;
import rx.observables.ConnectableObservable;

/**
 * Created by denis.shuvalov on 05/12/2017.
 */
public class EmulateSubscriber {
    public static void main(String[] args) {
        Observable<String> observable = Observable.create(s -> {
            s.onNext("abc");
            s.onCompleted();
        });


        //The idiomatic solution is to use publish().connect() duet that creates an artificial
        //Subscriber immediately while keeping just one upstream Subscriber
        ConnectableObservable<String> published = observable.publish();
        published.connect();
    }
}
