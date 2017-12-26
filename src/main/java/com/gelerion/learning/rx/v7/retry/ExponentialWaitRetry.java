package com.gelerion.learning.rx.v7.retry;

import com.gelerion.learning.rx.utils.Observables;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.gelerion.learning.rx.v7.retry.RetryWithAttempts.ATTEMPTS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by denis.shuvalov on 24/12/2017.
 *
 * We can stop retrying when a certain exception appears or when too many attempts were already performed.
 * Moreover, we can adjust the delay time between attempts! For example, the first retry can appear immediately
 * but the delays between subsequent retries should grow exponentially
 */
public class ExponentialWaitRetry {

    public static void main(String[] args) throws InterruptedException {
        risky()
                .timeout(1, SECONDS)
                .doOnError(th -> System.err.println("Will retry: " + th.getMessage()))
                .retryWhen(failures -> failures
                        .zipWith(Observable.range(1, ATTEMPTS), ExponentialWaitRetry::handleRetryAttempt)
                        .flatMap(Observables.identity())
                )
                .subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(10);
    }

    static Observable<Long> handleRetryAttempt(Throwable err, int attempt) {
        switch (attempt) {
            case 1:
                return Observable.just(42L);
            case ATTEMPTS:
                return Observable.error(err);
            default:
                long expDelay = (long) Math.pow(2, attempt - 2);
                return Observable.timer(expDelay, SECONDS);
        }
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
