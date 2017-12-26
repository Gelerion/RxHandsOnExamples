package com.gelerion.learning.rx.utils;

import rx.Observable;
import rx.functions.Func1;

import java.util.concurrent.CompletableFuture;

/**
 * Created by denis.shuvalov on 12/12/2017.
 */
public class Observables {

    public static <T> Func1<T, T> identity() {
        return t -> t;
    }

    /**
     * It is tempting to register an unsubscription handler that tries to cancel CompletableFuture in case of unsubscription:
     *
     *      Don't do this!
     *      subscriber.add(Subscriptions.create(
     *                  () -> future.cancel(true)));
     *
     * This is a bad idea. We can create many Observables based on one CompletableFuture, and every Observable can
     * have multiple Subscribers. If just one Subscriber decides to unsubscribe prior to Future’s completion,
     * cancellation will affect all other Subscribers.
     *
     * Remember that CompletableFuture is hot and cached using Rx terminology. It begins computation immediately,
     * whereas Observable will not start computation until someone actually subscribes.
     */
    public static <T> Observable<T> observe (CompletableFuture<T> future) {
        return Observable.create(subscriber -> {
            future.whenComplete((value, exception) -> {
                if (exception == null) {
                    subscriber.onNext(value);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(exception);
                }
            });
        });
    }

    /**
     * Keep in mind that this transformation has an important side effect: it subscribes to Observable,
     * thus forcing evaluation and computation of cold Observables. Moreover, each invocation of this
     * transformer will subscribe again; it is just a design choice that you must be aware of.
     */
    public static <T> CompletableFuture<T> toFuture(Observable<T> observable) {
        CompletableFuture<T> promise = new CompletableFuture<>();
        observable
                .single()
                .subscribe(
                        promise::complete,
                        promise::completeExceptionally
                );
        return promise;
    }

}
