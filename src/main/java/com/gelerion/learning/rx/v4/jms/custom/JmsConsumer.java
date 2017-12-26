package com.gelerion.learning.rx.v4.jms.custom;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import javax.jms.*;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

/**
 * Created by denis.shuvalov on 14/12/2017.
 */
class JmsConsumer {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp:localhost:61616");
        Topic topic = new ActiveMQTopic("order");

        JmsConsumer jmsConsumer = new JmsConsumer();

        jmsConsumer.observe(connectionFactory, topic)
                   .cast(TextMessage.class)
                   //.flatMap(textMessage -> Observable.fromCallable(textMessage::getText))
                   .flatMap(textMessage -> {
                       try {
                           return Observable.just(textMessage.getText());
                       } catch (JMSException e) {
                           return Observable.error(e);
                       }
                   });

    }


    Observable<Message> observe(ConnectionFactory connectionFactory, Topic topic) {
        return Observable.create(subscriber -> {
            try {
                subscribeThrowing(connectionFactory, topic, subscriber);
            } catch (JMSException e) {
                subscriber.onError(e);
            }
        });
    }

    private void subscribeThrowing(ConnectionFactory connectionFactory, Topic orders,
                                   Subscriber<? super Message> subscriber) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(orders);

        /*
        The JMS API provides two ways of receiving messages from a broker: synchronous via blocking receive() method,
        and nonblocking, using MessageListener. The nonblocking API is beneficial for many reasons; for example, it
        holds less resources like threads and stack memory. Also it aligns beautifully with the Rx style of programming.
        Rather than creating a MessageListener instance and calling our subscriber from within it, we can use this terse
        syntax with method reference:
         */
        consumer.setMessageListener(subscriber::onNext);
        subscriber.add(onUnsubscribe(connection));
    }

    private Subscription onUnsubscribe(Connection connection) {
        return Subscriptions.create(() -> {
            try {
                connection.close();
            } catch (JMSException e) {
                System.err.println("Error, can not close");
            }
        });
    }


}
