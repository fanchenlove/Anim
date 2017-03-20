package com.github.why168.modle;

/**
 * @author Edwin.Wu
 * @version 2016/12/6 17:32
 * @since JDK1.8
 */
public interface IBanner<T> {

    String getTitle();

    String getCover();

    T getData();

    int getBannerType();

}
