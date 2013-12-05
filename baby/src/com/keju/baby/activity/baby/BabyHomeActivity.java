package com.keju.baby.activity.baby;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.NewAddBabyRecordActivity;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.util.AndroidUtil;
import com.keju.baby.util.SharedPrefUtil;

public class BabyHomeActivity extends BaseWebViewActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft.setVisibility(View.INVISIBLE);
//		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnRight.setImageResource(R.drawable.btn_add_record_selector);
		btnRight.setOnClickListener(this);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText("");
	}

	private void fillData() {
		loadUrl(Constants.URL_VISIT_RECORD + SharedPrefUtil.getUid(this) + "?entrance_type=baby");
		tvTitle.setText(SharedPrefUtil.getName(this));
	}
	@Override
	protected void onResume() {
		super.onResume();
		webView.reload();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRight:
			Bundle b = new Bundle();
			b.putInt(Constants.EXTRA_DATA, SharedPrefUtil.getUid(this));
			Intent intent = new Intent(this, NewAddBabyRecordActivity.class);
			intent.putExtras(b);
			startActivityForResult(intent, Constants.REQUEST_NEW_ADD_VISIT_CODE);
			break;

		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == Constants.REQUEST_NEW_ADD_VISIT_CODE){
			webView.reload();
		}
	}
	private long exitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if(webView.getUrl().contains(Constants.URL_NEED)){
				if(webView.canGoBack()){
					webView.goBack();
					return true;
				}
			}else{
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
