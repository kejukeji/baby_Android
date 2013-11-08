package com.keju.baby.activity.doctor;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;

/**
 * 医生信息编辑界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:56:27
 */
public class DoctorInfoEditActivity extends BaseActivity implements OnClickListener{

	private Button btnLeft,btnRight;
	private TextView tvTitle;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_info_edit_activity);
		
		findView();
		fillData();
		btnLeft.setOnClickListener(this);
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
		
		btnLeft.setText("Back");
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("修改资料");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			this.finish();
			break;

		default:
			break;
		}
		
	}
}
