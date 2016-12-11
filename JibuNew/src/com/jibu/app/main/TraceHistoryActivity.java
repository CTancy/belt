package com.jibu.app.main;

import com.jibu.app.R;
import com.jibu.app.fragment.TraceListFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class TraceHistoryActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_fragment);
		FragmentManager fm =  getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		if (fragment == null) {
			fragment = new TraceListFragment();
			
			fm.beginTransaction()
			.add(R.id.fragmentContainer, fragment);
		}
	}
	
	
}
