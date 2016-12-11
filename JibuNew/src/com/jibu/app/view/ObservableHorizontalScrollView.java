package com.jibu.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
/**
 * 水平刻度尺子
 * created on 2015/8/25 by zhangwm
 * */
public class ObservableHorizontalScrollView extends HorizontalScrollView {
	private Runnable scrollerTask;
	private int intitPosition;
	private int newCheck = 100;
	private OnScrollStopListner onScrollstopListner;

	private int startValue = -1;
	ObservableHorizontalScrollView view;
	public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = this;
		scrollerTask = new Runnable() {
			@Override
			public void run() {
				
				if(startValue==-1){
				int newPosition = getScrollX();
				if (intitPosition - newPosition == 0) {
					if (onScrollstopListner == null) {
						return;
					}
					onScrollstopListner.onScrollChange(getScrollX());
				} else {
					intitPosition = getScrollX();
					postDelayed(scrollerTask, newCheck);
				}
				
				}else{
					view.scrollTo(startValue, 0);
					onScrollstopListner.onScrollChange(startValue);
					startValue = -1;
				}
			}
		};
	}

	public ObservableHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public ObservableHorizontalScrollView(Context context) {
		super(context);
		
	}

	public interface OnScrollStopListner {
		void onScrollChange(int index);
	}

	public void setOnScrollStopListner(OnScrollStopListner listner) {
		onScrollstopListner = listner;
	}

	public void startScrollerTask() {
		intitPosition = getScrollX();
		postDelayed(scrollerTask, newCheck);
	}
	
	private int dp2px(int value) {
		float v = getContext().getResources().getDisplayMetrics().density;
		return (int) (v * value + 0.5f);
	}
	
	public void setStarScrallValue(int value){
		
		startValue = dp2px(value);
		
		postDelayed(scrollerTask, newCheck);
	}
}
