package com.gelerion.learning.rx.v8.retrofit.model;

import java.util.List;

/**
 * Created by denis.shuvalov on 25/12/2017.
 */
public class Cities {
    private List<City> results;

    public List<City> results() {
        return this.results;
    }

    public Cities setResults(List<City> results) {
        this.results = results;
        return this;
    }
}
