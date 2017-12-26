package com.gelerion.learning.rx.v6.buffer;

import rx.Observable;

import java.util.List;

/**
 * Created by denis.shuvalov on 21/12/2017.
 *
 * The buffer() operator aggregates batches of events in real time into a List. However, unlike the toList()
 * operator, buffer() emits several lists grouping some number of subsequent events as opposed to just one
 * containing all events (like toList()). The simplest form of buffer() groups values from upstream Observable
 * into a lists of equal size
 */
public class BatchingValues {

    public static void main(String[] args) {
        Observable
                .range(1, 7)
                .buffer(3)
                .subscribe(list -> System.out.println("list = " + list));

        System.out.println("-----------------------------------------------");

        //A slightly more complex version allows you to configure how many oldest values from internal
        //buffer to drop when buffer() pushes the list downstream.
        Observable
                .range(1, 7)
                .buffer(3, 1)
                .subscribe(System.out::println);

        System.out.println("-----------------------------------------------");

        //Interestingly, the second parameter of buffer(int, int) (that specifies how many elements to skip
        //when the buffer is pushed downstream) can be bigger than the first argument, effectively skipping some elements!
        Observable<List<Integer>> odd = Observable
                .range(1, 7)
                .buffer(1, 2);
              //.flatMapIterable(list -> list);
        odd.subscribe(System.out::println);
    }

}
