package com.gelerion.learning.rx.v1.cache;

import rx.Observable;

/**
 * Created by denis.shuvalov on 03/12/2017.
 *
 * If you would like to avoid calling create() for each subscriber and simply reuse events that were
 * already computed, there exists a handy cache() operator
 */
public class ReusableSubscriber {


    public static void main(String[] args) {
        Observable<Integer> ints = Observable.<Integer>create(s -> {
            log("Create hash: " + s.hashCode());
            s.onNext(42);
            s.onCompleted();
        })
         .cache(); //Operators wrap existing Observables, enhancing them, typically by intercepting subscription.
        //!!keeps a copy of all notifications internally

        log("Starting");
        ints.subscribe(i -> log("ElementA: " + i));
        ints.subscribe(i -> log("ElementB: " + i));
        log("Exit");
    }

    private static void log(Object msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }

}
