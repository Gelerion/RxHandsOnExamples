package com.gelerion.learning.rx.v8.retrofit;

import com.gelerion.learning.rx.v8.retrofit.model.Geoname;
import com.gelerion.learning.rx.v8.retrofit.model.SearchResult;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Objects;


/**
 * Created by denis.shuvalov on 25/12/2017.
 */
public interface GeoNames {

    default Observable<Integer> populationOf(String query) {
        return search(query)
                .concatMapIterable(SearchResult::getGeonames)
                .map(Geoname::getPopulation)
                .filter(Objects::nonNull)
                .singleOrDefault(0)
                //.doOnError(th -> log.warn("Falling back to 0 for {}", query, th))
                .onErrorReturn(th -> 0)
                .subscribeOn(Schedulers.io());
    }

    default Observable<SearchResult> search(String query) {
        return search(query, 1, "LONG", "some_user");
    }

    @GET("/searchJSON")
    Observable<SearchResult> search(
            @Query("q") String query,
            @Query("maxRows") int maxRows,
            @Query("style") String style,
            @Query("username") String username);

}
