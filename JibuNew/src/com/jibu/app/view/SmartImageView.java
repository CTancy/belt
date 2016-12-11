package com.jibu.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SmartImageView extends ImageView {  
	    
    /** ͼƬ��͸ߵı��� */  
    private float ratio = 2.43f;  
      

    public SmartImageView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    public SmartImageView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public SmartImageView(Context context) {  
        super(context);  
    }  
  
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
      
        // �������������Ŀ�ȷ����ϵ�ģʽ  
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
        // �������������ĸ߶ȷ����ϵ�ģʽ  
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
      
        // �������������Ŀ�ȵ�ֵ  
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()  
                - getPaddingRight();  
        // �������������ĸ߶ȵ�ֵ  
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft()  
                - getPaddingRight();  
      
        if (widthMode == MeasureSpec.EXACTLY  
                && heightMode != MeasureSpec.EXACTLY && ratio != 0.0f) {  
            // �ж�����Ϊ�����ģʽΪExactly��Ҳ������丸���������ָ����ȣ�  
            // �Ҹ߶�ģʽ����Exaclty���������õļȲ���fill_parentҲ���Ǿ����ֵ��������Ҫ�������  
            // ��ͼƬ�Ŀ�߱��Ѿ���ֵ��ϣ�������0.0f  
            // ��ʾ���ȷ����Ҫ�����߶�  
            height = (int) (width / ratio + 0.5f);  
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,  
                    MeasureSpec.EXACTLY);  
        } else if (widthMode != MeasureSpec.EXACTLY  
                && heightMode == MeasureSpec.EXACTLY && ratio != 0.0f) {  
            // �ж�������������෴����ȷ���͸߶ȷ������������  
            // ��ʾ�߶�ȷ����Ҫ�������  
            width = (int) (height * ratio + 0.5f);  
      
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,  
                    MeasureSpec.EXACTLY);  
        }  
      
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
    } 
    
    public void setRatio(float ratio) {  
        this.ratio = ratio;  
    }  
}  
