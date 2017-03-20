package com.fanchen.anim.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanchen.anim.R;
import com.fanchen.anim.adapter.wrapper.AdapterOperation;
import com.fanchen.anim.entity.AnimItem;
import com.fanchen.anim.entity.AnimRecom;
import com.fanchen.anim.fragment.home.HomeBangumiFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanchen on 2017/3/17.
 */
public class AnimRecomAdapter extends RecyclerView.Adapter<AnimRecomAdapter.RecomViewHodler> implements AdapterOperation<AnimRecom> {

    private ArrayList<AnimRecom> mList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private Context context;
    private Picasso picasso;

    public AnimRecomAdapter(Context context, Picasso picasso) {
        this.context = context;
        this.picasso = picasso;
        mLayoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public AnimRecomAdapter.RecomViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        AnimRecomAdapter.RecomViewHodler holder = null;
        if (viewType == AnimRecom.TYPE_VERTICAL) {
            holder = new RecomViewHodler(mLayoutInflater.inflate(R.layout.item_home_vertical_recom, parent, false));
        } else {
            holder = new RecomViewHodler(mLayoutInflater.inflate(R.layout.item_home_horizontal_recom, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(AnimRecomAdapter.RecomViewHodler holder, int position) {
        if (position >= mList.size()) {
            return;
        }
        AnimRecom animRecom = mList.get(position);
        for (HorizontalCardViewHodler viewHodler : holder.cardViewHodlers) {
            viewHodler.itemView.setVisibility(View.INVISIBLE);
        }
        holder.bangumiTextView.setText(animRecom.getTitle());
        if (animRecom.isResource()) {
            try {
                holder.imageView.setImageResource(Integer.valueOf(animRecom.getCover()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            picasso.load(animRecom.getCover()).tag(HomeBangumiFragment.class).config(Bitmap.Config.RGB_565).into(holder.imageView);
        }
        List<AnimItem> value = animRecom.getItems();
        for (int i = 0; i < value.size() && i < 6; i++) {
            AnimItem animItem = value.get(i);
            HorizontalCardViewHodler viewHodler = holder.cardViewHodlers.get(i);
            viewHodler.itemView.setVisibility(View.VISIBLE);
            viewHodler.titleTextView.setText(animItem.getTitle());
            viewHodler.timeTextView.setText(animItem.getTime());
            picasso.load(animItem.getCover()).tag(HomeBangumiFragment.class).config(Bitmap.Config.RGB_565).into(viewHodler.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    /**
     * @param mbooru
     */
    public void add(AnimRecom mbooru) {
        if (mbooru == null)
            return;
        int size = mList.size();
        mList.add(mbooru);
        notifyItemRangeInserted(size, 1);
    }

    @Override
    public <W extends AnimRecom> void addAll(ArrayList<W> all) {
        if (all == null)
            return;
        int size = mList.size();
        mList.addAll(all);
        notifyItemRangeInserted(size, all.size());
    }

    /**
     * @param mbooru
     */
    public void remove(AnimRecom mbooru) {
        if (mbooru == null)
            return;
        mList.remove(mbooru);
        notifyDataSetChanged();
    }

    @Override
    public ArrayList<AnimRecom> getAll() {
        return mList;
    }

    @Override
    public AnimRecom get(int position) {
        return mList.get(position);
    }

    /**
     *
     */
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * @param pos
     */
    public void remove(int pos) {
        if (pos < 0 || pos >= mList.size())
            return;
        mList.remove(pos);
        notifyItemRemoved(pos);
    }

    /**
     * @return
     */
    public ArrayList<AnimRecom> getItemList() {
        return mList;
    }

    public static class RecomViewHodler extends RecyclerView.ViewHolder {

        public List<HorizontalCardViewHodler> cardViewHodlers = new ArrayList<>();
        public TextView bangumiTextView;
        public ImageView imageView;
        public View moreView;

        public RecomViewHodler(View itemView) {
            super(itemView);
            ViewGroup viewGroup = (ViewGroup) itemView;
            ViewGroup viewGroup2 = (ViewGroup) viewGroup.getChildAt(1);
            ViewGroup viewGroup3 = (ViewGroup) viewGroup.getChildAt(2);
            cardViewHodlers.add(new HorizontalCardViewHodler(viewGroup2.getChildAt(0)));
            cardViewHodlers.add(new HorizontalCardViewHodler(viewGroup2.getChildAt(1)));
            cardViewHodlers.add(new HorizontalCardViewHodler(viewGroup2.getChildAt(2)));
            cardViewHodlers.add(new HorizontalCardViewHodler(viewGroup3.getChildAt(0)));
            cardViewHodlers.add(new HorizontalCardViewHodler(viewGroup3.getChildAt(1)));
            cardViewHodlers.add(new HorizontalCardViewHodler(viewGroup3.getChildAt(2)));

            imageView = (ImageView) itemView.findViewById(R.id.item_bangumi_ic);
            bangumiTextView = (TextView) itemView.findViewById(R.id.item_title_bangumi);
            moreView = itemView.findViewById(R.id.item_title_more);
        }
    }

    public static class HorizontalCardViewHodler {
        public ImageView imageView;
        public TextView titleTextView;
        public TextView timeTextView;
        public View itemView;

        public HorizontalCardViewHodler(View itemView) {
            this.itemView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            titleTextView = (TextView) itemView.findViewById(R.id.item_title);
            timeTextView = (TextView) itemView.findViewById(R.id.item_time);
        }
    }
}
