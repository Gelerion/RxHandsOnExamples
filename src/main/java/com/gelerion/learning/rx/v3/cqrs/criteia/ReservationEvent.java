package com.gelerion.learning.rx.v3.cqrs.criteia;

import java.util.UUID;

/**
 * Created by denis.shuvalov on 10/12/2017.
 */
class ReservationEvent {

    private final UUID uuid = UUID.randomUUID();

    public UUID getReservationUuid() {
        return uuid;
    }

    public UUID getUUID() {
        return uuid;
    }
}
