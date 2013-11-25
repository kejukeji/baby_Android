package com.keju.baby.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.baby.AddVisitRecordActivity;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.util.AndroidUtil;

public class HomeActivity extends BaseActivity implements OnClickListener{
	private Button btnLeft,btnRight;
	private TextView tvTitle;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		findView();
		fillData();
		btnRight.setOnClickListener(this);
	}
	private void findView() {
		
		btnLeft=(Button)findViewById(R.id.btnLeft);
		btnRight=(Button)findViewById(R.id.btnRight);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
	}
	/**
	 * 数据填充
	 */
	private void fillData() {
		
		btnLeft.setVisibility(View.GONE);
		btnRight.setText("新增随访记");
		tvTitle.setText("龙宝宝");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRight:
			openActivity(AddVisitRecordActivity.class);
			break;

		default:
			break;
		}
		
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
