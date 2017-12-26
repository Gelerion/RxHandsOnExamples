package com.gelerion.learning.rx.v5.db;

import org.postgresql.PGNotification;
import org.postgresql.jdbc2.AbstractJdbc2Connection;
import org.postgresql.jdbc42.Jdbc42Connection;
import rx.Observable;
import rx.observers.Subscribers;
import rx.subscriptions.Subscriptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by denis.shuvalov on 19/12/2017.
 *
 * It’s amazing how much shorter this example could have been if there were no SQLException.
 */
public class ListenForDb {

    public static Observable<PGNotification> listen(String channel, int pollInterval) {
        return Observable.<PGNotification>create(subscriber -> {
            try {
                //1. Postpone opening a connection to a database until someone actually subscribes
                Connection connection = DriverManager.getConnection("jdbc:postgresql:db");

                //2. Ensure that the connection is closed when the Subscriber unsubscribes.
                //   Moreover, when an error occurs in the stream, the unsubscription and therefore closing
                //   the connection happens.
                subscriber.add(Subscriptions.create(() -> closeQuitely(connection)));

                //3. Now we are ready to call listenOn() and begin receiving notifications over an open connection.
                listenOn(connection, channel);

                //4. We do not want to block any thread so instead, we create an inner Observable with interval()
                //   inside pollForNotifications(). We subscribe to that Observable with the same Subscriber but wrapped
                //   with Subscribers.wrap() so that onStart() is not executed twice on that Subscriber
                Jdbc42Connection pgConn = (Jdbc42Connection) connection;
                pollForNotifications(pgConn, pollInterval)
                        //NOTE!
                        .subscribe(Subscribers.wrap(subscriber));
            }
            catch (SQLException e) {
                subscriber.onError(e);
            }
            //NOTE!
            //5. One last tiny bit of implementation is publish() and refCount(), close to the end of the first method.
            //   These two methods make it possible to share a single JDBC connection among multiple subscribers.
            //   Without them, every new subscriber would open a new connection and listen on it, which is quite wasteful.
            //   Additionally refCount() keeps track of the number of subscribers and when the last one unsubscribes it
            //   physically closes the database connection.
        }).share();
    }

    private static Observable<PGNotification> pollForNotifications(AbstractJdbc2Connection pgConn, int pollInterval) {
        return Observable.interval(pollInterval, TimeUnit.MILLISECONDS)
                  .flatMap(x -> tryGetNotification(pgConn))
                  .filter(Objects::nonNull)
                  //NOTE
                  .flatMapIterable(Arrays::asList);

    }

    private static void listenOn(Connection connection, String channel) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("LISTEN " + channel);
        }
    }

    private static Observable<PGNotification[]> tryGetNotification(AbstractJdbc2Connection pgConn) {
        try {
            return Observable.just(pgConn.getNotifications());
        } catch (SQLException e) {
            return Observable.error(e);
        }
    }

    private static void closeQuitely(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
