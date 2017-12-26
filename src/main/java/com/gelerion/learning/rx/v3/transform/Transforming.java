package com.gelerion.learning.rx.v3.transform;

import rx.Observable;

/**
 * Created by denis.shuvalov on 05/12/2017.
 */
public class Transforming {

    public static void main(String[] args) {
        long id = 10;

        Observable<Integer> rating = uploadVideo(id)
                .flatMap(
                        bytes -> Observable.empty(), //not interesting in progress
                        error -> Observable.error(error),
                        () -> rate(id)
                );
    }

    //rate the upload video upon complete
    private static Observable<Integer> rate(long id) {
        return null;
    }


    //upload video and emits progress
    private static Observable<Integer> uploadVideo(long id) {
        return Observable.just(10, 20, 30, 40, 60, 80, 100);
    }

}
