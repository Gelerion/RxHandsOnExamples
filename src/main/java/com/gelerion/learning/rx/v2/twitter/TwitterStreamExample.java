package com.gelerion.learning.rx.v2.twitter;

import rx.Observable;
import rx.observers.Subscribers;
import rx.subscriptions.Subscriptions;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * Created by denis.shuvalov on 04/12/2017.
 */
public class TwitterStreamExample {

    public static void main(String[] args) {
        observe().subscribe(
                status -> Logger.getGlobal().info(format("Status: %s", status)),
                ex -> Logger.getGlobal().warning(format("Error callback %s", ex))
        );
    }

    public static Observable<Status> observe() {
        return Observable.create(subscriber -> {
            System.out.println("Establishing connection");
            //still opens http connection on each subscriber
            TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

            twitterStream.addListener(new TwitterStatusListener() {
                @Override
                public void onStatus(Status status) {
                    subscriber.onNext(status);
                }

                @Override
                public void onException(Exception ex) {
                    subscriber.onError(ex);
                }
            });

            twitterStream.sample();

            //release connection
            subscriber.add(Subscriptions.create(() -> {
                System.out.println("Disconnecting");
                twitterStream.shutdown();
            }));
        });
    }

    static void oldFashion() throws InterruptedException {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new TwitterStatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("Status: " + status);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        });
        twitterStream.sample();
        TimeUnit.SECONDS.sleep(10);
        twitterStream.shutdown();
    }

}
