package com.gelerion.learning.rx.v3.cqrs.criteia;

import java.util.UUID; /**
 * Created by denis.shuvalov on 10/12/2017.
 */
class Reservation {

    private UUID uuid;

    Reservation(UUID uuid) {
        this.uuid = uuid;
    }

    Reservation consume(ReservationEvent event) {
        //mutate myself
        return this;
    }

}
