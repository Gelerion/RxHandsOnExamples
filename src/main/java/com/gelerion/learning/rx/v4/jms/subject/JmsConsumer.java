package com.gelerion.learning.rx.v4.jms.subject;

import rx.Observable;
import rx.subjects.PublishSubject;

import javax.jms.Message;

/**
 * Created by denis.shuvalov on 14/12/2017.
 *
 * Keep in mind that Observable<Message> is hot; it begins emitting JMS messages as soon as they are consumed.
 * If no one is subscribed at the moment, messages are simply lost. ReplaySubject is an alternative, but because
 * it caches all events since the application startup, it’s not suitable for long-running processes. In case you
 * have a subscriber that absolutely must receive all messages, ensure that it subscribes before the JMS message
 * listener is initialized. Additionally, our message listener has a concurrency="1" parameter to ensure that
 * Subject is not invoked from multiple threads. As an alternative, you can use Subject.toSerialized().
 */
//@Component
class JmsConsumer {

    private final PublishSubject<Message> subject = PublishSubject.create();


    //@JmsListener(destination = "orders")
    void newOrder(Message message) {
        //...
        subject.onNext(message);
    }

    Observable<Message> observe() {
        return subject;
    }


}
