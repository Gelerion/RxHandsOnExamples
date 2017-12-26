package com.gelerion.learning.rx.v3.cartesian.vacation;

import rx.Observable;

import java.time.LocalDate;

/**
 * Created by denis.shuvalov on 06/12/2017.
 */
class Vacation {
    private final City where;
    private final LocalDate when;

    Vacation(City where, LocalDate when) {
        this.where = where;
        this.when = when;
    }

    public Observable<Weather> weather() {
        //...
        return Observable.just(new Weather(new Temperature(), new Wind()));
    }

    public Observable<Flight> cheapFlightFrom(City from) {
        //...
        return Observable.just(new Flight());
    }

    public Observable<Hotel> cheapHotel() {
        //...
        return Observable.just(new Hotel());
    }
}
