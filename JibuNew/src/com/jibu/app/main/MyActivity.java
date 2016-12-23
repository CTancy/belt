package com.jibu.app.main;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.google.gson.Gson;
import com.jibu.app.R;
import com.jibu.app.database.MoveDataService;
import com.jibu.app.database.UserService;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.User;
import com.jibu.app.headportrait.GetPhoto;
import com.jibu.app.login.LoginAndRegActivity;
import com.jibu.app.server.AutoSyncService;
import com.jibu.app.user.HeightActivity;
import com.jibu.app.user.WaistlineActivity;
import com.jibu.app.user.WeightActivity;
import com.szants.hw.bleservice.util.Keeper;
import com.umeng.update.UmengUpdateAgent;
import com.veclink.bracelet.bletask.BleCallBack;
import com.veclink.hw.bleservice.VLBleServiceManager;
import com.veclink.hw.bleservice.util.Debug;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;


public class MyActivity extends WaitingActivity implements OnClickListener{
	
	
	public static final String ACTION_SYNC_ALL_DATA = "sync_all_data_finished_intent";
	
	private final String TAG = "MyActivity";
	public static MyActivity activity = null;
	
	protected String txOriPhoto = "tx_oriPhoto.jpg"; // 图片裁剪前名称
	protected String txCrophoto = "tx_crophoto.jpg"; // 图片裁剪后名称
	private int pickImgFlg = -1;
	
	private GetPhoto getPhoto;
	
	MainApplication mainApplication;
	
	User user;

	UserService userService;
	
//	ImageView imageViewSex,imageViewHead,imageViewNoMove;
	
//	TextView textViewName,textViewHeight,textViewWeight,textViewWaist
	TextView textViewStep;
	
