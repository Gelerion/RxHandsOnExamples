package com.gelerion.learning.rx.test.hystrix;


import com.gelerion.learning.rx.v8.hystrix.commands.NonBlockingCmd;
import com.gelerion.learning.rx.v8.retrofit.MeetupApi;
import com.gelerion.learning.rx.v8.retrofit.model.Cities;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;

/**
 * Created by denis.shuvalov on 25/12/2017.
 */
public class HystrixInAction {

    private MeetupApi api;

    @Before
    public void setUp() throws Exception {
        api = mock(MeetupApi.class);
    }

    @Test
    public void some() throws InterruptedException {
        given(api.listCities(anyDouble(), anyDouble())).willReturn(
                Observable
                        .<Cities>error(new RuntimeException("Broken"))
                        .doOnSubscribe(() -> System.out.println("Invoking"))
                        .delay(2, TimeUnit.SECONDS)
        );

        //The default timeout is one second, so in fact you will never really see the "Broken"
        //exception because timeout will kick in first.

        //Now, we would like to invoke MeetupApi multiple times concurrently and see how Hystrix behaves:
        Observable<Cities> requesting = Observable
                .interval(50, TimeUnit.MILLISECONDS)
                .doOnNext(x -> System.out.println("Requesting"))
                .flatMap(x -> new NonBlockingCmd(api, 52.229841, 21.011736)
                        .toObservable()
                        .onErrorResumeNext(ex -> Observable.empty()), 5);

        requesting.subscribe();

        TimeUnit.MINUTES.sleep(1);
    }
}
