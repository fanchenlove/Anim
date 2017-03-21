package com.fanchen.anim.entity.inter;


public interface IBanner<T> {

    String getTitle();

    String getCover();

    T getData();

    int getBannerType();

}
