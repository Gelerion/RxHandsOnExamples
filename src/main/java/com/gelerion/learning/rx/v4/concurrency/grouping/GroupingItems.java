package com.gelerion.learning.rx.v4.concurrency.grouping;

import rx.Observable;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.gelerion.learning.rx.utils.CustomSchedulers.schedulerA;
import static com.gelerion.learning.rx.utils.Observables.identity;

/**
 * Created by denis.shuvalov on 18/12/2017.
 */
public class GroupingItems {

    public static void main(String[] args) throws InterruptedException {
        Observable<BigDecimal> totalPrice = Observable
                .just("bread", "butter", "egg", "milk", "tomato", "cheese", "tomato", "egg", "egg")
                .groupBy(identity())
                .flatMap(grouped -> grouped
                        .count()
                        .map(count -> Map.entry(grouped.getKey(), count)))
                .flatMap(order -> purchase(order.getKey(), order.getValue())
                        .subscribeOn(schedulerA()))
                .reduce(BigDecimal::add)
                .single();

        totalPrice.subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(2);
    }

    private static BigDecimal doPurchase(String prod, int qty) {
        log("Purchasing " + qty + " " + prod);
        //real logic here
        sleepOneSecond();
        log("Done " + qty + " " + prod);
        return new BigDecimal(qty);
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
