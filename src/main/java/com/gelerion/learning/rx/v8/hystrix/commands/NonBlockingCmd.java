package com.gelerion.learning.rx.v8.hystrix.commands;

import com.gelerion.learning.rx.v8.retrofit.MeetupApi;
import com.gelerion.learning.rx.v8.retrofit.model.Cities;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;

/**
 * Created by denis.shuvalov on 25/12/2017.
 *
 * If your application was designed with RxJava in mind, the chances are that your actions involving third-party
 * services or unknown libraries are already modeled as Observables. The basic HystrixCommand supports only
 * blocking code. If your interactions with the outside world are already an Observable that you want to further
 * protect via Hystrix, the HystrixObservableCommand is much better suited:
 */
public class NonBlockingCmd extends HystrixObservableCommand<Cities> {

    private final MeetupApi api;
    private final double lat;
    private final double lon;

    public NonBlockingCmd(MeetupApi api, double lat, double lon) {
        super(HystrixCommandGroupKey.Factory.asKey("Meetup"));
        this.api = api;
        this.lat = lat;
        this.lon = lon;
    }


    /**
     * The advantage of the HystrixObservableCommand over HystrixCommand is that the former does not require a thread
     * pool to operate. HystrixCommands are always executed in a bound thread pool, whereas Observable commands do not
     * require any extra threads. Of course, Observable returned from construct() (notice that it is no longer named
     * run()) can still use some threads, depending on the underlying implementation.
     */
    @Override
    protected Observable<Cities> construct() {
        return api.listCities(lat, lon);
    }
}
