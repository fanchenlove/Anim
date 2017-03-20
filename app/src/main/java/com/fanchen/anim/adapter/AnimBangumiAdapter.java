package com.fanchen.anim.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanchen.anim.R;
import com.fanchen.anim.base.BaseRecyclerAdapter;
import com.fanchen.anim.adapter.wrapper.AdapterOperation;
import com.fanchen.anim.entity.AnimBangumi;
import com.fanchen.anim.entity.AnimItem;
import com.fanchen.anim.fragment.home.HomeBangumiFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanchen on 2017/3/14.
 */
public class AnimBangumiAdapter extends BaseRecyclerAdapter<AnimBangumiAdapter.BangumiViewHodler, AnimBangumi> implements AdapterOperation<AnimBangumi> {

    private Picasso picasso;

    public AnimBangumiAdapter(Context context, Picasso picasso) {
        super(context);
        this.picasso = picasso;
    }

    @Override
    public void onBind(BangumiViewHodler holder, ArrayList<AnimBangumi> datas, int position) {
        if (holder == null || datas == null || datas.size() <= position) {
            return;
        }
        for (CardViewHodler viewHodler : holder.cardViewHodlers) {
            viewHodler.itemView.setVisibility(View.INVISIBLE);
        }
        AnimBangumi stringListMap = datas.get(position);
        holder.bangumiTextView.setText(stringListMap.getTitle());
        if (stringListMap.isResource()) {
            try {
                holder.imageView.setImageResource(Integer.valueOf(stringListMap.getCover()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            picasso.load(stringListMap.getCover()).tag(HomeBangumiFragment.class).config(Bitmap.Config.RGB_565).into(holder.imageView);
        }
        List<AnimItem> value = stringListMap.getItems();
        for (int i = 0; i < value.size() && i < 6; i++) {
            AnimItem animItem = value.get(i);
            CardViewHodler viewHodler = holder.cardViewHodlers.get(i);
            viewHodler.itemView.setVisibility(View.VISIBLE);
            viewHodler.lastTextView.setText(animItem.getLastEpisode());
            viewHodler.titleTextView.setText(animItem.getTitle());
            viewHodler.timeTextView.setText(animItem.getTime());
            picasso.load(animItem.getCover()).tag(HomeBangumiFragment.class).config(Bitmap.Config.RGB_565).into(viewHodler.imageView);
        }
    }

    @Override
    public BangumiViewHodler onCreateHolder(View inflate, int type) {
        return new BangumiViewHodler(inflate);
    }

    @Override
    public int getLayout() {
        return R.layout.item_home_bangumi;
    }

    @Override
    public ArrayList<AnimBangumi> getAll() {
        return mList;
    }

    @Override
    public AnimBangumi get(int position) {
        return mList.get(position);
    }


    public static class BangumiViewHodler extends RecyclerView.ViewHolder {

        public List<CardViewHodler> cardViewHodlers = new ArrayList<>();
        public TextView bangumiTextView;
        public ImageView imageView;
        public View moreView;

        public BangumiViewHodler(View itemView) {
            super(itemView);
            ViewGroup viewGroup = (ViewGroup) itemView;
            ViewGroup viewGroup2 = (ViewGroup) viewGroup.getChildAt(1);
            ViewGroup viewGroup3 = (ViewGroup) viewGroup.getChildAt(2);
            cardViewHodlers.add(new CardViewHodler(viewGroup2.getChildAt(0)));
            cardViewHodlers.add(new CardViewHodler(viewGroup2.getChildAt(1)));
            cardViewHodlers.add(new CardViewHodler(viewGroup2.getChildAt(2)));
            cardViewHodlers.add(new CardViewHodler(viewGroup3.getChildAt(0)));
            cardViewHodlers.add(new CardViewHodler(viewGroup3.getChildAt(1)));
            cardViewHodlers.add(new CardViewHodler(viewGroup3.getChildAt(2)));

            imageView = (ImageView) itemView.findViewById(R.id.item_bangumi_ic);
            bangumiTextView = (TextView) itemView.findViewById(R.id.item_title_bangumi);
            moreView = itemView.findViewById(R.id.item_title_more);
        }
    }

    public static class CardViewHodler {
        public ImageView imageView;
        public TextView titleTextView;
        public TextView lastTextView;
        public TextView timeTextView;
        public View itemView;

        public CardViewHodler(View itemView) {
            this.itemView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            titleTextView = (TextView) itemView.findViewById(R.id.item_title);
            lastTextView = (TextView) itemView.findViewById(R.id.item_last);
            timeTextView = (TextView) itemView.findViewById(R.id.item_time);
        }
    }
}
