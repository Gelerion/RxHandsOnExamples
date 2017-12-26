package com.gelerion.learning.rx.v3.cartesian.vacation;

import rx.Observable;

import java.time.LocalDate;

/**
 * Created by denis.shuvalov on 06/12/2017.
 */
public class VacationCalculator {

    public static void main(String[] args) {

        Observable<LocalDate> nextTenDays = Observable
                .range(1, 10)
                .map(i -> LocalDate.now().plusDays(1));

/*
        we end up with zip() operation of three streams, each emitting zero to one items. zip() completes
        early if any of the upstream Observables complete, discarding other streams early: thanks to this
        property, if any of weather, flight, or hotel is absent, the result of zip() completes with no
        items being emitted, as well. This leaves us with a stream of all possible vacation plans matching requirements.
*/
        Observable<Vacation> possibleVacations = Observable
                .just(City.Warsaw, City.London, City.NewYork)
                .flatMap(city -> nextTenDays.map(date -> new Vacation(city, date)))
                .flatMap(vacation -> Observable.zip(
                        vacation.weather().filter(Weather::isSunny),
                        vacation.cheapFlightFrom(City.Paris),
                        vacation.cheapHotel(),
                        (w, c, h) -> vacation
                ));
    }

}
