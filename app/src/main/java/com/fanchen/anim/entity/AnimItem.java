package com.fanchen.anim.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fanchen.anim.entity.inter.ITitle;

/**
 * Created by fanchen on 2017/1/11.
 */
public class AnimItem implements ITitle,Parcelable{

    private String cover;
    private String title;
    private int source;
    private int week;
    private String lastEpisode;
    private String time;
    private String id;
    private String catid;
    private int itemType ;

    public AnimItem() {
    }

    public AnimItem(Parcel in) {
        cover = in.readString();
        title = in.readString();
        source = in.readInt();
        week = in.readInt();
        lastEpisode = in.readString();
        time = in.readString();
        id = in.readString();
        catid = in.readString();
    }

    public static final Creator<AnimItem> CREATOR = new Creator<AnimItem>() {
        @Override
        public AnimItem createFromParcel(Parcel in) {
            return new AnimItem(in);
        }

        @Override
        public AnimItem[] newArray(int size) {
            return new AnimItem[size];
        }
    };

    @Override
    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getLastEpisode() {
        return lastEpisode;
    }

    public void setLastEpisode(String lastEpisode) {
        this.lastEpisode = lastEpisode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int type) {
        this.itemType = type;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cover);
        dest.writeString(title);
        dest.writeInt(source);
        dest.writeInt(week);
        dest.writeString(lastEpisode);
        dest.writeString(time);
        dest.writeString(id);
        dest.writeString(catid);
    }
}
