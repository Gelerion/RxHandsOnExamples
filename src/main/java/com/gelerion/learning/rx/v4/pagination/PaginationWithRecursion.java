package com.gelerion.learning.rx.v4.pagination;

import com.gelerion.learning.rx.v4.blocking.Person;
import rx.Observable;

import java.util.Collections;
import java.util.List;

import static rx.Observable.defer;
import static rx.Observable.from;

/**
 * Created by denis.shuvalov on 11/12/2017.
 */
public class PaginationWithRecursion {

    private static final int PAGE_SIZE = 10;

    /**
     * look carefully, there is an alleged infinite recursion in our code! allPeople(initialPage) calls
     * allPeople(initialPage + 1) without any stop condition. This is a recipe for StackOverflowError in
     * most languages, but not here. Again, calling allPeople() is always lazy, therefore the moment you stop
     * listening (unsubscribe), this recursion is over.
     */
    static Observable<Person> allPeoples(int page) {
        return defer(() -> from(listPeople(page)))
                .concatWith(defer(() -> allPeoples(page + 1)));
    }

    static List<Person> listPeople(int page) {
        return query(
                "SELECT * FROM PEOPLE ORDER BY id LIMIT ? OFFSET ?",
                PAGE_SIZE,
                page * PAGE_SIZE
        );
    }

    private static List<Person> query(String query, int pageSize, int page) {
        return Collections.emptyList();
    }


}
