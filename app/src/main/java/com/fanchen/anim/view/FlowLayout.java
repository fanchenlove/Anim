package com.fanchen.anim.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanchen.anim.R;
import com.fanchen.anim.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by taofangxin on 16/4/16.
 */
public class FlowLayout extends ViewGroup implements View.OnClickListener {

	private Context mContext;
	private int usefulWidth; // the space of a line we can use(line's width
								// minus the sum of left and right padding
	private int lineSpacing = 0; // the spacing between lines in flowlayout
	List<View> childList = new ArrayList<>();
	List<Integer> lineNumList = new ArrayList<>();
	private OnFlowItemClick onFlowItemClick;

	public FlowLayout(Context context) {
		this(context, null);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.FlowLayout);
		lineSpacing = mTypedArray.getDimensionPixelSize(
				R.styleable.FlowLayout_lineSpacing, 0);
		mTypedArray.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mPaddingLeft = getPaddingLeft();
		int mPaddingRight = getPaddingRight();
		int mPaddingTop = getPaddingTop();
		int mPaddingBottom = getPaddingBottom();

		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int lineUsed = mPaddingLeft + mPaddingRight;
		int lineY = mPaddingTop;
		int lineHeight = 0;
		for (int i = 0; i < this.getChildCount(); i++) {
			View child = this.getChildAt(i);
			if (child.getVisibility() == GONE) {
				continue;
			}
			int spaceWidth = 0;
			int spaceHeight = 0;
			LayoutParams childLp = child.getLayoutParams();
			if (childLp instanceof MarginLayoutParams) {
				measureChildWithMargins(child, widthMeasureSpec, 0,
						heightMeasureSpec, lineY);
				MarginLayoutParams mlp = (MarginLayoutParams) childLp;
				spaceWidth = mlp.leftMargin + mlp.rightMargin;
				spaceHeight = mlp.topMargin + mlp.bottomMargin;
			} else {
				measureChild(child, widthMeasureSpec, heightMeasureSpec);
			}

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();
			spaceWidth += childWidth;
			spaceHeight += childHeight;

			if (lineUsed + spaceWidth > widthSize) {
				// approach the limit of width and move to next line
				lineY += lineHeight + lineSpacing;
				lineUsed = mPaddingLeft + mPaddingRight;
				lineHeight = 0;
			}
			if (spaceHeight > lineHeight) {
				lineHeight = spaceHeight;
			}
			lineUsed += spaceWidth;
		}
		setMeasuredDimension(widthSize,
				heightMode == MeasureSpec.EXACTLY ? heightSize : lineY
						+ lineHeight + mPaddingBottom);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int mPaddingLeft = getPaddingLeft();
		int mPaddingRight = getPaddingRight();
		int mPaddingTop = getPaddingTop();

		int lineX = mPaddingLeft;
		int lineY = mPaddingTop;
		int lineWidth = r - l;
		usefulWidth = lineWidth - mPaddingLeft - mPaddingRight;
		int lineUsed = mPaddingLeft + mPaddingRight;
		int lineHeight = 0;
		int lineNum = 0;

