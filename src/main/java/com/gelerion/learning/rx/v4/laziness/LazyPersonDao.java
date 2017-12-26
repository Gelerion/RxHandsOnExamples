package com.gelerion.learning.rx.v4.laziness;

import com.gelerion.learning.rx.v4.blocking.Person;
import rx.Observable;

import java.util.Collections;
import java.util.List;

import static rx.Observable.from;

/**
 * Created by denis.shuvalov on 11/12/2017.
 */
public class LazyPersonDao {

    /**
     *  Because Observable is lazy, calling listPeople() has no side effects and almost no performance footprint.
     *  No database is queried yet. You can treat Observable<Person> as a promise but without any background processing
     *  happening yet. Notice that there is no asynchronous behavior at the moment, just lazy evaluation
     */
    public static void main(String[] args) {

    }

    //The underlying Observable is eager, so we want to postpone its creation
    //defer() will wait until the last possible moment to actually create Observable; that is, until someone actually subscribes to it
    Observable<Person> listPeople() {
        return Observable.defer(() -> from(query("SELECT * FROM PEOPLE")));
    }

    //Old fashion
//    List<Person> listPeople() {
//        return query("SELECT * FROM PEOPLE");
//    }

    private List<Person> query(String sql) {
        //...
        return Collections.emptyList();
    }

}
