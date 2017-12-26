package com.gelerion.learning.rx.v8.retrofit.model;

/**
 * Created by denis.shuvalov on 25/12/2017.
 */
public class City {
    private String city;
    private String country;
    private Double distance;
    private Integer id;
    private Double lat;
    private String localizedCountryName;
    private Double lon;
    private Integer memberCount;
    private Integer ranking;
    private String zip;

    public long distanceTo(double warsawLat, double warsawLon) {
        return 24;
    }

    public String city() {
        return this.city;
    }
}
