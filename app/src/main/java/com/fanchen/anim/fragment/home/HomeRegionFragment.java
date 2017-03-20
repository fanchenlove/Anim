package com.fanchen.anim.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.fanchen.anim.R;
import com.fanchen.anim.adapter.HomeRegionAdapter;
import com.fanchen.anim.base.BaseFragment;
import com.fanchen.anim.base.BaseRecyclerAdapter;
import com.fanchen.anim.util.BumimiUtil;

import java.util.ArrayList;

/**
 * 分类
 * Created by fanchen on 2017/2/25.
 */
public class HomeRegionFragment extends BaseFragment implements BaseRecyclerAdapter.OnItemClickListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private HomeRegionAdapter mRegionAdapter;

    public static HomeRegionFragment newInstance() {
        return new HomeRegionFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home_recyclerview;
    }

    @Override
    protected void findView(View v) {
        super.findView(v);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = findViewById(R.id.recycle);
    }

    @Override
    protected void initViewData(@Nullable Bundle savedInstanceState) {
        super.initViewData(savedInstanceState);
        mRegionAdapter = new HomeRegionAdapter(activity);
        mRecyclerView.setAdapter(mRegionAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRegionAdapter.addAll(BumimiUtil.getAnimClasss());
    }

    @Override
    protected void setListener() {
        super.setListener();
        mRegionAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ArrayList<?> mList, View v, int position) {

    }
}
