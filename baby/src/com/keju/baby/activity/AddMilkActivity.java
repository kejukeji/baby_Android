package com.keju.baby.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.base.BaseWebViewActivity;

public class AddMilkActivity extends BaseWebViewActivity implements OnClickListener {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findView();
		fillData();
	}

	private void findView() {

	}

	/**
	 * 数据填充
	 */
	private void fillData() {

		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnLeft.setOnClickListener(this);
		btnRight.setVisibility(View.INVISIBLE);
		tvTitle.setText("配方奶");

		loadUrl(Constants.URL_FORMULA);
		webView.addJavascriptInterface(new Object() {
			public void webviewFormula(int code) {
				if (code == 200) {
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
		case R.id.btnRight:

			break;

		default:
			break;
		}

	}
}
