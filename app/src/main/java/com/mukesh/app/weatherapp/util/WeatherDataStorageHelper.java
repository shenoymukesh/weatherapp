package com.mukesh.app.weatherapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mukesh.app.weatherapp.ui.model.RegionSearchSuggestion;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mukesh Shenoy on 2/12/2018.
 */

/**
 *
 * A helper class to locally store/cache the data related to search and weather. Currently it uses shared preferences to store data.
 *
 */

public class WeatherDataStorageHelper {

    private static final String SHARED_PREF_NAME = "weather_app_data";
    private static final String PREF_KEY_SEARCH_HISTORY = "search_history";
    private SharedPreferences mSharedPreferences;

    public WeatherDataStorageHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public ArrayList<RegionSearchSuggestion> getSearchHistoryData() {
        String historyData = mSharedPreferences.getString(PREF_KEY_SEARCH_HISTORY, "");
        if (!TextUtils.isEmpty(historyData)) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<RegionSearchSuggestion>>() {
            }.getType();
            return gson.fromJson(historyData, type);
        }
        return new ArrayList<>();
    }

    public void storeHistoryData(List<RegionSearchSuggestion> historyItems) {
        if (historyItems != null) {
            Gson gson = new Gson();
            mSharedPreferences.edit().putString(PREF_KEY_SEARCH_HISTORY, gson.toJson(historyItems)).commit();
        }
    }
}
