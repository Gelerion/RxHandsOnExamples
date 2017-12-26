package com.gelerion.learning.rx.v4.concurrency.complex;

import rx.Observable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.gelerion.learning.rx.utils.CustomSchedulers.*;

/**
 * Created by denis.shuvalov on 18/12/2017.
 */
public class ConcComplex {

    //The production of events occurs in schedulerA, but each event is processed independently using schedulerB to improve concurrency,
    public static void main(String[] args) throws InterruptedException {
        log("Starting");
        Observable<String> obs = Observable.create(subscriber -> {
            log("Subscribed");
            subscriber.onNext("A");
            subscriber.onNext("B");
            subscriber.onNext("C");
            subscriber.onNext("D");
            subscriber.onCompleted();
        });

        log("Created");

        obs
                .subscribeOn(schedulerA())
                .flatMap(record -> store(record).subscribeOn(schedulerB()))
                .observeOn(schedulerC())
                .subscribe(
                        id -> log("Got " + id),
                        Throwable::printStackTrace,
                        () -> log("Completed")
                );

        log("Exiting");

        TimeUnit.SECONDS.sleep(5);

        /*
        26   | main  | Starting
        93   | main  | Created
        121  | main  | Exiting

        122  | Sched-A-0 | Subscribed
        124  | Sched-B-0 | Storing A
        124  | Sched-B-1 | Storing B
        124  | Sched-B-2 | Storing C
        124  | Sched-B-3 | Storing D

        1136 | Sched-C-1 | Got: 44b8b999-e687-485f-b17a-a11f6a4bb9ce
        1136 | Sched-C-1 | Got: 532ed720-eb35-4764-844e-690327ac4fe8
        1136 | Sched-C-1 | Got: 13ddf253-c720-48fa-b248-4737579a2c2a
        1136 | Sched-C-1 | Got: 0eced01d-3fa7-45ec-96fb-572ff1e33587
        1137 | Sched-C-1 | Completed
        */
    }

    public static Observable<UUID> store(String record) {
        return Observable.create(subscriber -> {
            log("Storing " + record);
            subscriber.onNext(UUID.randomUUID());
            subscriber.onCompleted();
        });
    }

    static void log(Object label) {
        System.out.println(Thread.currentThread().getName() + "\t| " + label);
    }

}
