package com.gelerion.learning.rx.v3.delay;

import rx.Observable;

import java.util.concurrent.TimeUnit;

import static rx.Observable.timer;

/**
 * Created by denis.shuvalov on 05/12/2017.
 */
public class DeplayedEvents {

    public static void main(String[] args) throws InterruptedException {
/*        Observable
                .just("Lorem", "ipsum", "dolor", "sit", "amet",
                        "consectetur", "adipiscing", "elit")
                .delay(1, TimeUnit.SECONDS) // shifts all events further in time
                .subscribe(System.out::println);*/

        Observable
                .just("Lorem", "ipsum", "dolor", "sit", "amet",
                        "consectetur", "adipiscing", "elit")
                .delay(word -> timer(word.length(), TimeUnit.SECONDS)) // delay per event basis
                .subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(15);

        //A - Observable appears immediately in the outer stream but begins emitting events with some delay
/*        map(innerObs ->
                innerObs.delay(rnd.nextInt(5), SECONDS))*/

        //B - shift the entire Observable event forward in time so that it appears in the outer Observable much later
/*        flatMap(innerObs -> just(innerObs)
                .delay(rnd.nextInt(5), SECONDS))*/
    }

}
