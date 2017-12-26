package com.gelerion.learning.rx.v4.concurrency.explicit.model;

import java.util.Random;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * Created by denis.shuvalov on 12/12/2017.
 */
public class Flight {

    public Flight() {
        Random rnd = new Random();
        int value = rnd.nextInt(5);

        if(value > 3) {
            try {
                Thread.sleep(300);
                System.out.println(String.format("[%s] is creating %s", Thread.currentThread().getName(), lookup().lookupClass().getSimpleName()));
            } catch (InterruptedException e) {
                System.out.println(String.format("[%s] interrupted", Thread.currentThread().getName()));
            }
        }
        else {
            System.out.println(String.format("[%s] is creating %s", Thread.currentThread().getName(), lookup().lookupClass().getSimpleName()));
        }

    }

    @Override
    public String toString() {
        return "Flight";
    }
}
