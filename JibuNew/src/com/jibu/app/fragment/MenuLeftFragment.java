package com.jibu.app.fragment;


import com.jibu.app.R;
import com.jibu.app.entity.User;
import com.jibu.app.headportrait.CircleTransform;
import com.jibu.app.main.AntiLostActivity;
import com.jibu.app.main.AntiLostNewActivity;
import com.jibu.app.main.CejuActivity;
import com.jibu.app.main.MainActivity;
import com.jibu.app.main.MainApplication;
import com.jibu.app.main.MyActivity;
import com.jibu.app.main.PersonalActivity;
import com.jibu.app.main.ScanActivity;
import com.jibu.app.main.SetupLongSitRemindActivity;
import com.jibu.app.main.ShareActivity;
import com.jibu.app.main.YaodaiActivity;
import com.squareup.picasso.Picasso;
import com.veclink.hw.bleservice.util.Keeper;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuLeftFragment extends Fragment implements OnClickListener{
	
	private static final String TAG = "MenuLeftFragment";
	MainActivity activity;
	TextView textview_ebelt;
	ImageView imageview_head;
	TextView textview_name;
	View v;
	User user;
	MainApplication mainApplication;
	
	protected final String txOriPhoto = "tx_oriPhoto.jpg"; // Í¼Æ¬²Ã¼ôÇ°Ãû³Æ
	protected final String txCrophoto = "tx_crophoto.jpg"; // Í¼Æ¬²Ã¼ôºóÃû³Æ
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainApplication = (MainApplication) getActivity().getApplication();
		v = inflater.inflate(R.layout.activity_main_menu, container, false);
		initView(v);
		return v;
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity =(MainActivity) activity;
	}


	private void initView(View v) {
		// TODO Auto-generated method stub
		
		v.findViewById(R.id.id_linearlayout_menu_long_sit).setOnClickListener(this);
		v.findViewById(R.id.id_linearlayout_menu_phone_antilost).setOnClickListener(this);
		v.findViewById(R.id.id_linearlayout_menu_my_ebelt).setOnClickListener(this);
		v.findViewById(R.id.id_linearlayout_menu_share).setOnClickListener(this);
		v.findViewById(R.id.id_linearlayout_menu_setup).setOnClickListener(this);
		v.findViewById(R.id.id_imageview_user_head).setOnClickListener(this);
		v.findViewById(R.id.id_linearlayout_menu_ceju).setOnClickListener(this);
		
		imageview_head = (ImageView) v.findViewById(R.id.id_imageview_user_head);
		textview_name  = (TextView) v.findViewById(R.id.id_textview_user_name);
	}
	
	private void initUser(View v) {
		user = mainApplication.user;
		if (user != null) {
//			user.userHead = ApplicationSharedPreferences.getUserHeadPath(getActivity());
//			user.userHead = GetPhoto.FILE_SAVEPATH + txCrophoto;
			Log.e(TAG, "userHead = " + user.userHead);
			
			if(user.userHead != null && !user.userHead.equals("")) {
				try {
					setUserHead(user.userHead);
				} catch (Exception e){
					e.printStackTrace();
					if(user.sex==0){
						imageview_head.setBackgroundResource(R.drawable.man_normal);
					}else{
						imageview_head.setBackgroundResource(R.drawable.woman_normal);
					}
				}
				
			} else {
				if(user.sex==0){
					imageview_head.setBackgroundResource(R.drawable.man_normal);
				}else{
					imageview_head.setBackgroundResource(R.drawable.woman_normal);
				}
			}
			
			if(user.userName!=null&&user.userName.length()>0){
				textview_name.setText(user.userName);
			}else{
				textview_name.setText(user.userId);
			}
		}
	}
	private void setUserHead(String url){
		imageview_head.setBackgroundColor(Color.TRANSPARENT);
		if (url.contains("http")) {
			Picasso.with(getActivity())
			.load(url)
			.transform(new CircleTransform())
			.into(imageview_head);
		} else {
			Bitmap bitmap = MyActivity.getLoacalBitmap(url); 
			bitmap = MyActivity.toRoundBitmap(bitmap);
			imageview_head.setImageBitmap(bitmap);
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (v == null ) return ;
		textview_ebelt = (TextView) v.findViewById(R.id.id_textview_my_ebelt);
		if (Keeper.getUserHasBindBand(getActivity())) {
			textview_ebelt.setText(R.string.my_ebelt);
		} else {
			textview_ebelt.setText(R.string.search_ebelt);
		}
		initUser(v);
	}


	@Override
	public void onDetach() {
		super.onDetach();
		activity = null;
	}


	@Override
	public void onClick(View v) {
		if (activity == null) {
			return ;
		}
		switch(v.getId()) {
		case R.id.id_linearlayout_menu_long_sit:
			SetupLongSitRemindActivity.gotoActivity(activity);
			break;
		case R.id.id_linearlayout_menu_phone_antilost:
			AntiLostNewActivity.gotoActivity(activity);
			break;
		case R.id.id_linearlayout_menu_my_ebelt:
			if(Keeper.getUserHasBindBand(activity)){
				YaodaiActivity.gotoActivity(activity);
			}else{
				ScanActivity.gotoActivity(activity);
				if(MainActivity.activity!=null){
					MainActivity.activity.finish();
				}
			}
			break;
		case R.id.id_linearlayout_menu_share:
			ShareActivity.gotoActivity(activity);
			break;
		case R.id.id_linearlayout_menu_setup:
			MyActivity.gotoActivity(activity);
			break;
		case R.id.id_imageview_user_head:
			PersonalActivity.gotoActivity(activity);
			break;
		case R.id.id_linearlayout_menu_ceju:
			CejuActivity.gotoActivity(activity);
			break;
		}
	}
	
	
}
