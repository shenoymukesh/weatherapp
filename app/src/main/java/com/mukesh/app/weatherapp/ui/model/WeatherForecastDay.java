package com.mukesh.app.weatherapp.ui.model;

import com.mukesh.app.weatherapp.rest.model.Date;
import com.mukesh.app.weatherapp.rest.model.ForecastPeriod;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Created by Mukesh Shenoy on 1/28/2018.
 */

@Accessors(prefix = "m")
public class WeatherForecastDay {

    @Getter
    private Date mDate;

    @Getter
    private ForecastPeriod mDayForecast;

    @Getter
    private ForecastPeriod mNightForecast;

    public WeatherForecastDay(Date date, ForecastPeriod dayForecast, ForecastPeriod nightForecast) {
        mDate = date;
        mDayForecast = dayForecast;
        mNightForecast = nightForecast;
    }
}
