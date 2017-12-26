package com.gelerion.learning.rx.v6.buffer;

import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.gelerion.learning.rx.v6.sampling.AbsoluteDelay.delayedNames;

/**
 * Created by denis.shuvalov on 21/12/2017.
 */
public class TimePeriodBuffer {

    public static void main(String[] args) throws InterruptedException {
        Observable<String> delayedNames = delayedNames();

        delayedNames
                .buffer(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(10);
    }
}
