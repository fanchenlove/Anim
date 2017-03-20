package com.fanchen.anim.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fanchen.anim.entity.inter.IAmin;

import java.io.Serializable;

/**
 * 分类
 * Created by fanchen on 2017/3/15.
 */
public class AnimClass<T extends Serializable> implements Parcelable,IAmin {
    private int drawable = -1;
    private String drawableUrl;
    private String title;
    private int source;
    private T data;

    public AnimClass() {
    }

    public AnimClass(int drawable, String title) {
        this.drawable = drawable;
        this.title = title;
    }

    public AnimClass(String drawableUrl, String title) {
        this.drawableUrl = drawableUrl;
        this.title = title;
    }

    public AnimClass(int drawable, String title, T data) {
        this.drawable = drawable;
        this.title = title;
        this.data = data;
    }

    public AnimClass(String drawableUrl, String title, T data) {
        this.drawableUrl = drawableUrl;
        this.title = title;
        this.data = data;
    }

    protected AnimClass(Parcel in) {
        drawable = in.readInt();
        drawableUrl = in.readString();
        title = in.readString();
        data = (T) in.readSerializable();
        source = in.readInt();
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getDrawableUrl() {
        return drawableUrl;
    }

    public void setDrawableUrl(String drawableUrl) {
        this.drawableUrl = drawableUrl;
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
        dest.writeInt(drawable);
        dest.writeString(drawableUrl);
        dest.writeString(title);
        dest.writeSerializable(data);
        dest.writeInt(source);
    }

    public static final Creator<AnimClass> CREATOR = new Creator<AnimClass>() {
        @Override
        public AnimClass createFromParcel(Parcel in) {
            return new AnimClass(in);
        }

        @Override
        public AnimClass[] newArray(int size) {
            return new AnimClass[size];
        }
    };
}
