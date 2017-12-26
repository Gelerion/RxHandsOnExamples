package com.gelerion.learning.rx.v3.two.observables;

import rx.Observable;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static rx.Observable.interval;

/**
 * Created by denis.shuvalov on 06/12/2017.
 */
public class CombineSlow {

    public static void main(String[] args) throws InterruptedException {
        //combineLatest();

        Observable<String> fast = interval(10, MILLISECONDS).map(x -> "F" + x);
        Observable<String> slow = interval(17, MILLISECONDS).map(x -> "S" + x);
        slow
          .withLatestFrom(fast, (s, f) -> s + ":" + f)
          .forEach(System.out::println);

//        S0:F1
//        S1:F2
//        S2:F4
//        S3:F5
//        S4:F7

        TimeUnit.SECONDS.sleep(5);
    }

    private static void combineLatest() {
        Observable.combineLatest(
                interval(17, MILLISECONDS).map(x -> "S" + x),
                interval(10, MILLISECONDS).map(x -> "F" + x),
                (s, f) -> f + ":" + s
        ).forEach(System.out::println);

//        F0:S0
//        F1:S0
//        F2:S0
//        F2:S1
//        F3:S1
//        F4:S1
    }

}
