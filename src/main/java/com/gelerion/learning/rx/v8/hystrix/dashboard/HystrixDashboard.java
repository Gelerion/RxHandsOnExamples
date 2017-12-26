package com.gelerion.learning.rx.v8.hystrix.dashboard;

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.metric.HystrixCommandCompletion;
import com.netflix.hystrix.metric.HystrixCommandCompletionStream;
import rx.Observable;

import java.util.concurrent.TimeUnit;



/**
 * Created by denis.shuvalov on 25/12/2017.
 */
public class HystrixDashboard {

    /**
     * But building a monitoring infrastructure on top of these streams requires a bit of design and work. Also
     * you might want to externalize monitoring from the actual application. Hystrix, via the
     * hystrix-metrics-event-stream module, supports pushing all aggregated metrics via HTTP. If your application
     * already runs on top of or has an embedded servlet container, it is enough to add a built-in
     * HystrixMetricsStreamServlet to your mappings. Otherwise, you can start a tiny container yourself:
     */
    public static void main(String[] args) {
/*      import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
        import org.eclipse.jetty.server.Server;
        import org.eclipse.jetty.servlet.ServletContextHandler;
        import org.eclipse.jetty.servlet.ServletHolder;
        import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;*/

/*      ServletContextHandler context = new ServletContextHandler(NO_SESSIONS);
        HystrixMetricsStreamServlet servlet = new HystrixMetricsStreamServlet();
        context.addServlet(new ServletHolder(servlet), "/hystrix.stream");
        Server server = new Server(8080);
        server.setHandler(context);
        server.start();*/

    }

    public void failedPerSecondCommands() {
        HystrixCommandCompletionStream
                .getInstance(HystrixCommandKey.Factory.asKey("FetchRating"))
                .observe()
                .filter(e -> e.getEventCounts().getCount(HystrixEventType.FAILURE) > 0)
                .window(1, TimeUnit.SECONDS)
                .flatMap(Observable::count)
                .subscribe(x -> System.out.println(x + " failures/s"));
    }

    /**
     * Hystrix provides several ways to digest it. You can subscribe to several types of streams prepared by Hystrix
     * that emit events about occurrences within the library. For example, the following code creates a stream of
     * HystrixCommandCompletion events emitted every time a command FetchRating completes:
     */
    public void completionStream() {
        Observable<HystrixCommandCompletion> stats = HystrixCommandCompletionStream
                .getInstance(HystrixCommandKey.Factory.asKey("FetchRating"))
                .observe();
    }
}
