package com.mukesh.app.weatherapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mukesh.app.weatherapp.R;

/**
 * This is a simple container activity. Currently just WeatherHomeFragment is added in the layout. Can be enhanced to add navigation drawer etc and add/switch fragments on the go.
 */
public class WeatherHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_home);
    }

    @Override
    public void onBackPressed() {
        WeatherHomeFragment currentFragment =
                (WeatherHomeFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragment);
        ;

        if (currentFragment == null || !currentFragment.onActivityBackPress()) {
            super.onBackPressed();
        }
    }
}
