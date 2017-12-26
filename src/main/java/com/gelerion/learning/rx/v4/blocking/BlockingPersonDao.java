package com.gelerion.learning.rx.v4.blocking;

import rx.Observable;
import rx.observables.BlockingObservable;

import java.util.Collections;
import java.util.List;

/**
 * Created by denis.shuvalov on 11/12/2017.
 */
public class BlockingPersonDao {

    //Not lazy at all
    public static void main(String[] args) {
        //consuming api
        BlockingPersonDao dao = new BlockingPersonDao();
        Observable<Person> peopleStream = dao.listPeople();
        Observable<List<Person>> peopleList = peopleStream.toList();
        BlockingObservable<List<Person>> peopleBlocking = peopleList.toBlocking();
        List<Person> persons = peopleBlocking.single(); //not first

    }

    Observable<Person> listPeople() {
        return Observable.from(query("SELECT * FROM PEOPLE"));
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
