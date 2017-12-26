package com.gelerion.learning.rx.v7.timeout;

import rx.Observable;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static java.time.Month.*;

/**
 * Created by denis.shuvalov on 24/12/2017.
 * <p>
 * when you want to limit the time you wish to wait for a response or responses. However, sometimes a fixed timeout
 * threshold is too strict and you would like to adjust timeouts at runtime. Suppose that we built an algorithm
 * for predicting the next solar eclipse. The interface of that algorithm is an Observable<LocalDate> (of course!)
 * which streams future dates of these kinds of events. Imagine for a second that this algorithm is really
 * computationally intensive, which again we are going to simulate, this time by using the interval() operator
 * by zipping a fixed list of dates with a slowly progressing stream generated by interval(). The first date available
 * appears after 500 milliseconds, and every subsequent one after 50 milliseconds, thanks to interval(500, 50, MILLISECONDS).
 * This is quite common in real-life systems: the initial element of the response has relatively high latency as a
 * result of establishing the connection, SSL handshake, query optimization, or whatever the server is doing. But
 * subsequent responses are either readily available or easily retrievable, so latency between them is much lower
 */
public class FlexibleTimeout {

    public static void main(String[] args) throws InterruptedException {

        //In these types of scenarios, having one fixed threshold is problematic. The first event should have a
        //pessimistic limit, whereas subsequent limits should be much more aggressive. The overloaded version of
        //timeout() does just that: it accept two factories of Observables, one marking the timeout of the first
        //event, and the second one for each subsequent element. An example is worth a thousand words:
        nextSolarEclipse(LocalDate.of(2016, SEPTEMBER, 1))
                .timeout(
                        () -> Observable.timer(1000, TimeUnit.MILLISECONDS),
                        date -> Observable.timer(100, TimeUnit.MILLISECONDS)
                )
        .subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(5);
    }


    static Observable<LocalDate> nextSolarEclipse(LocalDate after) {
        return Observable
                .just(
                        LocalDate.of(2016, MARCH, 9),
                        LocalDate.of(2016, SEPTEMBER, 1),
                        LocalDate.of(2017, FEBRUARY, 26),
                        LocalDate.of(2017, AUGUST, 21),
                        LocalDate.of(2018, FEBRUARY, 15),
                        LocalDate.of(2018, JULY, 13),
                        LocalDate.of(2018, AUGUST, 11),
                        LocalDate.of(2019, JANUARY, 6),
                        LocalDate.of(2019, JULY, 2),
                        LocalDate.of(2019, DECEMBER, 26))
                .skipWhile(date -> !date.isAfter(after))
                .zipWith(
                        Observable.interval(500, 50, TimeUnit.MILLISECONDS),
                        (date, counter) -> date
                );
    }
}