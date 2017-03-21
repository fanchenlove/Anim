package com.fanchen.anim.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanchen.anim.R;
import com.fanchen.anim.base.BaseRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by fanchen on 2017/3/20.
 */
public class SearchHistoryAdapter extends BaseRecyclerAdapter<SearchHistoryAdapter.MyViewHolder, String> {

    private OnItemDeleteListener onItemDeleteListener;

    public SearchHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBind(MyViewHolder holder,final ArrayList<String> datas,final int position) {
        if (datas == null || datas.size() <= position) return;
        holder.historyInfo.setText(datas.get(position));
        if(onItemDeleteListener == null)return;
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemDeleteListener.OnItemDelete(datas,position);
            }
        });
    }

    @Override
    public MyViewHolder onCreateHolder(View inflate, int type) {
        return new MyViewHolder(inflate);
    }

    @Override
    public int getLayout() {
        return R.layout.item_search_history;
    }

    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView historyInfo;
        ImageView delete;

        public MyViewHolder(View view) {
            super(view);
            historyInfo = (TextView) view.findViewById(R.id.tv_item_search_history);
            delete = (ImageView) view.findViewById(R.id.iv_item_search_delete);
        }
    }

    public interface OnItemDeleteListener{
        void OnItemDelete(ArrayList<String> datas,int position);
    }
}
