package com.fanchen.anim.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * 
 * @author fanchen
 *
 * @param <T>
 * @param <V>
 */
public abstract class BaseRecyclerAdapter<T extends ViewHolder, V> extends
		Adapter<T> {

	protected ArrayList<V> mList = new ArrayList<>();
	protected LayoutInflater mLayoutInflater;
	protected Context context;

	private OnItemClickListener mItemClickListener;
	private OnItemLongClickListener mItemLongClickListener;

	public BaseRecyclerAdapter(Context context) {
		this(context,null);
		this.context = context;
	}

	public BaseRecyclerAdapter(Context context,List<V> list) {
		mLayoutInflater = LayoutInflater.from(context);
		if(list != null)mList.addAll(list);
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder arg0, int arg1) {
		if(mItemLongClickListener != null){
			arg0.itemView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				@SuppressWarnings("deprecation")
				public boolean onLongClick(View v) {
					return mItemLongClickListener.onItemLongClick(mList, v, arg0.getPosition());
				}
			});
		}
		if(mItemClickListener != null){
			arg0.itemView.setOnClickListener(new OnClickListener() {

				@Override
				@SuppressWarnings("deprecation")
				public void onClick(View v) {
					mItemClickListener.onItemClick(mList,v, arg0.getPosition());
				}
			});
		}
		onBind((T)arg0, mList, arg1);
	}

	@Override
	public T onCreateViewHolder(ViewGroup arg0, int arg1) {
		View inflate = mLayoutInflater.inflate(getLayout(), arg0, false);
		return onCreateHolder(inflate, arg1);
	}

	/**
	 * 
	 * @param holder
	 * @param datas
	 * @param position
	 */
	public abstract void onBind(T holder, ArrayList<V> datas,int position);

	/**
	 * 
	 * @param inflate
	 * @param type
	 * @return
	 */
	public abstract T onCreateHolder(View inflate, int type);

	/**
	 * 
	 * @return
	 */
	public abstract int getLayout();

	public void setOnItemClickListener(OnItemClickListener l) {
		this.mItemClickListener = l;
	}

	public void setOnItemLongClickListener(OnItemLongClickListener l) {
		this.mItemLongClickListener = l;
	}

	public OnItemClickListener getItemClickListener() {
		return mItemClickListener;
	}

	public OnItemLongClickListener getItemLongClickListener() {
		return mItemLongClickListener;
	}

	/**
	 * 
	 * @param mbooru
	 */
	public <W extends V> void addAll(ArrayList<W> mbooru) {
		if (mbooru == null)
			return;
		int size = mList.size();
		mList.addAll(mbooru);
		notifyItemRangeInserted(size, mbooru.size());
	}

	/**
	 * 
	 * @param mbooru
	 */
	public void add(V mbooru) {
		if (mbooru == null)
			return;
		int size = mList.size();
		mList.add(mbooru);
		notifyItemRangeInserted(size, 1);
	}

	/**
	 * 
	 * @param mbooru
	 */
	public void remove(V mbooru) {
		if (mbooru == null)
			return;
		mList.remove(mbooru);
		notifyDataSetChanged();
	}

	/**
	 * 
	 */
	public void clear() {
		mList.clear();
		notifyDataSetChanged();
	}

	/**
	 * 
	 * @param pos
	 */
	public void remove(int pos) {
		if (pos < 0 || pos >= mList.size())
			return;
		mList.remove(pos);
		notifyItemRemoved(pos);
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <W extends V> ArrayList<W> getItemList() {
		return (ArrayList<W>) mList;
	}

	public Context getContext() {
		return context;
	}

	/**
	 * 
	 */
	public interface OnItemClickListener {
		void onItemClick(ArrayList<?> mList, View v, int position);
	}

	/**
	 * 
	 */
	public interface OnItemLongClickListener {
		boolean onItemLongClick(ArrayList<?> list, View v, int position);
	}

	/**
	 * 
	 * @author fanchen
	 * 
	 */
	public static class SimpleItemDecoration extends ItemDecoration {
		private int space;

		public SimpleItemDecoration(Context context) {
			Display defaultDisplay = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			DisplayMetrics outMetrics = new DisplayMetrics();
			defaultDisplay.getMetrics(outMetrics);
			space = 5;
			if (outMetrics.widthPixels <= 720) {
				space = 4;
			} else if (outMetrics.widthPixels <= 1080) {
				space = 6;
			} else if (outMetrics.widthPixels > 1080){
				space = 8;
			}
		}

		@Override
		public void getItemOffsets(Rect outRect, View view,
				RecyclerView parent, RecyclerView.State state) {
			outRect.left = space;
			outRect.right = space;
			outRect.bottom = space;
			if (parent.getChildAdapterPosition(view) == 0) {
				outRect.top = space;
			}else{
				outRect.top = space / 2;
			}
		}
	}
}
