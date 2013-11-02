package com.baby.activity.doctor;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baby.R;
import com.baby.activity.base.BaseActivity;

/**
 * 医生资料界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:53:44
 */
public class DoctorMyActivity extends BaseActivity implements OnClickListener{
	private Button btnChangeInform,btnChangePassword;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_my_activity);
		
		btnChangeInform=(Button)findViewById(R.id.btnDoctor_my_change_inform);
		btnChangePassword=(Button)findViewById(R.id.btnDoctor_my_change_pw);
		btnChangeInform.setOnClickListener(this);
		btnChangePassword.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDoctor_my_change_inform:
			openActivity(DoctorInfoEditActivity.class);
			break;

		default:
			break;
		}
		
	}
}
