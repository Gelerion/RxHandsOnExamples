package com.gelerion.learning.rx.v4.concurrency.explicit;

import com.gelerion.learning.rx.v4.concurrency.explicit.model.Flight;
import com.gelerion.learning.rx.v4.concurrency.explicit.model.Passenger;
import com.gelerion.learning.rx.v4.concurrency.explicit.model.SmtpResponse;
import com.gelerion.learning.rx.v4.concurrency.explicit.model.Ticket;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.gelerion.learning.rx.utils.Observables.identity;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static rx.Observable.defer;
import static rx.Observable.fromCallable;
import static rx.Observable.just;

/**
 * Created by denis.shuvalov on 12/12/2017.
 *
 * subscribeOn()
 * When subscription occurs, the lambda expression passed to Observable.create() is executed within the
 * supplied Scheduler rather than the client thread
 *
 * timeout()
 * Talking about slowness, you can declaratively apply a timeout, as well, when a given Observable does not emit
 * any value in the specified amount of time
 * If method takes more than X milliseconds, you will end up with TimeoutException rather than an emitted value sent
 * downstream to every subscriber
 *
 * !Note
 * in practice, Schedulers and subscribeOn() are weapons of last resort, not something seen commonly
 */
public class BlockingToConcurrent {


    /**
     * drawbacks:
     *  1. The list can be potentially quite long.
     *  2. Sending an email might take several milliseconds or even seconds.
     *  3. The application must keep running gracefully in case of failures, but report in the end which tickets failed to be delivered.
     */
    public static void main(String[] args) throws InterruptedException {
        ReactiveBookingService booking = new ReactiveBookingService();

        Observable
                .zip(
                        booking.lookupFlight("Su 512")
                               .subscribeOn(Schedulers.io()).timeout(100, MILLISECONDS),
                        booking.findPassenger(1)
                               .subscribeOn(Schedulers.io()).timeout(120, MILLISECONDS),
                        booking::bookTicket
                )
                // You might perceive flatMap() as merely a syntactic trick to avoid a nested Observable<Observable<...>> problem, but it’s much more fundamental than this
                .flatMap(identity())
                .subscribe(booking::sendEmail, error -> System.err.println("The code is timing out: " + error))
        ;

        TimeUnit.MILLISECONDS.sleep(300);
    }


    /**
     * defer() as a tool for ensuring Observable code runs when subscribed (rather than when created)
     */
    public static class ReactiveBookingService {
        Observable<Flight> lookupFlight(String flightNo) {
            //...
            //without defer it'll block the thread because you're calling method() before passing it to Observable.just()
            return defer(() -> just(new Flight()));
        }

        Observable<Passenger> findPassenger(long id) {
            //...
            return defer(() -> just(new Passenger()));
        }

        Observable<Ticket> bookTicket(Flight flight, Passenger passenger) {
            //...
            return defer(() -> just(new Ticket()));
        }

        SmtpResponse sendEmail(Ticket ticket) {
            //...
            return new SmtpResponse();
        }

        public Observable<SmtpResponse> rxSendEmail(Ticket ticket) {
            //unusual synchronous Observable
            return fromCallable(() -> sendEmail(ticket));
        }
    }


    static class BookingService {
        Flight lookupFlight(String flightNo) {
            //...
            return new Flight();
        }

        Passenger findPassenger(long id) {
            //...
            return new Passenger();
        }

        Ticket bookTicket(Flight flight, Passenger passenger) {
            //...
            return new Ticket();
        }

        SmtpResponse sendEmail(Ticket ticket) {
            //...
            return new SmtpResponse();
        }
    }

}
