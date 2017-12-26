package com.gelerion.learning.rx.v7.timeout;

import com.google.common.base.MoreObjects;
import rx.Observable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by denis.shuvalov on 24/12/2017.
 *
 * timeout() operator that listens to the upstream Observable, constantly monitoring how much time elapsed since
 * the last event or subscription. If it so happens that the silence between consecutive events is longer than a
 * given period, the timeout() operator publishes an error notification that contains TimeoutException. To better
 * understand how timeout() works, first let’s consider an Observable that emits only one event after a certain time.
 */
public class TimeoutExample {

    public static void main(String[] args) throws InterruptedException {

        confirmation()
                .timeout(210 /*190*/, TimeUnit.MILLISECONDS)
                .forEach(
                        System.out::println,
                        th -> {
                            if ((th instanceof TimeoutException)) {
                                System.out.println("Too long");
                            } else {
                                th.printStackTrace();
                            }
                        }
                );

        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * For the purposes of this demonstration, we will create an Observable that returns some Confirmation event
     * after 200 milliseconds. We simulate the latency by adding delay(100, MILLISECONDS). Moreover, we would like
     * to simulate additional latency between the event and completion notification. That is the purpose of empty()
     * Observable that normally just completes immediately but with the extra delay() it waits before sending a completion
     */
    static Observable<Confirmation> confirmation() {
        Observable<Confirmation> delayedBeforeCompletion = Observable
                .<Confirmation>empty()
                .delay(200, TimeUnit.MILLISECONDS);
        return Observable.just(new Confirmation())
                         .delay(100, TimeUnit.MILLISECONDS)
                         .concatWith(delayedBeforeCompletion);
    }

    private static class Confirmation {
        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                              .toString();
        }
    }
}
