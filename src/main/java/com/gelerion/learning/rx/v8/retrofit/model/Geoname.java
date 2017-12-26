package com.gelerion.learning.rx.v8.retrofit.model;

/**
 * Created by denis.shuvalov on 25/12/2017.
 */
public class Geoname {
    private String lat;
    private String lng;
    private Integer geonameId;
    private Integer population;
    private String countryCode;
    private String name;

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public Integer getGeonameId() {
        return geonameId;
    }

    public Integer getPopulation() {
        return population;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getName() {
        return name;
    }
}
