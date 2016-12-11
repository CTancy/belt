package com.jibu.app.view;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

public class ObservableVerScrollView extends ScrollView{
	private Runnable scrollerTask;
	private int intitPosition;
	private int newCheck = 100;
	private OnScrollStopListner_ver onScrollstopListner;
	
	private int startValue = -1;
	
	ObservableVerScrollView view;

	private int dp2px(int value) {
		float v = getContext().getResources().getDisplayMetrics().density;
		return (int) (v * value + 0.5f);
	}
	
	public ObservableVerScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = this;
		
		scrollerTask = new Runnable() {
			@Override
			public void run() {
				
				if(startValue==-1){
					int newPosition = getScrollY();
					Log.i("log","getScrollY()="+getScrollY()+" intitPosition="+intitPosition);
					if (intitPosition - newPosition == 0) {
						if (onScrollstopListner == null) {
							return;
						}
						onScrollstopListner.onScrollChange(getScrollY());
					} else {
						intitPosition = getScrollY();
						postDelayed(scrollerTask, newCheck);
					}
				}else{
					view.scrollTo(0, startValue);
					onScrollstopListner.onScrollChange(startValue);
					startValue = -1;
					
				}
			}
		};
	}

	public ObservableVerScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public ObservableVerScrollView(Context context) {
		super(context);
		
	}

	public interface OnScrollStopListner_ver {
		void onScrollChange(int index);
	}

	public void setOnScrollStopListner_Ver(OnScrollStopListner_ver listner) {
		onScrollstopListner = listner;
	}

	
	public void setStarScrallValue(int value){
		
		startValue = dp2px(value);
		
		postDelayed(scrollerTask, newCheck);
	}
	
	public void startScrollerTask() {
		intitPosition = getScrollY();
		postDelayed(scrollerTask, newCheck);
	}
}
