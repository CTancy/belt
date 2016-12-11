package com.jibu.app.view;

import u.aly.dp;

import com.jibu.app.R;

import android.animation.ValueAnimator;
import android.content.Context;  
import android.content.res.TypedArray;  
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;  
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;  
import android.graphics.Typeface;  
import android.util.AttributeSet;  
import android.util.Log;  
import android.view.View;  
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
  
/** 
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度 
 * @author xiaanming 
 * 
 */  
public class RoundProgressBar extends View {  
    /** 
     * 画笔对象的引用 
     */  
    private Paint paint;  
      
    /** 
     * 圆环的颜色 
     */  
    private int roundColor;  
      
    /** 
     * 圆环进度的颜色 
     */  
    private int roundProgressColor;  
      
    /** 
     * 中间进度百分比的字符串的颜色 
     */  
    private int textColor;  
      
    /** 
     * 中间进度百分比的字符串的字体 
     */  
    private float textSize;  
    
    
    private String textTop;
    private String textBottom;
    
    private float paddingTop;
    private float paddingBottom;
      
    /** 
     * 圆环的宽度 
     */  
    private float roundWidth;  
      
    /** 
     * 最大进度 
     */  
    private int max;  
      
    /** 
     * 当前进度 
     */  
    private int progress;  
    /** 
     * 是否显示中间的进度 
     */  
    private boolean textIsDisplayable;  
      
    /** 
     * 进度的风格，实心或者空心 
     */  
    private int style;  
      
    public static final int STROKE = 0;  
    public static final int FILL = 1;  
      
    
    private boolean isBFB;
    
    private boolean hasAnimation;
    
    private ValueAnimator animator = null;
    
    private int beginAngle = 0;
    
    public RoundProgressBar(Context context) {  
        this(context, null);  
    }  
  
    public RoundProgressBar(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
    }  
      
    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
          
