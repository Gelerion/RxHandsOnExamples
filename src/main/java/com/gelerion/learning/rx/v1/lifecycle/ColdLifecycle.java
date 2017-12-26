package com.gelerion.learning.rx.v1.lifecycle;

import rx.Observable;

/**
 * Created by denis.shuvalov on 03/12/2017.
 *
 * new Subscriber is created and passed to Observable.create()  per subscribe call
 */
public class ColdLifecycle {

    public static void main(String[] args) {
        Observable<Integer> ints = Observable.create(s -> {
            log("Create hash: " + s.hashCode());
            s.onNext(42);
            s.onCompleted();
        });

        log("Starting");
        ints.subscribe(i -> log("ElementA: " + i));
        ints.subscribe(i -> log("ElementB: " + i));
        log("Exit");
    }

    private static void log(Object msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }

}
