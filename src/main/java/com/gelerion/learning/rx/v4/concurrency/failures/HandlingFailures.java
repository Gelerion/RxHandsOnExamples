package com.gelerion.learning.rx.v4.concurrency.failures;

import com.gelerion.learning.rx.v4.concurrency.explicit.BlockingToConcurrent.ReactiveBookingService;
import com.gelerion.learning.rx.v4.concurrency.explicit.model.Ticket;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.List;

/**
 * Created by denis.shuvalov on 12/12/2017.
 *
 *  If it were just flatMap(this::rxSendEmail), code would work; however, any failure emitted from rxSendEmail
 *  would terminate the entire stream. But we want to “catch” all emitted errors and collect them for later consumption.
 *
 *  !Note
 *  Wrapping up if you are implementing Observables from scratch, making them asynchronous by default is more idiomatic.
 *  That means placing subscribeOn() directly inside rxSendEmail() rather than externally.
 */
public class HandlingFailures {

    public static void main(String[] args) {
        ReactiveBookingService booking = new ReactiveBookingService();

        Observable<Ticket> tickets = Observable.from(List.of(new Ticket()));

        List<Ticket> failures = tickets
                .flatMap(ticket ->
                        booking.rxSendEmail(ticket)
                               //discard successful tickets
                               .flatMap(smtpResponse -> Observable.<Ticket>empty())
                               .doOnError(Throwable::printStackTrace)
                               .onErrorReturn(error -> ticket))
                .toList()
                //A toBlocking() operator on its own doesn’t force evaluation by subscribing to the underlying stream
                //and it doesn’t even block. Subscription and thus iteration and sending emails is postponed until single() is invoked
                .toBlocking()
                .single();

        //However, to turn our sequential code into multithreaded computation we barely need to add one extra operator:
        tickets.flatMap(ticket ->
                booking.rxSendEmail(ticket)
                       .flatMap(smtpResponse -> Observable.<Ticket>empty())
                       .doOnError(Throwable::printStackTrace)
                       .onErrorReturn(err -> ticket)
                       //run in separate thread
                       .subscribeOn(Schedulers.io()));
    }

}
