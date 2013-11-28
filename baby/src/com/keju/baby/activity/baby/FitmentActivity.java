package com.keju.baby.activity.baby;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.util.AndroidUtil;

/**
 * 育儿指南
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:27:27
 */
public class FitmentActivity extends BaseWebViewActivity implements OnClickListener{

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft.setOnClickListener(this);
	}

	/**
	 * 数据填充
	 */
	private void fillData() {
		btnLeft.setVisibility(View.INVISIBLE);
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("育儿指南");
		loadUrl(Constants.URL_FITMENT_LIST);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			if (webView.canGoBack()) {
				webView.goBack();
			}
			break;

		default:
			break;
		}
	}
	private long exitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (webView.canGoBack()) {
				webView.goBack();// goBack()表示返回webView的上一页面，而不直接关闭WebView
				return true;
			}else{
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					showShortToast(R.string.try_again_logout);
					exitTime = System.currentTimeMillis();
				} else {
					AndroidUtil.exitApp(this);
					finish();
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
