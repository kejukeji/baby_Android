package com.keju.baby.activity.login;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.keju.baby.Constants;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.activity.doctor.DoctorMainActivity;
import com.keju.baby.util.SharedPrefUtil;

/**
 * 医生登录界面
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:35:21
 */
public class DoctorLoginActivity extends BaseWebViewActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft.setVisibility(View.INVISIBLE);
		btnRight.setVisibility(View.INVISIBLE);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText("登录");
	}

	private void fillData() {
		loadUrl(Constants.URL_DOCTOR_LOGIN);
		webView.addJavascriptInterface(new Object() {
			public void webviewLogin(int uid,int isRemember) {
				if(uid <= 0){
					return;
				}
				SharedPrefUtil.setUid(DoctorLoginActivity.this, uid);
				if(isRemember == 1){
					SharedPrefUtil.setIsLogin(DoctorLoginActivity.this);
					SharedPrefUtil.setUserType(DoctorLoginActivity.this,Constants.USER_DOCTOR);
				}
				openActivity(DoctorMainActivity.class);
				finish();
			}
		}, "app");
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (webView.canGoBack()) {
				webView.goBack(); // goBack()表示返回webView的上一页面，而不直接关闭WebView
				return true;
			} 
		}

		return super.onKeyDown(keyCode, event);
	}
}
