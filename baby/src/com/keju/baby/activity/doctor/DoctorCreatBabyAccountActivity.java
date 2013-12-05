package com.keju.baby.activity.doctor;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.baby.BabyMainActivity;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.activity.login.BabyLoginActivity;
import com.keju.baby.util.SharedPrefUtil;

/**
 * 创建婴儿账户
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:57:49
 */

public class DoctorCreatBabyAccountActivity extends BaseWebViewActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnLeft.setOnClickListener(this);
		btnRight.setVisibility(View.INVISIBLE);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText("创建婴儿账户");
	}

	private void fillData() {
		loadUrl(Constants.URL_CREATE_BABY);
		webView.addJavascriptInterface(new Object() {
			public void webviewCreateBaby(int code) {
				if(code == 200){
					setResult(RESULT_OK);
					finish();
				}
			}
		}, "app");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;

		default:
			break;
		}
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
