package com.fanchen.anim.manager;


/**
 * Created by fanchen on 2017/3/17.
 */
public abstract class IManager<T,P> {

    private T manager;

    private IManager (P p){
    }

//    public final T getManager(P p){
//        if(manager == null){
//            synchronized (IManager.class){
//                if(manager == null){
//                   manager = new  IManager(p);
//                }
//            }
//        }
//    }

}
