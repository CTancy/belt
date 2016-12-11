package com.jibu.app.view;

import com.jibu.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ProgressViewDistanceSetup extends View {
private Paint paint;
	
	private int max;
	
	private int progress;
	
	int width;
	
	int height;

	public ProgressViewDistanceSetup(Context context) {
		super(context);
		init();
	}

	public ProgressViewDistanceSetup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {

		max = 300;
		progress = 150;

		paint = new Paint();
		
	}

	public void setProgress(int progress) {
		if(progress>max){
			this.progress = max;
		}
		
		this.progress = progress;
		
		postInvalidate(); 
		
	}

	
	
	
	
	private void drawBmp(Canvas canvas,int id,int x,int y,int w,int h){
	
	Bitmap rawBitmap = BitmapFactory.decodeStream(getResources().openRawResource(id));
    int image_w = rawBitmap.getWidth();
    int image_h = rawBitmap.getHeight();
 
    
    float scaleWidth = ((float) w) / image_w;

    float scaleHeight = ((float) h) / image_h;
    
    Matrix matrix = new Matrix();

    matrix.postScale(scaleWidth, scaleHeight);
    Bitmap newbm = Bitmap.createBitmap(rawBitmap, 0, 0, image_w, image_h, matrix,

    		true);
    
    
    Rect mSrcRect = new Rect(0, 0, w, h); 
    
    Rect mDestRect = new Rect(x, y, x + w, y + h);
    canvas.drawBitmap(newbm, mSrcRect, mDestRect, paint);
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		width = getWidth();
		height = getHeight();
		int rectHeight = height/5;
		
		int rectX = height/2;
		
		int rectY = height*2/5;
		
		int rectWidth = width - height;
		
		
		drawBmp(canvas,R.drawable.progress_bg,rectX,rectY,rectWidth,rectHeight);
		
		
		int icon_x = progress*rectWidth/max;
		int icon_y = 0;
		int iconWidth = height;
		int iconHeight = height;
		
		
		if(progress<100){
			drawBmp(canvas,R.drawable.progress_head_1,icon_x,icon_y,iconWidth,iconHeight);
		}else if(progress>=100&&progress<200){
			drawBmp(canvas,R.drawable.progress_head_2,icon_x,icon_y,iconWidth,iconHeight);
		}else{
			drawBmp(canvas,R.drawable.progress_head_3,icon_x,icon_y,iconWidth,iconHeight);
		}
		
		
	}
}
