package com.mukesh.app.weatherapp.rest.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Created by Mukesh Shenoy on 1/27/2018.
 */

/*
    {
        "name": "Edinburgh, United Kingdom",
        "type": "city",
        "c": "GB",
        "zmw": "00000.53.03160",
        "tz": "Europe/London",
        "tzs": "GMT",
        "l": "/q/zmw:00000.53.03160",
        "ll": "55.950001 -3.200000",
        "lat": "55.950001",
        "lon": "-3.200000"
    }
 */

@Accessors(prefix = "m")
public class Region {

    @Getter
    @SerializedName("name")
    private String mName;

    @Getter
    @SerializedName("type")
    private String mType;

    @Getter
    @SerializedName("c")
    private String mCountry;

    @Getter
    @SerializedName("zmw")
    private String mZmw;

    @Getter
    @SerializedName("tz")
    private String mTz;

    @Getter
    @SerializedName("tzs")
    private String mTzs;

    @Getter
    @SerializedName("l")
    private String mLink;

    @Getter
    @SerializedName("ll")
    private String mLatLon;

    @Getter
    @SerializedName("lat")
    private String mLat;

    @Getter
    @SerializedName("lon")
    private String mLon;
}
