package com.gelerion.learning.rx.v1.hello;

import rx.Observable;
import rx.Subscription;

import static rx.internal.util.InternalObservableUtils.ERROR_NOT_IMPLEMENTED;

/**
 * Created by denis.shuvalov on 29/11/2017.
 */
public class InMemoryObservable {
    public static void main(String[] args) {
        Observable<Integer> observable = Observable.<Integer>create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onNext(4);
            s.onCompleted();
        }).doOnNext(num -> System.out.println(Thread.currentThread()));


        //subscribing to the Observable causes the work to be done
        Subscription subscription =
                observable.subscribe(System.out::println, ERROR_NOT_IMPLEMENTED, () -> System.out.println("Done"));
        //subscription.unsubscribe();

        //can be subscribed twice
        //observable.subscribe(System.out::println, ERROR_NOT_IMPLEMENTED, () -> System.out.println("Done"));
    }
}
