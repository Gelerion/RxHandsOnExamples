package com.gelerion.learning.rx.v8.hystrix.collapsing;

import com.gelerion.learning.rx.v8.hystrix.collapsing.model.Book;
import com.gelerion.learning.rx.v8.hystrix.collapsing.model.Rating;
import com.netflix.hystrix.*;
import rx.functions.Func1;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by denis.shuvalov on 25/12/2017.
 * <p>
 * What if we have multiple concurrent clients, each asking for some Rating? When two independent requests from two
 * browsers hit our server, we would still like to batch these two requests together and make just one downstream call.
 * However, this would require interthread synchronization and some global registry of all requests. Imagine one thread
 * trying to invoke a given command and another thread invoking the same command (with different arguments) just
 * milliseconds later. We would like to wait a little bit after the first request attempted to start a command, just
 * in case another thread wants to invoke the same command shortly thereafter. In that case, we want to capture these
 * two requests, merge them together, make just one batch request, and map batch response back to individual requests.
 * This is precisely what Hystrix is doing with a little help from us:
 */
public class FetchRatingsCollapser extends HystrixObservableCollapser<Book, Rating, Rating, Book> {

    private final Book book;

    //internally, there are a few interesting details that allow batching. First, in the constructor,
    //apart from storing Book for this request we configure the collapsing of requests:
    FetchRatingsCollapser(Book book) {
        super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("Books"))
                    .andCollapserPropertiesDefaults(HystrixCollapserProperties
                            .Setter()
                            .withTimerDelayInMilliseconds(20) //length of time window during which collapsing occurs
                            .withMaxRequestsInBatch(50)) // When this time elapses or as many as 50 requests are already queued up the gate is opened
                    .andScope(Scope.GLOBAL));

        this.book = book;
    }

    @Override
    public Book getRequestArgument() {
        return book;
    }

    /**
     * Hystrix will not magically batch your command into one; you must instruct it how to do this
     */
    @Override
    protected HystrixObservableCommand<Rating> createCommand(Collection<HystrixCollapser.CollapsedRequest<Rating, Book>> requests) {
        List<Book> books = requests.stream()
                                     .map(request -> request.getArgument())
                                     .collect(Collectors.toList());
        return new FetchManyRatings(books);
    }

    /*
    When values begin to emerge from FetchManyRatings, we must somehow map the Rating instances to independent requests.
    Remember that at this point we might have several individual threads and transactions, each waiting for just one Rating.
    This routing and dispatching of batch response to small individual requests occurs more or less automatically with
    the help of the following methods:
     */

    @Override
    protected void onMissingResponse(HystrixCollapser.CollapsedRequest<Rating, Book> r) {
        r.setException(new RuntimeException("Not found for: " + r.getArgument()));

    }

    /**
     * You use this to instruct Hystrix which request key (Book) this particular response (Rating) answers.
     * For simplicities sake, each Rating returned from a batch response has a getBook() method that indicates
     * to which Book it is related.
     */
    @Override
    protected Func1<Rating, Book> getBatchReturnTypeKeySelector() {
        return Rating::getBook;
    }


    /**
     * This maps from an individual request argument (Book) into a key that will later be used to map a batch response.
     * In our case, we simply use the same Book instance, thus the identity x -> x transformation.
     */
    @Override
    protected Func1<Book, Book> getRequestArgumentKeySelector() {
        return x -> x;
    }

    /**
     * This maps one item from the batch response to one individual response.
     * Again, the identity x -> x transformation is sufficient in our case
     */
    @Override
    protected Func1<Rating, Rating> getBatchReturnTypeToResponseTypeMapper() {
        return x -> x;
    }
}
