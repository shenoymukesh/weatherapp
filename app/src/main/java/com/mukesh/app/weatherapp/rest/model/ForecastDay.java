package com.mukesh.app.weatherapp.rest.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Created by Mukesh Shenoy on 1/28/2018.
 */
/*
{
      "date": {},
      "period": 1,
      "high": {
        "fahrenheit": "63",
        "celsius": "17"
      },
      "low": {
        "fahrenheit": "43",
        "celsius": "6"
      },
      "conditions": "Partly Cloudy",
      "icon": "partlycloudy",
      "icon_url": "http://icons.wxug.com/i/c/k/partlycloudy.gif",
      "skyicon": "",
      "pop": 10,
      "qpf_allday": {},
      "qpf_day": {},
      "qpf_night": {},
      "snow_allday": {},
      "snow_day": {},
      "snow_night": {},
      "maxwind": {},
      "avewind": {},
      "avehumidity": 89,
      "maxhumidity": 0,
      "minhumidity": 0
}
 */
@Accessors(prefix = "m")
public class ForecastDay {

    @Getter
    @SerializedName("period")
    private int mPeriod;

    @Getter
    @SerializedName("date")
    private Date mDate;
}
