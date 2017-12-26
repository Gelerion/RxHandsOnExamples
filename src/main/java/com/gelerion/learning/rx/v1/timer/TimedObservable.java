package com.gelerion.learning.rx.v1.timer;

import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Created by denis.shuvalov on 04/12/2017.
 */
public class TimedObservable {
    public static void main(String[] args) {
        Observable.timer(1, TimeUnit.SECONDS) // an asynchronous equivalent of Thread.sleep()
                  .subscribe(System.out::println);

        Observable.interval(100, TimeUnit.MILLISECONDS)
                  .subscribe(System.out::println);
    }
}
