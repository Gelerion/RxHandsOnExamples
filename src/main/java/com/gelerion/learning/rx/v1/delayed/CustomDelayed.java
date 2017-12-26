package com.gelerion.learning.rx.v1.delayed;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import java.util.concurrent.TimeUnit;

/**
 * Created by denis.shuvalov on 03/12/2017.
 */
public class CustomDelayed {

    public static void main(String[] args) throws InterruptedException {
        Observable<String> observable = CustomDelayed.badDelayed("Denis").serialize();

        observable.subscribe(System.out::println);
    }

    static <T> Observable<T> delayed(T value) {
        return Observable.create(subscriber -> {
            Runnable run = () -> {
                sleep(10, TimeUnit.SECONDS);
                while (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(value);
                    subscriber.onCompleted();
                }
            };

            Thread thread = new Thread(run);
            thread.start();
            subscriber.add(Subscriptions.create(thread::interrupt));
        });
    }

    //what if the subscriber decides to unsubscribe one second after subscribing, long before the event is supposed to be emitted?
    static <T> Observable<T> badDelayed(T value) {
        return Observable.create(subscriber -> {
            Runnable run = () -> {
                sleep(10, TimeUnit.SECONDS);
                while (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(value);
                    subscriber.onCompleted();
                }
            };

            new Thread(run).start();
        });
    }

    static void sleep(int timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException ignored) {
            //intentionally ignored
        }
    }

}
