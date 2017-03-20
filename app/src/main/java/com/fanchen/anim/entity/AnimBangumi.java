package com.fanchen.anim.entity;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.fanchen.anim.entity.inter.ITitle;

import java.util.List;

/**
 *
 * Created by fanchen on 2017/3/17.
 */
public class AnimBangumi implements ITitle ,Parcelable {
    private List<AnimItem> items;
    private boolean isResource;
    private String title;
    private String cover;

    public AnimBangumi() {
    }

    protected AnimBangumi(Parcel in) {
        items = in.createTypedArrayList(AnimItem.CREATOR);
        isResource = in.readByte() != 0;
        title = in.readString();
        cover = in.readString();
    }

    public static final Creator<AnimBangumi> CREATOR = new Creator<AnimBangumi>() {
        @Override
        public AnimBangumi createFromParcel(Parcel in) {
            return new AnimBangumi(in);
        }

        @Override
        public AnimBangumi[] newArray(int size) {
            return new AnimBangumi[size];
        }
    };

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getCover() {
        return cover;
    }

    public List<AnimItem> getItems() {
        return items;
    }

    public void setItems(List<AnimItem> items) {
        this.items = items;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public boolean isResource() {
        return isResource;
    }

    public void setIsResource(boolean isResource) {
        this.isResource = isResource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeByte((byte) (isResource ? 1 : 0));
        dest.writeString(title);
        dest.writeString(cover);
    }
}
