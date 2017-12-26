package com.gelerion.learning.rx.v7.metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.Timer;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Created by denis.shuvalov on 24/12/2017.
 */
public class MetricExample {

    public static void main(String[] args) throws InterruptedException {
        MetricRegistry metricRegistry = new MetricRegistry();
        Slf4jReporter reporter = Slf4jReporter
                .forRegistry(metricRegistry)
                .outputTo(LoggerFactory.getLogger(MetricExample.class))
                .build();
        reporter.start(1, TimeUnit.SECONDS);

        Observable<Long> observable = Observable.interval(300, TimeUnit.MILLISECONDS)
                                                .take(10);

        //------------------------------------------------------------------------------------------
        //counter(metricRegistry);
        //------------------------------------------------------------------------------------------


        //We use a little trick. First, we lazily start time with a help of the defer() operator.
        //This way, the timer starts exactly when subscription happens.
        Timer timer = metricRegistry.timer("timer");
        Observable<Long> externalWithTimer = Observable
                .defer(() -> Observable.just(timer.time()))
                .flatMap(timerCtx -> observable.doOnCompleted(timerCtx::stop));

        externalWithTimer.subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(4);
    }

    private static void counter(MetricRegistry metricRegistry) throws InterruptedException {
        Counter counter = metricRegistry.counter("counter");
        Observable<Long> observable = Observable.interval(300, TimeUnit.MILLISECONDS)
                                                .take(10);

        //The preceding example assumes that makeNetworkCall() always returns just one item and never
        //fails (never completes with onError()
/*        observable
                .doOnNext(x -> counter.inc())
                .flatMap(MetricExample::makeNetworkCall)
                .doOnNext(x -> counter.dec())
                .subscribe(System.out::println);*/

        //If instead you want to measure the time between subscription to the internal Observable
        //(when the work actually began) and its completion, it is equally straightforward:
        observable
                .flatMap(x -> makeNetworkCall(x)
                        .doOnSubscribe(counter::inc)
                        .doOnTerminate(counter::dec)
                )
                .subscribe(System.out::println);


        TimeUnit.SECONDS.sleep(4);
    }

    static Observable<Long> makeNetworkCall(long x) {
        return Observable.defer(() -> Observable.just(1L).delay(200, TimeUnit.MILLISECONDS));
    }

}
