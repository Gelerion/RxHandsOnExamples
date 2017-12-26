package com.gelerion.learning.rx.v1.completable;

import rx.Completable;

/**
 * Created by denis.shuvalov on 03/12/2017.
 *
 * In addition to Single, RxJava also has a Completable type that addresses the surprisingly common use
 * case of having no return type, just the need to represent successful or failed completion. Often
 * Observable<Void> or Single<Void> ends up being used
 */
public class VoidAsReturn {
    public static void main(String[] args) {
        //Completable c = writeToDatabase("data");
    }

//    private static Completable writeToDatabase(Object data) {
//        return Completable.create(s -> {
//            doAsyncWrite(data,
//                    // callback for successful completion
//                    () -> s.onCompleted(),
//                    // callback for failure with Throwable
//                    error -> s.onError(error));
//        });
//    }
}
