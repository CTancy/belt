package com.jibu.app.main;


import com.jibu.app.R;
import com.jibu.app.database.CejuService;
import com.jibu.app.database.MoveDataService;
import com.jibu.app.entity.CejuData;
import com.jibu.app.entity.HuanSuanUtil;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.User;
import com.jibu.app.server.AutoSyncService;
import com.veclink.hw.bleservice.util.Keeper;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CejuActivity extends WaitingActivity implements OnClickListener{
	private final String TAG = "CejuActivity";
	
	MainApplication mainApplication;
	
	User user;
	
	CejuService cejuService;
	
	TextView textViewJuli1,textViewJuli2;
	TextView textViewBushu, textViewYongshi, textViewSudu, textViewPinlv, textViewBuchang;
	Button buttonCeju;
	LinearLayout ll1, ll2, ll3;
	RelativeLayout rl;
	
	enum CejuIngOrNot {
		ING,
		NOT,
		OVER
	};
	
	CejuIngOrNot cejuIngOrNot;
	int step = 0;
	long beginTime = 0;
	
	long cejushijian = 0;
	int cejubushu = 0;
	String cejubeizhu  = "";
	
	//�����Զ�ͬ��ʱ�Զ������˲��
	private boolean isBeginCeju;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ceju);
		mainApplication = (MainApplication) this.getApplication();
		mainApplication.addActivity(this);
		
		initReceiver();
		
		if (null == mainApplication || null == mainApplication.user) {
			WelcomeActivity.gotoActivity(this);
			this.finish();
		} else {
			
			user = mainApplication.user;
			
			cejuService = new CejuService(CejuActivity.this);
			
			cejuIngOrNot = isInWitchState();
			
			initView();
			
			
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(CejuReceiver);
	}
	
	private void initView() {
		rl  = (RelativeLayout) findViewById(R.id.id_rl_ceju);
		ll1 = (LinearLayout) findViewById(R.id.id_ll_juli);
		ll2 = (LinearLayout) findViewById(R.id.id_ll_ceju_ing);
		ll3 = (LinearLayout) findViewById(R.id.id_ll_ceju_info);
		
		textViewJuli2 = (TextView) findViewById(R.id.id_textview_juli2);
		
		textViewBushu = (TextView) findViewById(R.id.id_textview_bushu);
		textViewYongshi = (TextView) findViewById(R.id.id_textview_yongshi);
		textViewSudu    = (TextView) findViewById(R.id.id_textview_sudu);
		textViewPinlv   = (TextView) findViewById(R.id.id_textview_pinlv);
		textViewBuchang   = (TextView) findViewById(R.id.id_textview_buchang);
		
		buttonCeju = (Button) findViewById(R.id.id_button_kaishiceju);
		buttonCeju.setOnClickListener(this);
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		findViewById(R.id.id_linearlayout_title_right).setOnClickListener(this);
		
		
		boolean isCejuIng = ApplicationSharedPreferences.getIsCejuIng(CejuActivity.this);
		if (isCejuIng) {
			cejuIngOrNot = CejuIngOrNot.ING;
			this.step = ApplicationSharedPreferences.getCejuStep(CejuActivity.this);
			this.beginTime = ApplicationSharedPreferences.getCejuTime(CejuActivity.this);
			isBeginCeju  = true;
		} else {
			cejuIngOrNot = CejuIngOrNot.NOT;
			isBeginCeju = false;
		}
		
		
		//δ��ʼ���UI��ʾ
		if (cejuIngOrNot == CejuIngOrNot.NOT){
			UISetCejuNot();
		} else {
			UISetCejuIng();
		}
		
		initReceiver();
	}

	/**
	 *@category �жϵ�ǰ����ʲô״̬���ڲ�໹��δ��ࡣ���ǵ�Ҳ��ϵͳɱ������Ҫ��̬�洢
	 *@see  0����δ��࣬ 1�������ڲ��
	 * 
	 * */
	private CejuIngOrNot isInWitchState() {
		int isInWitchState = 0; 
		if (isInWitchState == 0) {
			return CejuIngOrNot.NOT;
		} else if (isInWitchState == 1){
			return CejuIngOrNot.ING;
		} else {
			return CejuIngOrNot.OVER;
		}
	}
	/**
	 * δ��ʼ���UI����
	 * */
	/**
	 * 
	 */
	private void UISetCejuNot() {
		rl.setBackgroundResource(R.drawable.begin_ceju_bg);
		ll1.setVisibility(View.VISIBLE);
		ll2.setVisibility(View.INVISIBLE);
		ll3.setVisibility(View.VISIBLE);
		
		buttonCeju.setText(R.string.begin_ceju);
		
		textViewBushu.setText(getString(R.string.bushu, 0));
		textViewYongshi.setText(getString(R.string.yongshi, 0, 0));
		textViewBuchang.setText(getString(R.string.buchang, HuanSuanUtil.getBuchang(user.height)));
		textViewSudu.setText(getString(R.string.sudu, 0.0f));
		textViewPinlv.setText(getString(R.string.pinlv, 0.0f));
		textViewJuli2.setText("0.00");
		
		
	}
	
	/**
	 * ���ڲ��UI��ʾ
	 */
	private void UISetCejuIng() {
		rl.setBackgroundResource(R.drawable.ing_ceju_bg);
		
		ll1.setVisibility(View.INVISIBLE);
		ll2.setVisibility(View.VISIBLE);
		ll3.setVisibility(View.INVISIBLE);
		
		buttonCeju.setText(R.string.end_ceju);
	}
	/**
	 * ������Ҫ����
	 * 
	 */
	private void UISetCejuOver(int step) {
		ll1.setVisibility(View.VISIBLE);
		ll2.setVisibility(View.INVISIBLE);
		ll3.setVisibility(View.VISIBLE);
		
		rl.setBackgroundResource(R.drawable.end_ceju_bg);
		
		buttonCeju.setText(R.string.save_ceju);
		
		int distance  = step - this.step;
		long time     = (System.currentTimeMillis() - beginTime)/1000;
		
		float minus   = time/60.0f;
		float buchang = HuanSuanUtil.getBuchang(user.height*100)/100;
		float juli    = buchang * distance;
		float sudu    = juli / minus;
		float pinlv   = distance / minus;
		
		
		Log.e(TAG, "����� = " + distance +"\n ʱ�� = " + time/1000);
		textViewBushu.setText(getString(R.string.bushu, distance));
		textViewYongshi.setText(getString(R.string.yongshi, time/60, time%60));
		textViewBuchang.setText(getString(R.string.buchang, buchang));
		textViewSudu.setText(getString(R.string.sudu, sudu));
		textViewPinlv.setText(getString(R.string.pinlv, pinlv));
		textViewJuli2.setText(juli + "");
		/*�������ʱ�洢*/
		setCejubushu(distance);
		setCejushijian(time);
	}
	/**
	 * ��� ��ʼ��� 
	 */
	private void beginCeju() {
		showWaitCanelable(getString(R.string.open_ceju));
		Intent i = new Intent(CejuActivity.this, AutoSyncService.class);
		startService(i);
		isBeginCeju = true;
	}
	
	/**
	 * ͬ����ʼ���ݣ��ɹ�����뿪ʼ���ҳ�棬ʧ�ܷ���
	 * 
	 */
	private void beginCejuOver(boolean isSuccess, int step) {
		waitClose();
		if (isSuccess) {
			UISetCejuIng();
			this.step =  step;
			beginTime = System.currentTimeMillis();
			cejuIngOrNot = CejuIngOrNot.ING;
//			Log.e(TAG, "��ʼ���� " + step +"\n ��ʼʱ�� = " + beginTime);
			
			saveToSharedPreferences(true, step, beginTime);
		} else {
			ToastUtil.toast(R.string.open_ceju_failed);
			UISetCejuNot();
		}
	}
	
	private void saveToSharedPreferences(boolean isCejuIng, int step, long time) {
		ApplicationSharedPreferences.setIsCejuIng(CejuActivity.this, isCejuIng);
		ApplicationSharedPreferences.setCejuStep(CejuActivity.this, step);
		ApplicationSharedPreferences.setCejuTime(CejuActivity.this, time);
	}
	/**
	 * ��� ������� 
	 */
	private void endCeju() {
		showWaitCanelable(getString(R.string.close_ceju));
		Intent i = new Intent(CejuActivity.this, AutoSyncService.class);
		startService(i);
	}
	
	/**
	 * ͬ���������ݣ��ɹ���������ͳ��ҳ�棬ʧ�ܷ�������һ��
	 * 
	 */
	private void endCejuOver(boolean isSuccess, int step) {
		waitClose();
		if (isSuccess) {
			UISetCejuOver(step);
			cejuIngOrNot = CejuIngOrNot.OVER;
			
			saveToSharedPreferences(false, 0, 0);
			
		} else {
			ToastUtil.toast(R.string.close_ceju_failed);
		}
	}
	
	
	/**
	 * ��������
	 * 
	 */
	private void saveCeju() {
		ShowSaveCejuDialog(CejuActivity.this);
	}
	
	private void initReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AutoSyncService.ACTION_STATE);
		
		registerReceiver(CejuReceiver, intentFilter);
	}
	
	BroadcastReceiver CejuReceiver = new BroadcastReceiver() 
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			if (!isBeginCeju) return; 
			String action = intent.getAction();
			if(action.equals(AutoSyncService.ACTION_STATE)) {
				Log.e(TAG, "�豸�յ������Զ�ͬ�������Ĺ㲥");
				int state = intent.getIntExtra(AutoSyncService.ACTION_STATE, -1);
				changeSycnState(state);
			}
		}
		
	};
	
	private void changeSycnState(int value) {
		switch(value) {
		case AutoSyncService.SYNC_FAILD:
			if (cejuIngOrNot == CejuIngOrNot.NOT) { 
				beginCejuOver(false, 0);
			} else {
				endCejuOver(false, 0);
			}
			break;
		case AutoSyncService.SYNC_STEP:
			break;
		case AutoSyncService.SYNC_COMPLETE:
			int step = queryTodayMoveData();
			if (cejuIngOrNot == CejuIngOrNot.NOT) { 
				beginCejuOver(true, step);
			} else {
				endCejuOver(true, step);
			}
			break;
		default:
			break;
		}
	}
	
	private int queryTodayMoveData() {
		MoveData moveData = new MoveDataService(this).queryMoveDataByUserSpecDay(user.userId, System.currentTimeMillis());
		return moveData.getStep();
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.id_button_kaishiceju:
			if (!checkIsBindBelt()) return;
			if (cejuIngOrNot == CejuIngOrNot.NOT) { 
				beginCeju();
			} else if (cejuIngOrNot == CejuIngOrNot.ING){
				endCeju();
			} else {
				saveCeju();
			}
			break;
		case R.id.id_linearlayout_title_left:
			this.finish();
			break;
		case R.id.id_linearlayout_title_right:
			CejuHistoryActivity.gotoActivity(CejuActivity.this);
			break;
		}
	}
	
	private AlertDialog ShowSaveCejuDialog(Context context) {
		final View v = LayoutInflater.from(context)
				.inflate(R.layout.linearlayout_save_ceju, null);
		final AlertDialog saveDailog = new AlertDialog.Builder(context).create();
		saveDailog.show();
		saveDailog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		saveDailog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); 
		saveDailog.setContentView(v);
		
		TextView tv  = (TextView) v.findViewById(R.id.id_textview_save_data);
		TextView tv2 = (TextView) v.findViewById(R.id.id_textview_ceju_m);
		final EditText ed  = (EditText) v.findViewById(R.id.id_edittext_info);
		
		tv2.setText(getString(R.string.n_meter, getCejubushu() * HuanSuanUtil.getBuchang(user.height)));
		
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				insertCejuData(getCejubushu(), getCejushijian(), ed.getText().toString());
				
//				Log.e(TAG, "cejubushu=" + cejubushu + "cejushijian = "+ cejushijian);
//				cejuService.deletedAllCejuData(user.userId);
				//������Ϸ��س�ʼδ��ʼ���״̬��
				UISetCejuNot();
				cejuIngOrNot = CejuIngOrNot.NOT;
				
				saveDailog.dismiss();
				
			}
		});
		
		return saveDailog;
	}
	
	private void insertCejuData(int step, long duration, String info) {
		CejuData g = new CejuData(user.userId, System.currentTimeMillis(), step, duration, info);
		cejuService.insertCejuData(g);
	}

	
	public long getCejushijian() {
		return cejushijian;
	}

	public void setCejushijian(long cejushijian) {
		this.cejushijian = cejushijian;
	}

	public int getCejubushu() {
		return cejubushu;
	}

	public void setCejubushu(int cejubushu) {
		this.cejubushu = cejubushu;
	}

	public boolean checkIsBindBelt() {
		if(Keeper.getUserHasBindBand(CejuActivity.this)) {
			return true;
		} else {
			ToastUtil.toast(R.string.unbind_ebelt_yet);
			return false;
		}
	}
	
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, CejuActivity.class);
		context.startActivity(intent);
	}
}
