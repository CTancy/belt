/*
 *  Copyright (C) 2015, gelitenight(gelitenight@gmail.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.handmark.pulltorefresh.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class WaveView3 extends View {
    /**
     * +------------------------+
     * |<--wave length->        |______
     * |   /\          |   /\   |  |
     * |  /  \         |  /  \  | amplitude
     * | /    \        | /    \ |  |
     * |/      \       |/      \|__|____
     * |        \      /        |  |
     * |         \    /         |  |
     * |          \  /          |  |
     * |           \/           | water level
     * |                        |  |
     * |                        |  |
     * +------------------------+__|____
     */
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.35f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;

    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#28FFFFFF");
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#3CFFFFFF");
    public static final ShapeType DEFAULT_WAVE_SHAPE = ShapeType.CIRCLE;

    public static final float DEFAULT_MOVE_TIME_BFB = 0.5f;
    
    public enum ShapeType {
        CIRCLE,
        SQUARE
    }

    // if true, the shader will display the wave
    private boolean mShowWave;

    // shader containing repeated waves
    private BitmapShader mWaveShader;
    // shader matrix
    private Matrix mShaderMatrix;
    // paint to draw wave
    private Paint mViewPaint;
    // paint to draw border
    private Paint mBorderPaint;

    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private float mDefaultWaveLength;
    private double mDefaultAngularFrequency;
    
    private float mDefalutBFB = DEFAULT_MOVE_TIME_BFB;
    private float mSavedBFB   = DEFAULT_MOVE_TIME_BFB;
    private float mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
    private float mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO;
    private float mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;

    private int mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR;
    private int mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR;
    private ShapeType mShapeType = DEFAULT_WAVE_SHAPE;

    
	private Paint movePaint;// 坐标轴 轴线 画笔：
	private Paint noMovePaint;// 坐标轴水平内部 虚线画笔
	
    float[] waveY ;
    float[] waveY2 ;
	
    public WaveView3(Context context) {
        super(context);
        init();
    }

    public WaveView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView3(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mShaderMatrix = new Matrix();
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
        mViewPaint.setColor(Color.WHITE);
        
        movePaint = new Paint();
		movePaint.setColor(Color.rgb(252, 176, 60));
		movePaint.setTextSize(dp2px(14));
		movePaint.setStrokeWidth(dp2px(3));
		movePaint.setAntiAlias(true);
		noMovePaint = new Paint();
		noMovePaint.setColor(Color.parseColor("#7FFFD4"));
		noMovePaint.setTextSize(dp2px(14));
		noMovePaint.setStrokeWidth(dp2px(3));
		noMovePaint.setAntiAlias(true);
    }

    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    /**
     * Shift the wave horizontally according to <code>waveShiftRatio</code>.
     *
     * @param waveShiftRatio Should be 0 ~ 1. Default to be 0.
     *                       <br/>Result of waveShiftRatio multiples width of WaveView is the length to shift.
     */
    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaterLevelRatio() {
        return mWaterLevelRatio;
    }

    /**
     * Set water level according to <code>waterLevelRatio</code>.
     *
     * @param waterLevelRatio Should be 0 ~ 1. Default to be 0.5.
     *                        <br/>Ratio of water level to WaveView height.
     */
    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }

    /**
     * Set vertical size of wave according to <code>amplitudeRatio</code>
     *
     * @param amplitudeRatio Default to be 0.05. Result of amplitudeRatio + waterLevelRatio should be less than 1.
     *                       <br/>Ratio of amplitude to height of WaveView.
     */
    public void setAmplitudeRatio(float amplitudeRatio) {
        if (mAmplitudeRatio != amplitudeRatio) {
            mAmplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    public float getWaveLengthRatio() {
        return mWaveLengthRatio;
    }

    /**
     * Set horizontal size of wave according to <code>waveLengthRatio</code>
     *
     * @param waveLengthRatio Default to be 1.
     *                        <br/>Ratio of wave length to width of WaveView.
     */
    public void setWaveLengthRatio(float waveLengthRatio) {
        mWaveLengthRatio = waveLengthRatio;
    }

    public boolean isShowWave() {
        return mShowWave;
    }

    public void setShowWave(boolean showWave) {
        mShowWave = showWave;
    }

    public void setBorder(int width, int color) {
        if (mBorderPaint == null) {
            mBorderPaint = new Paint();
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setStyle(Style.STROKE);
        }
        mBorderPaint.setColor(color);
        mBorderPaint.setStrokeWidth(width);

        invalidate();
    }

    public void setWaveColor(int behindWaveColor, int frontWaveColor) {
        mBehindWaveColor = behindWaveColor;
        mFrontWaveColor = frontWaveColor;

        // need to recreate shader when color changed
        mWaveShader = null;
//        createShader();
        invalidate();
    }

    public void setShapeType(ShapeType shapeType) {
        mShapeType = shapeType;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        createShader();
    }

    /**
     * Create the shader with default waves which repeat horizontally, and clamp vertically
     */
    private void createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();
        mDefaultAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        mDefaultWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;
        mDefaultWaveLength = getWidth();
        
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);
        wavePaint.setColor(Color.WHITE);

        // Draw default waves into the bitmap
        // y=Asin(蠅x+蠁)+h
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

         waveY = new float[endX];
         waveY2 = new float[endX];
        //波纹的起始从0.2π开始
        /**绘制阴影部分 */
//        final double initial = Math.PI * 0.2;
//        
//        wavePaint.setColor(mBehindWaveColor);
//        wavePaint.setAlpha(80);
//        for (int beginX = 0; beginX <  endX * 0.9; beginX++) {
//            double wx = beginX * mDefaultAngularFrequency + initial;
//            float beginY = (float) (mDefaultWaterLevel - mDefaultAmplitude * Math.sin(wx));
//            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);
//            waveY[beginX] = beginY;
//        }
        
        /**绘制白色背景主体*/
//        wavePaint.setColor();
//        final int wave2Shift = (int) (mDefaultWaveLength / 2);
//        for (int beginX = 0; beginX < endX; beginX++) {
//            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
//        }
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency ;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
        	canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);
        	waveY2[beginX] = beginY;
        }
        
        /**绘制运动与静坐时长百分比**/
        for (int beginX = 0; beginX < endX - 1; beginX++) {
        	canvas.drawLine(beginX, waveY2[beginX], beginX + 1, waveY2[beginX + 1], noMovePaint);
        }
