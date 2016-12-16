package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.internal.LoadingLayout;

public class JibuLoadingLayout extends LoadingLayout {
    private AnimationDrawable animationDrawable;  
    
    
	public JibuLoadingLayout(Context context, Mode mode,
			Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);
		// TODO Auto-generated constructor stub
		// ≥ı ºªØ  
        mHeaderImage.setImageResource(R.anim.animation_cezou);  
        animationDrawable = (AnimationDrawable) mHeaderImage.getDrawable();  
	}

	@Override
	protected int getDefaultDrawableResId() {
		// TODO Auto-generated method stub
		return R.drawable.cezou1;
	}

	@Override
	protected void onLoadingDrawableSet(Drawable imageDrawable) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onPullImpl(float scaleOfLayout) {
		// TODO Auto-generated method stub
		animationDrawable.start(); 
	}

	@Override
	protected void pullToRefreshImpl() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshingImpl() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void releaseToRefreshImpl() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void resetImpl() {
		// TODO Auto-generated method stub
        mHeaderImage.setVisibility(View.VISIBLE);  
        mHeaderImage.clearAnimation();  
	}

}
