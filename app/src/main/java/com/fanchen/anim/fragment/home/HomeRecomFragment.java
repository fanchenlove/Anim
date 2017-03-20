package com.fanchen.anim.fragment.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.fanchen.anim.R;
import com.fanchen.anim.adapter.AnimRecomAdapter;
import com.fanchen.anim.base.BaseFragment;
import com.fanchen.anim.adapter.wrapper.HeaderAndFooterWrapper;
import com.fanchen.anim.entity.AnimRecomHome;
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
public class HomeRecomFragment extends BaseFragment implements OnBannerItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LoopViewPagerLayout mBannerView;

    private AnimRecomAdapter mAnimRecomAdapter;
    private Picasso picasso;

    public static HomeRecomFragment newIntance() {
        return new HomeRecomFragment();
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
    HeaderAndFooterWrapper wrapper;

    @Override
    protected void initViewData(@Nullable Bundle savedInstanceState) {
        super.initViewData(savedInstanceState);
        picasso = Picasso.with(activity);
        mAnimRecomAdapter = new AnimRecomAdapter(activity, picasso);
        wrapper = new HeaderAndFooterWrapper(mAnimRecomAdapter);
        LayoutInflater layoutInflater = getLayoutInflater();
        if (layoutInflater != null) {
            View inflate = layoutInflater.inflate(R.layout.layout_banner, null, false);
            mBannerView = (LoopViewPagerLayout) inflate.findViewById(R.id.lvpl_banner);
            wrapper.addHeaderView(inflate);
            mBannerView.initializeData(activity);//初始化数据
            mBannerView.setOnLoadImageViewListener(imageViewLoader);//设置图片加载&自定义图片监听
        }
        mRecyclerView.setAdapter(wrapper);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        RetrofitManager.getManager().enqueue(BumimiService.class,callback,"loadRecomHome");
    }

    @Override
    protected void setListener() {
        super.setListener();
        mBannerView.setOnBannerItemClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void onBannerClick(int index, ArrayList<? extends IBanner> banner) {

    }

    @Override
    public void onRefresh() {

    }

    private OnDefaultImageViewLoader imageViewLoader = new OnDefaultImageViewLoader() {
        @Override
        public void onLoadImageView(ImageView imageView, Object parameter) {
            picasso.load(parameter.toString()).tag(HomeRecomFragment.class).config(Bitmap.Config.RGB_565).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    };

    private RetrofitCallback<AnimRecomHome> callback = new RetrofitCallback<AnimRecomHome>(){

        @Override
        public void onStart(int enqueueKey) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        public void onFinish(int enqueueKey) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onSuccess(int enqueueKey, AnimRecomHome response) {
            if (mBannerView != null && response.isHasBanner()) {
                mBannerView.setLoopData(response.getBanners());//设置数据
                mBannerView.startLoop();
            }
            mAnimRecomAdapter.addAll(response.getItems());
            wrapper.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int enqueueKey, String throwable) {
            showSnackbar(throwable);
        }
    };
}
