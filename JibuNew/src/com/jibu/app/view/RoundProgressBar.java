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
 * ��iphone�����ȵĽ��������̰߳�ȫ��View����ֱ�����߳��и��½��� 
 * @author xiaanming 
 * 
 */  
public class RoundProgressBar extends View {  
    /** 
     * ���ʶ�������� 
     */  
    private Paint paint;  
      
    /** 
     * Բ������ɫ 
     */  
    private int roundColor;  
      
    /** 
     * Բ�����ȵ���ɫ 
     */  
    private int roundProgressColor;  
      
    /** 
     * �м���Ȱٷֱȵ��ַ�������ɫ 
     */  
    private int textColor;  
      
    /** 
     * �м���Ȱٷֱȵ��ַ��������� 
     */  
    private float textSize;  
    
    
    private String textTop;
    private String textBottom;
    
    private float paddingTop;
    private float paddingBottom;
      
    /** 
     * Բ���Ŀ�� 
     */  
    private float roundWidth;  
      
    /** 
     * ������ 
     */  
    private int max;  
      
    /** 
     * ��ǰ���� 
     */  
    private int progress;  
    /** 
     * �Ƿ���ʾ�м�Ľ��� 
     */  
    private boolean textIsDisplayable;  
      
    /** 
     * ���ȵķ��ʵ�Ļ��߿��� 
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
        //��ȡ�Զ������Ժ�Ĭ��ֵ  
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
         * �������Ĵ�Բ�� 
         */  
        int centre = getWidth()/2; //��ȡԲ�ĵ�x����  
        
        int radius = (int) (centre - roundWidth/2); //Բ���İ뾶  
        PathEffect effects1 = new DashPathEffect(new float[] { 0,0}, 1);  
        paint.setPathEffect(effects1);
        paint.setColor(roundColor); //����Բ������ɫ  
        paint.setStyle(Paint.Style.STROKE); //���ÿ���  
        paint.setStrokeWidth(roundWidth); //����Բ���Ŀ��  
        paint.setAntiAlias(true);  //�������   
        
        canvas.drawCircle(centre, centre, radius, paint); //����Բ��  
//        Log.e("log", centre + "  "+centre );  
          
        /** 
         * �����Ȱٷֱ� 
         */  
        {
	        paint.setStrokeWidth(0);   
	        paint.setColor(textColor);  
	        paint.setTextSize(textSize);  
	      //  paint.setTypeface(Typeface.DEFAULT_BOLD); //��������  
	        int percent = (int)(((float)progress / (float)max) * 100);  //�м�Ľ��Ȱٷֱȣ���ת����float�ڽ��г������㣬��Ȼ��Ϊ0  
	        float textWidth;
	        if(isBFB){
	        	textWidth = paint.measureText(String.valueOf(percent)+"%");
	        }else{
	        	textWidth = paint.measureText(String.valueOf(progress));   //���������ȣ�������Ҫ��������Ŀ��������Բ���м�  
		        
	        }
	        if(textIsDisplayable  && style == STROKE){
	        	if(isBFB){
	        		canvas.drawText(String.valueOf(percent)+"%", centre - textWidth / 2, centre + textSize/2, paint); //�������Ȱٷֱ�  
	        	}else{
	        		canvas.drawText(String.valueOf(progress), centre - textWidth / 2, centre + textSize/2, paint);  
	        	}
	        }  
        } 
        
        {

	      //  paint.setTypeface(Typeface.DEFAULT_BOLD); //��������  
	       
	        paint.setStrokeWidth(0);   
	        paint.setColor(textColor);  
	        paint.setTextSize(textSize/3);  
	        
	        float textWidth = paint.measureText(textTop);   //���������ȣ�������Ҫ��������Ŀ��������Բ���м�  
	          
	        float x =  centre - textWidth / 2;
	        float y = centre + (textSize/2) - paddingTop - dp2px(20);
	        
//	        //���ƽ��ղ�������Ӱ
//	        RectF rectF = new RectF();
//	        
//	        rectF.set(centre - textWidth/2 - dp2px(10), y - textSize/3, 
//	        		centre + textWidth/2 + dp2px(10), y + textSize/8);
	        
//	        canvas.drawRoundRect(rectF, textSize/2, textSize/2, paint);
	        
	        
	        
//	        paint.setStrokeWidth(0);   
//	        paint.setColor(textColor);  
//	        paint.setTextSize(textSize/3);  
	        canvas.drawText(textTop, x, y, paint); //�������Ȱٷֱ�  
	        

        } 
          
        
        {
	        paint.setStrokeWidth(0);   
	        paint.setColor(textColor);  
	        paint.setTextSize(textSize/3);  
	      //  paint.setTypeface(Typeface.DEFAULT_BOLD); //��������  
	       
	        float textWidth = paint.measureText(textBottom);   //���������ȣ�������Ҫ��������Ŀ��������Բ���м�  
	          
	        float x =  centre - textWidth / 2;
	        float y = centre + (textSize/2) + paddingBottom;
	        
	        canvas.drawText(textBottom, x, y, paint); //�������Ȱٷֱ�  
	         
        } 
        /** 
         * ��Բ�� ����Բ���Ľ��� 
         */  
          
        //���ý�����ʵ�Ļ��ǿ���  
        paint.setStrokeWidth(roundWidth); //����Բ���Ŀ��  
        paint.setColor(roundProgressColor);  //���ý��ȵ���ɫ  
        RectF oval = new RectF(centre - radius, centre - radius, centre  
                + radius, centre + radius);  //���ڶ����Բ������״�ʹ�С�Ľ���  
          
        switch (style) {  
        case STROKE:{  
            paint.setStyle(Paint.Style.STROKE);  
            PathEffect effects = new DashPathEffect(new float[] { 15,1}, 1);  
            paint.setPathEffect(effects);  
            
            if (hasAnimation) {
            	canvas.drawArc(oval, beginAngle, 90, false, paint);
            } else {
            	canvas.drawArc(oval, -90, 360 * progress / max, false, paint);  //���ݽ��Ȼ�Բ��  
            }
            break;  
        }  
        case FILL:{  
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            if(progress !=0)   
            	canvas.drawArc(oval, 90, 360 * progress / max, true, paint);  //���ݽ��Ȼ�Բ��  
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
     * ���ý��ȵ����ֵ 
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
     * ��ȡ����.��Ҫͬ�� 
     * @return 
     */  
    public synchronized int getProgress() {  
        return progress;  
    }  
  
    /** 
     * ���ý��ȣ���Ϊ�̰߳�ȫ�ؼ������ڿ��Ƕ��ߵ����⣬��Ҫͬ�� 
     * ˢ�½������postInvalidate()���ڷ�UI�߳�ˢ�� 
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
  
    //����ͬ������
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
    
    //��������
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