package com.fanchen.anim.adapter.wrapper;

import java.util.ArrayList;

/**
 * Created by fanchen on 2017/3/18.
 */
public interface AdapterOperation<T> {

    /**
     *
     * @param data
     */
    void add(T data);

    /**
     *
     * @param all
     */
    <W extends T> void  addAll(ArrayList<W> all);
    /**
     *
     */
    void clear();

    /**
     *
     * @param position
     */
    void remove(int position);

    /**
     *
     * @param data
     */
    void remove(T data);

    /**
     *
     * @return
     */
    ArrayList<T> getAll();

    /**
     *
     * @param position
     * @return
     */
    T get(int position);
}
