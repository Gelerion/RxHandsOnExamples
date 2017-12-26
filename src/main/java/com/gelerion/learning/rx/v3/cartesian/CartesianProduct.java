package com.gelerion.learning.rx.v3.cartesian;

import rx.Observable;

/**
 * Created by denis.shuvalov on 06/12/2017.
 */
public class CartesianProduct {

    public static void main(String[] args) {
        Observable<Integer> oneToEight = Observable.range(1, 8);
        Observable<String> ranks = oneToEight.map(Object::toString);

        Observable<String> files = oneToEight
                .map(x -> 'a' + x - 1)
                .map(ascii -> (char) ascii.intValue())
                .map(ch -> Character.toString(ch));

        Observable<String> squares = files
                .flatMap(file -> ranks.map(rank -> file + rank));

        //a1, a2,…a8, followed by b1, b2, and so on until it finally reaches h7 and h8
        squares.subscribe(System.out::println);
    }
}