        paint = new Paint();  
  
          
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,  
                R.styleable.RoundProgressBar);  
        isBFB = false;  
        hasAnimation = false;
        //获取自定义属性和默认值  
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);  
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);  
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GREEN);  
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);  
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);  
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);  
        progress = mTypedArray.getInteger(R.styleable.RoundProgressBar_progress, 0); 
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);  
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);  
          
        textTop = mTypedArray.getString(R.styleable.RoundProgressBar_textTop);
        textBottom = mTypedArray.getString(R.styleable.RoundProgressBar_textBottom);
        paddingTop = mTypedArray.getDimension(R.styleable.RoundProgressBar_paddingTop, 15);  
        paddingBottom = mTypedArray.getDimension(R.styleable.RoundProgressBar_paddingBottom, 25);  
        
        mTypedArray.recycle();  
    }  
      
    
 
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        try { 
        /** 
         * 画最外层的大圆环 
         */  
        int centre = getWidth()/2; //获取圆心的x坐标  
        
        int radius = (int) (centre - roundWidth/2); //圆环的半径  
        PathEffect effects1 = new DashPathEffect(new float[] { 0,0}, 1);  
        paint.setPathEffect(effects1);
        paint.setColor(roundColor); //设置圆环的颜色  
        paint.setStyle(Paint.Style.STROKE); //设置空心  
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度  
        paint.setAntiAlias(true);  //消除锯齿   
        
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环  
//        Log.e("log", centre + "  "+centre );  
          
        /** 
         * 画进度百分比 
         */  
        {
	        paint.setStrokeWidth(0);   
	        paint.setColor(textColor);  
	        paint.setTextSize(textSize);  
	      //  paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体  
	        int percent = (int)(((float)progress / (float)max) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0  
	        float textWidth;
	        if(isBFB){
	        	textWidth = paint.measureText(String.valueOf(percent)+"%");
	        }else{
	        	textWidth = paint.measureText(String.valueOf(progress));   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间  
		        
	        }
	        if(textIsDisplayable  && style == STROKE){
	        	if(isBFB){
	        		canvas.drawText(String.valueOf(percent)+"%", centre - textWidth / 2, centre + textSize/2, paint); //画出进度百分比  
	        	}else{
	        		canvas.drawText(String.valueOf(progress), centre - textWidth / 2, centre + textSize/2, paint);  
	        	}
	        }  
        } 
        
        {

	      //  paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体  
	       
	        paint.setStrokeWidth(0);   
	        paint.setColor(textColor);  
	        paint.setTextSize(textSize/3);  
	        
	        float textWidth = paint.measureText(textTop);   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间  
	          
	        float x =  centre - textWidth / 2;
	        float y = centre + (textSize/2) - paddingTop - dp2px(20);
	        
//	        //绘制今日步数的阴影
//	        RectF rectF = new RectF();
//	        
//	        rectF.set(centre - textWidth/2 - dp2px(10), y - textSize/3, 
//	        		centre + textWidth/2 + dp2px(10), y + textSize/8);
	        
//	        canvas.drawRoundRect(rectF, textSize/2, textSize/2, paint);
	        
	        
	        
//	        paint.setStrokeWidth(0);   
//	        paint.setColor(textColor);  
//	        paint.setTextSize(textSize/3);  
	        canvas.drawText(textTop, x, y, paint); //画出进度百分比  
	        

        } 
          
        
        {
	        paint.setStrokeWidth(0);   
	        paint.setColor(textColor);  
	        paint.setTextSize(textSize/3);  
	      //  paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体  
	       
	        float textWidth = paint.measureText(textBottom);   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间  
	          
	        float x =  centre - textWidth / 2;
	        float y = centre + (textSize/2) + paddingBottom;
	        
	        canvas.drawText(textBottom, x, y, paint); //画出进度百分比  
	         
        } 
        /** 
         * 画圆弧 ，画圆环的进度 
         */  
          
        //设置进度是实心还是空心  
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度  
        paint.setColor(roundProgressColor);  //设置进度的颜色  
        RectF oval = new RectF(centre - radius, centre - radius, centre  
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限  
          
        switch (style) {  
        case STROKE:{  
            paint.setStyle(Paint.Style.STROKE);  
            PathEffect effects = new DashPathEffect(new float[] { 15,1}, 1);  
            paint.setPathEffect(effects);  
            
            if (hasAnimation) {
            	canvas.drawArc(oval, beginAngle, 90, false, paint);
            } else {
            	canvas.drawArc(oval, -90, 360 * progress / max, false, paint);  //根据进度画圆弧  
            }
            break;  
        }  
        case FILL:{  
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            if(progress !=0)   
            	canvas.drawArc(oval, 90, 360 * progress / max, true, paint);  //根据进度画圆弧  
            break;  
        }  
        }  
        } catch (Exception e) {
        	
        }
    }  
      
      
    public synchronized int getMax() {  
        return max;  
    }  
  
    /** 
     * 设置进度的最大值 
     * @param max 
     */  
    public synchronized void setMax(int max) {  
        if(max < 0){  
            throw new IllegalArgumentException("max not less than 0");  
        }  
        this.max = max;  
    }  
    
    public synchronized void setTextTop(String textTop) {  
         
        this.textTop = textTop;  
    }  
    
    public synchronized void setTextBottom(String textBottom) {  
        
        this.textBottom = textBottom;  
    }  
  
    public synchronized void setIsBFB(boolean isBFB) {  
        
        this.isBFB = isBFB;  
    } 
    
    /** 
     * 获取进度.需要同步 
     * @return 
     */  
    public synchronized int getProgress() {  
        return progress;  
    }  
  
    /** 
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 
     * 刷新界面调用postInvalidate()能在非UI线程刷新 
     * @param progress 
     */  
    public synchronized void setProgress(int progress) {  
        if(progress < 0){  
            throw new IllegalArgumentException("progress not less than 0");  
        }  
       
         this.progress = progress;  
           
          
        postInvalidate();     
    }  
    
    public synchronized void setBeginAngle(int value) {
    	this.beginAngle = value;
    	postInvalidate();   
    }
    public int getCricleColor() {  
        return roundColor;  
    }  
  
    public void setCricleColor(int cricleColor) {  
        this.roundColor = cricleColor;  
    }  
  
    public int getCricleProgressColor() {  
        return roundProgressColor;  
    }  
  
    public void setCricleProgressColor(int cricleProgressColor) {  
        this.roundProgressColor = cricleProgressColor;  
    }  
  
    public int getTextColor() {  
        return textColor;  
    }  
  
    public void setTextColor(int textColor) {  
        this.textColor = textColor;  
    }  
  
    public float getTextSize() {  
        return textSize;  
    }  
  
    public void setTextSize(float textSize) {  
        this.textSize = textSize;  
    }  
  
    public float getRoundWidth() {  
        return roundWidth;  
    }  
  
    public void setRoundWidth(float roundWidth) {  
        this.roundWidth = roundWidth;  
    }  
  
    //加载同步动画
    public void beginAnimation() {
    	hasAnimation = true;
    	animator = ValueAnimator.ofInt(-90, 269);
    	animator.setDuration(1000);
    	animator.setRepeatCount(Animation.INFINITE);
    	animator.setRepeatMode(Animation.RESTART);
    	animator.setInterpolator(new LinearInterpolator());
    	animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				int value = (Integer) animation.getAnimatedValue();
				setBeginAngle(value); 
				
			}
		});
    	animator.start();
    }
    
    public void endAnmation() {
    	hasAnimation = false;
    	animator.cancel();
    }
    
    //结束动画
    public void beginAnimation(int stepBegin, int stepEnd) {
//    	hasAnimation = true;
    	animator = ValueAnimator.ofInt(stepBegin, stepEnd);
    	animator.setDuration(2000);
    	animator.setInterpolator(new LinearInterpolator());
    	animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				int value = (Integer) animation.getAnimatedValue();
				setProgress(value);
				
			}
		});
    	animator.start();
    }
    
	private int dp2px(int value) {
		float v = getContext().getResources().getDisplayMetrics().density;
		return (int) (v * value + 0.5f);
	}
    
    
}  