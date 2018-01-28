package com.mukesh.app.weatherapp.rest.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mukesh.app.weatherapp.rest.Constants.BASE_URL;

/**
 * Created by Mukesh Shenoy on 1/27/2018.
 */

public class ServiceCreator {

    private final Retrofit mRetrofit;

    public ServiceCreator() {
        mRetrofit = buildRetrofit();
    }

    private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public WeatherApiClient createService() {
        return mRetrofit.create(WeatherApiClient.class);
    }
}
