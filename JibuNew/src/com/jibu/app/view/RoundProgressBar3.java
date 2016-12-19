package com.jibu.app.view;

import com.jibu.app.R;

import android.annotation.SuppressLint;
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
import android.text.style.LineHeightSpan.WithDensity;
import android.util.AttributeSet;  
import android.view.View;  
  
/** 
 * ��iphone�����ȵĽ��������̰߳�ȫ��View����ֱ�����߳��и��½��� 
 * @author xiaanming 
 * 
 */  
public class RoundProgressBar3 extends View {  
    /** 
     * ���ʶ�������� 
     */  
    private Paint paint;  
    
    private Paint imgPaint;  
      
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
    
    private int drawBitmap;
    private int drawBitmap_width_dp;
    private int drawBitmap_height_dp;
    
    public RoundProgressBar3(Context context) {  
        this(context, null);  
    }  
  
    public RoundProgressBar3(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
    }  
      
    public RoundProgressBar3(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
          
        paint = new Paint();  
  
        imgPaint = new Paint();
          
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,  
                R.styleable.RoundProgressBar);  
        isBFB = false;  
        //��ȡ�Զ������Ժ�Ĭ��ֵ  
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);  
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);  
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GREEN);  
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);  
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 2);  
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);  
        progress = mTypedArray.getInteger(R.styleable.RoundProgressBar_progress, 0); 
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);  
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);  
          
        textTop = mTypedArray.getString(R.styleable.RoundProgressBar_textTop);
        textBottom = mTypedArray.getString(R.styleable.RoundProgressBar_textBottom);
        paddingTop = mTypedArray.getDimension(R.styleable.RoundProgressBar_paddingTop, 15);  
        paddingBottom = mTypedArray.getDimension(R.styleable.RoundProgressBar_paddingBottom, 25);  
        
        drawBitmap = R.drawable.belt;
        drawBitmap_width_dp = 230;
        drawBitmap_height_dp = 230;
        progress = 100;
        
        mTypedArray.recycle();  
    }  
      
    private int dp2px(int value) {
		float v = getContext().getResources().getDisplayMetrics().density;
		return (int) (v * value + 0.5f);
	}
  
    @SuppressLint("DrawAllocation")
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
        paint.setColor(Color.WHITE); //����Բ������ɫ  
        paint.setStyle(Paint.Style.STROKE); //���ÿ���  
        paint.setStrokeWidth(3); //����Բ���Ŀ��  
        paint.setAntiAlias(true);  //�������   
        
        canvas.drawCircle(centre, centre, radius, paint); //����Բ��  
        
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
        if(progress > max){  
            progress = max;  
        }  
        if(progress <= max){  
            this.progress = progress;  
            postInvalidate();  
        }  
          
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
  
}  