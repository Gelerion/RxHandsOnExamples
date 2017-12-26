package com.gelerion.learning.rx.v8.hystrix.commands;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.Observable;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Future;

/**
 * Created by denis.shuvalov on 25/12/2017.
 */
public class BlockingCmd extends HystrixCommand<String> {

    /**
     * Always create a new instance of BlockingCmd—you cannot reuse a command instance for multiple executions
     */
    public static void main(String[] args) {

        //blocking
        String s              = new BlockingCmd().execute();
        Future<String> future = new BlockingCmd().queue();


        /*
        The semantic difference between observe() and toObservable() is quite important. toObservable() converts a
        command to a lazy and cold Observable—the command will not be executed until someone actually subscribes to
        this Observable. Moreover, the Observable is not cached, so each subscribe() will trigger command execution.
        observe(), in contrast, invokes the command asynchronously straight away, returning a hot but also cached
        Observable
         */
        Observable<String> eager = new BlockingCmd().observe();
        Observable<String> lazy  = new BlockingCmd().toObservable();

/*        Observable<String> retried = new BlockingCmd()
                .toObservable()
                .doOnError(ex -> log.warn("Error ", ex))
                .retryWhen(ex -> ex.delay(500, MILLISECONDS))
                .timeout(3, SECONDS);*/
    }


    BlockingCmd() {
        super(HystrixCommandGroupKey.Factory.asKey("SomeGroup"));
    }

    @Override
    protected String run() throws Exception {
        final URL url = new URL("http://www.example.com");
        try (InputStream input = url.openStream()) {
            //return IOUtils.toString(input, StandardCharsets.UTF_8);
            return "";
        }
    }
}
