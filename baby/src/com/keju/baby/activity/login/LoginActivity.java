package com.keju.baby.activity.login;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.baby.BabyMainActivity;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.activity.doctor.DoctorMainActivity;

/**
 * 登录切换界面
 * 
 * @author Zhoujun
 * @version 创建时间�?013-10-25 下午2:33:05
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private Button btnLeft, btnRight;
	private TextView tvTitle;
	private Button btnDoctor, btnBaby;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setVisibility(View.INVISIBLE);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		btnDoctor = (Button) findViewById(R.id.btnDoctor);
		btnBaby = (Button) findViewById(R.id.btnBaby);
		btnDoctor.setOnClickListener(this);
		btnBaby.setOnClickListener(this);
	}

	private void fillData() {
		tvTitle.setText("选择入口");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDoctor:
//			openActivity(DoctorMainActivity.class);
			openActivity(DoctorLoginActivity.class);
			finish();
			break;
		case R.id.btnBaby:
			openActivity(BabyMainActivity.class);
			finish();
			break;

		default:
			break;
		}

	}
}
