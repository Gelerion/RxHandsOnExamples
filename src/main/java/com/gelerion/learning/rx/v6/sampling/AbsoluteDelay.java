package com.gelerion.learning.rx.v6.sampling;

import com.gelerion.learning.rx.utils.Observables;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Created by denis.shuvalov on 21/12/2017.
 */
public class AbsoluteDelay {

    public static void main(String[] args) throws InterruptedException {
        Observable<String> delayedNames = delayedNames();

        //Linda -> Barbara -> Susan -> Dorothy
        delayedNames
                .sample(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(10);

        //Mary -> Barbara -> Elizabeth -> Margaret
        delayedNames
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);

    }

    public static Observable<String> delayedNames() {
        Observable<String> names = Observable
                .just("Mary", "Patricia", "Linda", //last one
                        "Barbara",
                        "Elizabeth", "Jennifer", "Maria", "Susan",
                        "Margaret", "Dorothy");

        Observable<Long> absoluteDelayMillis = Observable
                .just(0.1, 0.6, 0.9,
                        1.1,
                        3.3, 3.4, 3.5, 3.6,
                        4.4, 4.8)
                .map(d -> (long) (d * 1_000));


        return Observable
                .zip(names, absoluteDelayMillis,
                        (n, d) -> Observable
                                .just(n)
                                .delay(d, TimeUnit.MILLISECONDS))
                .flatMap(Observables.identity());
    }

}
