package com.gelerion.learning.rx.v8.retrofit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.gelerion.learning.rx.v8.retrofit.model.Cities;
import com.gelerion.learning.rx.v8.retrofit.model.City;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observable;

/**
 * Created by denis.shuvalov on 25/12/2017.
 */
public class RetroCall {

    public static void main(String[] args) {
        Retrofit retrofit = retrofit();

        MeetupApi meetup = retrofit.create(MeetupApi.class);

        double warsawLat = 52.229841;
        double warsawLon = 21.011736;
        Observable<Cities> cities = meetup.listCities(warsawLat, warsawLon);
        Observable<City> cityObs = cities.concatMapIterable(Cities::results);
        Observable<String> map = cityObs
                .filter(city -> city.distanceTo(warsawLat, warsawLon) < 50)
                .map(City::city);


    }

    private static Retrofit retrofit() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return new Retrofit.Builder()
                .baseUrl("https://api.meetup.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }

}
