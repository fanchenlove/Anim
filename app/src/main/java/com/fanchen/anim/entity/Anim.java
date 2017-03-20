package com.fanchen.anim.entity;

import android.os.Parcel;
/**
 * fanchen
 * Created by fanchen on 2017/1/10.
 */
public class Anim extends AnimItem{

    private String state;
    private String author;
    private String type;
    private String tags;
    private String intro;

    public Anim() {
    }

    protected Anim(Parcel in) {
        super(in);
        state = in.readString();
        author = in.readString();
        type = in.readString();
        tags = in.readString();
        intro = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(state);
        dest.writeString(author);
        dest.writeString(type);
        dest.writeString(tags);
        dest.writeString(intro);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Anim> CREATOR = new Creator<Anim>() {
        @Override
        public Anim createFromParcel(Parcel in) {
            return new Anim(in);
        }

        @Override
        public Anim[] newArray(int size) {
            return new Anim[size];
        }
    };

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Anim{");
        sb.append("state='").append(state).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", tags='").append(tags).append('\'');
        sb.append(", intro='").append(intro).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
