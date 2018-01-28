package com.mukesh.app.weatherapp.rest.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Created by Mukesh Shenoy on 1/27/2018.
 */

/*
    {
      "period": 0,
      "icon": "chancerain",
      "icon_url": "http://icons.wxug.com/i/c/k/chancerain.gif",
      "title": "Sunday",
      "fcttext": "Light rain likely. Winds increasing late. High 54F. Winds WSW at 25 to 35 mph. Chance of rain 50%. Winds could occasionally gust over 40 mph.",
      "fcttext_metric": "Cloudy with periods of light rain. Becoming windy late. High 12C. Winds WSW at 30 to 50 km/h. Chance of rain 50%. Winds could occasionally gust over 65 km/h.",
      "pop": "50"
    }
 */

@Accessors(prefix = "m")
public class ForecastPeriod {

    @Getter
    @SerializedName("period")
    private int mPeriod;

    @Getter
    @SerializedName("icon")
    private String mIcon;

    @Getter
    @SerializedName("icon_url")
    private String mIconUrl;

    @Getter
    @SerializedName("title")
    private String mTitle;

    @Getter
    @SerializedName("fcttext")
    private String mFctText;

    @Getter
    @SerializedName("fcttext_metric")
    private String mFctTextMetric;

    @Getter
    @SerializedName("pop")
    private String mPop;
}
