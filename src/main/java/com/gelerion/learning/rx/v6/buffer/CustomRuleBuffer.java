package com.gelerion.learning.rx.v6.buffer;

import com.gelerion.learning.rx.v6.buffer.model.TeleData;
import rx.Observable;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static rx.Observable.empty;

/**
 * Created by denis.shuvalov on 21/12/2017.
 *
 * <p>
 * During business hours (9:00–17:00), we take 100-millisecond long snapshots every second (processing approximately 10% of data)
 * <p>
 * Outside business hours we look only at 200-millisecond long snapshots taken every 5 seconds (4%)
 */
public class CustomRuleBuffer {

    public static void main(String[] args) throws InterruptedException {
/*
        Observable<TeleData> upstream = //...
        Observable<List<TeleData>> samples = upstream.buffer(openings);

        Notice how we pass carefully crafted openings stream to the buffer() operator. The preceding code example
        slices the upstream source of TeleData values. The ticking clock of openings stream batches events from upstream.

        Within business hours, a new batch is created every second, outside business hours, batches
        group values in five-second periods. Importantly in this version, all events from upstream are preserved
        because they either land in one batch or the other.
*/

        Observable<TeleData> upstream = Observable
                //The second parameter is an Observable that must complete whenever we want to stop sampling.
                .defer(() -> Observable.just(new TeleData())
                                       .delay(ThreadLocalRandom.current().nextInt(30), TimeUnit.MILLISECONDS))
                .repeat(1000);

        long startTime = System.currentTimeMillis();
/*

        upstream
                .buffer(openings()) //batch once value emitted, equivalent to obs.buffer(Observable.interval(...))
                .map(List::size)
                .timestamp()
                .subscribe(a -> System.out.println(a.getTimestampMillis() - startTime + "ms: Emitted value sized: " + a.getValue()));

        //output:
//            1092ms: Emitted value sized: 62
//            2058ms: Emitted value sized: 63
//            3058ms: Emitted value sized: 65
*/

        //Importantly in this version, all events from upstream are preserved because they either land in one batch
        //or the other. However, an overloaded version of the buffer() operator also allows marking the end of a batch:

        upstream
                .buffer(openings(), duration -> empty().delay(duration.toMillis(), TimeUnit.MILLISECONDS))
                .map(List::size)
                .timestamp()
                .subscribe(a -> System.out.println(a.getTimestampMillis() - startTime + "ms: Emitted value sized: " + a.getValue()));

        //output
//            1184ms: Emitted value sized: 6
//            2152ms: Emitted value sized: 5
//            3152ms: Emitted value sized: 7

        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * First using the interval() operator we generate timer ticks every second but exclude those that are not within business hours.
     * This way, we get a steady clock ticking every second between 9:00 and 17:00.
     */
    private static Observable<Duration> openings() {
        Observable<Duration> insideBusinessHours = Observable
                .interval(1, TimeUnit.SECONDS)
                .filter(x -> isBusinessHour())
                .map(x -> Duration.ofMillis(100));

        Observable<Duration> outsideBusinessHours = Observable
                .interval(5, TimeUnit.SECONDS)
                .filter(x -> !isBusinessHour())
                .map(x -> Duration.ofMillis(200));

        return Observable.merge(insideBusinessHours, outsideBusinessHours);
    }

    private static final LocalTime BUSINESS_START = LocalTime.of(9, 0);
    private static final LocalTime BUSINESS_END = LocalTime.of(17, 0);
    private static boolean isBusinessHour() {
        ZoneId zone = ZoneId.of("Europe/Warsaw");
        ZonedDateTime zdt = ZonedDateTime.now(zone);
        LocalTime localTime = zdt.toLocalTime();
        return !localTime.isBefore(BUSINESS_START) && !localTime.isAfter(BUSINESS_END);
    }
}
