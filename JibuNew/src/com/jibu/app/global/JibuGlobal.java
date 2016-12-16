package com.jibu.app.global;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.jibu.app.R;

public class JibuGlobal {
	public static  String getVersion(Context context) {

		try {

		PackageManager manager = context.getPackageManager();

		PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);

		String version = info.versionName;

		return  version;

		} catch (Exception e) {

			e.printStackTrace();
			return context.getString(R.string.can_not_find_version_name);
		}
	}
}
