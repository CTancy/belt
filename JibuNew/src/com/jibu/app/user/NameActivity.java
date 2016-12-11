package com.jibu.app.user;

import com.jibu.app.R;
import com.jibu.app.database.UserService;
import com.jibu.app.entity.User;
import com.jibu.app.main.MainApplication;
import com.jibu.app.main.PersonalActivity;
import com.jibu.app.main.ToastUtil;
import com.jibu.app.main.WelcomeActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;

public class NameActivity extends Activity implements OnClickListener{
	
	
	
	private EditText editTextName;
	
	MainApplication mainApplication;
	
	UserService userService;
	User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_name);
		
		mainApplication = (MainApplication) this.getApplication();
		
		if (null == mainApplication.user) {
			WelcomeActivity.gotoActivity(this);
			this.finish();
		} else {
			user = mainApplication.user;
			userService = new UserService(this);
			initView();
		}
	}

	private void initView() {
		editTextName = (EditText) findViewById(R.id.id_edittext_name);
		if (null != user.userName && !"".equals(user.userName)) {
			editTextName.setText(user.userName);
		} else {
			editTextName.setText(user.userId);
		}
		
		
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		findViewById(R.id.id_linearlayout_title_right).setOnClickListener(this);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.id_linearlayout_title_left:
//			setResult(Activity.RESULT_CANCELED);
			this.finish();
			break;
		case R.id.id_linearlayout_title_right:
			fixName();
			break;
		}
	}
	
	private void fixName() {
		String name = editTextName.getText().toString();
		if (nameIsValid(name)) {
			user.userName = name;
			if (userService.updateUser(user) != -1) {
//				setResult(Activity.RESULT_OK);
				this.finish();
			}

		} else {
			ToastUtil.toast(R.string.invalid_name);
		}
	}
	
	private boolean nameIsValid(String name) {
		if(null == name || name.length() > 20 || name.length() < 1) {
			return false;
		}
		return true;
	}
	
	public static void gotoActivity(Context context) {
		Intent  intent = new Intent(context, NameActivity.class);
		context.startActivity(intent);
	}
}
