package com.gelerion.learning.rx.v6.backpressure;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by denis.shuvalov on 21/12/2017.
 */
public class BpSimplestExample {

    public static void main(String[] args) {
        Observable
                .range(1, 10)
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onStart() {
                        request(3);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        request(1);
                        System.out.println("Next " + integer);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

}
