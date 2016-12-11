package com.jibu.app.view;


import java.util.Vector;

import com.jibu.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class BFBView extends View {

	private Paint movePaint;// 坐标轴 轴线 画笔：
	private Paint noMovePaint;// 坐标轴水平内部 虚线画笔
	
	float moveTime;
	float noMoveTime;
	
	int viewHeight; 
	

	public BFBView(Context context) {
		super(context);
		init();
	}

	public BFBView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {

		

		movePaint = new Paint();
		noMovePaint = new Paint();
		
		movePaint.setColor(Color.rgb(252, 176, 60));
		movePaint.setTextSize(dp2px(14));
		noMovePaint.setColor(Color.rgb(65, 177, 249));
		noMovePaint.setTextSize(dp2px(14));
		
		moveTime = 1;
		noMoveTime = 1;
		
		viewHeight = dp2px(20);
		// 加载画图
	//	bitmap = BitmapFactory
	//			.decodeResource(getResources(), R.drawable.column);
	}

	public void setTime(float moveTime,float noMoveTime) {
		this.moveTime = moveTime;
		this.noMoveTime = noMoveTime;
		
		postInvalidate(); 
		//this.startAnimation(ani);
	}

	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int width = getWidth();
		int height = getHeight();
		int moveValue,noMoveValue;
		if(moveTime<0.1&&noMoveTime<0.1){
			moveValue = 50;
			noMoveValue = 50;
		}else if(moveTime<0.1&&noMoveTime>0.1){
			moveValue = 0;
			noMoveValue = 100;
		}else if(moveTime>0.1&&noMoveTime<0.1){
			moveValue = 100;
			noMoveValue = 0;
		}else{
			moveValue = (int)((int)(moveTime*60*100))/((int)((moveTime+noMoveTime)*60));
			noMoveValue = 100-moveValue;
		}
		
		
		Rect rect = new Rect(0,0,width,viewHeight);
		canvas.drawRect(rect, noMovePaint);
		
		Rect rect2 = new Rect(0,0,width*moveValue/100,viewHeight);
		canvas.drawRect(rect2, movePaint);

		float textY = viewHeight+dp2px(26);
		
		float textWidth1 = movePaint.measureText(String.valueOf(moveTime) + "h");
		float textWidth2 = noMovePaint.measureText(String.valueOf(noMoveTime) + "h");
		
		float textX1 = (width*moveValue/200)-(textWidth1/2);
		float textX2 = (width*moveValue/100)+(width*noMoveValue/200)-(textWidth2/2);
		
		if(moveValue>0){
			canvas.drawText(String.valueOf(moveTime) + "h", textX1, textY, movePaint);
		}
		if(noMoveValue>0){
			canvas.drawText(String.valueOf(noMoveTime) + "h", textX2, textY, noMovePaint);
		}
		
		
		
		

	}

	private int dp2px(int value) {
		float v = getContext().getResources().getDisplayMetrics().density;
		return (int) (v * value + 0.5f);
	}

	private int sp2px(int value) {
		float v = getContext().getResources().getDisplayMetrics().scaledDensity;
		return (int) (v * value + 0.5f);
	}



}