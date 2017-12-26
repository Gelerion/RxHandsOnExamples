package com.gelerion.learning.rx.v4.concurrency.explicit.model;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * Created by denis.shuvalov on 12/12/2017.
 */
public class Ticket {

    public Ticket() {
        System.out.println(String.format("[%s] is creating %s", Thread.currentThread().getName(), lookup().lookupClass().getSimpleName()));
    }

    @Override
    public String toString() {
        return "Ticket";
    }

}
