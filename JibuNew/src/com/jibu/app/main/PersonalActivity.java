package com.jibu.app.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import com.jibu.app.R;
import com.jibu.app.database.MoveDataService;
import com.jibu.app.database.PersonalInfoService;
import com.jibu.app.database.UserService;
import com.jibu.app.entity.HuanSuanUtil;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.User;
import com.jibu.app.entity.UserPersonalInfo;
import com.jibu.app.headportrait.CircleTransform;
import com.jibu.app.headportrait.GetPhoto;
import com.jibu.app.login.LoginAndRegActivity;
import com.jibu.app.user.HeightActivity;
import com.jibu.app.user.NameActivity;
import com.jibu.app.user.WaistlineActivity;
import com.jibu.app.user.WeightActivity;
import com.squareup.picasso.Picasso;
import com.szants.hw.bleservice.util.Keeper;
import com.veclink.hw.bleservice.VLBleServiceManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalActivity extends Activity implements OnClickListener{
	
	
	MainApplication mainApplication;
	MoveDataService  moveDataService;
	UserService userService;
	PersonalInfoService personalInfoService;
	User user;
	
	protected String txOriPhoto = "tx_oriPhoto.jpg"; // 图片裁剪前名称
	protected String txCrophoto = "tx_crophoto.jpg"; // 图片裁剪后名称
	private int pickImgFlg = -1;
	
	private GetPhoto getPhoto;
	
	
	ImageView imageViewSex,imageViewHead;
	
	TextView textViewName,textViewHeight,textViewWeight,textViewWaist;
	
	TextView textViewLongestSitDay, textViewLongestSitDayYear, textViewLongestSitTime;
	TextView textViewLongestSportDay, textViewLongestSportDayYear, textViewLongestSportStep;
	TextView textViewTotalDistance, textViewEarthAround, textviewSitTimeTotal, textViewOneYear;
	TextView textViewDabiaotianshu, textViewLianxudabiaotianshu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personal_info);
		
		mainApplication = (MainApplication) this.getApplication();
		if (null == mainApplication.user) {
			WelcomeActivity.gotoActivity(this);
			this.finish();
		} else {
			user = mainApplication.user;
			userService = new UserService(this);
			initView();
		
		
			user = mainApplication.user;
			moveDataService = new MoveDataService(this);
			userService = new UserService(this);
			personalInfoService = new PersonalInfoService(this);
		
			new updateUserPersonalInfoTask2().execute();
		}
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		onViewDataChanged();
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
	}



	private void initView() {
		imageViewSex = (ImageView)findViewById(R.id.id_imageview_my_sex);
		imageViewHead = (ImageView)findViewById(R.id.id_imageview_my_head);
//		imageViewNoMove = (ImageView)findViewById(R.id.id_imageview_my_no_move_next);
		
		textViewName = (TextView)findViewById(R.id.id_textview_my_name);
		textViewHeight = (TextView)findViewById(R.id.id_textview_my_height);
		textViewWaist = (TextView)findViewById(R.id.id_textview_my_waist);
		textViewWeight = (TextView)findViewById(R.id.id_textview_my_weight);
		
		
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		findViewById(R.id.id_imageview_my_head).setOnClickListener(this);
		
		findViewById(R.id.id_linearlayout_my_height).setOnClickListener(this);
		findViewById(R.id.id_linearlayout_my_weight).setOnClickListener(this);
		findViewById(R.id.id_linearlayout_my_waist).setOnClickListener(this);
				
//		findViewById(R.id.id_textview_my_exit).setOnClickListener(this);
		
		findViewById(R.id.id_textview_my_name).setOnClickListener(this);
		
		
		textViewLongestSitDay     = (TextView) findViewById(R.id.id_textview_longest_sit_day);
		textViewLongestSitDayYear = (TextView) findViewById(R.id.id_textview_longest_sit_day_year);
		textViewLongestSitTime    = (TextView) findViewById(R.id.id_textview_longest_sit_time);
		
		textViewLongestSportDay     = (TextView) findViewById(R.id.id_textview_longest_sport_day);
		textViewLongestSportDayYear = (TextView) findViewById(R.id.id_textview_longest_sport_day_year);
		textViewLongestSportStep    = (TextView) findViewById(R.id.id_textview_longest_sport_step);
		
		textViewTotalDistance  = (TextView) findViewById(R.id.id_textview_totoal_distance);
		textViewEarthAround = (TextView) findViewById(R.id.id_textview_earth_around);
		textviewSitTimeTotal = (TextView) findViewById(R.id.id_textview_sit_time_total);
		textViewOneYear = (TextView) findViewById(R.id.id_textView_one_year);
		
		textViewDabiaotianshu = (TextView) findViewById(R.id.id_textview_dabiaotianshu);
		textViewLianxudabiaotianshu = (TextView) findViewById(R.id.id_textview_lianxudabiaotianshu);
		getPhoto = new GetPhoto(this);
	}
	
	private void onViewDataChanged() {
		if(user!=null){
			
			if(user.userName!=null&&user.userName.length()>0){
				textViewName.setText(user.userName);
			}else{
				textViewName.setText(user.userId);
			}
			
			
			textViewHeight.setText(String.valueOf(user.height));
			
			textViewWeight.setText(String.valueOf(user.weight));
			
			textViewWaist.setText(String.valueOf(user.waist));
			
			
			if(user.sex==0){
				imageViewSex.setBackgroundResource(R.drawable.my_man);
			}else{
				imageViewSex.setBackgroundResource(R.drawable.my_woman);
			}
			
			if(user.userHead != null && !user.userHead.equals("")) {
				try {
					setUserHead(user.userHead);
				} catch(Exception e) {
					if(user.sex==0){
						imageViewHead.setBackgroundResource(R.drawable.man_normal);
					}else{
						imageViewHead.setBackgroundResource(R.drawable.woman_normal);
					}
				}
				
			} else {
				if(user.sex==0){
					imageViewHead.setBackgroundResource(R.drawable.man_normal);
				}else{
					imageViewHead.setBackgroundResource(R.drawable.woman_normal);
				}
			}
		}
	}
	
	private void onStasticViewChanged(UserPersonalInfo info) {
		if (info == null ) return;
		
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(info.longestSitDay);
		
		/*set longest Sit*/
		SimpleDateFormat fmt = new SimpleDateFormat("MM月dd日");
		textViewLongestSitDay.setText(fmt.format(cl.getTime()));
		textViewLongestSitDayYear.setText(String.valueOf(cl.get(Calendar.YEAR)));
		textViewLongestSitTime.setText(String.valueOf(info.longestSitDayTime));
		
		/*set longest Sport*/
		cl.setTimeInMillis(info.longestSportDay);
		textViewLongestSportDay.setText(fmt.format(cl.getTime()));
		textViewLongestSportDayYear.setText(String.valueOf(cl.get(Calendar.YEAR)));
		textViewLongestSportStep.setText(String.valueOf(info.longestSportDayStep));
		
		/*set total distance&&*/
		textViewTotalDistance.setText(String.valueOf(HuanSuanUtil.getLengthByStep(info.sportDistanceTotal)));
		textviewSitTimeTotal.setText(String.valueOf(info.sitTimeTotal));
		
		textViewDabiaotianshu.setText(String.valueOf(info.dabiaotianshu));
		textViewLianxudabiaotianshu.setText(String.valueOf(info.lianxudabiao));
		
		Log.e("TAG", "lianxudabiao = " + info.lianxudabiao + "\n" + "zongdabiao = " +  info.dabiaotianshu + "\n");
//		textViewEarthAround.setText(getString(R.string.ground_around, info.sportDistanceTotal/800));
//		textViewOneYear.setText(getString(R.string.one_day, info.sitTimeTotal/24));
	}
	
	private void setUserHead(String url){
		imageViewHead.setBackgroundColor(Color.TRANSPARENT);
		if (url.contains("http")) {
			Picasso.with(this)
			.load(url)
			.transform(new CircleTransform())
			.into(imageViewHead);
		} else {
			Bitmap bitmap = MyActivity.getLoacalBitmap(url); 
			bitmap = MyActivity.toRoundBitmap(bitmap);
			imageViewHead.setImageBitmap(bitmap);
		}
	}
	
	public static void gotoActivity(Context context) {
		Intent  intent = new Intent(context, PersonalActivity.class);
		context.startActivity(intent);
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
				
		case R.id.id_textview_my_exit:
			exitLogin();
			break;
		case R.id.id_textview_my_name:
			NameActivity.gotoActivity(this);
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
	/**
	 * 显示选定的图片。
	 * 
	 * @creator：zmt
	 */
	private void showPicImg() {
		switch (pickImgFlg) {

		case 1:
			setUserHead(GetPhoto.FILE_SAVEPATH + txCrophoto + user.userId);
			mainApplication.user.userHead = GetPhoto.FILE_SAVEPATH + txCrophoto + user.userId;
			userService.updateUser(mainApplication.user);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case GetPhoto.REQUEST_CODE_GETIMAGE_BYCAMERA:
			getPhoto.startActionCrop(GetPhoto.origUri, GetPhoto.cropUri);// 拍照后裁剪
			break;
		case GetPhoto.REQUEST_CODE_GETIMAGE_BYSDCARD: // 相册选定剪切回调。
			showPicImg();
			break;
		case GetPhoto.REQUEST_CODE_GETIMAGE_BYCROP: // 拍照后剪切回调
			showPicImg();
			break;

		default:
			break;
		}
	}
	
	private class updateUserPersonalInfoTask extends AsyncTask<Void, Void, UserPersonalInfo> {

		@Override
		protected UserPersonalInfo doInBackground(Void... params) {
			try {
				 long lastDate  = 0;
				 float longestSitTime = 0;
				 int longestSportStep = 0;
				 int sportDistanceTotal = 0;
				 float sitTimeTotal = 0;
				 MoveData longestSitDayData = null;
				 MoveData longestSportDayData = null;
				UserPersonalInfo upi = personalInfoService.queryPersonalInfoByUser(user.userId);
				long CurrentUpdateTime = user.updateTime;
				if(moveDataService == null) {
					moveDataService = new MoveDataService(PersonalActivity.this);
				}
				Vector<MoveData> moveDatas = moveDataService.queryMoveDataByBeginEndDay(user.userId, upi.lastDate, CurrentUpdateTime);
				/*计算最长久坐日和最长运动日*/
				for(int i = 0; i < moveDatas.size(); i++){
					MoveData moveData = moveDatas.elementAt(i);
					float noMoveTime = moveData.getNoMoveTime();
					int moveStep = moveData.getStep();
					if(longestSitTime <= noMoveTime) {
						longestSitTime = noMoveTime;
						longestSitDayData = moveData;
					}
					if(longestSportStep <= moveStep) {
						longestSportStep = moveStep;
						longestSportDayData = moveData;
					}
					/*总路程计算*/
					sportDistanceTotal += moveStep;
					sitTimeTotal += noMoveTime;
				}
				
				/*总路程计算要减去当天的*/
				sportDistanceTotal -= moveDatas.elementAt(moveDatas.size() - 1).getStep();
				sitTimeTotal -= moveDatas.elementAt(moveDatas.size() - 1).getNoMoveTime();
				
				/*更新最长久坐日*/
				if (longestSitDayData != null) {
					Calendar cl = Calendar.getInstance();
					cl.set(longestSitDayData.year, longestSitDayData.mounth, longestSitDayData.day);
					if (upi.longestSitDayTime <= longestSitTime) {
						upi.longestSitDayTime = longestSitTime;
						upi.longestSitDay = cl.getTimeInMillis();
					}
				} 
				
				/*更新最长运动日*/
				if (longestSportDayData != null) {
					Calendar cl = Calendar.getInstance();
					cl.set(longestSportDayData.year, longestSportDayData.mounth, longestSportDayData.day);
					if (upi.longestSportDayStep <= longestSportStep) {
						upi.longestSportDayStep = longestSportStep;
						upi.longestSportDay = cl.getTimeInMillis();
					}
				} 
				
				upi.sitTimeTotal += sitTimeTotal;
				upi.sportDistanceTotal += sportDistanceTotal;
				upi.lastDate = CurrentUpdateTime;
				
				personalInfoService.UpdateUserPersonalInfo(upi);
				
				/*进行更新显示要加上当天数据*/
				upi.sportDistanceTotal += moveDatas.elementAt(moveDatas.size() - 1).getStep();
				upi.sitTimeTotal += moveDatas.elementAt(moveDatas.size() - 1).getNoMoveTime();
				return upi;
			} catch (Exception e){
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(UserPersonalInfo result) {
			
			onStasticViewChanged(result);
		}
		

	}
	
	
	public static class MyDialogFragment extends DialogFragment {
	
	}

	private class updateUserPersonalInfoTask2 extends AsyncTask<Void, Void, UserPersonalInfo> {

		@Override
		protected UserPersonalInfo doInBackground(Void... params) {
			try {
				 float longestSitTime = 0;
				 int longestSportStep = 0;
				 int sportDistanceTotal = 0;
				 int dabiaotianshu = 0;
				 int lianxudabiaotianshu = 0;
				 float sitTimeTotal = 0;
				 MoveData longestSitDayData = null;
				 MoveData longestSportDayData = null;
				UserPersonalInfo upi = personalInfoService.queryPersonalInfoByUser(user.userId);
				long CurrentUpdateTime = user.updateTime;
				int firstIndex = -1, twoIndex = -1;
				if(moveDataService == null) {
					moveDataService = new MoveDataService(PersonalActivity.this);
				}
				Vector<MoveData> moveDatas = moveDataService.queryAllMoveDataByUser(user.userId);
				/*计算最长久坐日和最长运动日*/
				for(int i = 0; i < moveDatas.size(); i++){
					MoveData moveData = moveDatas.elementAt(i);
					float noMoveTime = moveData.getNoMoveTime();
					int moveStep = moveData.getStep();
					
					/**处理之前未保存的步数目标*/
					
					if (moveData.todayTarget <= 0) {
						moveData.todayTarget = user.step;
						moveDataService.insertMoveData(moveData);
					}
					
					if(longestSitTime <= noMoveTime) {
						longestSitTime = noMoveTime;
						longestSitDayData = moveData;
					}
					if(longestSportStep <= moveStep) {
						longestSportStep = moveStep;
						longestSportDayData = moveData;
					}
					/*总路程计算*/
					sportDistanceTotal += moveStep;
					sitTimeTotal += noMoveTime;
					
					/*达标天数计算 && */
					if (moveStep >= moveData.todayTarget) {//达标
						dabiaotianshu++;
						
						if (firstIndex == -1) { //第一次达标的位置
							firstIndex = i;
						} else if (i == moveDatas.size() -1){ //处理最后一天为达标的情况
							twoIndex = i + 1;
							if (lianxudabiaotianshu < twoIndex - firstIndex) {
								lianxudabiaotianshu = twoIndex - firstIndex;
							}
							firstIndex = twoIndex = -1;
						}
						
					} else { 	// 不达标的位置
						if (firstIndex == -1) {
							
						} else {
							twoIndex = i;
							if (lianxudabiaotianshu < twoIndex - firstIndex) {
								lianxudabiaotianshu = twoIndex - firstIndex;
							}
							firstIndex = twoIndex = -1;
						}
					}
					
					
				}
				
				/*总路程计算要减去当天的*/
//				sportDistanceTotal -= moveDatas.elementAt(moveDatas.size() - 1).getStep();
//				sitTimeTotal -= moveDatas.elementAt(moveDatas.size() - 1).getNoMoveTime();
				
				/*更新最长久坐日*/
				if (longestSitDayData != null) {
					Calendar cl = Calendar.getInstance();
					cl.set(longestSitDayData.year, longestSitDayData.mounth, longestSitDayData.day);
//					if (upi.longestSitDayTime <= longestSitTime) {
						upi.longestSitDayTime = longestSitTime;
						upi.longestSitDay = cl.getTimeInMillis();
//					}
				} 
				
				/*更新最长运动日*/
				if (longestSportDayData != null) {
					Calendar cl = Calendar.getInstance();
					cl.set(longestSportDayData.year, longestSportDayData.mounth, longestSportDayData.day);
//					if (upi.longestSportDayStep <= longestSportStep) {
						upi.longestSportDayStep = longestSportStep;
						upi.longestSportDay = cl.getTimeInMillis();
//					}
				} 
				
				upi.sitTimeTotal  =  sitTimeTotal;
				upi.sportDistanceTotal = sportDistanceTotal;
				upi.dabiaotianshu =  dabiaotianshu;
				upi.lianxudabiao = lianxudabiaotianshu;
//				upi.lastDate = CurrentUpdateTime;
//				personalInfoService.UpdateUserPersonalInfo(upi);
				return upi;
			} catch (Exception e){
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(UserPersonalInfo result) {
			
			onStasticViewChanged(result);
		}
		

	}
	
}
