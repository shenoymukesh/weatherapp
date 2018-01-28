package com.mukesh.app.weatherapp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mukesh.app.weatherapp.R;
import com.mukesh.app.weatherapp.rest.model.ForecastPeriod;
import com.mukesh.app.weatherapp.ui.model.WeatherForecastDay;

import java.util.List;

/**
 * Created by Mukesh Shenoy on 1/28/2018.
 */

class ForecastListAdapter extends RecyclerView.Adapter<ForecastListAdapter.ViewHolder> {


    private List<WeatherForecastDay> mForecastSummaries;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherForecastDay weatherForecastDay = mForecastSummaries.get(position);
        holder.mTitleText.setText(weatherForecastDay.getDate().toString());
        holder.mFcDescription.setText(weatherForecastDay.getDayForecast().getFctText());
        ImageView weatherIconView = holder.mIconView;
        GlideApp.with(weatherIconView.getContext())
                .load(weatherForecastDay.getDayForecast().getIconUrl())
                .placeholder(R.drawable.ic_flare_24dp)
                .error(R.drawable.ic_error_outline_24dp)
                .into(weatherIconView);

    }

    @Override
    public int getItemCount() {
        return mForecastSummaries != null ? mForecastSummaries.size() : 0;
    }

    public void swapData(List<WeatherForecastDay> forecastSummaries) {
        mForecastSummaries = forecastSummaries;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitleText;
        private final ImageView mIconView;
        private final TextView mFcDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.title);
            mIconView = itemView.findViewById(R.id.icon_view);
            mFcDescription = itemView.findViewById(R.id.fc_desc);
        }
    }
}
