package com.gelerion.learning.rx.v3.custom.operator;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by denis.shuvalov on 10/12/2017.
 */
public class OddOperator {

    public static void main(String[] args) {
        Observable<String> odd = Observable
                .range(1, 9)
                .lift(stringOfOdd());

        odd.subscribe(System.out::println);
        //Will emit: "1", "3", "5", "7" and "9" strings
    }

    static <T>Observable.Operator<String, T> stringOfOdd() {
        return new Observable.Operator<>() {

            private boolean odd = true;

            @Override
            public Subscriber<? super T> call(Subscriber<? super String> child) {
                //BROKEN return child -> new Subscriber<T>()
                return new Subscriber<>(child) {
                    @Override
                    public void onCompleted() {
                        child.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        child.onError(e);
                    }

                    @Override
                    public void onNext(T t) {
                        if (odd) {
                            child.onNext(t.toString());
                        } else {
                            request(1);
                        }

                        odd = !odd;
                    }
                };
            }
        };
    }

}
