package com.mukesh.app.weatherapp.rest.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.mukesh.app.weatherapp.rest.model.FeaturesResponse;
import com.mukesh.app.weatherapp.rest.model.SearchResponse;

/**
 * Created by Mukesh Shenoy on 1/27/2018.
 */

public interface WeatherApiClient {

    @GET("http://autocomplete.wunderground.com/aq")
    Call<SearchResponse> getSearchResults(@Query("query") String queryString);

    @GET("{apiKey}/{feature}/q/zmw:{zwm}.json")
    Call<FeaturesResponse> getForecast(@Path("apiKey") String apiKey, @Path("feature") String feature, @Path("zwm") String zwm);
}
