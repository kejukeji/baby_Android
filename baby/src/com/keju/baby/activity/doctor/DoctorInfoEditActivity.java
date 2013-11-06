package com.keju.baby.activity.doctor;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;

/**
 * 医生信息编辑界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:56:27
 */
public class DoctorInfoEditActivity extends BaseActivity implements OnClickListener{
	private Button btnBack;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_info_edit_activity);
		
		btnBack=(Button)findViewById(R.id.btnBack);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			openActivity(DoctorMyActivity.class);
			break;

		default:
			break;
		}
		
	}
}
