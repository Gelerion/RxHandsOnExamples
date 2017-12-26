package com.gelerion.learning.rx.v8.limitations;

import com.gelerion.learning.rx.v8.hystrix.collapsing.model.Book;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Created by denis.shuvalov on 26/12/2017.
 */
public class DistinctLimitation {

    //distinct holds all the data in-memory
    public static void main(String[] args) {
        //relaxed requirement
        Observable
                .range(0, Integer.MAX_VALUE)
                .map(x -> new Book())
                .window(10, TimeUnit.SECONDS)
                .flatMap(Observable::distinct);

        //.window(100)
        //.flatMap(Observable::toList)
        //This is equivalent to the following:
        //.buffer(100)
    }

}
