package com.fanchen.anim.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.fanchen.anim.R;
import com.fanchen.anim.activity.MainActivity;
import com.fanchen.anim.adapter.pager.HomePagerAdapter;
import com.fanchen.anim.base.BaseFragment;
import com.fanchen.anim.view.CircleImageView;
import com.flyco.tablayout.SlidingTabLayout;

/**
 * Created by fanchen on 2017/2/24.
 */
public class HomePagerFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener, SearchFragment.OnSearchClickListener, View.OnClickListener {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTab;
    private CircleImageView mCircleImageView;

    private HomePagerAdapter mHomePagerAdapter;
    private SearchFragment mSearchFragment;

    @Override
    protected int getLayout() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void findView(View v) {
        mToolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.view_pager);
        mSlidingTab = findViewById(R.id.sliding_tabs);
    }

    @Override
    protected void initViewData(@Nullable Bundle savedInstanceState, Bundle args) {
        super.initViewData(savedInstanceState, args);
        setHasOptionsMenu(true);
        mToolbar.setTitle("");
        activity.setSupportActionBar(mToolbar);
        mSearchFragment = SearchFragment.newInstance();
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mHomePagerAdapter);
        mSlidingTab.setViewPager(mViewPager);
//        mViewPager.setCurrentItem(1);
    }

    @Override
    protected void setListener() {
        super.setListener();
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setOnClickListener(this);
        mSearchFragment.setOnSearchClickListener(this);
    }

    @Override
    public void onSearchClick(String keyword) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_action_search://点击搜索
                mSearchFragment.show(getActivity().getSupportFragmentManager(), SearchFragment.TAG);
                break;
            case R.id.id_action_download:
                break;
            case R.id.id_action_game:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar:
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).toggleDrawer();
                }
                break;
        }
    }

}
