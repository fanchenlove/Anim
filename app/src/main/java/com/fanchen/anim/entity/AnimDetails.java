package com.fanchen.anim.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 动漫详情
 * Created by fanchen on 2017/1/10.
 */
public class AnimDetails extends Anim {

    private List<AnimEpisode> episodes;
    private List<AnimItem> recommends;

    public AnimDetails() {

    }

    protected AnimDetails(Parcel in) {
        super(in);
        episodes = in.createTypedArrayList(AnimEpisode.CREATOR);
        recommends = in.createTypedArrayList(AnimItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(episodes);
        dest.writeTypedList(recommends);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AnimDetails> CREATOR = new Creator<AnimDetails>() {
        @Override
        public AnimDetails createFromParcel(Parcel in) {
            return new AnimDetails(in);
        }

        @Override
        public AnimDetails[] newArray(int size) {
            return new AnimDetails[size];
        }
    };

    public List<AnimEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<AnimEpisode> episodes) {
        this.episodes = episodes;
    }

    public List<AnimItem> getRecommends() {
        return recommends;
    }

    public void setRecommends(List<AnimItem> recommends) {
        this.recommends = recommends;
    }

    @Override
    public String toString() {
        return "AnimDetails{} " + super.toString();
    }
}
