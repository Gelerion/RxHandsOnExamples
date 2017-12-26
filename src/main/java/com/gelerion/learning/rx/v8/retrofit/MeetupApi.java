package com.gelerion.learning.rx.v8.retrofit;

import com.gelerion.learning.rx.v8.retrofit.model.Cities;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by denis.shuvalov on 25/12/2017.
 */
public interface MeetupApi {

    @GET("/2/cities")
    Observable<Cities> listCities(
            @Query("lat") double lat,
            @Query("lon") double lon
    );

}
