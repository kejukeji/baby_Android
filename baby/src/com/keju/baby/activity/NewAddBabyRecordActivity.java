package com.keju.baby.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.bean.BabyBean;

/**
 * 新增宝宝记录(妈妈新增的时候，不可以新增配方奶，传boolean判断)
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:32:11
 */
public class NewAddBabyRecordActivity extends BaseWebViewActivity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft.setVisibility(View.VISIBLE);
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnLeft.setOnClickListener(this);
		btnRight.setVisibility(View.INVISIBLE);
		tvTitle.setVisibility(View.VISIBLE);
		webView = (WebView) findViewById(R.id.webview);
	}

	private void fillData() {
		loadUrl(Constants.URL_ADD_FOLLOW_UP);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			if(webView.getUrl().contains(Constants.URL_FORMULA)){
				webView.goBack();
			}else{
				finish();
			}
			break;

		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if(webView.canGoBack()){
				webView.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
