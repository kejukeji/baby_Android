package com.keju.baby.activity.login;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.activity.doctor.DoctorMainActivity;
import com.keju.baby.util.AndroidUtil;
import com.keju.baby.util.SharedPrefUtil;

/**
 * 医生登录界面
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:35:21
 */
public class DoctorLoginActivity extends BaseWebViewActivity {
	private Button btnLeft, btnRight;
	private TextView tvTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft = (Button) this.findViewById(R.id.btnLeft);
		btnLeft.setVisibility(View.INVISIBLE);
		btnRight = (Button) this.findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText("登录");
		webView = (WebView) findViewById(R.id.webview);
	}

	private void fillData() {
		loadUrl(Constants.URL_DOCTOR_LOGIN);
		webView.addJavascriptInterface(new Object() {
			public void webviewLogin(int uid,int isRemember) {
				if(uid <= 0){
					return;
				}
				if(isRemember == 1){
					SharedPrefUtil.setUid(DoctorLoginActivity.this, uid);
					SharedPrefUtil.setUserType(DoctorLoginActivity.this,Constants.USER_DOCTOR);
				}
				openActivity(DoctorMainActivity.class);
				finish();
			}
		}, "app");
		
	}
	private long exitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack(); // goBack()表示返回webView的上一页面，而不直接关闭WebView
				return true;
			} else {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					showShortToast(R.string.try_again_logout);
					exitTime = System.currentTimeMillis();
				} else {
					AndroidUtil.exitApp(this);
					finish();
				}
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}
}
