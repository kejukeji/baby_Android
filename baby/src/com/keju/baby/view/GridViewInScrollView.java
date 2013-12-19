package com.keju.baby.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 重写GridView，实现ScrollView嵌套GridView可用
 * 
 * @author Zhoujun
 * 
 */
public class GridViewInScrollView extends GridView {
	public GridViewInScrollView(Context context) {
		super(context);
	}

	public GridViewInScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GridViewInScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		return false;
//	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int expandSpec = MeasureSpec.makeMeasureSpec(
					Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);		
	}
}
