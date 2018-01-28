package com.mukesh.app.weatherapp.ui;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.mukesh.app.weatherapp.R;
import com.mukesh.app.weatherapp.rest.NetworkManager;
import com.mukesh.app.weatherapp.rest.model.FeaturesResponse;
import com.mukesh.app.weatherapp.rest.model.ForecastDay;
import com.mukesh.app.weatherapp.rest.model.ForecastFeature;
import com.mukesh.app.weatherapp.rest.model.ForecastPeriod;
import com.mukesh.app.weatherapp.rest.model.Region;
import com.mukesh.app.weatherapp.rest.model.SearchResponse;
import com.mukesh.app.weatherapp.rest.model.SimpleForecast;
import com.mukesh.app.weatherapp.rest.model.TextForecast;
import com.mukesh.app.weatherapp.ui.model.RegionSearchSuggestion;
import com.mukesh.app.weatherapp.ui.model.WeatherForecastDay;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class WeatherHomeFragment extends Fragment {

    private static final String TAG = WeatherHomeFragment.class.getSimpleName();

    //Feature is hardcoded for now. This can be enhanced to display hourly forecast etc
    private static final String FEATURE = "forecast10day";
    private static final int SUGGESTION_LIMIT = 10;
    private static final String REGION_TYPE_CITY = "city";

    private RecyclerView mForecastList;
    private ForecastListAdapter mForecastListAdapter;
    private FloatingSearchView mSearchView;
    private String mLastQuery;

    public WeatherHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        mForecastList = (RecyclerView) view.findViewById(R.id.forecast_list);

        setupFloatingSearch();
        setupResultsList();
    }

    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                Log.d(TAG, "onSearchTextChanged(): " + oldQuery + " --> " + newQuery);
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();
                    Call<SearchResponse> searchResponseCall = NetworkManager.getInstance().getSearchSuggestions(newQuery);
                    Callback<SearchResponse> searchResponseCallback = new Callback<SearchResponse>() {
                        @Override
                        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                            mSearchView.swapSuggestions(getSuggestionsFromSearchResults(response, SUGGESTION_LIMIT));
                            mSearchView.hideProgress();
                        }

                        @Override
                        public void onFailure(Call<SearchResponse> call, Throwable t) {
                            mSearchView.hideProgress();
                            //TODO: Need to handle failure case
                        }
                    };
                    searchResponseCall.enqueue(searchResponseCallback);
                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                mSearchView.clearSearchFocus();
                RegionSearchSuggestion regionSuggestion = (RegionSearchSuggestion) searchSuggestion;
                Log.d(TAG, "onSuggestionClicked(): " + regionSuggestion.getRegionName() + " " + regionSuggestion.getZwm());
                Call<FeaturesResponse> forecastCall = NetworkManager.getInstance().getWeatherForecast(FEATURE, regionSuggestion.getZwm());
                Callback<FeaturesResponse> forecastCallback = new Callback<FeaturesResponse>() {
                    @Override
                    public void onResponse(Call<FeaturesResponse> call, Response<FeaturesResponse> response) {
                        mForecastListAdapter.swapData(getConsolidatedForecasts(response.body().getForecast()));
                    }

                    @Override
                    public void onFailure(Call<FeaturesResponse> call, Throwable t) {
                        //TODO: Handle error case; Show appropriate error
                    }
                };
                forecastCall.enqueue(forecastCallback);
                mLastQuery = searchSuggestion.getBody();
                mSearchView.setSearchBarTitle(mLastQuery != null ? mLastQuery : "");
            }

            @Override
            public void onSearchAction(String query) {
                Log.d(TAG, "onSearchAction(): " + query);
                //mLastQuery = query;
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {
                Log.d(TAG, "onFocusCleared()");
                mSearchView.setSearchBarTitle(mLastQuery != null ? mLastQuery : "");
            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                RegionSearchSuggestion regionSuggestion = (RegionSearchSuggestion) item;

                leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_place_24dp, null));

                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
                String textHighlight = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.black) & 0x00ffffff);
                String text = regionSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"" + textHighlight + "\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });

        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                Log.d(TAG, "onClearSearchClicked()");
            }
        });
    }

    private List<RegionSearchSuggestion> getSuggestionsFromSearchResults(Response<SearchResponse> response, int limit) {
        List<RegionSearchSuggestion> suggestions = new ArrayList<>();
        if (response != null) {
            List<Region> results = response.body().getResults();
            if (results != null) {
                for (Region region : results) {
                    if (REGION_TYPE_CITY.equals(region.getType())) {
                        suggestions.add(new RegionSearchSuggestion(region.getName(), region.getZmw()));
                    }
                    if (suggestions.size() == limit) break;
                }
            }
        }
        return suggestions;
    }

    //TODO: Need to encapsulate this into respective models
    private List<WeatherForecastDay> getConsolidatedForecasts(ForecastFeature forecastFeature) {
        List<WeatherForecastDay> summaryForecasts = new ArrayList<>();
        if (forecastFeature != null) {
            SimpleForecast simpleforecast = forecastFeature.getSimpleForecast();
            TextForecast textForecast = forecastFeature.getTextForecast();
            List<ForecastPeriod> textForecastPeriods = null;
            if (textForecast != null) {
                textForecastPeriods = textForecast.getForecastPeriods();
            }
            if (simpleforecast != null) {
                List<ForecastDay> simpleforecastDays = simpleforecast.getForecastDays();
                if (simpleforecastDays != null) {
                    for (int i = 0; i < simpleforecastDays.size(); i++) {
                        ForecastPeriod dayForecast = textForecastPeriods.get(2 * i);
                        ForecastPeriod nightForecast = textForecastPeriods.get((2 * i) + 1);
                        WeatherForecastDay summaryForecast
                                = new WeatherForecastDay(simpleforecastDays.get(i).getDate(), dayForecast, nightForecast);
                        summaryForecasts.add(summaryForecast);
                    }
                }

            }
        }
        return summaryForecasts;
    }

    private void setupResultsList() {
        mForecastListAdapter = new ForecastListAdapter();
        mForecastList.setAdapter(mForecastListAdapter);
        mForecastList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public boolean onActivityBackPress() {
        //if mSearchView.setSearchFocused(false) causes the focused search
        //to close, then we don't want to close the activity. if mSearchView.setSearchFocused(false)
        //returns false, we know that the search was already closed so the call didn't change the focus
        //state and it makes sense to call supper onBackPressed() and close the activity
        if (!mSearchView.setSearchFocused(false)) {
            return false;
        }
        return true;
    }

}
