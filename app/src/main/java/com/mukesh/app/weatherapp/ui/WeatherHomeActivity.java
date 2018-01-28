package com.mukesh.app.weatherapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mukesh.app.weatherapp.R;

import java.util.List;

public class WeatherHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_home);
    }

    @Override
    public void onBackPressed() {
        WeatherHomeFragment currentFragment =
                (WeatherHomeFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragment);;

        if (currentFragment == null || !currentFragment.onActivityBackPress()) {
            super.onBackPressed();
        }
    }
}
