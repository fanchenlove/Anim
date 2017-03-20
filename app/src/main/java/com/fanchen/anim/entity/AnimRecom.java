package com.fanchen.anim.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fanchen on 2017/3/17.
 */
public class AnimRecom extends AnimBangumi implements Parcelable {

    public static final int TYPE_HORIZONTAL = 0;
    public static final int TYPE_VERTICAL = 1;

    private int type = TYPE_HORIZONTAL;

    public AnimRecom() {
    }

    protected AnimRecom(Parcel in) {
        super(in);
        type = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AnimRecom> CREATOR = new Creator<AnimRecom>() {
        @Override
        public AnimRecom createFromParcel(Parcel in) {
            return new AnimRecom(in);
        }

        @Override
        public AnimRecom[] newArray(int size) {
            return new AnimRecom[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
