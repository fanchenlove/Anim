package com.fanchen.anim.fragment.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanchen.anim.R;
import com.fanchen.anim.activity.CaptureActivity;
import com.fanchen.anim.activity.MainActivity;
import com.fanchen.anim.base.BaseFragment;
import com.fanchen.anim.fragment.SearchFragment;
import com.fanchen.anim.util.DisplayUtil;
import com.fanchen.anim.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanchen on 2017/2/25.
 */
public class HomeMoreFragment extends BaseFragment implements View.OnClickListener, FlowLayout.OnFlowItemClick, SearchFragment.OnSearchClickListener {

    public static final int DIP_64 = 64;
    public static final int DIP_128 = 128;

    private View mScanView;
    private View mSearchView;
    private ViewGroup mMoreHotView;
    private FlowLayout mHotFlowLayout;
    private NestedScrollView mNestedScrollView;

    private SearchFragment mSearchFragment;

    public static HomeMoreFragment newInstance() {
        return new HomeMoreFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home_more;
    }

    @Override
    protected void findView(View v) {
        super.findView(v);
        mSearchView = findViewById(R.id.search_bar);
        mScanView = findViewById(R.id.qr_scan);
        mMoreHotView = findViewById(R.id.ll_more_hotword);
        mHotFlowLayout = findViewById(R.id.flowlayout_work);
        mNestedScrollView = findViewById(R.id.nsv_hotword);
    }

    @Override
    protected void initViewData(@Nullable Bundle savedInstanceState, Bundle args) {
        super.initViewData(savedInstanceState, args);
        mSearchFragment = SearchFragment.newInstance();
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 100; i ++ ){
            datas.add("数据" + i);
        }
        mHotFlowLayout.addDataList2TextView(datas);
    }

    @Override
    protected void setListener() {
        super.setListener();
        mSearchView.setOnClickListener(this);
        mMoreHotView.setOnClickListener(this);
        mScanView.setOnClickListener(this);
        mHotFlowLayout.setOnFlowItemClick(this);
        mSearchFragment.setOnSearchClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_bar:
                if(activity != null && isAdded() && !isDetached()){
                    mSearchFragment.show(activity.getSupportFragmentManager(), SearchFragment.TAG);
                }
                break;
            case R.id.qr_scan:
                startActivity(CaptureActivity.class);
                break;
            case R.id.ll_more_hotword:
                ViewGroup.LayoutParams layoutParams = mNestedScrollView.getLayoutParams();
                if(layoutParams.height == DisplayUtil.dip2px(activity,DIP_128)){
                    layoutParams.height = DisplayUtil.dip2px(activity,DIP_64);
                    ((TextView)mMoreHotView.getChildAt(1)).setText("  查看更多");
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_down_gray_round, activity.getTheme());
                    ((TextView)mMoreHotView.getChildAt(1)).setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                }else{
                    layoutParams.height = DisplayUtil.dip2px(activity,DIP_128);
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_up_gray_round, activity.getTheme());
                    ((TextView)mMoreHotView.getChildAt(1)).setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    ((TextView)mMoreHotView.getChildAt(1)).setText("  收起");
                }
                mNestedScrollView.setLayoutParams(layoutParams);
                break;
        }
    }

    @Override
    public <T> void OnItemClick(View v, T data, int position) {

    }

    @Override
    public void onSearchClick(String word) {

    }
}
