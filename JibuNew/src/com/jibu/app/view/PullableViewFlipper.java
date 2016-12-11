package com.jibu.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class PullableViewFlipper extends ViewFlipper implements Pullable{

	public PullableViewFlipper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public PullableViewFlipper(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public boolean canPullDown() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canPullUp() {
		// TODO Auto-generated method stub
		return false;
	}

}
