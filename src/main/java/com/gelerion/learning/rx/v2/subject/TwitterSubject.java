package com.gelerion.learning.rx.v2.subject;

import com.gelerion.learning.rx.v2.twitter.TwitterStatusListener;
import rx.Observable;
import rx.subjects.PublishSubject;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * Created by denis.shuvalov on 04/12/2017.
 */
public class TwitterSubject {

    private final PublishSubject<Status> subject = PublishSubject.create();

    public TwitterSubject() {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

        twitterStream.addListener(new TwitterStatusListener() {
            @Override
            public void onStatus(Status status) {
                subject.onNext(status);
            }

            @Override
            public void onException(Exception ex) {
                subject.onError(ex);
            }
        });

        twitterStream.sample();
    }

    Observable<Status> observe() {
        return subject;
    }
}
