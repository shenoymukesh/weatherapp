package com.mukesh.app.weatherapp.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mukesh.app.weatherapp.util.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This fragment hosts a searchview to search a city and a recyclerview to display 10 day forecast for selected city.
 */
public class WeatherHomeFragment extends Fragment {

    private static final String TAG = WeatherHomeFragment.class.getSimpleName();

    //Feature is fixed for now. This can be enhanced to display hourly forecast etc
    private static final String FEATURE = "forecast10day";
    private static final int SUGGESTION_LIMIT = 10;
    private static final String REGION_TYPE_CITY = "city";

    private RecyclerView mForecastList;
    private ForecastListAdapter mForecastListAdapter;
    private FloatingSearchView mSearchView;
    private String mLastQuery;
    private AlertDialog mNoInternetAlert;
    private Call<SearchResponse> mSearchResponseCall;
    private Call<FeaturesResponse> mForecastCall;

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

        mSearchView = view.findViewById(R.id.floating_search_view);
        mForecastList = view.findViewById(R.id.forecast_list);

        setupResultsList();
        setupSearchBar();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Utility.isDataNetworkAvailable(getContext())) {
            showNoInternetDialog();
        }
    }

    private void showNoInternetDialog() {
        if (mNoInternetAlert == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.no_internet)
                    .setMessage(R.string.please_connect_internet)
                    .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(true);
            mNoInternetAlert = builder.create();
        }
        if (!mNoInternetAlert.isShowing()) {
            mNoInternetAlert.show();
        }
    }

    private void setupResultsList() {
        mForecastListAdapter = new ForecastListAdapter();
        mForecastList.setAdapter(mForecastListAdapter);
        mForecastList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupSearchBar() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                Log.d(TAG, "onSearchTextChanged(): " + oldQuery + " --> " + newQuery);
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    getSearchSuggestionsForQuery(newQuery);
                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                mSearchView.clearSearchFocus();
                RegionSearchSuggestion regionSuggestion = (RegionSearchSuggestion) searchSuggestion;
                Log.d(TAG, "onSuggestionClicked(): " + regionSuggestion.getRegionName() + " " + regionSuggestion.getZwm());
                getForecastForSelectedRegion(regionSuggestion);
                mLastQuery = searchSuggestion.getBody();
                mSearchView.setSearchBarTitle(mLastQuery != null ? mLastQuery : "");
            }

            @Override
            public void onSearchAction(String query) {
                Log.d(TAG, "onSearchAction(): " + query);
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
                formatSearchSuggestionItem(leftIcon, textView, (RegionSearchSuggestion) item);
            }

        });
    }

    private void formatSearchSuggestionItem(ImageView leftIcon, TextView textView, RegionSearchSuggestion item) {
        RegionSearchSuggestion regionSuggestion = item;

        leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_place_24dp, null));

        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
        String textHighlight = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.black) & 0x00ffffff);
        String text = regionSuggestion.getBody()
                .replaceFirst(mSearchView.getQuery(),
                        "<font color=\"" + textHighlight + "\">" + mSearchView.getQuery() + "</font>");
        textView.setText(Html.fromHtml(text));
    }

    private void getForecastForSelectedRegion(final RegionSearchSuggestion regionSuggestion) {
        mForecastCall = NetworkManager.getInstance().getWeatherForecast(FEATURE, regionSuggestion.getZwm());
        Callback<FeaturesResponse> forecastCallback = new Callback<FeaturesResponse>() {
            @Override
            public void onResponse(Call<FeaturesResponse> call, Response<FeaturesResponse> response) {
                handleForecastResponse(response, regionSuggestion);
            }

            @Override
            public void onFailure(Call<FeaturesResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    handleForecastFailure(regionSuggestion);
                }
            }
        };
        mForecastCall.enqueue(forecastCallback);
    }

    private void handleForecastResponse(Response<FeaturesResponse> response, RegionSearchSuggestion regionSuggestion) {
        if (response.isSuccessful()) {
            List<WeatherForecastDay> forecasts = getConsolidatedForecasts(response.body().getForecast());
            mForecastListAdapter.swapData(forecasts);
            if (forecasts.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.no_forecast_available, regionSuggestion.getRegionName()), Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(TAG, "getForecastForSelectedRegion(): onResponse(): " + response.code() + " " + response.message());
            //TODO: Need to display error to user. Not much info available on WU API doc for non 200 OK error formats
            Toast.makeText(getContext(), getString(R.string.no_forecast_available, regionSuggestion.getRegionName()), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleForecastFailure(RegionSearchSuggestion regionSuggestion) {
        if (!Utility.isDataNetworkAvailable(getContext())) {
            showNoInternetDialog();
        } else {
            Toast.makeText(getContext(), getString(R.string.no_forecast_available, regionSuggestion.getRegionName()), Toast.LENGTH_SHORT).show();
        }
    }

    private void getSearchSuggestionsForQuery(String newQuery) {
        mSearchView.showProgress();
        mSearchResponseCall = NetworkManager.getInstance().getSearchSuggestions(newQuery);
        Callback<SearchResponse> searchResponseCallback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                handleSearchSuggestionSuccess(response);
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                mSearchView.hideProgress();
                if (!call.isCanceled()) {
                    handleSearchSuggestionFailure();
                }
            }
        };
        mSearchResponseCall.enqueue(searchResponseCallback);
    }

    private void handleSearchSuggestionSuccess(Response<SearchResponse> response) {
        if (response.isSuccessful()) {
            List<? extends SearchSuggestion> suggestions = getSuggestionsFromSearchResults(response, SUGGESTION_LIMIT);
            mSearchView.swapSuggestions(suggestions);
            mSearchView.hideProgress();
            if (suggestions.isEmpty()) {
                Toast.makeText(getContext(), R.string.no_results_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(TAG, "getSearchSuggestionsForQuery(): onResponse(): " + response.code() + " " + response.message());
            //TODO: Need to display error to user. Not much info available on WU API doc for non 200 OK error formats
            Toast.makeText(getContext(), R.string.no_results_found, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSearchSuggestionFailure() {
        if (!Utility.isDataNetworkAvailable(getContext())) {
            showNoInternetDialog();
        } else {
            Toast.makeText(getContext(), R.string.failed_get_results, Toast.LENGTH_SHORT).show();
        }
    }

    private List<RegionSearchSuggestion> getSuggestionsFromSearchResults(Response<SearchResponse> response, int limit) {
        List<RegionSearchSuggestion> suggestions = new ArrayList<>();
        if (response != null) {
            List<Region> results = response.body().getResults();
            if (results != null) {
                for (Region region : results) {
                    //The API provides results including state and country matching the string. We only need city
                    if (REGION_TYPE_CITY.equals(region.getType())) {
                        suggestions.add(new RegionSearchSuggestion(region.getName(), region.getZmw()));
                    }
                    if (suggestions.size() == limit) break;
                }
            }
        }
        return suggestions;
    }

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

    public boolean onActivityBackPress() {
        //if mSearchView.setSearchFocused(false) causes the focused search
        //to close, then we don't want to close the activity. if mSearchView.setSearchFocused(false)
        //returns false, we know that the search was already closed so the call didn't change the focus
        //state and it makes sense to call supper onBackPressed() and close the activity
        return mSearchView.setSearchFocused(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSearchResponseCall != null) {
            mSearchResponseCall.cancel();
        }
        if (mForecastCall != null) {
            mForecastCall.cancel();
        }
    }
}
