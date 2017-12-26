package com.gelerion.learning.rx.v5.fastest.travel.agency.service.regular;

import com.gelerion.learning.rx.v5.fastest.travel.agency.model.*;

import java.util.List;
import java.util.concurrent.*;

import static java.util.Collections.emptyList;

/**
 * Created by denis.shuvalov on 20/12/2017.
 *
 * Sell to a fastest responded travel agency
 */
public class SimpleBookingService {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SimpleBookingService bookingService = new SimpleBookingService();
        List<TravelAgency> agencies = emptyList();

        User user = bookingService.findById(12L);
        GeoLocation locate = bookingService.locate();

/*
        for (TravelAgency agency : agencies) {
            Flight flight = agency.search(user, locate);
            Ticket ticket = bookingService.book(flight);
        }
*/

        ExecutorService pool = Executors.newFixedThreadPool(5);

        CompletionService<Flight> completionService =
                new ExecutorCompletionService<>(pool);
        for (TravelAgency agency : agencies) {
            completionService.submit(() -> agency.search(user, locate));
        }
        Flight firstFlight = completionService.take().get(); //or pool
        Ticket ticket = bookingService.book(firstFlight);

    }

    private User findById(long id) {
        return new User();
    }

    private GeoLocation locate() {
        return new GeoLocation();
    }

    private Ticket book(Flight flight) {
        return new Ticket();
    }
}
