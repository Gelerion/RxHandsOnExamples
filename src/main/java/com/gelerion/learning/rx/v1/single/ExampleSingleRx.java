package com.gelerion.learning.rx.v1.single;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

/**
 * Created by denis.shuvalov on 03/12/2017.
 *
 * Now, despite Rx Observable being great at handling multivalued streams, the simplicity of a single-valued
 * representation is very nice for API design and consumption. Additionally, basic request/response behavior
 * is extremely common in applications. For this reason, RxJava provides a Single type, which is a lazy equivalent
 * to a Future. Think of it as a Future with two benefits: first, it is lazy, so it can be subscribed to multiple
 * times and easily composed, and second, it fits the RxJava API, so it can easily interact with an Observable
 */
public class ExampleSingleRx {

    public static void main(String[] args) throws InterruptedException {
        Observable<String> a_merge_b = getDataA().mergeWith(getDataB());

        //executed inside RxIoScheduler thread
        a_merge_b.subscribe(System.out::println);

        Thread.sleep(100);
    }

    private static Single<String> getDataA() {
        return Single.<String>create(s -> s.onSuccess("DataA"))
                .subscribeOn(Schedulers.io());
    }

    private static Single<String> getDataB() {
        return Single.just("DataB").subscribeOn(Schedulers.io());
    }

}
