package com.gelerion.learning.rx.v3.repeat;

import rx.Observable;

import static rx.Observable.just;

/**
 * Created by denis.shuvalov on 10/12/2017.
 *
 * repeat() simply intercepts completion notification from upstream and rather than passing it downstream it
 * resubscribes. Therefore, it is not guaranteed that repeat() will keep cycling through the same sequence of
 * events, but it happens to be the case when upstream is a simple fixed stream.
 */
public class OddSequenceCompose {

    public static void main(String[] args) {
        //[A, B, C, D, E...]
        Observable<Character> alphabet = Observable
                .range(0, 'z' - 'a' + 1)
                .map(c -> (char) ('a' + c));

        //[A, C, E, G, I...]
        alphabet.compose(odd())
                .forEach(System.out::println);

    }

    static <T> Observable<T> odd(Observable<T> upstream) {
        Observable<Boolean> trueFalse = just(true, false).repeat();
        return upstream.zipWith(trueFalse, (value, condition) -> condition ? just(value) : Observable.<T>empty())
                                           .flatMap(obs -> obs);

/*        return upstream
                .zipWith(trueFalse, Pair::of)
                .filter(Pair::getRight)
                .map(Pair::getLeft)*/
    }

    static <T> Observable.Transformer<T, T> odd() {
        Observable<Boolean> trueFalse = just(true, false).repeat();
        return upstream -> upstream.zipWith(trueFalse, (value, condition) -> condition ? just(value) : Observable.<T>empty())
                                   .flatMap(obs -> obs);
    }

}
