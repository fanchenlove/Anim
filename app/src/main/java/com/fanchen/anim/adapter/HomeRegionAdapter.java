package com.fanchen.anim.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanchen.anim.R;
import com.fanchen.anim.base.BaseRecyclerAdapter;
import com.fanchen.anim.entity.AnimClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by fanchen on 2017/3/15.
 */
public class HomeRegionAdapter extends BaseRecyclerAdapter<HomeRegionAdapter.RegionViewHolder, AnimClass> {

    public HomeRegionAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBind(RegionViewHolder holder, ArrayList<AnimClass> datas, int position) {
        if(datas == null || holder == null ||  datas.size() <= position )return;
        AnimClass animClass = datas.get(position);
        holder.textView.setText(animClass.getTitle());
        if(animClass.getDrawable() == -1){
            Picasso.with(holder.imageView.getContext()).load(animClass.getDrawableUrl()).into(holder.imageView);
        }else{
            Picasso.with(holder.imageView.getContext()).load(animClass.getDrawable()).into(holder.imageView);
        }
    }

    @Override
    public RegionViewHolder onCreateHolder(View inflate, int type) {
        return new RegionViewHolder(inflate);
    }

    @Override
    public int getLayout() {
        return R.layout.item_home_region;
    }

    public static class RegionViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;

        public RegionViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_img_region);
            textView = (TextView) itemView.findViewById(R.id.item_title_region);
        }
    }
}
