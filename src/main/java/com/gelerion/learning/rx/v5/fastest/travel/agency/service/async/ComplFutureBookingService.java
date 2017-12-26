package com.gelerion.learning.rx.v5.fastest.travel.agency.service.async;

import com.gelerion.learning.rx.v5.fastest.travel.agency.model.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.function.Function.identity;

/**
 * Created by denis.shuvalov on 20/12/2017.
 */
public class ComplFutureBookingService {

    public static void main(String[] args) {
        List<TravelAgency> agencies = Collections.emptyList();

        ComplFutureBookingService bookingService = new ComplFutureBookingService();

        CompletableFuture<User> user = bookingService.findById(12);
        CompletableFuture<GeoLocation> location = bookingService.locate();

/*
        for (TravelAgency agency : agencies) {
            Flight flight = agency.search(user, locate);
            Ticket ticket = bookingService.book(flight);
        }
*/
        //CompletableFuture.anyOf() - more verbose solution

        // 1. thenCombine()
        //    We need the results of both in order to proceed, of course without blocking and wasting the
        //    client thread. This is what thenCombine() is doing—it takes two CompletableFutures (user and location)
        //    and invokes a callback when both are completed, asynchronously.
        CompletableFuture<Ticket> ticketFuture = user
                // similar to zip()
                .thenCombine(location, (theUser, geo) ->
                        // 2. There exists a CompletableFuture.applyToEither() operator that accepts two CompletableFutures
                        //    and applies a given transformation on the first one to complete. The applyToEither()
                        //    transformation is extremely useful when you have two homogeneous tasks and you only care
                        //    about the first one to complete.
                        //    By iteratively calling applyToEither(), we get the CompletableFuture representing the fastest overall
                        agencies
                                .stream()
                                .map(agency -> agency.searchAsync(theUser, geo))
                                .reduce((f1, f2) -> f1.applyToEither(f2, identity()))
                                .get())
                //thenApply acts like map()
                //thenCompose acts like flatMap()
                .thenCompose(identity())
                .thenCompose(flight -> bookingService.book(flight));

    }

    private CompletableFuture<User> findById(long id) {
        return CompletableFuture.supplyAsync(User::new);
    }

    private CompletableFuture<GeoLocation> locate() {
        return CompletableFuture.supplyAsync(GeoLocation::new);
    }

    private CompletableFuture<Ticket> book(Flight flight) {
        return CompletableFuture.supplyAsync(Ticket::new);
    }
}
