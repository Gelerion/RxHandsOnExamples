package com.gelerion.learning.rx.v3.two.observables;

import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Created by denis.shuvalov on 06/12/2017.
 *
 * In zip() transformation, we simply compare the time difference between creation of events in each stream.
 * When streams are synchronized, this value oscillates around zero. However, if we slightly slow down one
 * Observable, say green becomes Observable.interval(11, MILLISECONDS), the situation is much different.
 * The time difference between red and green keeps going up: red is consumed in real time but it must wait,
 * increasing the amount of time for the slower item. Over time this difference piles up and can lead to
 * stale data or even memory leak
 */
public class EventsEmitFrequency {

    public static void main(String[] args) throws InterruptedException {
        sameFrequency();

        TimeUnit.SECONDS.sleep(10);
    }

    private static void sameFrequency() {
        Observable<Long> red   = Observable.interval(10, TimeUnit.MILLISECONDS);
        Observable<Long> green = Observable.interval(10, TimeUnit.MILLISECONDS);

        Observable.zip(
                red.timestamp(),
                green.timestamp(),
                (r, g) -> r.getTimestampMillis() - g.getTimestampMillis()
        ).forEach(System.out::println);
    }

}
