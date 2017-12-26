package com.gelerion.learning.rx.v4.concurrency.scheduler.trampoline;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by denis.shuvalov on 17/12/2017.
 */
public class TrampolineThread {

    public static void main(String[] args) {
//        main	| Main start
//        main	|  Outer start
//        main	|   Inner start
//        main	|   Inner end
//        main	|  Outer end
//        main	| Main end
        //immediateExample();

//        main	| Main start
//        main	|  Outer start
//        main	|  Outer end
//        main	|   Inner start
//        main	|   Inner end
//        main	| Main end
        trampolineExample();
    }

    static void trampolineExample() {
        Scheduler trampoline = Schedulers.trampoline();
        Scheduler.Worker worker = trampoline.createWorker();

        log("Main start");
        worker.schedule(() -> {
            log(" Outer start");
            sleepOneSecond();
            worker.schedule(() -> {
                log("  Inner start");
                sleepOneSecond();
                log("  Inner end");
            });
            log(" Outer end");
        });
        log("Main end");
        worker.unsubscribe();
    }

    static void immediateExample() {
        Scheduler immediate = Schedulers.immediate();
        Scheduler.Worker worker = immediate.createWorker();

        log("Main start");
        worker.schedule(() -> {
            log(" Outer start");
            sleepOneSecond();
            worker.schedule(() -> {
                log("  Inner start");
                sleepOneSecond();
                log("  Inner end");
            });
            log(" Outer end");
        });
        log("Main end");
        worker.unsubscribe();
    }

    static void log(Object label) {
        System.out.println(Thread.currentThread().getName()   + "\t| " + label);
    }

    private static void sleepOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
