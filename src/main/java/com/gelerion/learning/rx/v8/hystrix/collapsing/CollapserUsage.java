package com.gelerion.learning.rx.v8.hystrix.collapsing;

import com.gelerion.learning.rx.v8.hystrix.collapsing.model.Book;
import com.gelerion.learning.rx.v8.hystrix.collapsing.model.Rating;
import rx.Observable;

/**
 * Created by denis.shuvalov on 25/12/2017.
 */
public class CollapserUsage {

    public static void main(String[] args) {
        //When we want to retrieve one Rating for a given Book, we create an instance of FetchRatingsCollapser like that:
        Observable<Rating> ratingObservable = new FetchRatingsCollapser(new Book()).toObservable();
    }

}
