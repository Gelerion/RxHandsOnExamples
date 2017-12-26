package com.gelerion.learning.rx.v7.unit;

import rx.Notification;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.List;

/**
 * Created by denis.shuvalov on 24/12/2017.
 */
public class TestMaterialize {

    public static void main(String[] args) {
        Observable<Integer> obs = Observable
                .just(3, 0, 2, 0, 1, 0)
                .concatMapDelayError(x -> Observable.fromCallable(() -> 100 / x));

        Observable<Notification<Integer>> notification = obs
                .materialize();

        List<Notification.Kind> kinds = notification
                .map(Notification::getKind)
                .toList()
                .toBlocking()
                .single();

        System.out.println("kinds = " + kinds);
        //assertThat(kinds).containsExactly(OnNext, OnNext, OnNext, OnError);

        //The TestSubscriber class is quite simple: it stores all events and notifications it received
        //internally so that we can later query it
        TestSubscriber<Integer> ts = new TestSubscriber<>();
        obs.subscribe(ts);
        System.out.println("Values: " + ts.getOnNextEvents());
    }

}
