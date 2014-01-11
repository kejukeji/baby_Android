package com.keju.baby.activity;

import android.os.Bundle;
import android.view.View;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.activity.login.BabyLoginActivity;
import com.keju.baby.activity.login.DoctorLoginActivity;
import com.keju.baby.util.SharedPrefUtil;

/**
 * 修改密码界面(通过参数判断是修改医生的密码还是婴儿的密码)
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:55:04
 */
public class ChangePasswordActivity extends BaseWebViewActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fillData();
	}

	/**
	 * 数据填充
	 */
	private void fillData() {

		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("修改密码");
		loadUrl(Constants.URL_CHANGE_PASSWORD);
		webView.addJavascriptInterface(new Object() {
			public void webviewPassword(int code) {
				if(code == 200){
					if(SharedPrefUtil.getUserType(ChangePasswordActivity.this) == Constants.USER_DOCTOR){
						openActivity(DoctorLoginActivity.class);
					}else{
						openActivity(BabyLoginActivity.class);
					}
					finish();
				}
			}
		}, "app");
	}
}
