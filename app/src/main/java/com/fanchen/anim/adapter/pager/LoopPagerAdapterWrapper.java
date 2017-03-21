package com.fanchen.anim.adapter.pager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanchen.anim.entity.inter.IBanner;
import com.fanchen.anim.view.LoopViewPagerLayout;

import java.util.ArrayList;

/**
 * LoopPagerAdapterWrapper
 *
 * @author Edwin.Wu
 * @version 2016/12/1 17:48
 * @since JDK1.8
 */
public class LoopPagerAdapterWrapper extends PagerAdapter {
    private final Context context;
    private final ArrayList<? extends IBanner> bannerInfos;//banner data
    private final LoopViewPagerLayout.OnBannerItemClickListener onBannerItemClickListener;
    private final LoopViewPagerLayout.OnLoadImageViewListener onLoadImageViewListener;

    public LoopPagerAdapterWrapper(Context context, ArrayList<? extends IBanner> bannerInfos, LoopViewPagerLayout.OnBannerItemClickListener onBannerItemClickListener, LoopViewPagerLayout.OnLoadImageViewListener onLoadImageViewListener) {
        this.context = context;
        this.bannerInfos = bannerInfos;
        this.onBannerItemClickListener = onBannerItemClickListener;
        this.onLoadImageViewListener = onLoadImageViewListener;
    }


    @Override
    public int getCount() {
        return Short.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int index = position % bannerInfos.size();
        final IBanner bannerInfo = bannerInfos.get(index);
        ImageView child = null;
        if (onLoadImageViewListener != null) {
            child = onLoadImageViewListener.createImageView(context);
            onLoadImageViewListener.onLoadImageView(child, bannerInfo.getCover());
            container.addView(child);

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBannerItemClickListener != null)
                        onBannerItemClickListener.onBannerClick(index, bannerInfos);
                }
            });
        } else {
            throw new NullPointerException("LoopViewPagerLayout onLoadImageViewListener is not initialize,Be sure to initialize the onLoadImageView");
        }


        return child;
    }
}