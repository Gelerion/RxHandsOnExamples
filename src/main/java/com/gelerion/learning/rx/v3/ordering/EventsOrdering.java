package com.gelerion.learning.rx.v3.ordering;

import rx.Observable;

import java.time.DayOfWeek;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by denis.shuvalov on 05/12/2017.
 */
public class EventsOrdering {


    public static void main(String[] args) throws InterruptedException {
        preserveOrder();
        //noOrder();
    }

    /**
     * When the first event (Sunday) appears from upstream, concatMap() subscribes to an Observable returned
     * from loadRecordsFor() and passes all events emitted from it downstream. When this inner stream completes,
     * concatMap() waits for the next upstream event (Monday) and continues.
     */
    private static void preserveOrder() throws InterruptedException {
        Observable.just(DayOfWeek.MONDAY, DayOfWeek.SUNDAY)
                  .concatMap(EventsOrdering::loadRecordFor)
                  .subscribe(event -> System.out.print(event + ", "));

        SECONDS.sleep(2);
    }

    //with flatMap event order is undefined
    //flatMap() uses the merge() operator internally that subscribes to all sub-Observables at the same time and does not make any distinction between them
    private static void noOrder() throws InterruptedException {
        Observable.just(DayOfWeek.MONDAY, DayOfWeek.SUNDAY)
                  .flatMap(EventsOrdering::loadRecordFor)
                  .subscribe(event -> System.out.print(event + ", "));

        SECONDS.sleep(2);
    }

    static Observable<String> loadRecordFor(DayOfWeek day) {
        switch (day) {
            case MONDAY:
                return Observable
                        .interval(90, MILLISECONDS) //generates increasing numbers from zero preceded by a fixed delay
                        .take(5)
                        .map(i -> "Mon-" + i);
            case SUNDAY:
                return Observable
                        .interval(65, MILLISECONDS)
                        .take(5)
                        .map(i -> "Sun-" + i);
            default:
                return Observable.empty();

        }
    }

}
