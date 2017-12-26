package com.gelerion.learning.rx.v3.cqrs.criteia;

import rx.Observable;

/**
 * Created by denis.shuvalov on 10/12/2017.
 */
public interface FactStore {

    Observable<ReservationEvent> observe();

}
