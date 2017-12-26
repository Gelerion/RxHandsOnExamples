package com.gelerion.learning.rx.v7.pitfails;

import rx.Observable;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by denis.shuvalov on 24/12/2017.
 */
public class TwiceSubscribed {

    /**
     * We would like to transform this stream a little bit by calculating the duration (using Duration class
     * from the java.time package) between each subsequent pairs of Instants: first and second, second and
     * third, and so on. One way to do this is to zip() stream with itself shifted by one element. This way we
     * tie together the first with the second element, the second with the third, up to the end. What we did not
     * anticipate is that zipWith() actually subscribes to all of the underlying streams, effectively subscribing
     * to the same timestamps Observable twice.
     */
    public static void main(String[] args) {
        Observable<Instant> timestamps = Observable
                .fromCallable(() -> dbQuery())
                .doOnSubscribe(() -> System.out.println("subscribe()"))
                .doOnRequest(count -> System.out.println(String.format("requested(%s)", count))) //logs Requested 128, the value chosen by zip operator
                .doOnNext(instant -> System.out.println("Got: " + instant));

        timestamps
                .zipWith(timestamps.skip(1), Duration::between)
                .map(Object::toString)
                .subscribe(System.out::println);
    }

    private static Instant dbQuery() {
        return Instant.now();
    }

}
