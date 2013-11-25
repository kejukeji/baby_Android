package com.keju.baby.activity.baby;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
public class FitmentActivity extends BaseWebViewActivity {

	private Button btnLeft, btnRight;
	private TextView tvTitle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnRight = (Button) findViewById(R.id.btnRight);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
	}

	/**
	 * 数据填充
	 */
	private void fillData() {

		btnLeft.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("育儿指南");
		loadUrl(Constants.URL_FITMENT);
	}

	private long exitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				showShortToast(R.string.try_again_logout);
				exitTime = System.currentTimeMillis();
			} else {
				AndroidUtil.exitApp(this);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
