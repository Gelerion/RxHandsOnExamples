package com.gelerion.learning.rx.v4.concurrency.observation;

import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.gelerion.learning.rx.utils.CustomSchedulers.schedulerA;
import static com.gelerion.learning.rx.utils.CustomSchedulers.schedulerB;
import static com.gelerion.learning.rx.utils.CustomSchedulers.schedulerC;

/**
 * Created by denis.shuvalov on 18/12/2017.
 */
public class SimpleObserveExample {

    public static void main(String[] args) throws InterruptedException {
        log("Starting");
        final Observable<String> obs = simple();
        log("Created");
        obs
                .doOnNext(x -> log("Found 1: " + x))
                .observeOn(schedulerB())
                .doOnNext(x -> log("Found 2: " + x))
                .observeOn(schedulerC())
                .doOnNext(x -> log("Found 3: " + x))
                .subscribeOn(schedulerA())
                .subscribe(
                        x -> log("Got 1: " + x),
                        Throwable::printStackTrace,
                        () -> log("Completed")
                );
        log("Exiting");

        TimeUnit.SECONDS.sleep(3);
    }

    //observeOn() occurs somewhere in the pipeline chain, and this time, as opposed to subscribeOn(),
    //the position of observeOn() is quite important. No matter what Scheduler was running operators above
    //observeOn() (if any), everything below uses the supplied Scheduler
    public static void oneArgument() {
        log("Starting");
        Observable<String> obs = simple();
        log("Created");

        obs
                .doOnNext(x -> log("Found 1: " + x))
                .observeOn(schedulerA())
                .doOnNext(x -> log("Found 2: " + x))
                .subscribe(
                        x -> log("Got 1: " + x),
                        Throwable::printStackTrace,
                        () -> log("Completed")
                );

        log("Exiting");

        /*
        main	| Starting
        main	| Created
        main	| Subscribed
        main	| Found 1: A
        main	| Found 1: B
        main	| Exiting
        Sched-A-1	| Found 2: A
        Sched-A-1	| Got 1: A
        Sched-A-1	| Found 2: B
        Sched-A-1	| Got 1: B
        Sched-A-1	| Completed
         */
    }


    public static Observable<String> simple() {
        return Observable.create(subscriber -> {
            log("Subscribed");
            subscriber.onNext("A");
            subscriber.onNext("B");
            subscriber.onCompleted();
        });
    }

    static void log(Object label) {
        System.out.println(Thread.currentThread().getName() + "\t| " + label);
    }
}
