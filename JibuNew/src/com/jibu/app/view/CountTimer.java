package com.jibu.app.view;

import android.os.CountDownTimer;
import android.widget.TextView;

public class CountTimer extends CountDownTimer {
	public static final int TIME_COUNT = 61000;//时间防止从59s开始显示（以倒计时120s为例子）
	private TextView btn;
	private int endStrRid;
	private String reacquire_code;
	private int normalColor, timingColor;//未计时的文字颜色，计时期间的文字颜色
	public CountTimer(long millisInFuture, long countDownInterval, TextView btn, int endStrRid) {
		super(millisInFuture, countDownInterval);
		this.btn = btn;
		this.endStrRid = endStrRid;
	}
	
	public CountTimer( TextView btn, int endStrRid, String reacquire_code)  {
		super(TIME_COUNT, 1000);
		this.btn = btn;
		this.endStrRid = endStrRid;
		this.reacquire_code = reacquire_code;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		btn.setEnabled(false);
		btn.setText(millisUntilFinished/1000 + reacquire_code);
	}

	@Override
	public void onFinish() {
		btn.setText(endStrRid);
		btn.setEnabled(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
