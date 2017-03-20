package com.fanchen.anim.retrofit.call;

/**
 * 网络请求接口
 * Created by fanchen on 2017/2/10.
 */
public interface SRetrofitCallback<T> {
    /**
     * 请求成功
     *
     * @param enqueueKey
     * @param response
     */
    void onSuccess(int enqueueKey, T response);

    /**
     * 请求出错
     *
     * @param enqueueKey
     * @param throwable
     */
    void onFailure(int enqueueKey, String throwable);
}
