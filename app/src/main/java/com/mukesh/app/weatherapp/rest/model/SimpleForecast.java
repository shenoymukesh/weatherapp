package com.mukesh.app.weatherapp.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Created by Mukesh Shenoy on 1/28/2018.
 */

@Accessors(prefix = "m")
public class SimpleForecast {

    @Getter
    @SerializedName("forecastday")
    private List<ForecastDay> mForecastDays;
}
