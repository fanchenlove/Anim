package com.fanchen.anim.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 重写ImageView的onDetachedFromWindow方法，
 * 在它从屏幕中消失时回调，
 * 去掉drawable引用，能加快内存的回收。
 * Created by fanchen on 2017/1/18.
 */
public class RecyclerImageView extends ImageView {
    public RecyclerImageView(Context context) {
        super(context);
    }

    public RecyclerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RecyclerImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }
}
