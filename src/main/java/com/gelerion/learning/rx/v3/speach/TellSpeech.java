package com.gelerion.learning.rx.v3.speach;

import rx.Observable;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static rx.Observable.just;

/**
 * Created by denis.shuvalov on 07/12/2017.
 */
public class TellSpeech {

    public static void main(String[] args) throws InterruptedException {
        Observable<String> alice = speak("To be, or not to be: that is the question", 110);
        Observable<String> bob = speak("Though this be madness, yet there is method in't", 90);
        Observable<String> jane = speak("There are more things in Heaven and Earth, " +
                        "Horatio, than are dreamt of in your philosophy", 100);

        //merge(alice, bob, jane);
        //concat(alice, bob, jane);
        Observable.switchOnNext(quotes(alice, bob, jane))
            .subscribe(System.out::println);


        TimeUnit.SECONDS.sleep(5);
    }

    private static void merge(Observable<String> alice, Observable<String> bob, Observable<String> jane) {
        Observable<String> speakers = Observable.merge(
                alice.map(w -> "Alice: " + w),
                bob.map(w -> "Bob: " + w),
                jane.map(w -> "Jane: " + w)
        );

        speakers.subscribe(System.out::println);
    }

    private static void concat(Observable<String> alice, Observable<String> bob, Observable<String> jane) {
        Observable<String> speakers = Observable.concat(
                alice.map(w -> "Alice: " + w),
                bob.map(w -> "Bob: " + w),
                jane.map(w -> "Jane: " + w)
        );

        speakers.subscribe(System.out::println);
    }

    /**
     *  switchOnNext() begins by subscribing to an outer Observable<Observable<T>>, which emits inner Observable<T>s.
     *  As soon as the first inner Observable<T> appears, this operator subscribes to it and begins pushing events of
     *  type T downstream. Now what happens if next inner Observable<T> appears? switchOnNext() discards the first
     *  Observable<T> by unsubscribing from it and switches to the next one (thus, the name). In other words, when
     *  we have a stream of streams, switchOnNext() always forwards downstream events from the last inner stream,
     *  even if older streams keep forwarding fresh events
     */
    private static Observable<Observable<String>> quotes(Observable<String> alice, Observable<String> bob, Observable<String> jane) {
        Random rnd = new Random();
        return just(
                alice.map(w -> "Alice: " + w),
                bob.map(w -> "Bob: " + w),
                jane.map(w -> "Jane: " + w)
        ).flatMap(inner -> just(inner).delay(rnd.nextInt(5), SECONDS));
    }

    private static Observable<String> speak(String quote, int delayPerChar) {
        String[] tokens = quote.replaceAll("[:,]", "").split(" ");

        Observable<String> words = Observable.from(tokens);
        Observable<Integer> absoluteDelay = words
                .map(String::length)
                .scan((delay, chars) -> delay += chars * delayPerChar);

        return words.zipWith(absoluteDelay.startWith(0), Map::entry)
                    .flatMap(pair -> just(pair.getKey()) //shift each word event forward in time so that it appears in the outer Observable much later
                                       .delay(pair.getValue(), MILLISECONDS));


    }

}
