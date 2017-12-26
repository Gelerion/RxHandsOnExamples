package com.gelerion.learning.rx.v6.buffer;

import rx.Observable;

import java.util.List;
import java.util.Random;

import static rx.Observable.just;

/**
 * Created by denis.shuvalov on 21/12/2017.
 */
public class MovingAverage {

    public static void main(String[] args) {
        Random random = new Random();
        Observable
                .defer(() -> just(random.nextGaussian()))
                .repeat(1000)
                .buffer(100, 1)
                .map(MovingAverage::averageOfList)
                .subscribe(System.out::println);
    }

    private static double averageOfList(List<Double> list) {
        //collect(Collectors.averagingDouble(x -> x));
        return list.stream().mapToDouble(d -> d).average().getAsDouble();
    }

}
