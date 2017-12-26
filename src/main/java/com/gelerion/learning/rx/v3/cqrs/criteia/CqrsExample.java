package com.gelerion.learning.rx.v3.cqrs.criteia;

import rx.Observable;
import rx.observables.GroupedObservable;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by denis.shuvalov on 10/12/2017.
 *
 * The process of applying events on top of an initial empty state is known as projection in event sourcing.
 * A single source of facts can drive multiple different projections. For example, we might have a stream of
 * facts related to a reservation system, like TicketReserved, ReservationConfirmed, and TicketBought—the past
 * tense is important because facts always reflect actions and events that already occurred. From a single
 * stream of facts (also being the single source of truth), we can derive multiple projections
 */
public class CqrsExample {

    public static void main(String[] args) {
        FactStore factStore = new CassandraFactStore();
        //Each ReservationEvent has a subclass for different types of events, like TicketBought
        Observable<ReservationEvent> facts = factStore.observe();

        /*
         * Remember that projections are independent from facts, they can use any other persistence mechanism or
         * even keep state in-memory. Moreover, you can have multiple projections consuming the same stream of
         * facts but building a different snapshot. For example, you can have an Accounting object that consumes
         * the same stream of facts but is only concerned about money coming in and out. Another projection might
         * only be interested in FraudDetected facts, summarizing fraudulent situations.
         */

        facts.subscribe(CqrsExample::updateProjection);

        //.flatMap(this::updateProjectionAsync) ->
        //  We quickly see a possible race-condition: two threads can consume different events, modify
        //  the same Reservation and try to store it—but the first update is overwritten and effectively lost.

        Observable<GroupedObservable<UUID, ReservationEvent>> grouped = facts
                .groupBy(ReservationEvent::getReservationUuid);

        grouped.subscribe(byUUID -> byUUID.subscribe(CqrsExample::updateProjection));


    }

    private static void updateProjection(ReservationEvent event) {
        UUID uuid = event.getReservationUuid();
        Reservation res = loadBy(uuid).orElse(new Reservation(uuid));
        res.consume(event);
        store(event.getUUID(), res);
    }

    private static Optional<Reservation> loadBy(UUID key) {
        return Optional.empty();
    }

    private static void store(UUID uuid, Reservation reservation) {
        //...
    }

}
