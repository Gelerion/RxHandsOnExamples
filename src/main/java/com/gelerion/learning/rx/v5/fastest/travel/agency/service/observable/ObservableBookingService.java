package com.gelerion.learning.rx.v5.fastest.travel.agency.service.observable;

import com.gelerion.learning.rx.utils.Observables;
import com.gelerion.learning.rx.v5.fastest.travel.agency.model.*;
import rx.Observable;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * Created by denis.shuvalov on 20/12/2017.
 */
public class ObservableBookingService {

    public static void main(String[] args) {
        Observable<TravelAgency> agencies = Observable.from(new ArrayList<TravelAgency>());

        ObservableBookingService bookingService = new ObservableBookingService();
        Observable<User> user = bookingService.findById(12);
        Observable<GeoLocation> location = bookingService.locate();

        Observable<Ticket> ticket = Observable
                .zip(user, location, (theUser, geo) ->
                        agencies.flatMap(agency -> agency.searchRx(theUser, geo))
                                .first())
                .flatMap(Observables.identity())
                .flatMap(flight -> bookingService.book(flight));

/*
        Totally flat:
        Observable<Ticket> ticket = user
                .zipWith(location, (usr, loc) -> Pair.of(usr, loc))
                .flatMap(pair -> agencies
                        .flatMap(agency -> {
                            User usr = pair.getLeft();
                            GeoLocation loc = pair.getRight();
                            return agency.rxSearch(usr, loc);
                        }))
                .first() <---- First HERE
                .flatMap(this::rxBook);
*/
    }

    private Observable<User> findById(long id) {
        return Observables.observe(CompletableFuture.supplyAsync(User::new));
    }

    private Observable<GeoLocation> locate() {
        return Observables.observe(CompletableFuture.supplyAsync(GeoLocation::new));
    }

    private Observable<Ticket> book(Flight flight) {
        return Observables.observe(CompletableFuture.supplyAsync(Ticket::new));
    }
}