	TextView textViewAPPVersionState, textView_fireware_version;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my);
		((MainApplication) this.getApplication()).addActivity(this);

		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		if (null == mainApplication.user) {
			WelcomeActivity.gotoActivity(this);
			this.finish();
		} else{
			user = mainApplication.user;
			userService = new UserService(this);
		
//		initView();
			checkVersion();
		}
	}
	

	@Override
	protected void onStart() {

		super.onStart();

		

	}

	@Override
	protected void onPause() {

		super.onPause();

		
		
	}

	@Override
	protected void onStop() {

		super.onStop();
	
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		user = mainApplication.user;
		initView();

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		activity = null;
		

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
	
//			((MainApplication) this.getApplication()).exit();
			this.finish();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			
		case R.id.id_linearlayout_title_left:
			this.finish();
				break;
		case R.id.id_linearlayout_title_right:
		
				break;
		case R.id.id_imageview_my_head:			
			//未完善屏蔽
			//裁剪后的文件名按照userId分开
			pickImgFlg = 1;
			getPhoto.imageChooseItem(R.array.GetImgType, txOriPhoto, txCrophoto + user.userId);
				break;
		case R.id.id_linearlayout_my_height:
			HeightActivity.gotoActivity(this, HeightActivity.ENTRY_MODE_SETTING);
				break;
		case R.id.id_linearlayout_my_weight:
			WeightActivity.gotoActivity(this, WeightActivity.ENTRY_MODE_SETTING);
				break;
		case R.id.id_linearlayout_my_waist:
			WaistlineActivity.gotoActivity(this, WaistlineActivity.ENTRY_MODE_SETTING);
				break;
		case R.id.id_relativelayout_my_step:
			StepSettingActivity.gotoActivity(this);
				break;
		
		case R.id.id_relativelayout_my_no_move:
//			SetupLongSitRemindActivity.gotoActivity(this);
//			AntiLostActivity.gotoAntiLostActivity(this);
			Log.e(TAG, "forceUpdate");
//			showWaitCanelable(getString(R.string.check_version));
			clickToUpdate();
				break;
//		case R.id.id_relativelayout_my_fankui:
//			ToastUtil.toast(R.string.open_soon);
//				break;
//		case R.id.id_relativelayout_my_about:
//			AboutusActivity.gotoActivity(this);
//				break;
		case R.id.id_textview_my_exit:
			exitLogin();
			break;
		case R.id.id_textview_my_name:
//			user.userHead = "";
//			userService.updateUser(user);
			break;
		case R.id.id_relativelayout_data_revert:
			
//			if (!AutoSyncService.isAutoSync) {
//				BleTask task = new BleSyncNewDeviceDataTask(MyActivity.this, syncStepDataCallBack,BraceletNewDevice.SPORT_DATA);
//				if (task != null) {
//					task.work();
//					showWait(getString(R.string.syncing));
//				}
//			}
			SyncDataToYunActivity.gotoActivity(MyActivity.this);
			break;
		}
		
	}
	
	
	private void exitLogin(){
		
		mainApplication.user.password = "";
		userService.updateUser(user);
		mainApplication.user = null;
		unBindDevice();
		LoginAndRegActivity.gotoActivity(this);
		mainApplication.exitToLogin();
		
	}
	
	private void unBindDevice(){
		Keeper.clearBindDeviceMessage(this);
		VLBleServiceManager.getInstance().unBindService(getApplication());
		
	}
	
	
	private void initView(){
		
//		imageViewSex = (ImageView)findViewById(R.id.id_imageview_my_sex);
//		imageViewHead = (ImageView)findViewById(R.id.id_imageview_my_head);
//		imageViewNoMove = (ImageView)findViewById(R.id.id_imageview_my_no_move_next);
		
//		textViewName = (TextView)findViewById(R.id.id_textview_my_name);
//		textViewHeight = (TextView)findViewById(R.id.id_textview_my_height);
		textViewStep = (TextView)findViewById(R.id.id_textview_my_step);
//		textViewWaist = (TextView)findViewById(R.id.id_textview_my_waist);
//		textViewWeight = (TextView)findViewById(R.id.id_textview_my_weight);
		
		
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		findViewById(R.id.id_linearlayout_title_right).setOnClickListener(this);
//		findViewById(R.id.id_imageview_my_head).setOnClickListener(this);
		
//		findViewById(R.id.id_linearlayout_my_height).setOnClickListener(this);
//		findViewById(R.id.id_linearlayout_my_weight).setOnClickListener(this);
//		findViewById(R.id.id_linearlayout_my_waist).setOnClickListener(this);
		
		findViewById(R.id.id_relativelayout_my_step).setOnClickListener(this);
		findViewById(R.id.id_relativelayout_my_no_move).setOnClickListener(this);
//		findViewById(R.id.id_relativelayout_my_fankui).setOnClickListener(this);
//		findViewById(R.id.id_relativelayout_my_about).setOnClickListener(this);
//		findViewById(R.id.id_relativelayout_data_revert).setOnClickListener(this);
		
		if (user != null) {
			textViewStep.setText(String.valueOf(user.step));
		}
		findViewById(R.id.id_textview_my_exit).setOnClickListener(this);
		
		textView_fireware_version = (TextView) findViewById(R.id.id_textview_fireware_version);
		((TextView) findViewById(R.id.id_textview_app_version)).setText(getVersion());
		
		if (Keeper.getUserHasBindBand(this)) {
			String DeviceRomVersion = Keeper.getDeviceRomVersion(this);
			if ( null != DeviceRomVersion  && !DeviceRomVersion.equals("")) {
				textView_fireware_version.setText("D013A_V" + DeviceRomVersion);
			}
		} 
//		
//		findViewById(R.id.id_textview_my_name).setOnClickListener(this);
//		if(user!=null){
//			
//			if(user.userName!=null&&user.userName.length()>0){
//				textViewName.setText(user.userName);
//			}else{
//				textViewName.setText(user.userId);
//			}
//			
//			
//			textViewHeight.setText(String.valueOf(user.height));
//			
//			textViewWeight.setText(String.valueOf(user.weight));
//			
//			textViewWaist.setText(String.valueOf(user.waist));
//			
//			textViewStep.setText(String.valueOf(user.step));
//			
//			if(user.sex==0){
//				imageViewSex.setBackgroundResource(R.drawable.my_man);
//			}else{
//				imageViewSex.setBackgroundResource(R.drawable.my_woman);
//			}
//			
//			if(user.userHead != null && !user.userHead.equals("")) {
//				setUserHead(user.userHead);
//			} else {
//				if(user.sex==0){
//					imageViewHead.setBackgroundResource(R.drawable.man_normal);
//				}else{
//					imageViewHead.setBackgroundResource(R.drawable.woman_normal);
//				}
//			}
//
//			
//			
//			
//		}
		
/*		boolean statusIsOpen = ApplicationSharedPreferences.getRemindStatus(this);
		
		if(statusIsOpen){
			imageViewNoMove.setBackgroundResource(R.drawable.btn_toggle_selected);
		}else{
			imageViewNoMove.setBackgroundResource(R.drawable.btn_toggle_normal);
		}*/
		
//		getPhoto = new GetPhoto(this);
	}

	
	
	
	
	
	
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case GetPhoto.REQUEST_CODE_GETIMAGE_BYCAMERA:
			// Log.d("/////////////////===========" + requestCode);
			getPhoto.startActionCrop(GetPhoto.origUri, GetPhoto.cropUri);// 拍照后裁剪
			break;
		case GetPhoto.REQUEST_CODE_GETIMAGE_BYSDCARD: // 相册选定剪切回调。
			// Log.d("---------------" + requestCode);
			showPicImg();
			break;
		case GetPhoto.REQUEST_CODE_GETIMAGE_BYCROP: // 拍照后剪切回调
			// Log.d("******************" + requestCode);
			showPicImg();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 显示选定的图片。
	 * 
	 * @creator：zmt
	 */
	private void showPicImg() {
		switch (pickImgFlg) {

		case 1:
//			mainApplication.user.userHead = GetPhoto.FILE_SAVEPATH + txCrophoto;
//			userService.updateUser(mainApplication.user);
//			Picasso.with(this)
//					.load(new File(GetPhoto.FILE_SAVEPATH + txCrophoto))
//					.skipMemoryCache().transform(new CircleTransform()).into(imageViewHead);
			setUserHead(GetPhoto.FILE_SAVEPATH + txCrophoto + user.userId);
			mainApplication.user.userHead = GetPhoto.FILE_SAVEPATH + txCrophoto + user.userId;
			userService.updateUser(mainApplication.user);
//			ApplicationSharedPreferences.setUserHeadPath(this, GetPhoto.FILE_SAVEPATH + txCrophoto + user.userId);
			break;

		default:
			break;
		}
	}

	/**
	 * 检查版本更新显示
	 **/
	private void checkVersion() {
		textViewAPPVersionState = (TextView) findViewById(R.id.id_textview_app_version_state);
		int status = ApplicationSharedPreferences.getAPPVersion(MyActivity.this);
		if (status == 0) {
			textViewAPPVersionState.setText(R.string.has_new_version);
			findViewById(R.id.id_textview_has_new_version).setVisibility(View.VISIBLE);
		} else {
			textViewAPPVersionState.setText(R.string.is_lastest_version);
			findViewById(R.id.id_textview_has_new_version).setVisibility(View.INVISIBLE);
		}
	}
	
	private void clickToUpdate() {
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.forceUpdate(this);
	}
	public static Bitmap getLoacalBitmap(String url) {
	     try {
	          FileInputStream fis = new FileInputStream(url);
	          return BitmapFactory.decodeStream(fis);
	     } catch (FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	     } catch (Exception e) {
	    	 return null;
	     }
	}
	
	
	private void setUserHead(String url){
//		imageViewHead.setBackgroundColor(Color.TRANSPARENT);
//		Bitmap bitmap = getLoacalBitmap(url); 
//		bitmap = toRoundBitmap(bitmap);
//		imageViewHead.setImageBitmap(bitmap);
//		bitmap.recycle();
	}
    /**
     * 把bitmap转成圆形
     * */
    public static Bitmap toRoundBitmap(Bitmap bitmap){
            int width=bitmap.getWidth();
            int height=bitmap.getHeight();
            int r=0;
            //取最短边做边长
            if(width<height){
                    r=width;
            }else{
                    r=height;
            }
            //构建一个bitmap
            Bitmap backgroundBm=Bitmap.createBitmap(width,height,Config.ARGB_8888);
            //new一个Canvas，在backgroundBmp上画图
            Canvas canvas=new Canvas(backgroundBm);
            Paint p=new Paint();
            //设置边缘光滑，去掉锯齿
            p.setAntiAlias(true);
            RectF rect=new RectF(0, 0, r, r);
            //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时， 
            //且都等于r/2时，画出来的圆角矩形就是圆形
            canvas.drawRoundRect(rect, r/2, r/2, p);
            //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
            p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            //canvas将bitmap画在backgroundBmp上
            canvas.drawBitmap(bitmap, null, rect, p);
            //头像周围画白线
            p.setStrokeWidth(10);
            p.setColor(Color.WHITE);
            p.setStyle(Style.STROKE);
            canvas.drawCircle(r/2, r/2, r/2, p);
            return backgroundBm;
    }
    
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MyActivity.class);
		context.startActivity(intent);
	}
	
	
	
	
	Handler syncStepDataHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BleCallBack.TASK_START:
				break;

			case BleCallBack.TASK_PROGRESS:
				if(msg.obj!=null){
					int progress = (Integer) msg.obj;
//					Log.e(TAG , "progress = " + progress);
					changeWaitMsg(getString(R.string.sync_complete)  + progress + "%" );
				}
				break;

			case BleCallBack.TASK_FINISH:
				if(msg.obj!=null){
					Gson gon = new Gson();
					String result = gon.toJson( msg.obj);
					MoveData.hdlrSyncStepData(user,new MoveDataService(MyActivity.this),result);
					Debug.logBleByTag("sync result is ", result);
				}
				/*通知主界面更新数据*/
				sendSyncDataFinishedBroadCast();
				waitClose();
				ToastUtil.toast(R.string.sync_complete);
				break;

			case BleCallBack.TASK_FAILED:
				ToastUtil.toast(R.string.sync_fail_try_again); 
				waitClose();
				break;
			}
		}
		
	};
	
	BleCallBack syncStepDataCallBack = new BleCallBack(syncStepDataHandler);
	
	private void sendSyncDataFinishedBroadCast() {
		Intent intent = new Intent();
		intent.setAction(ACTION_SYNC_ALL_DATA);
		this.sendBroadcast(intent);
	}
	
	public String getVersion() {

		try {

		PackageManager manager = this.getPackageManager();

		PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);

		String version = info.versionName;

		return  version;

		} catch (Exception e) {

			e.printStackTrace();
			return this.getString(R.string.can_not_find_version_name);
		}
	}
}
