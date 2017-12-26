package com.gelerion.learning.rx.v8.hystrix.collapsing;

import com.gelerion.learning.rx.v8.hystrix.collapsing.model.Book;
import com.gelerion.learning.rx.v8.hystrix.collapsing.model.Rating;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;

import java.util.Collection;

/**
 * Created by denis.shuvalov on 25/12/2017.
 * Hystrix knows about every single command you execute. When it discovers that you are about to start two
 * similar commands at the same time (e.g., to fetch two Ratings) at the same time it can collapse these two
 * commands into one bigger batch command. This batch command is invoked, and when batch response arrives, replies
 * are mapped back to individual requests.
 *
 * Manual batching example:
 */
public class FetchManyRatings extends HystrixObservableCommand<Rating> {

    private final Collection<Book> books;

    FetchManyRatings(Collection<Book> books) {
        super(HystrixCommandGroupKey.Factory.asKey("Books"));
        this.books = books;
    }

    @Override
    protected Observable<Rating> construct() {
        return fetchManyRatings(books);
    }

    /**
     * The fetchManyRatings() method takes several books as an argument and emits several Rating instances. Internally,
     * it can make a single batch HTTP request asking for several ratings, as opposed to the fetchRating(book) method,
     * which always retrieves just one
     */
    private Observable<Rating> fetchManyRatings(Collection<Book> books) {
        return Observable.just(new Rating());
    }
}