		lineNumList.clear();
		for (int i = 0; i < this.getChildCount(); i++) {
			View child = this.getChildAt(i);
			if (child.getVisibility() == GONE) {
				continue;
			}
			int spaceWidth = 0;
			int spaceHeight = 0;
			int left = 0;
			int top = 0;
			int right = 0;
			int bottom = 0;
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			LayoutParams childLp = child.getLayoutParams();
			if (childLp instanceof MarginLayoutParams) {
				MarginLayoutParams mlp = (MarginLayoutParams) childLp;
				spaceWidth = mlp.leftMargin + mlp.rightMargin;
				spaceHeight = mlp.topMargin + mlp.bottomMargin;
				left = lineX + mlp.leftMargin;
				top = lineY + mlp.topMargin;
				right = lineX + mlp.leftMargin + childWidth;
				bottom = lineY + mlp.topMargin + childHeight;
			} else {
				left = lineX;
				top = lineY;
				right = lineX + childWidth;
				bottom = lineY + childHeight;
			}
			spaceWidth += childWidth;
			spaceHeight += childHeight;

			if (lineUsed + spaceWidth > lineWidth) {
				// approach the limit of width and move to next line
				lineNumList.add(lineNum);
				lineY += lineHeight + lineSpacing;
				lineUsed = mPaddingLeft + mPaddingRight;
				lineX = mPaddingLeft;
				lineHeight = 0;
				lineNum = 0;
				if (childLp instanceof MarginLayoutParams) {
					MarginLayoutParams mlp = (MarginLayoutParams) childLp;
					left = lineX + mlp.leftMargin;
					top = lineY + mlp.topMargin;
					right = lineX + mlp.leftMargin + childWidth;
					bottom = lineY + mlp.topMargin + childHeight;
				} else {
					left = lineX;
					top = lineY;
					right = lineX + childWidth;
					bottom = lineY + childHeight;
				}
			}
			child.layout(left, top, right, bottom);
			lineNum++;
			if (spaceHeight > lineHeight) {
				lineHeight = spaceHeight;
			}
			lineUsed += spaceWidth;
			lineX += spaceWidth;
		}
		// add the num of last line
		lineNumList.add(lineNum);
	}

	/**
	 * resort child elements to use lines as few as possible
	 */
	public void relayoutToCompress() {
		int childCount = this.getChildCount();
		if (0 == childCount) {
			// no need to sort if flowlayout has no child view
			return;
		}
		int count = 0;
		for (int i = 0; i < childCount; i++) {
			View v = getChildAt(i);
			if (v instanceof BlankView) {
				// BlankView is just to make childs look in alignment, we should
				// ignore them when we relayout
				continue;
			}
			count++;
		}
		View[] childs = new View[count];
		int[] spaces = new int[count];
		int n = 0;
		for (int i = 0; i < childCount; i++) {
			View v = getChildAt(i);
			if (v instanceof BlankView) {
				// BlankView is just to make childs look in alignment, we should
				// ignore them when we relayout
				continue;
			}
			childs[n] = v;
			LayoutParams childLp = v.getLayoutParams();
			int childWidth = v.getMeasuredWidth();
			if (childLp instanceof MarginLayoutParams) {
				MarginLayoutParams mlp = (MarginLayoutParams) childLp;
				spaces[n] = mlp.leftMargin + childWidth + mlp.rightMargin;
			} else {
				spaces[n] = childWidth;
			}
			n++;
		}
		int[] compressSpaces = new int[count];
		for (int i = 0; i < count; i++) {
			compressSpaces[i] = spaces[i] > usefulWidth ? usefulWidth
					: spaces[i];
		}
		sortToCompress(childs, compressSpaces);
		this.removeAllViews();
		for (View v : childList) {
			this.addView(v);
		}
		childList.clear();
	}

	private void sortToCompress(View[] childs, int[] spaces) {
		int childCount = childs.length;
		int[][] table = new int[childCount + 1][usefulWidth + 1];
		for (int i = 0; i < childCount + 1; i++) {
			for (int j = 0; j < usefulWidth; j++) {
				table[i][j] = 0;
			}
		}
		boolean[] flag = new boolean[childCount];
		for (int i = 0; i < childCount; i++) {
			flag[i] = false;
		}
		for (int i = 1; i <= childCount; i++) {
			for (int j = spaces[i - 1]; j <= usefulWidth; j++) {
				table[i][j] = (table[i - 1][j] > table[i - 1][j - spaces[i - 1]]
						+ spaces[i - 1]) ? table[i - 1][j] : table[i - 1][j
						- spaces[i - 1]]
						+ spaces[i - 1];
			}
		}
		int v = usefulWidth;
		for (int i = childCount; i > 0 && v >= spaces[i - 1]; i--) {
			if (table[i][v] == table[i - 1][v - spaces[i - 1]] + spaces[i - 1]) {
				flag[i - 1] = true;
				v = v - spaces[i - 1];
			}
		}
		int rest = childCount;
		View[] restArray;
		int[] restSpaces;
		for (int i = 0; i < flag.length; i++) {
			if (flag[i] == true) {
				childList.add(childs[i]);
				rest--;
			}
		}

		if (0 == rest) {
			return;
		}
		restArray = new View[rest];
		restSpaces = new int[rest];
		int index = 0;
		for (int i = 0; i < flag.length; i++) {
			if (flag[i] == false) {
				restArray[index] = childs[i];
				restSpaces[index] = spaces[i];
				index++;
			}
		}
		table = null;
		childs = null;
		flag = null;
		sortToCompress(restArray, restSpaces);
	}

	/**
	 * add some blank view to make child elements look in alignment
	 */
	public void relayoutToAlign() {
		int childCount = this.getChildCount();
		if (0 == childCount) {
			// no need to sort if flowlayout has no child view
			return;
		}
		int count = 0;
		for (int i = 0; i < childCount; i++) {
			View v = getChildAt(i);
			if (v instanceof BlankView) {
				// BlankView is just to make childs look in alignment, we should
				// ignore them when we relayout
				continue;
			}
			count++;
		}
		View[] childs = new View[count];
		int[] spaces = new int[count];
		int n = 0;
		for (int i = 0; i < childCount; i++) {
			View v = getChildAt(i);
			if (v instanceof BlankView) {
				// BlankView is just to make childs look in alignment, we should
				// ignore them when we relayout
				continue;
			}
			childs[n] = v;
			LayoutParams childLp = v.getLayoutParams();
			int childWidth = v.getMeasuredWidth();
			if (childLp instanceof MarginLayoutParams) {
				MarginLayoutParams mlp = (MarginLayoutParams) childLp;
				spaces[n] = mlp.leftMargin + childWidth + mlp.rightMargin;
			} else {
				spaces[n] = childWidth;
			}
			n++;
		}
		int lineTotal = 0;
		int start = 0;
		this.removeAllViews();
		for (int i = 0; i < count; i++) {
			if (lineTotal + spaces[i] > usefulWidth) {
				int blankWidth = usefulWidth - lineTotal;
				int end = i - 1;
				int blankCount = end - start;
				if (blankCount >= 0) {
					if (blankCount > 0) {
						int eachBlankWidth = blankWidth / blankCount;
						MarginLayoutParams lp = new MarginLayoutParams(
								eachBlankWidth, 0);
						for (int j = start; j < end; j++) {
							this.addView(childs[j]);
							BlankView blank = new BlankView(mContext);
							this.addView(blank, lp);
						}
					}
					this.addView(childs[end]);
					start = i;
					i--;
					lineTotal = 0;
				} else {
					this.addView(childs[i]);
					start = i + 1;
					lineTotal = 0;
				}
			} else {
				lineTotal += spaces[i];
			}
		}
		for (int i = start; i < count; i++) {
			this.addView(childs[i]);
		}
	}

	/**
	 * use both of relayout methods together
	 */
	public void relayoutToCompressAndAlign() {
		this.relayoutToCompress();
		this.relayoutToAlign();
	}

	/**
	 * cut the flowlayout to the specified num of lines
	 * 
	 * @param line_num
	 */
	public void specifyLines(int line_num) {
		int childNum = 0;
		if (line_num > lineNumList.size()) {
			line_num = lineNumList.size();
		}
		for (int i = 0; i < line_num; i++) {
			childNum += lineNumList.get(i);
		}
		List<View> viewList = new ArrayList<>();
		for (int i = 0; i < childNum; i++) {
			viewList.add(getChildAt(i));
		}
		removeAllViews();
		for (View v : viewList) {
			addView(v);
		}
	}

	@Override
	protected LayoutParams generateLayoutParams(LayoutParams p) {
		return new MarginLayoutParams(p);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new MarginLayoutParams(super.generateDefaultLayoutParams());
	}

	class BlankView extends View {

		public BlankView(Context context) {
			super(context);
		}
	}

	public void addData2TextView(Object data) {
		if (data == null) {
			return;
		}
		int ranHeight = DisplayUtil.dip2px(getContext(), 24);
		MarginLayoutParams lp = new MarginLayoutParams(
				LayoutParams.WRAP_CONTENT, ranHeight);
		lp.setMargins(DisplayUtil.dip2px(getContext(), 8), 0, 0, 0);
		TextView tv = new TextView(getContext());
		tv.setPadding(DisplayUtil.dip2px(getContext(), 10), 0,
				DisplayUtil.dip2px(getContext(), 10), 0);
		TypedValue typedValue = new TypedValue();
		getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
		tv.setTextColor(typedValue.data);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setText(data.toString());
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setLines(1);
		tv.setTag(data);
		tv.setBackgroundResource(R.drawable.bg_tag);
		tv.setOnClickListener(this);
		addView(tv, lp);
	}

	public <T> void addDataList2TextView(List<T> data) {
		if (data == null) {
			return;
		}
		for (int i = 0; i < data.size(); i++) {
			addData2TextView(data.get(i));
		}
	}

	public static interface OnFlowItemClick {
		<T> void OnItemClick(View v, T data, int position);
	}

	@Override
	public void onClick(View v) {
		Object tag = v.getTag();
		int childCount = getChildCount();
		int k = 0;
		for (int i = 0; i < childCount; i++) {
			if (v == getChildAt(i)) {
				k = i;
				break;
			}
		}
		if (onFlowItemClick != null) {
			onFlowItemClick.OnItemClick(v, tag, k);
		}
	}

	public void setOnFlowItemClick(OnFlowItemClick onFlowItemClick) {
		this.onFlowItemClick = onFlowItemClick;
	}
	
	
}
