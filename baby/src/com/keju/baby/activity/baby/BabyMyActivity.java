package com.keju.baby.activity.baby;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.AndroidUtil;
import com.keju.baby.util.NetUtil;

/**
 * 宝宝的资料
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:25:43
 */
public class BabyMyActivity extends BaseActivity implements OnClickListener {
	private Button btnLeft, btnRight;
	private TextView tvTitle;
	private Button btnChangeInfo, btnChangePassword;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_my_activity);

		btnChangeInfo = (Button) findViewById(R.id.btnBabyChangeInform);
		btnChangePassword = (Button) findViewById(R.id.btnBabyChangePw);
		btnChangeInfo.setOnClickListener(this);
		btnChangePassword.setOnClickListener(this);

		findView();
		fillData();
	}

	private void findView() {

		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnRight = (Button) findViewById(R.id.btnRight);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		if (NetUtil.checkNet(this)) {
			new GetBabyInfor().execute();
		} else {
			showShortToast(R.string.NoSignalException);
		}
	}

	/**
	 * 数据填充
	 */
	private void fillData() {

		btnLeft.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("个人中心");
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBabyChangeInform:
			openActivity(BabyInfoEditActivity.class);
			break;
		case R.id.btnBabyChangePw:

			break;
		default:
			break;
		}

	}

	/***
	 * 获得Baby资料
	 */
	private class GetBabyInfor extends AsyncTask<Void, Void, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showPd("正在加载...");
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
//			return new BusinessHelper().;
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
            dismissPd();
		}

	}
}
