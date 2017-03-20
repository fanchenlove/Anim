package com.fanchen.anim.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by fanchen on 2017/3/11.
 */
public class AnimBangumiHome implements Parcelable {

    @NonNull//避免 NullPoint
    private ArrayList<AnimBanner> banners = new ArrayList<>();
    @NonNull//避免 NullPoint
    private ArrayList<AnimBangumi> items = new ArrayList<>();

    private boolean hasBanner = false;

    public AnimBangumiHome() {
    }


    protected AnimBangumiHome(Parcel in) {
        banners = in.createTypedArrayList(AnimBanner.CREATOR);
        items = in.createTypedArrayList(AnimBangumi.CREATOR);
        hasBanner = in.readByte() != 0;
    }

    public static final Creator<AnimBangumiHome> CREATOR = new Creator<AnimBangumiHome>() {
        @Override
        public AnimBangumiHome createFromParcel(Parcel in) {
            return new AnimBangumiHome(in);
        }

        @Override
        public AnimBangumiHome[] newArray(int size) {
            return new AnimBangumiHome[size];
        }
    };

    public ArrayList<AnimBanner> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<AnimBanner> banners) {
        this.banners = banners;
        if (this.banners != null && this.banners.size() > 0)
            hasBanner = true;
    }

    public ArrayList<AnimBangumi> getItems() {
        return items;
    }

    public void setItems(ArrayList<AnimBangumi> items) {
        this.items = items;
    }

    public boolean isHasBanner() {
        return hasBanner;
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
