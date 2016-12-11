package com.jibu.app.main;

import java.util.ArrayList;
import java.util.Vector;

import com.jibu.app.R;
import com.jibu.app.database.MoveDataService;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.User;
import com.jibu.app.view.SmartImageView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.CallbackConfig.ICallbackListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ShareActivity extends Activity implements OnClickListener{

	
	private final int SPORT_TYPE_0 = 10;
	private final int SPORT_TYPE_1 = 11;
	private final int SPORT_TYPE_2 = 12;
	private final int SPORT_TYPE_3 = 13;
	
	private final int SIT_TYPE_0 = 0;
	private final int SIT_TYPE_1 = 1;
	private final int SIT_TYPE_2 = 2;
	private final int SIT_TYPE_3 = 3;
	
	public static ShareActivity activity = null;
	
	private String share_url   = "";
	private String share_title = "";
	private String share_content = "";
	final UMSocialService mController  = UMServiceFactory.getUMSocialService("com.umeng.share");
	
	MainApplication mainApplication;
	User user;
	private MoveDataService moveDataService;
	
	TextView textView_sit;
	TextView textView_distance;
	TextView textView_step;
	Button button_share;
	TextView textView_cancel;
	TextView textView_currenttime;
	ImageView imageView_peopel;
	
	/*久坐次数和步数*/
	RelativeLayout rl_longsit, rl_sport;
	SmartImageView rl_sport_bg;
	TextView textview_longsit_time, textview_sport_step;
	TextView textview_step_or_time;
	
	/*活动类型和评语*/
	TextView textview_sport_type, textview_sport_value;
	
	Spinner spinner;
	
	RadioGroup radioGroup;
	
	MoveData currShowMoveDataForday;
	
	private ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_share); 		
		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		if (null == mainApplication.user) {
			WelcomeActivity.gotoActivity(this);
			this.finish();
		} else {
			user = mainApplication.user;
			moveDataService = new MoveDataService(this);
		
			//初始化数据
			initShowMoveDataForDay();
			//更新UI
			initView2();
			//设置分享路径
			setupShare();
		
			addSharePlatform();
		}
	}
	private void setupShare() {
		 share_url   = getString(R.string.share_url);
		 share_title = getString(R.string.share_title);
		 share_content = getString(R.string.share_content);
	}
	
	private void initView2() {
		
		// 分享按钮以及取消按钮
		textView_cancel = (TextView) findViewById(R.id.id_textview_cancel);
		button_share    = (Button)   findViewById(R.id.id_button_share);
		
		findViewById(R.id.id_linearlayout_title_right).setOnClickListener(this);
		button_share.setOnClickListener(this);
		
		//
//		textview_longsit_times = (TextView) findViewById(R.id.id_r);
		rl_longsit = (RelativeLayout) findViewById(R.id.id_relativelayout_longsit);
		rl_sport   = (RelativeLayout) findViewById(R.id.id_relativelayout_sport);
		rl_sport_bg = (SmartImageView) findViewById(R.id.id_smartview_sport_bg);
		
		textview_longsit_time = (TextView) findViewById(R.id.id_textview_sit_time);
		textview_sport_step = (TextView) findViewById(R.id.id_textview_sport_step);
		textview_step_or_time = (TextView) findViewById(R.id.id_textview_step_or_time);
		
		textview_sport_type = (TextView) findViewById(R.id.id_textview_sport_type);
		textview_sport_value= (TextView) findViewById(R.id.id_textview_sport_value);
		
		
		initLongsitView();
		radioGroup = (RadioGroup) findViewById(R.id.id_rg);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Log.e("TAG", "checkedId = " + checkedId);
				switch(checkedId) {
				case R.id.rb0:
					initLongsitView();
					break;
				case R.id.rb1:
					initSportView();
					break;
				}
			}
		});
		
