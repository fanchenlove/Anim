package com.fanchen.anim.adapter.wrapper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fanchen.anim.adapter.wrapper.AdapterOperation;

import java.util.ArrayList;

/**
 * Created by fanchen on 2017/3/14.
 */
public class HeaderAndFooterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdapterOperation<T>{
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;
    private AdapterOperation<T> operation;

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        if(! (adapter instanceof AdapterOperation))
            throw new IllegalStateException("adapter must impl AdapterOperation");
        mInnerAdapter = adapter;
        operation = (AdapterOperation) this.mInnerAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            HeaderAndFooterViewHolder holder = HeaderAndFooterViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
            return holder;
        } else if (mFootViews.get(viewType) != null) {
            HeaderAndFooterViewHolder holder = HeaderAndFooterViewHolder.createViewHolder(parent.getContext(), mFootViews.get(viewType));
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            return;
        }
        if (isFooterViewPos(position)) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                } else if (mFootViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder);
        }
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }


    public void addFootView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }


    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }

    @Override
    public void add(T data) {
        operation.add(data);
        notifyDataSetChanged();
    }

    @Override
    public <W extends T> void addAll(ArrayList<W> all) {
        operation.addAll(all);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        operation.clear();
        notifyDataSetChanged();
    }

    @Override
    public void remove(int position) {
        operation.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void remove(T data) {
        operation.remove(data);
        notifyDataSetChanged();
    }

    @Override
    public ArrayList<T> getAll() {
        return operation.getAll();
    }

    @Override
    public T get(int position) {
        return operation.get(position);
    }


    public static class HeaderAndFooterViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;
        private Context mContext;

        public HeaderAndFooterViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            mConvertView = itemView;
            mViews = new SparseArray<View>();
        }


        public static HeaderAndFooterViewHolder createViewHolder(Context context, View itemView) {
            HeaderAndFooterViewHolder holder = new HeaderAndFooterViewHolder(context, itemView);
            return holder;
        }

        public static HeaderAndFooterViewHolder createViewHolder(Context context,
                                                                 ViewGroup parent, int layoutId) {
            View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            HeaderAndFooterViewHolder holder = new HeaderAndFooterViewHolder(context, itemView);
            return holder;
        }

        /**
         * 通过viewId获取控件
         *
         * @param viewId
         * @return
         */
        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public View getConvertView() {
            return mConvertView;
        }


        /****以下为辅助方法*****/

        /**
         * 设置TextView的值
         *
         * @param viewId
         * @param text
         * @return
         */
        public HeaderAndFooterViewHolder setText(int viewId, String text) {
            TextView tv = getView(viewId);
            tv.setText(text);
            return this;
        }

        public HeaderAndFooterViewHolder setImageResource(int viewId, int resId) {
            ImageView view = getView(viewId);
            view.setImageResource(resId);
            return this;
        }

        public HeaderAndFooterViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
            ImageView view = getView(viewId);
            view.setImageBitmap(bitmap);
            return this;
        }

        public HeaderAndFooterViewHolder setImageDrawable(int viewId, Drawable drawable) {
            ImageView view = getView(viewId);
            view.setImageDrawable(drawable);
            return this;
        }

        public HeaderAndFooterViewHolder setBackgroundColor(int viewId, int color) {
            View view = getView(viewId);
            view.setBackgroundColor(color);
            return this;
        }

        public HeaderAndFooterViewHolder setBackgroundRes(int viewId, int backgroundRes) {
            View view = getView(viewId);
            view.setBackgroundResource(backgroundRes);
            return this;
        }

        public HeaderAndFooterViewHolder setTextColor(int viewId, int textColor) {
            TextView view = getView(viewId);
            view.setTextColor(textColor);
            return this;
        }

        public HeaderAndFooterViewHolder setTextColorRes(int viewId, int textColorRes) {
            TextView view = getView(viewId);
            view.setTextColor(mContext.getResources().getColor(textColorRes));
            return this;
        }

        @SuppressLint("NewApi")
        public HeaderAndFooterViewHolder setAlpha(int viewId, float value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getView(viewId).setAlpha(value);
            } else {
                // Pre-honeycomb hack to set Alpha value
                AlphaAnimation alpha = new AlphaAnimation(value, value);
                alpha.setDuration(0);
                alpha.setFillAfter(true);
                getView(viewId).startAnimation(alpha);
            }
            return this;
        }

        public HeaderAndFooterViewHolder setVisible(int viewId, boolean visible) {
            View view = getView(viewId);
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
            return this;
        }

        public HeaderAndFooterViewHolder linkify(int viewId) {
            TextView view = getView(viewId);
            Linkify.addLinks(view, Linkify.ALL);
            return this;
        }

        public HeaderAndFooterViewHolder setTypeface(Typeface typeface, int... viewIds) {
            for (int viewId : viewIds) {
                TextView view = getView(viewId);
                view.setTypeface(typeface);
                view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            }
            return this;
        }

        public HeaderAndFooterViewHolder setProgress(int viewId, int progress) {
            ProgressBar view = getView(viewId);
            view.setProgress(progress);
            return this;
        }

        public HeaderAndFooterViewHolder setProgress(int viewId, int progress, int max) {
            ProgressBar view = getView(viewId);
            view.setMax(max);
            view.setProgress(progress);
            return this;
        }

        public HeaderAndFooterViewHolder setMax(int viewId, int max) {
            ProgressBar view = getView(viewId);
            view.setMax(max);
            return this;
        }

        public HeaderAndFooterViewHolder setRating(int viewId, float rating) {
            RatingBar view = getView(viewId);
            view.setRating(rating);
            return this;
        }

        public HeaderAndFooterViewHolder setRating(int viewId, float rating, int max) {
            RatingBar view = getView(viewId);
            view.setMax(max);
            view.setRating(rating);
            return this;
        }

        public HeaderAndFooterViewHolder setTag(int viewId, Object tag) {
            View view = getView(viewId);
            view.setTag(tag);
            return this;
        }

        public HeaderAndFooterViewHolder setTag(int viewId, int key, Object tag) {
            View view = getView(viewId);
            view.setTag(key, tag);
            return this;
        }

        public HeaderAndFooterViewHolder setChecked(int viewId, boolean checked) {
            Checkable view = (Checkable) getView(viewId);
            view.setChecked(checked);
            return this;
        }

        /**
         * 关于事件的
         */
        public HeaderAndFooterViewHolder setOnClickListener(int viewId,
                                                            View.OnClickListener listener) {
            View view = getView(viewId);
            view.setOnClickListener(listener);
            return this;
        }

        public HeaderAndFooterViewHolder setOnTouchListener(int viewId,
                                                            View.OnTouchListener listener) {
            View view = getView(viewId);
            view.setOnTouchListener(listener);
            return this;
        }

        public HeaderAndFooterViewHolder setOnLongClickListener(int viewId,
                                                                View.OnLongClickListener listener) {
            View view = getView(viewId);
            view.setOnLongClickListener(listener);
            return this;
        }
    }

    public static class WrapperUtils {
        public interface SpanSizeCallback {
            int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position);
        }

        public static void onAttachedToRecyclerView(Adapter innerAdapter, RecyclerView recyclerView, final SpanSizeCallback callback) {
            innerAdapter.onAttachedToRecyclerView(recyclerView);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return callback.getSpanSize(gridLayoutManager, spanSizeLookup, position);
                    }
                });
                gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
            }
        }

        public static void setFullSpan(RecyclerView.ViewHolder holder) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null  && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }else if(lp != null  && lp instanceof GridLayoutManager.LayoutParams){
                GridLayoutManager.LayoutParams p = (GridLayoutManager.LayoutParams) lp;
//                p.set
            }
        }
    }
}
