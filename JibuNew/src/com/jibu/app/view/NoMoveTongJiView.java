package com.jibu.app.view;


import java.util.Vector;

import com.jibu.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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

public class NoMoveTongJiView extends View {

	
	private Paint hLinePaint;// 坐标轴水平内部 虚线画笔
	private Paint bgPaint;// 绘制文本的画笔
	private Paint rectpaint;// 矩形画笔 柱状图的样式信息
	private Paint textpaint;
	private Paint iconpaint;
	//private int[] progress = { 100, 200, 200, 400, 2000, 0, 10000 };// 7
	private Vector<Float> values = new Vector<Float>();		
	private Vector<String> times = new Vector<String>();	// 条，显示各个柱状的数据
	
	private boolean needScroll = false;// 在柱状图上显示数字
	
	int width,height;
	int icon_x,icon_y,icon_w,icon_h;
	int gap_width;
	int content_x,content_w,content_h,content_y;
	int hLine_x,hLine_y,hLine_w,hLine_h;
	int text_x,text_y,text_w,text_h;
	int min_rect_width;
	
	int total_width;
	
	public NoMoveTongJiView(Context context) {
		super(context);
		init();
	}

	public NoMoveTongJiView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {

		
		
		
		needScroll = false;
		
		hLinePaint = new Paint();
		bgPaint = new Paint();
		rectpaint = new Paint();
		textpaint = new Paint();
		iconpaint = new Paint();
		// 给画笔设置颜色
		hLinePaint.setColor(Color.rgb(200, 200, 200));
		bgPaint.setColor(Color.rgb(0xe3, 0xee, 0xf9));
		rectpaint.setColor(Color.rgb(65, 177, 249));
		textpaint.setColor(Color.rgb(0x0d, 0x0b, 0x19));
		textpaint.setTextSize(dp2px(10));
		
		// 加载画图
	//	bitmap = BitmapFactory
	//			.decodeResource(getResources(), R.drawable.column);
	}

	public void setValue(Vector<Float> values,Vector<String> times) {
		this.values = values;
		this.times = times;
		postInvalidate(); 
		//this.startAnimation(ani);
	}


	   private void drawIcon(Canvas canvas) throws Exception{
	    	
	    	Bitmap rawBitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.tongji2_no_move));
	        int image_w = rawBitmap.getWidth();
	        int image_h = rawBitmap.getHeight();
	        
	        int newWidth = icon_w;
	        int newHeight = icon_h;
	        
	        float scaleWidth = ((float) newWidth) / image_w;

	        float scaleHeight = ((float) newHeight) / image_h;
	        
	        Matrix matrix = new Matrix();

	        matrix.postScale(scaleWidth, scaleHeight);
	        Bitmap newbm = Bitmap.createBitmap(rawBitmap, 0, 0, image_w, image_h, matrix,

	        		true);
	        
	        
	        Rect mSrcRect = new Rect(0, 0, newWidth, newHeight); 
	        
	        int left = icon_x;  
	     
	        int top = icon_y;
	        Rect mDestRect = new Rect(left, top, left + newWidth, top + newHeight);
	        canvas.drawBitmap(newbm, mSrcRect, mDestRect, iconpaint);
	    	
	    }
	
	   private void drawText(Canvas canvas){
		   
		   
		   float textX = text_x;
		   float gap = dp2px(4);
		   
		   Log.e("log","times.size()="+times.size());
		   
		   for(int i=0; i<times.size(); i++){
			   
			   Log.e("log","times.elementAt(i)="+times.elementAt(i)+" textX="+textX);
			   canvas.drawText(times.elementAt(i), textX, text_y, textpaint);
			   float textW = textpaint.measureText(times.elementAt(i));
			   
			   
			   
			   int beishu = (int)(values.elementAt(i)/0.5);
			   float rectWidth = min_rect_width*beishu;
			   
			   if(rectWidth>textW){
				   rectWidth = textW;
			   }
			   
			   float rectStartX = textX+(textW-rectWidth)/2;
			   float rectEndX = rectStartX+rectWidth;
			  
			   canvas.drawRect(rectStartX, content_y, rectEndX, content_y+content_h, rectpaint);
					   
				textX = textX+textW+gap;		   
			   
		   }
		   
	   }
	   
	   private void drawRect(Canvas canvas){
		   
		   
	   }
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		width = getWidth();
		height = getHeight();
		
		gap_width = dp2px(20);
		
		text_h = dp2px(20);
		
		icon_x = dp2px(10);
		icon_y = 0;
		icon_w = dp2px(6);
		icon_h = dp2px(12);
		
		hLine_x = dp2px(20);
		hLine_y = dp2px(6);
		hLine_w = dp2px(1);
		hLine_h = height-hLine_y-text_h;
		
		
		
		min_rect_width = dp2px(5);
		
		
		
		
		
		content_x = hLine_x+hLine_w;
		content_w = width-gap_width*2-hLine_w;
		content_y = hLine_y;
		content_h = hLine_h;
		
		text_x = content_x;
		text_y = content_y+content_h+dp2px(12);
		
		canvas.drawRect(hLine_x,hLine_y, hLine_x + hLine_w, hLine_y+hLine_h, hLinePaint);

		Rect rect = new Rect(content_x,content_y+content_h/2,content_x+content_w,hLine_y+hLine_h);
		canvas.drawRect(rect, bgPaint);
		try {
		drawIcon(canvas);
		} catch (Exception e) {
			
		}
		drawText(canvas);
		drawRect(canvas);
		

	}

	private int dp2px(int value) {
		float v = getContext().getResources().getDisplayMetrics().density;
		return (int) (v * value + 0.5f);
	}

	private int sp2px(int value) {
		float v = getContext().getResources().getDisplayMetrics().scaledDensity;
		return (int) (v * value + 0.5f);
	}

	/**
	 * 设置点击事件，是否显示数字
	 */
	
	/*
	public boolean onTouchEvent(MotionEvent event) {
		int step = (getWidth() - dp2px(30)) / 8;
		int x = (int) event.getX();
		for (int i = 0; i < 7; i++) {
			if (x > (dp2px(15) + step * (i + 1) - dp2px(15))
					&& x < (dp2px(15) + step * (i + 1) + dp2px(15))) {
				text[i] = 1;
				for (int j = 0; j < 7; j++) {
					if (i != j) {
						text[j] = 0;
					}
				}
				if (Looper.getMainLooper() == Looper.myLooper()) {
					invalidate();
				} else {
					postInvalidate();
				}
			}
		}
		return super.onTouchEvent(event);
	}
*/
	/**
	 * 集成animation的一个动画类
	 * 
	 * @author 李垭超
	 */
	
}