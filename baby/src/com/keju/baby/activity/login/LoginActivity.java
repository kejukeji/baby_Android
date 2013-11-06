package com.keju.baby.activity.login;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.keju.baby.R;
import com.keju.baby.activity.baby.MainBabyActivity;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.activity.doctor.DoctorMainActivity;

/**
 * 登录切换界面
 * @author Zhoujun
 * @version 创建时间�?013-10-25 下午2:33:05
 */
public class LoginActivity extends BaseActivity implements OnClickListener{
	private Button btnDoctor,btnBaby;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		
		btnDoctor=(Button)findViewById(R.id.btnDoctor);
		btnBaby=(Button)findViewById(R.id.btnBaby);
		btnDoctor.setOnClickListener(this);
		btnBaby.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDoctor:
			openActivity(DoctorMainActivity.class);
			this.finish();
			break;
		case R.id.btnBaby:
			openActivity(MainBabyActivity.class);	
			this.finish();
			break;

		default:
			break;
		}
		
	}
}
