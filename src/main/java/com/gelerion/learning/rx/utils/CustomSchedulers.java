package com.gelerion.learning.rx.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.concurrent.*;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * Created by denis.shuvalov on 18/12/2017.
 */
public class CustomSchedulers {

    public static Scheduler schedulerA() {
        ExecutorService poolA = newFixedThreadPool(10, threadFactory("Sched-A-%d"));
        Scheduler schedulerA = Schedulers.from(poolA);
        return schedulerA;

    }

    public static Scheduler schedulerB() {
        ExecutorService poolA = newFixedThreadPool(10, threadFactory("Sched-B-%d"));
        Scheduler schedulerA = Schedulers.from(poolA);
        return schedulerA;

    }

    public static Scheduler schedulerC() {
        ExecutorService poolA = newFixedThreadPool(10, threadFactory("Sched-C-%d"));
        Scheduler schedulerA = Schedulers.from(poolA);
        return schedulerA;

    }

    private static ThreadFactory threadFactory(String pattern) {
        return new ThreadFactoryBuilder()
                .setNameFormat(pattern)
                .setDaemon(true)
                .build();
    }

    public static Scheduler current() {
//        return Schedulers.from(new CurrentThreadExecutor());
//        return Schedulers.from(currentThreadExecutorService());
        return Schedulers.from(Runnable::run);
    }

    public static Scheduler current(Executor executor) {
        return Schedulers.from(executor);
    }


    static class CurrentThreadExecutor implements Executor {

        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }

    public static ExecutorService currentThreadExecutorService() {
        ThreadPoolExecutor.CallerRunsPolicy callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
        return new ThreadPoolExecutor(0, 1, 0L, TimeUnit.SECONDS,
                new SynchronousQueue<>(), callerRunsPolicy) {
            @Override
            public void execute(Runnable command) {
                System.out.println("Cur: " + Thread.currentThread().getName());
                callerRunsPolicy.rejectedExecution(command, this);
            }
        };
    }


}
