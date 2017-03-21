package com.fanchen.anim.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fanchen.anim.entity.inter.ITitle;
import com.fanchen.anim.entity.inter.IBanner;

/**
 * 横幅
 * Created by fanchen on 2017/3/11.
 */
public class AnimBanner implements ITitle, Parcelable,IBanner<AnimBanner> {

    private String cover;
    private String title;
    private String cid;
    private String id;
    private int source;

    public AnimBanner() {
    }


    protected AnimBanner(Parcel in) {
        cover = in.readString();
        title = in.readString();
        cid = in.readString();
        id = in.readString();
        source = in.readInt();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getCover() {
        return cover;
    }

    @Override
    public int getBannerType() {
        return 0;
    }

    @Override
    public AnimBanner getData() {
        return this;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(cover);
        dest.writeString(title);
        dest.writeString(cid);
        dest.writeString(id);
        dest.writeInt(source);
    }

    public static final Creator<AnimBanner> CREATOR = new Creator<AnimBanner>() {
        @Override
        public AnimBanner createFromParcel(Parcel in) {
            return new AnimBanner(in);
        }

        @Override
        public AnimBanner[] newArray(int size) {
            return new AnimBanner[size];
        }
    };
}
