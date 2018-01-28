package com.mukesh.app.weatherapp.rest.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Created by Mukesh Shenoy on 1/28/2018.
 */

/*
{
    "epoch": "1517108400",
    "pretty": "7:00 PM PST on January 27, 2018",
    "day": 27,
    "month": 1,
    "year": 2018,
    "yday": 26,
    "hour": 19,
    "min": "00",
    "sec": 0,
    "isdst": "0",
    "monthname": "January",
    "monthname_short": "Jan",
    "weekday_short": "Sat",
    "weekday": "Saturday",
    "ampm": "PM",
    "tz_short": "PST",
    "tz_long": "America/Los_Angeles"
}
 */
@Accessors(prefix = "m")
public class Date {

    @Getter
    @SerializedName("day")
    private String mDay;

    @Getter
    @SerializedName("monthname_short")
    private String mMonthShort;

    @Getter
    @SerializedName("year")
    private String mYear;

    @Getter
    @SerializedName("weekday")
    private String mWeekday;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(mWeekday).append(", ")
                .append(mMonthShort).append(" ")
                .append(mDay).append(" ")
                .append(mYear);
        return builder.toString();
    }
}
