package com.gelerion.learning.rx.v5.fastest.travel.agency.model;

import rx.Observable;

import java.util.concurrent.CompletableFuture;

/**
 * Created by denis.shuvalov on 20/12/2017.
 */
public interface TravelAgency {

    Flight search(User user, GeoLocation location);

    CompletableFuture<Flight> searchAsync(User user, GeoLocation location);

    Observable<Flight> searchRx(User user, GeoLocation location);
}
