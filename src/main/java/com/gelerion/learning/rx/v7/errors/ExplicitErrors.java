package com.gelerion.learning.rx.v7.errors;

import rx.Observable;

/**
 * Created by denis.shuvalov on 21/12/2017.
 */
public class ExplicitErrors {

    public static void main(String[] args) {
        Observable
                .just(1, 0)
                .flatMap(x -> (x == 0) ?
                        Observable.error(new ArithmeticException("Zero :-(")) :
                        Observable.just(10 / x)
                );
    }

}
