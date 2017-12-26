package com.gelerion.learning.rx.v2.single;

import rx.Observable;
import rx.Subscription;
import twitter4j.Status;

import static com.gelerion.learning.rx.v2.twitter.TwitterStreamExample.observe;

/**
 * Created by denis.shuvalov on 04/12/2017.
 *
 * ConnectableObservable is a type of Observable that ensures there exists at most one Subscriber at all times,
 * but in reality there can be many of them sharing the same underlying resource
 */
public class SingleSubscription {

    public static void main(String[] args) {
        Observable<Status> lazy = observe()//.share()
                .publish() //ConnectableObservable
                .refCount();

        System.out.println("Before subscribers");
        Subscription sub1 = lazy.subscribe();
        System.out.println("Subscribed 1");
        Subscription sub2 = lazy.subscribe();
        System.out.println("Subscribed 2");
        sub1.unsubscribe();
        System.out.println("Unsubscribed 1");
        sub2.unsubscribe();
        System.out.println("Unsubscribed 2");

/*        Here is the output:
                Before subscribers
                Establishing connection
                Subscribed 1
                Subscribed 2
                Unsubscribed 1
                Disconnecting
                Unsubscribed 2
     */
    }

}
