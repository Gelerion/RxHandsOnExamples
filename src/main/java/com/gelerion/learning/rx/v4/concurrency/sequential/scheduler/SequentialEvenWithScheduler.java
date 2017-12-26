package com.gelerion.learning.rx.v4.concurrency.sequential.scheduler;

import rx.Observable;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static com.gelerion.learning.rx.utils.CustomSchedulers.schedulerA;

/**
 * Created by denis.shuvalov on 18/12/2017.
 */
public class SequentialEvenWithScheduler {

    public static void main(String[] args) throws InterruptedException {
        //sequential();
        idiomaticSolution();
        /*
        113  | Sched-A-1 | Purchasing 1 butter
        114  | Sched-A-0 | Purchasing 1 bread
        125  | Sched-A-2 | Purchasing 1 milk
        125  | Sched-A-3 | Purchasing 1 tomato
        126  | Sched-A-4 | Purchasing 1 cheese
        1126 | Sched-A-2 | Done 1 milk
        1126 | Sched-A-0 | Done 1 bread
        1126 | Sched-A-1 | Done 1 butter
        1128 | Sched-A-3 | Done 1 tomato
        1128 | Sched-A-4 | Done 1 cheese
        */
    }


    //each substream created within flatMap() is supplied with a schedulerA. Every time subscribeOn() is used
    //to the Scheduler gets a chance to return a new Worker, and therefore a separate thread
    //BUT! we can no longer rely on the order of downstream events
    private static void idiomaticSolution() throws InterruptedException {
        Observable<BigDecimal> totalPrice = Observable
                .just("bread", "butter", "milk", "tomato", "cheese")
                .flatMap(prod -> purchase(prod, 1).subscribeOn(schedulerA()))
                .reduce(BigDecimal::add)
                .single();

        totalPrice.subscribe(System.out::println); //5
        TimeUnit.SECONDS.sleep(2);

    }
    //BROKEN
    //The code does not work concurrently because there is just a single flow of events, which by design must run sequentially.
    private static void sequential() throws InterruptedException {
        Observable<BigDecimal> totalPrice = Observable
                .just("bread", "butter", "milk", "tomato", "cheese")
                .subscribeOn(schedulerA())  //BROKEN!!!
                .map(prod -> doPurchase(prod, 1))
                //.doOnNext(SequentialEvenWithScheduler::log)
                .reduce(BigDecimal::add)
                .single();

        totalPrice.subscribe(System.out::println); //5
        TimeUnit.SECONDS.sleep(6);

    }

    private static BigDecimal doPurchase(String prod, int qty) {
        log("Purchasing " + qty + " " + prod);
        //real logic here
        sleepOneSecond();
        log("Done " + qty + " " + prod);
        return new BigDecimal(1);
    }

    private static Observable<BigDecimal> purchase(String productName, int quantity) {
        return Observable.fromCallable(() -> doPurchase(productName, quantity));
    }

    static void log(Object label) {
        System.out.println(Thread.currentThread().getName() + "\t| " + label);
    }

    private static void sleepOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
