package com.gelerion.learning.rx.v3.cartesian.vacation;

/**
 * Created by denis.shuvalov on 06/12/2017.
 */
class Weather {
    private final Temperature temperature;

    public Weather(Temperature temperature, Wind wind) {
        //...
        this.temperature = temperature;
    }

    public boolean isSunny() {
        return true;
    }

    Temperature getTemperature() {
        return temperature;
    }
}
