package com.fanchen.anim.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 动漫剧集
 * Created by fanchen on 2017/1/10.
 */
public class AnimEpisode implements Parcelable{
    private String title;
    private String from;
    private int source;
    private String id;
    private String catid;

    public AnimEpisode(Parcel in) {
        title = in.readString();
        from = in.readString();
        source = in.readInt();
        id = in.readString();
        catid = in.readString();
    }

    public AnimEpisode() {
    }

    public static final Creator<AnimEpisode> CREATOR = new Creator<AnimEpisode>() {
        @Override
        public AnimEpisode createFromParcel(Parcel in) {
            return new AnimEpisode(in);
        }

        @Override
        public AnimEpisode[] newArray(int size) {
            return new AnimEpisode[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(from);
        dest.writeInt(source);
        dest.writeString(id);
        dest.writeString(catid);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
