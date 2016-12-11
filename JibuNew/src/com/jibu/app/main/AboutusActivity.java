package com.jibu.app.main;

/**
 * @author ctc
 * 
 * πÿ”⁄“≥√Ê
 * 
 */

import com.jibu.app.R;
import com.umeng.analytics.MobclickAgent;
import com.veclink.hw.bleservice.util.Keeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class AboutusActivity extends Activity implements OnClickListener{
	private final String TAG = "AboutusActivity";
	
	TextView textView_fireware_version;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about_us);
		findViewById(R.id.id_revert).setOnClickListener(this);
		findViewById(R.id.id_rl_ebelt_website).setOnClickListener(this);
		findViewById(R.id.id_rl_to_score).setOnClickListener(this);
		textView_fireware_version = (TextView) findViewById(R.id.id_textview_fireware_version);
		((TextView) findViewById(R.id.id_textview_app_version)).setText(getVersion());
		
		if (Keeper.getUserHasBindBand(this)) {
			String DeviceRomVersion = Keeper.getDeviceRomVersion(this);
			if ( null != DeviceRomVersion  && !DeviceRomVersion.equals("")) {
				textView_fireware_version.setText("D013A_V" + DeviceRomVersion);
			}
		} 
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_revert:
		{
			Log.e("TAG", "Click");
			this.finish();
			break;
		}
		case R.id.id_rl_ebelt_website:
		{
			final Uri uri = Uri.parse("http://www.ebelt.cn/");          
			final Intent it = new Intent(Intent.ACTION_VIEW, uri);          
			startActivity(it);
			break;
		}
		case R.id.id_rl_to_score:
			final Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.jibu.app");          
			final Intent it = new Intent(Intent.ACTION_VIEW, uri);          
			startActivity(it);
			break;
		}
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
	public static void gotoActivity(Context context)
	{
		Intent intent = new Intent();
		intent.setClass(context, AboutusActivity.class);
		context.startActivity(intent);
	}
	

}
