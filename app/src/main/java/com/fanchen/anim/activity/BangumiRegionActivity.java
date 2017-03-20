package com.fanchen.anim.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.fanchen.anim.R;
import com.fanchen.anim.adapter.BangumiRegionAdapter;
import com.fanchen.anim.base.BaseActivity;
import com.fanchen.anim.base.BaseRecyclerAdapter;
import com.fanchen.anim.entity.AnimClass;
import com.fanchen.anim.retrofit.BumimiService;
import com.fanchen.anim.retrofit.call.RetrofitCallback;
import com.fanchen.anim.retrofit.manager.RetrofitManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 番剧分类
 * Created by fanchen on 2017/3/16.
 */
public class BangumiRegionActivity extends BaseActivity implements BaseRecyclerAdapter.OnItemClickListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private BangumiRegionAdapter mRegionAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void findView(View view) {
        super.findView(view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle);
    }

    @Override
    protected void initViewData(Bundle savedState, LayoutInflater inflater) {
        super.initViewData(savedState, inflater);
        mRegionAdapter = new BangumiRegionAdapter(this);
        mRecyclerView.setAdapter(mRegionAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        RetrofitManager.getManager().enqueue(BumimiService.class, retrofitCallback, "loadRegionList", new Object[]{"727013F07683993C07A542796E8E5EDD","8df31adf60444e32fccab9c9a1b1fbba"});
    }

    @Override
    protected void setListener() {
        super.setListener();
        mRegionAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ArrayList<?> mList, View v, int position) {

    }

    private RetrofitCallback<ArrayList<AnimClass<String>>> retrofitCallback = new RetrofitCallback<ArrayList<AnimClass<String>>>() {
        @Override
        public void onStart(int enqueueKey) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        public void onFinish(int enqueueKey) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onSuccess(int enqueueKey, ArrayList<AnimClass<String>> response) {
            mRegionAdapter.clear();
            mRegionAdapter.addAll(response);
        }

        @Override
        public void onFailure(int enqueueKey, String throwable) {
            showSnackbar(throwable);
        }
    };
}