//		spinner = (Spinner) findViewById(R.id.id_spinner);
//		String[] str = getResources().getStringArray(R.array.share_message);
//		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str);
//		spinner.setAdapter(adapter);
//		
//		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				if (position == 0) {
//					initLongsitView();
//				} else if (position == 1) {
//					initSportView();
//				}
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				
//			}
//		});
		
	}
	
	private void initLongsitView() {
		rl_longsit.setVisibility(View.VISIBLE);
		rl_sport.setVisibility(View.VISIBLE);
		rl_sport_bg.setBackgroundResource(R.drawable.share_bg2);
		rl_sport_bg.setRatio(1.4f);
		
		textview_step_or_time.setText(R.string.long_sit_time);
		if (currShowMoveDataForday != null) {
			textview_longsit_time.setText(String.valueOf(currShowMoveDataForday.getNoMoveIndex()));
			textview_sport_step.setText(String.valueOf(currShowMoveDataForday.getNoMoveTime()));
		}
		int sit_type = getSitType(currShowMoveDataForday);
		if (sit_type != -1) {
			sit_type  = sit_type % 10;
		}
		String[] str_types = getResources().getStringArray(R.array.sit_type);
		String[] str_value = getResources().getStringArray(R.array.sit_value);
		
		textview_sport_type.setText(str_types[sit_type]);
		textview_sport_value.setText(str_value[sit_type]);
	}
	
	private int getSitType(MoveData moveData) {
		int sitIndex = moveData.getNoMoveIndex();
		float sitTimes = moveData.getNoMoveTime();
		
		if (sitTimes < 1) {
			return SIT_TYPE_0;
		}
		
		if (sitTimes <= 2) {
			return SIT_TYPE_1;
		}
		
		if (sitTimes <= 7) {
			return SIT_TYPE_2;
		}
		
//		if (sitTimes > 6) {
			return SIT_TYPE_3;
//		}
	}
	
	private int getSportType(MoveData moveData, User user) {
		
		int step = moveData.getStep();
		
		
		
		if (step < 5000) {
			return SPORT_TYPE_0;
		}
		
		if (step >= 15000) {
			return SPORT_TYPE_3;
		}
		
		if (step >= user.step) {
			
			return SPORT_TYPE_2;
		}
		
//		if (step <= 10000) {
			return SPORT_TYPE_1;
//		}
		

		
//		if (sitTimes > 6) {
//		}
	}
	private void initSportView() {
		rl_longsit.setVisibility(View.INVISIBLE);
		rl_sport.setVisibility(View.VISIBLE);
		rl_sport_bg.setBackgroundResource(R.drawable.share_bg1);
		textview_step_or_time.setText(R.string.step);
		if (currShowMoveDataForday != null) {
			textview_sport_step.setText(String.valueOf(currShowMoveDataForday.getStep()));
		}
		
		int sport_type = getSportType(currShowMoveDataForday, user);
		if (sport_type != -1) {
			sport_type  = sport_type % 10;
		}
		String[] str_types = getResources().getStringArray(R.array.sport_type);
		String[] str_value = getResources().getStringArray(R.array.sport_value);
		
		textview_sport_type.setText(str_types[sport_type]);
		textview_sport_value.setText(str_value[sport_type]);
	}
 	
	private void updateView() {
		
	}
	//更新数据
	private void initView() {
		// TODO Auto-generated method stub
		textView_sit = (TextView) findViewById(R.id.sit);
		textView_distance = (TextView) findViewById(R.id.distance);
		textView_step = (TextView) findViewById(R.id.step);
		imageView_peopel = (ImageView) findViewById(R.id.peopel);
		button_share = (Button) findViewById(R.id.id_textview_share);
		textView_cancel = (TextView) findViewById(R.id.id_textview_cancel);
		textView_currenttime = (TextView) findViewById(R.id.current_time_tv);
		
		button_share.setOnClickListener(this);
		textView_cancel.setOnClickListener(this);
		
		textView_sit.setText(String.valueOf(currShowMoveDataForday.getNoMoveIndex()));
		textView_distance.setText(String.valueOf(currShowMoveDataForday.getMoveLength()));
		textView_step.setText(String.valueOf(currShowMoveDataForday.getStep()));
		textView_currenttime.setText(currShowMoveDataForday.year + getString(R.string.year) + (currShowMoveDataForday.mounth+1)
				+ getString(R.string.month) + currShowMoveDataForday.day + getString(R.string.day));
		if (user.sex == 0) {
			imageView_peopel.setImageResource(R.drawable.share_man);
		} else {
			imageView_peopel.setImageResource(R.drawable.share_women);
		}
		
	}
	
	private void initShowMoveDataForDay(){
		
		long currTime = System.currentTimeMillis();
		currShowMoveDataForday = moveDataService.queryMoveDataByUserSpecDay(user.userId,currTime);
		
		if(currShowMoveDataForday!=null){
		}else{
			currShowMoveDataForday = new MoveData(user.userId, currTime);
			moveDataService.insertMoveData(currShowMoveDataForday);
		}
	}
	// 获取指定Activity的截屏，保存到png文件
	private Bitmap takeScreenShot(Activity activity) {

		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();
		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		System.out.println(statusBarHeight);

		// 获取屏幕长和高
//		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
//		int height = activity.getWindowManager().getDefaultDisplay()
//				.getHeight();

		// 去掉标题栏
// 		Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
//		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
//					- statusBarHeight);
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, 
				b1.getWidth(), b1.getHeight() - statusBarHeight);
		view.destroyDrawingCache();
		return b;
	}	
	
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, ShareActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.id_button_share:
			share();
			break;
		case R.id.id_linearlayout_title_right:
			this.finish();
			break;
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/**使用SSO授权必须添加如下代码 */
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}


	private void share()
	{
		button_share.setVisibility(View.INVISIBLE);
		textView_cancel.setVisibility(View.INVISIBLE);
//		// 设置分享内容
//        mController.setShareContent("您的健康助手");
//        // 设置分享图片, 参数2为图片的url地址
//        mController.setShareMedia(new UMImage(activity, takeScreenShot(activity)));
		UMImage image = new UMImage(ShareActivity.this, takeScreenShot(ShareActivity.this));
		
    	setWeiXinContent(image);
    	setCircleContent(image);
    	setQQContent(image);
    	setQzoneContent(image);
    	
		button_share.setVisibility(View.VISIBLE);
		textView_cancel.setVisibility(View.VISIBLE);
        
		mController.openShare(activity, false);
	}
	private void addSharePlatform()
	{
        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA);
        addWxPlatform();
        addQQPlatform();
        addWeiboPlatform();
	}
	
    private void addWxPlatform()
    {
    	String appId = "wx68f0f9afdb191980";
    	String appSecret = "b7d05eb4cb2d3c158a1b00f90bc9f9a0";
    	// 添加微信平台
    	UMWXHandler wxHandler = new UMWXHandler(activity, appId, appSecret);
    	wxHandler.addToSocialSDK();
    	
    	// 添加微信朋友圈
    	UMWXHandler wxCircleHandler = new UMWXHandler(activity, appId, appSecret);
    	wxCircleHandler.setToCircle(true);
    	wxCircleHandler.addToSocialSDK();
    	
    }
    private void setWeiXinContent(UMImage image) {
    	//设置微信好友分享内容
    	WeiXinShareContent weixinContent = new WeiXinShareContent();
    	//设置分享内容
//    	weixinContent.setShareContent(share_content);
    	//设置title
    	weixinContent.setTitle(share_title);
    	//设置分享内容跳转URL
    	weixinContent.setTargetUrl(share_url);
    	//设置分享图片
    	weixinContent.setShareImage(image);
    	
    	mController.setShareMedia(weixinContent);
    }
    
    private void setCircleContent(UMImage image) {
    	//设置微信朋友圈分享内容
    	CircleShareContent circleMedia = new CircleShareContent();
    	//设置分享内容
//    	circleMedia.setShareContent(share_content);
    	
    	circleMedia.setShareImage(image);
    	
    	circleMedia.setTargetUrl(share_url);
    	mController.setShareMedia(circleMedia);

    }
    
    private void setQQContent(UMImage image) {
    	QQShareContent qqShareContent = new QQShareContent();
    	//设置分享图片
    	qqShareContent.setShareImage(image);
    	//设置点击分享内容的跳转链接
    	mController.setShareMedia(qqShareContent);
    }
    
    private void setQzoneContent(UMImage image) {
    	QZoneShareContent qzone = new QZoneShareContent();
    	//设置分享文字
    	qzone.setShareContent(share_content);
    	//设置点击消息的跳转URL
    	qzone.setTargetUrl(share_url);
    	
    	qzone.setTitle(share_title);
    	//设置分享图片
    	qzone.setShareImage(image);
    	mController.setShareMedia(qzone);

    }
    private void addQQPlatform()
    {
    	//参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.    	
    	UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, "1104885232",
    	                "qNEbXwwn0a1jg41g");
    	qqSsoHandler.addToSocialSDK();
    	
    	//参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
    	QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, "1104885232",
    	                "qNEbXwwn0a1jg41g");
    	qZoneSsoHandler.addToSocialSDK();
    	
    }
    

    
    private void addWeiboPlatform()
    {
    	mController.getConfig().setSsoHandler(new SinaSsoHandler());
    }

    
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		activity = null;
	}
	
	@Override
	protected void onPause() {
		super.onStop();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		
	}
}
