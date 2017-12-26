package com.gelerion.learning.rx.v3.state.operators;

import rx.Observable;

/**
 * Created by denis.shuvalov on 06/12/2017.
 */
public class ProgressExample {

    public static void main(String[] args) {
        //file transfer
        Observable<Integer> progress = Observable.just(10, 14,12,13,14,16);

        Observable<Integer> totalProgress = progress.scan((total, chunk) -> total + chunk);

        totalProgress.forEach(System.out::println);
    }

}
