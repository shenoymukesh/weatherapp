package com.mukesh.app.weatherapp.ui.model;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * Created by Mukesh Shenoy on 1/28/2018.
 */

/**
 * This is a model class for populating search suggestions.
 */
@Accessors(prefix = "m")
public class RegionSearchSuggestion implements SearchSuggestion {

    @Getter
    private final String mRegionName;

    @Getter
    private final String mZwm;

    @Getter
    @Setter
    private boolean mIsHistory;

    public RegionSearchSuggestion(String regionName, String zwm) {
        mRegionName = regionName;
        mZwm = zwm;
    }

    public RegionSearchSuggestion(Parcel in) {
        mRegionName = in.readString();
        mZwm = in.readString();
    }

    @Override
    public String getBody() {
        return mRegionName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mRegionName);
        parcel.writeString(mZwm);
    }

    public static final Creator<RegionSearchSuggestion> CREATOR = new Creator<RegionSearchSuggestion>() {
        @Override
        public RegionSearchSuggestion createFromParcel(Parcel in) {
            return new RegionSearchSuggestion(in);
        }

        @Override
        public RegionSearchSuggestion[] newArray(int size) {
            return new RegionSearchSuggestion[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegionSearchSuggestion that = (RegionSearchSuggestion) o;

        if (!mRegionName.equals(that.mRegionName)) return false;
        return mZwm.equals(that.mZwm);
    }

    @Override
    public int hashCode() {
        int result = mRegionName.hashCode();
        result = 31 * result + mZwm.hashCode();
        return result;
    }
}
