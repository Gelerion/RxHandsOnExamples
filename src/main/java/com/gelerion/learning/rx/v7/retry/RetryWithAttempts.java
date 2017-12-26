package com.gelerion.learning.rx.v7.retry;

import rx.Observable;

import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by denis.shuvalov on 24/12/2017.
 */
public class RetryWithAttempts {
    static final int ATTEMPTS = 11;

    public static void main(String[] args) {
        risky()
                .timeout(1, SECONDS)
                .retry((attempt, error) -> attempt <= 10 && !(error instanceof TimeoutException));

/*      If failures are transient, waiting a little bit prior to a resubscription attempt sounds like a good idea.
        The retry() operator does not provide such a possibility out of the box, but it is relatively easy to implement.
        A more robust version of retry() called retryWhen() takes a function receiving an Observable of failures.
        Every time an upstream fails, this Observable emits a Throwable. Our responsibility is to transform this
        Observable in such a way that it emits some arbitrary event when we want to retry (hence the name):*/

        risky().timeout(1, SECONDS)
               .retryWhen(failures -> failures.delay(1, SECONDS));

/*      The preceding example of retryWhen() receives an Observable that emits a Throwable every time the upstream
        fails. We simply delay that event by one second so that it appears in the resulting stream one second later.
        If we simply returned the same stream (retryWhen(x -> x)), retryWhen() would behave exactly like retry(),
        resubscribing immediately when an error occurs
        */

        //------------------------------------------------------------------------------------------------------------

        //We receive an event each time a failure occurs. The stream we return is supposed to emit an arbitrary
        //event when we want to retry.
        risky().timeout(1, SECONDS)
               .retryWhen(failures -> failures.take(10));
        //Thus, we simply forward the first 10 failures, causing each one of them to be retried immediately.

        /*
        But what happens when eleventh failure occurs in a failures Observable? This is where it becomes tricky.
        The take(10) operator emits an onComplete event immediately following the 10th failure. Therefore, after the
        10th retry, retryWhen() receives a completion event. This completion event is interpreted as a signal to stop
        retrying and complete downstream. It means that after 10 failed attempts, we simply emit nothing and complete.
        However, if we complete Observable returned inside retryWhen() with an error, this error will be propagated
        downstream

        !!!
        In other words, as long as we emit any event from an Observable inside retryWhen(), they are interpreted as
        retry requests. However, if we send a completion or error notification, retry is abandoned and this completion
        or error is passed downstream. Doing just failures.take(10) will retry 10 times, but in case of yet another
        failure, we do not propagate the last error but the successful completion, instead
        */

        risky()
                .timeout(1, SECONDS)
                .retryWhen(failures -> failures
                        .zipWith(Observable.range(1, ATTEMPTS), (err, attempt) ->
                                attempt < ATTEMPTS ?
                                        Observable.timer(1, SECONDS) :
                                        Observable.error(err))
                        .flatMap(x -> x));
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
