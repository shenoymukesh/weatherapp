package com.mukesh.app.weatherapp.rest;

import com.mukesh.app.weatherapp.rest.model.FeaturesResponse;
import com.mukesh.app.weatherapp.rest.model.SearchResponse;
import com.mukesh.app.weatherapp.rest.service.ServiceCreator;
import com.mukesh.app.weatherapp.rest.service.WeatherApiClient;

import retrofit2.Call;

/**
 * Created by Mukesh Shenoy on 1/27/2018.
 */

public class NetworkManager {

    private static final NetworkManager sInstance = new NetworkManager();
    private final WeatherApiClient mWeatherApiClient;

    public static NetworkManager getInstance() {
        return sInstance;
    }

    private NetworkManager() {
        mWeatherApiClient = new ServiceCreator().createService();
    }

    public Call<SearchResponse> getSearchSuggestions(String queryString) {
        return mWeatherApiClient.getSearchResults(queryString);
    }

    public Call<FeaturesResponse> getWeatherForecast(String feature, String zwm) {
        return mWeatherApiClient.getForecast(Constants.WU_API_KEY, feature, zwm);
    }
}
