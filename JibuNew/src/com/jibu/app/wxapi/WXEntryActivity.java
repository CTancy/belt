package com.jibu.app.wxapi;
import android.widget.Toast;

import com.jibu.app.R;
import com.jibu.app.main.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
public class WXEntryActivity extends WXCallbackActivity {

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		super.onResp(resp);
        if (resp.errCode == 0) {
            Toast.makeText(getApplicationContext(), "∑÷œÌ≥…π¶.", Toast.LENGTH_SHORT).show();
            ToastUtil.toast(R.string.share_success);
        } else {
             String eMsg = "";
             if (resp.errCode == -101){
                 eMsg = getString(R.string.without_authorization);
             }
             ToastUtil.toast(getString(R.string.share_failed) + "[" + resp.errCode + "] " + 
                     eMsg);
        }

	}
	
}