package com.gelerion.learning.rx.v6.sampling;

import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Created by denis.shuvalov on 21/12/2017.
 *
 * There are cases for which you definitely want to receive and process every single event pushed from the
 * upstream Observable. But, there are some scenarios for which periodic sampling is enough. The most obvious case
 * is receiving measurements from some device; for example, temperature. The frequency at which the device
 * produces new measurements is often irrelevant for us, especially when the measurements appear often but are
 * very similar to one another. The sample() operator looks at the upstream Observable periodically
 * (for example, every second) and emits the last encountered event. If there were no event at all in the last
 * one-second period, no sample is forwarded downstream and the next sample will be taken after one second
 */
public class PeriodicSamples {

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        //equivalent:
//        obs.sample(1, SECONDS);
//        obs.sample(Observable.interval(1, SECONDS));

        Observable
                .interval(7, TimeUnit.MILLISECONDS)
                .timestamp()
                .sample(1, TimeUnit.SECONDS)
                .map(ts -> ts.getTimestampMillis() - startTime + "ms: " + ts.getValue())
                .take(5)
                .subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(6);

    }

}
