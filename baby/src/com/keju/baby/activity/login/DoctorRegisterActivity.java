package com.keju.baby.activity.login;

import android.os.Bundle;
import android.view.View;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.activity.doctor.DoctorMainActivity;
import com.keju.baby.util.SharedPrefUtil;

/**
 * 医生注册
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:36:52
 */
public class DoctorRegisterActivity extends BaseWebViewActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(webView.canGoBack()){
					webView.goBack();
				}
			}
		});
		btnLeft.setVisibility(View.VISIBLE);
		btnRight.setVisibility(View.INVISIBLE);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText("注册");
	}

	private void fillData() {
		loadUrl(Constants.URL_DOCTOR_LOGIN);
		webView.addJavascriptInterface(new Object() {
			public void webviewRegister(String code,int uid) {
				if(!code.equals("200")){
					showShortToast("注册失败");
					return;
				}
				SharedPrefUtil.setUid(DoctorRegisterActivity.this, uid);
				SharedPrefUtil.setUserType(DoctorRegisterActivity.this,Constants.USER_DOCTOR);
				SharedPrefUtil.setIsLogin(DoctorRegisterActivity.this);
				openActivity(DoctorMainActivity.class);
				finish();
			}
		}, "app");
		
	}
}
