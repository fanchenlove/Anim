package com.fanchen.anim.retrofit.manager;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fanchen.anim.parser.annotation.AnimParser;
import com.fanchen.anim.parser.annotation.AnimType;
import com.fanchen.anim.retrofit.call.RetrofitCallback;
import com.fanchen.anim.retrofit.call.SRetrofitCallback;
import com.fanchen.anim.retrofit.factory.AnimFactory;
import com.fanchen.anim.util.LogUtil;
import com.fanchen.anim.util.NetworkUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by fanchen on 2017/2/2.
 */
public class RetrofitManager {

    private static RetrofitManager manager;

    private static Context appContext;

    private static Map<AnimType, Retrofit> retrofitMap = new HashMap<>();

    private static Map<String, Object> clazzCache = new HashMap<>();

    private static Map<String,Integer> keyCache = new HashMap<>();

    //http://app46.bumimi.com/index.php?newc=1&m=content&c=index&a=lists&catid=5&in=app&uuid=727013F07683993C07A542796E8E5EDD&appkey=8df31adf60444e32fccab9c9a1b1fbba
    static {
        Converter.Factory animFactory = AnimFactory.create();
        retrofitMap.put(AnimType.BUMIMI, new Retrofit.Builder().baseUrl("http://app46.bumimi.com/").addConverterFactory(animFactory).build());
        retrofitMap.put(AnimType.DALIDY, new Retrofit.Builder().baseUrl("http://www.daendy.com").addConverterFactory(animFactory).build());
        retrofitMap.put(AnimType.DILIDILI, new Retrofit.Builder().baseUrl("http://www.dilidili.wang").addConverterFactory(animFactory).build());
        retrofitMap.put(AnimType.FENGCHE_COMIC, new Retrofit.Builder().baseUrl("http://fengchedm.com").addConverterFactory(animFactory).build());
    }

    /**
     *
     * @param context
     */
    private RetrofitManager(Context context) {
        if (appContext == null)
            appContext = context.getApplicationContext();
    }

    /**
     *
     * @param context
     */
    public static void init(Context context){
        if(manager == null)
            manager = new RetrofitManager(context);
    }

    /**
     *
     * @return
     */
    public static RetrofitManager getManager(){
        if(manager == null)
            throw new IllegalArgumentException("you must before call init(Context context)");
        return manager;
    }

    /**
     *
     * @param clazz
     * @param callback
     * @param method
     * @param enqueueKey
     * @param args
     * @param <T>
     */
    public <T> void enqueue(Class<?> clazz,final SRetrofitCallback<T> callback, String method,final int enqueueKey,Object... args){
        String key = clazz.getSimpleName();
        //取缓存
        Object o = clazzCache.get(key);
        if (o == null) {
            o = create(clazz);
            clazzCache.put(key, o);
        }
        Method enqueueMethod = findMethod(method, o);
        if (enqueueMethod == null) {
            throw new RuntimeException("can not find Method name " + method);
        }
        try{
            Call<T> invoke = (Call<T>) enqueueMethod.invoke(o, args);
            if(NetworkUtil.isNetWorkAvailable(appContext)){
                if(callback instanceof RetrofitCallback)
                    ((RetrofitCallback)callback).onStart(enqueueKey);
                invoke.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse(Call<T> call, Response<T> response) {
                        callback.onSuccess(enqueueKey, response.body());
                        if(callback instanceof RetrofitCallback)
                            ((RetrofitCallback)callback).onFinish(enqueueKey);
                    }

                    @Override
                    public void onFailure(Call<T> call, Throwable throwable) {
                        callback.onFailure(enqueueKey, throwable.toString());
                        if(callback instanceof RetrofitCallback)
                            ((RetrofitCallback)callback).onFinish(enqueueKey);
                    }
                });
            }else{
                //网络不可用
                callback.onFailure(enqueueKey, "当前网络不可用");
                if(callback instanceof RetrofitCallback)
                    ((RetrofitCallback)callback).onFinish(enqueueKey);
            }
        }catch (Throwable e){
            LogUtil.e(getClass().getSimpleName(),e.toString());
            callback.onFailure(enqueueKey, e.toString());
            if(callback instanceof RetrofitCallback)
                ((RetrofitCallback)callback).onFinish(enqueueKey);
        }
    }

    /**
     *
     * @param clazz
     * @param callback
     * @param method
     * @param enqueueKey
     * @param <T>
     */
    public <T> void enqueue(Class<?> clazz,RetrofitCallback<T> callback, String method,int enqueueKey){
        enqueue(clazz,callback,method,enqueueKey,null);
    }

    /**
     *
     * @param clazz
     * @param callback
     * @param method
     * @param <T>
     */
    public <T> void enqueue(Class<?> clazz,RetrofitCallback<T> callback, String method){
        Integer integer = findKey(clazz, method);
        enqueue(clazz,callback,method,integer);
    }

    /**
     *
     * @param clazz
     * @param callback
     * @param method
     * @param args
     * @param <T>
     */
    public <T> void enqueue(Class<?> clazz,final SRetrofitCallback<T> callback, String method,Object... args){
        Integer integer = findKey(clazz, method);
        enqueue(clazz,callback,method,integer,args);
    }


    /**
     *
     * @param method
     * @param o
     * @return
     */
    @Nullable
    private Method findMethod(String method, Object o) {
        Method[] declaredMethods = o.getClass().getDeclaredMethods();
        if (declaredMethods == null || declaredMethods.length == 0) {
            throw new RuntimeException("this class not Method");
        }
        Method enqueueMethod = null;
        for (Method m : declaredMethods) {
            //这里暂不考虑重载情况
            if (m.getName().equals(method)) {
                enqueueMethod = m;
                break;
            }
        }
        return enqueueMethod;
    }

    /**
     *
     * @param clazz
     * @return
     * @throws RuntimeException
     */
    private Object create(Class<?> clazz) throws RuntimeException{
        AnimParser annotations = clazz.getAnnotation(AnimParser.class);
        if (annotations == null) {
            throw new RuntimeException("this class not annotations flag");
        }
        AnimType type = annotations.value();
        Retrofit retrofit = retrofitMap.get(type);
        if (retrofit == null) {
            throw new RuntimeException("can not find retrofit by key " + type);
        }
        return retrofit.create(clazz);
    }

    @NonNull
    private Integer findKey(Class<?> clazz, String method) {
        String key = clazz.getName() + "@" + method;
        Integer integer = keyCache.get(key);
        if(integer == null){
            integer = keyCache.size();
        }
        return integer;
    }
}
