package com.gelerion.learning.rx.v1.hello;

//import java.util.concurrent.Flow;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by denis.shuvalov on 03/12/2017.
 *
 * Normally, this can be done by using the built-in takeUntil() operator, but for the
 * time being we can unsubscribe manually
 */
public class SelfUnsubscribe {
    public static void main(String[] args) {

        Subscriber<String> subscriber = new Subscriber<>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
                if (s.equals("Java")) {
                    //Self unsubscribe
                    unsubscribe();
                }
            }
        };

        Observable<String> observable = Observable.create(s -> {
            s.onNext("ABC");
            s.onNext("Java");
            s.onNext("Denis");
            s.onNext("Elena");
            s.onCompleted();
        });

        observable.subscribe(subscriber);

    }
}
