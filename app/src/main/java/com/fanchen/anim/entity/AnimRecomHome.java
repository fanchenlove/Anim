package com.fanchen.anim.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by fanchen on 2017/3/17.
 */
public class AnimRecomHome implements Parcelable{
    @NonNull//避免 NullPoint
    private ArrayList<AnimBanner> banners = new ArrayList<>();
    @NonNull//避免 NullPoint
    private ArrayList<AnimRecom> items = new ArrayList<>();

    private boolean hasBanner = false;

    public AnimRecomHome() {
    }

    protected AnimRecomHome(Parcel in) {
        banners = in.createTypedArrayList(AnimBanner.CREATOR);
        items = in.createTypedArrayList(AnimRecom.CREATOR);
        hasBanner = in.readByte() != 0;
    }

    public static final Creator<AnimRecomHome> CREATOR = new Creator<AnimRecomHome>() {
        @Override
        public AnimRecomHome createFromParcel(Parcel in) {
            return new AnimRecomHome(in);
        }

        @Override
        public AnimRecomHome[] newArray(int size) {
            return new AnimRecomHome[size];
        }
    };

    @NonNull
    public ArrayList<AnimBanner> getBanners() {
        return banners;
    }

    public void setBanners(@NonNull ArrayList<AnimBanner> banners) {
        this.banners = banners;
        if(this.banners != null && this.banners.size() > 0)setHasBanner(true);
    }

    @NonNull
    public ArrayList<AnimRecom> getItems() {
        return items;
    }

    public void setItems(@NonNull ArrayList<AnimRecom> items) {
        this.items = items;
    }

    public boolean isHasBanner() {
        return hasBanner;
    }

    public void setHasBanner(boolean hasBanner) {
        this.hasBanner = hasBanner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(banners);
        dest.writeTypedList(items);
        dest.writeByte((byte) (hasBanner ? 1 : 0));
    }
}
