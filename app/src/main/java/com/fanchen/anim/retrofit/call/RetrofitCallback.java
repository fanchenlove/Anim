package com.fanchen.anim.retrofit.call;

/**
 * 网络请求接口
 *
 * 包含开始请求，和请求结束回调
 * Created by fanchen on 2017/2/8.
 */
public interface RetrofitCallback<T> extends SRetrofitCallback<T>{
    /**
     * 网络请求开始
     *
     * @param enqueueKey
     */
    void onStart(int enqueueKey);

    /**
     * 请求结束
     *
     * @param enqueueKey
     */
    void onFinish(int enqueueKey);
}
