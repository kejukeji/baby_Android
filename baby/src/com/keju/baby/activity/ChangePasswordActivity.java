package com.keju.baby.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.base.BaseWebViewActivity;

/**
 * 修改密码界面(通过参数判断是修改医生的密码还是婴儿的密码)
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:55:04
 */
public class ChangePasswordActivity extends BaseWebViewActivity {
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
		tvTitle.setText("修改密码");
		loadUrl(Constants.URL_CHANGE_PASSWORD);
	}
}
