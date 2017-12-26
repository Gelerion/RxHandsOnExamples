package com.gelerion.learning.rx.v8.retrofit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis.shuvalov on 25/12/2017.
 */
public class SearchResult {
    private List<Geoname> geonames = new ArrayList<>();

    public List<Geoname> getGeonames() {
        return geonames;
    }
}
