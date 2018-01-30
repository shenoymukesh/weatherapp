package com.mukesh.app.weatherapp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mukesh.app.weatherapp.R;
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
        holder.mPeriodTitleDay.setText(weatherForecastDay.getDayForecast().getTitle() + ":");
        holder.mPeriodTitleNight.setText(weatherForecastDay.getNightForecast().getTitle() + ":");
        holder.mFcDescDay.setText(weatherForecastDay.getDayForecast().getFctText());
        holder.mFcDescNight.setText(weatherForecastDay.getNightForecast().getFctText());
        ImageView iconViewDay = holder.mIconViewDay;
        GlideApp.with(iconViewDay.getContext())
                .load(weatherForecastDay.getDayForecast().getIconUrl())
                .placeholder(R.drawable.ic_flare_24dp)
                .error(R.drawable.ic_error_outline_24dp)
                .into(iconViewDay);
        ImageView iconViewNight = holder.mIconViewNight;
        GlideApp.with(iconViewDay.getContext())
                .load(weatherForecastDay.getNightForecast().getIconUrl())
                .placeholder(R.drawable.ic_flare_24dp)
                .error(R.drawable.ic_error_outline_24dp)
                .into(iconViewNight);

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
        private final ImageView mIconViewDay;
        private final TextView mPeriodTitleDay;
        private final TextView mFcDescDay;
        private final ImageView mIconViewNight;
        private final TextView mPeriodTitleNight;
        private final TextView mFcDescNight;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.title);
            mIconViewDay = itemView.findViewById(R.id.icon_view_day);
            mPeriodTitleDay = itemView.findViewById(R.id.period_title_day);
            mFcDescDay = itemView.findViewById(R.id.fc_desc_day);
            mIconViewNight = itemView.findViewById(R.id.icon_view_night);
            mPeriodTitleNight = itemView.findViewById(R.id.period_title_night);
            mFcDescNight = itemView.findViewById(R.id.fc_desc_night);
        }
    }
}
