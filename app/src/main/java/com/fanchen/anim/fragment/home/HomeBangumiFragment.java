package com.fanchen.anim.fragment.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.fanchen.anim.R;
import com.fanchen.anim.activity.BangumiRegionActivity;
import com.fanchen.anim.adapter.AnimBangumiAdapter;
import com.fanchen.anim.base.BaseFragment;
import com.fanchen.anim.adapter.wrapper.HeaderAndFooterWrapper;
import com.fanchen.anim.entity.AnimBangumiHome;
import com.fanchen.anim.retrofit.BumimiService;
import com.fanchen.anim.retrofit.call.RetrofitCallback;
import com.fanchen.anim.retrofit.manager.RetrofitManager;
import com.github.why168.LoopViewPagerLayout;
import com.github.why168.listener.OnBannerItemClickListener;
import com.github.why168.loader.OnDefaultImageViewLoader;
import com.github.why168.modle.IBanner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by fanchen on 2017/2/25.
 */
public class HomeBangumiFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnBannerItemClickListener, View.OnClickListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LoopViewPagerLayout mBannerView;
    private View indexView;
    private View timelineView;

    private AnimBangumiAdapter mBangumiAdapter;
    private Picasso picasso;


    public static HomeBangumiFragment newInstance() {
        return new HomeBangumiFragment();
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
        picasso = Picasso.with(activity);
        mBangumiAdapter = new AnimBangumiAdapter(activity,picasso);
        HeaderAndFooterWrapper wrapper = new HeaderAndFooterWrapper(mBangumiAdapter);
        LayoutInflater layoutInflater = getLayoutInflater();
        if (layoutInflater != null) {
            View inflate = layoutInflater.inflate(R.layout.layout_bangumi_banner, null, false);
            mBannerView = (LoopViewPagerLayout) inflate.findViewById(R.id.lvpl_banner);
            indexView = inflate.findViewById(R.id.iv_index);
            timelineView = inflate.findViewById(R.id.iv_timeline);
            wrapper.addHeaderView(inflate);
            mBannerView.initializeData(activity);//初始化数据
            mBannerView.setOnLoadImageViewListener(imageViewLoader);//设置图片加载&自定义图片监听
        }
        mRecyclerView.setAdapter(wrapper);
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        mSwipeRefreshLayout.setColorSchemeColors(typedValue.data);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        RetrofitManager.getManager().enqueue(BumimiService.class, call, "loadBangumiHome", new Object[]{Integer.valueOf(1), "5", "727013F07683993C07A542796E8E5EDD", "8df31adf60444e32fccab9c9a1b1fbba"});
    }

    @Override
    protected void setListener() {
        super.setListener();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(scrollListener);
        indexView.setOnClickListener(this);
        timelineView.setOnClickListener(this);
        if (mBannerView != null) mBannerView.setOnBannerItemClickListener(this);//设置监听
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBannerView != null) {
            mBannerView.stopLoop();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_timeline:
                break;
            case R.id.iv_index:
                startActivity(BangumiRegionActivity.class);
                break;
        }
    }

    @Override
    public void onBannerClick(int index, ArrayList<? extends IBanner> banner) {
    }

    @Override
    public void onRefresh() {

    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if(newState == RecyclerView.SCROLL_STATE_IDLE){
                picasso.resumeTag(HomeBangumiFragment.class);
            }else{
                picasso.resumeTag(HomeBangumiFragment.class);
            }
        }
    };

    private OnDefaultImageViewLoader imageViewLoader = new OnDefaultImageViewLoader() {
        @Override
        public void onLoadImageView(ImageView imageView, Object parameter) {
            picasso.load(parameter.toString()).tag(HomeBangumiFragment.class).config(Bitmap.Config.RGB_565).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    };

    private RetrofitCallback<AnimBangumiHome> call = new RetrofitCallback<AnimBangumiHome>() {

        @Override
        public void onStart(int enqueueKey) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        public void onFinish(int enqueueKey) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onSuccess(int enqueueKey, AnimBangumiHome response) {
            if (mBannerView != null && response.isHasBanner()) {
                mBannerView.setLoopData(response.getBanners());//设置数据
                mBannerView.startLoop();
            }
            mBangumiAdapter.addAll(response.getItems());
        }

        @Override
        public void onFailure(int enqueueKey, String throwable) {
            showSnackbar(throwable);
        }
    };

}
