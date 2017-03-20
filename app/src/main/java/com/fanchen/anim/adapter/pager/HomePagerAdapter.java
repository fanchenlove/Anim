package com.fanchen.anim.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanchen.anim.fragment.home.HomeBangumiFragment;
import com.fanchen.anim.fragment.home.HomeMoreFragment;
import com.fanchen.anim.fragment.home.HomeRecomFragment;
import com.fanchen.anim.fragment.home.HomeRegionFragment;

/**
 * 主界面Fragment模块Adapter
 * Created by fanchen on 2017/2/25.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = new String[]{"次元推荐","次元番剧","次元分类","更多次元"};
    private Fragment[] fragments = new Fragment[TITLES.length];

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = HomeRecomFragment.newIntance();
                    break;
                case 1:
                    fragments[position] = HomeBangumiFragment.newInstance();
                    break;
                case 2:
                    fragments[position] = HomeRegionFragment.newInstance();
                    break;
                case 3:
                    fragments[position] = HomeMoreFragment.newInstance();
                    break;
                default:
                    break;
            }
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
