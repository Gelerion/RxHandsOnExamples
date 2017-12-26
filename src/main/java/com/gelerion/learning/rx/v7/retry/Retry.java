package com.gelerion.learning.rx.v7.retry;

import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Created by denis.shuvalov on 24/12/2017.
 *
 * The onError notification is terminal; no other event can ever appear in such stream. Therefore, if you want to
 * signal business conditions that are potentially nonfatal, avoid onError. This is not much different from a common
 * recommendation to avoid controlling the program flow by using exceptions. Instead, in Observables consider wrapping
 * errors in special types of events that can emerge multiple times next to ordinary events. For example, if you are
 * providing a stream of transaction results and some transactions can fail due to business reasons such as insufficient
 * funds, do not use onError notification for that. Instead, consider creating a TransactionResult abstract class with
 * two concrete subclasses, each representing either success or failure. onError notification in such a stream signals
 * that something is going terribly wrong, like a catastrophic failure preventing further emission of any event.
 */
public class Retry {

    /**
     * Remember that a slow system is generally indistinguishable from a broken one, but often it is even worse because
     * we experience additional latency. Having timeouts, sometimes even aggressive ones with a retry mechanism is
     * desirable—of course, as long as retrying has no side effects or the operation is idempotent.
     */
    public static void main(String[] args) throws InterruptedException {
        risky()
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(th -> System.err.println("Will retry: " + th.getMessage()))
                .retry()
                .subscribe(System.out::println);

        //A word of caution here: if your Observable is cached or otherwise guaranteed to always return
        //the same sequence of elements, retry() will not work:
        //  risky().cached().retry()  //BROKEN

        TimeUnit.SECONDS.sleep(10);
    }

    static Observable<String> risky() {
        return Observable.fromCallable(() -> {
            if (Math.random() < 0.1) {
                Thread.sleep((long) (Math.random() * 2000));
                return "OK";
            } else {
                throw new RuntimeException("Transient");
            }
        });
    }
}
