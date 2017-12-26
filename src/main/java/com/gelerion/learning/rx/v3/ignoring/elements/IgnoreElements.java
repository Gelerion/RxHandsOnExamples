package com.gelerion.learning.rx.v3.ignoring.elements;

import com.gelerion.learning.rx.v4.concurrency.explicit.BlockingToConcurrent;
import com.gelerion.learning.rx.v4.concurrency.explicit.model.Ticket;
import rx.Observable;

import java.util.List;

/**
 * Created by denis.shuvalov on 12/12/2017.
 *
 * It is easy to see that inner flatMap() in our example ignores response and returns an empty stream.
 * In such cases, flatMap() is an overkill; the ignoreElements() operator is far more efficient. ignoreElements()
 * simply ignores all emitted values and forwards onCompleted() or onError() notifications. Because we are ignoring
 * the actual response and just deal with errors, ignoreElements() works great here.
 */
public class IgnoreElements {

    public static void main(String[] args) {
        BlockingToConcurrent.ReactiveBookingService booking = new BlockingToConcurrent.ReactiveBookingService();

        Observable<Ticket> tickets = Observable.from(List.of(new Ticket()));

/*        List<Ticket> failures = tickets
                .flatMap(ticket ->
                        booking.rxSendEmail(ticket)
                               .ignoreElements()
                               .doOnError(Throwable::printStackTrace)
                               .onErrorReturn(error -> ticket)
                )
                .toList()
                .toBlocking()
                .single();*/
    }

}
