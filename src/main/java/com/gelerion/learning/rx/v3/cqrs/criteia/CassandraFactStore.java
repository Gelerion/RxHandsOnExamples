package com.gelerion.learning.rx.v3.cqrs.criteia;

import rx.Observable;

/**
 * Created by denis.shuvalov on 10/12/2017.
 */
public class CassandraFactStore implements FactStore {

    @Override
    public Observable<ReservationEvent> observe() {
        return Observable.just(new ReservationEvent());
    }

}