//        
//        for (int beginX = 0; beginX < endX * mDefalutBFB - 1; beginX++) {
//        	canvas.drawLine(beginX, waveY2[beginX], beginX + 1, waveY2[beginX + 1], movePaint);
//        }
        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
        mSavedBFB  = mDefalutBFB;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // modify paint shader according to mShowWave state
        if ( mWaveShader != null) {
        	// change The BFB , reCreateShader
//        	if (mSavedBFB != mDefalutBFB) {
//        		createShader();
//        	}
            // first call after mShowWave, assign it to our paint
            if (mViewPaint.getShader() == null) {
                mViewPaint.setShader(mWaveShader);
            }

            // sacle shader according to mWaveLengthRatio and mAmplitudeRatio
            // this decides the size(mWaveLengthRatio for width, mAmplitudeRatio for height) of waves
            mShaderMatrix.setScale(
                mWaveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,
                mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO,
                0,
                mDefaultWaterLevel);
            // translate shader according to mWaveShiftRatio and mWaterLevelRatio
            // this decides the start position(mWaveShiftRatio for x, mWaterLevelRatio for y) of waves
            mShaderMatrix.postTranslate(
                mWaveShiftRatio * getWidth(),
                (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());

            // assign matrix to invalidate the shader
            mWaveShader.setLocalMatrix(mShaderMatrix);

            float borderWidth = mBorderPaint == null ? 0f : mBorderPaint.getStrokeWidth();
            switch (mShapeType) {
                case CIRCLE:
                    if (borderWidth > 0) {
                        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                            (getWidth() - borderWidth) / 2f - 1f, mBorderPaint);
                    }
                    float radius = getWidth() / 2f - borderWidth;
                    canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, mViewPaint);
                    break;
                case SQUARE:
//                    if (borderWidth > 0) {
//                        canvas.drawRect(
//                            borderWidth / 2f,
//                            borderWidth / 2f,
//                            getWidth() - borderWidth / 2f - 0.5f,
//                            getHeight() - borderWidth / 2f - 0.5f,
//                            mBorderPaint);
//                    }
                    canvas.drawRect(borderWidth, borderWidth, getWidth() - borderWidth,
                        getHeight() - borderWidth, mViewPaint);
//                    if (mSavedBFB != mDefalutBFB) {
                    	drawWaveBfb(canvas);
//                    }
                    break;
            }
        } else {
            mViewPaint.setShader(null);
        }
    }
    
	private int dp2px(int value) {
		float v = getContext().getResources().getDisplayMetrics().density;
		return (int) (v * value + 0.5f);
	}
	
	public void setBFB(float bfb) {
		if(bfb < 0 || bfb > 1){
			return;
		}
		this.mDefalutBFB = bfb;
		postInvalidate();
	}
	
	private void drawWaveBfb(Canvas canvas) {
		final int endX = getWidth();
		if (waveY2.length < endX) {
			return;
		}
        
        for (int beginX = 0; beginX < endX * mDefalutBFB - 1; beginX++) {
        	canvas.drawLine(beginX, waveY2[beginX], beginX + 1, waveY2[beginX + 1], movePaint);
        }
        mSavedBFB  = mDefalutBFB;
	}
}
