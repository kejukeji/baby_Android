package com.keju.baby.activity.doctor;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;

/**
 * 创建婴儿账户
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:57:49
 */
public abstract class DoctorCreatBabyAccountActivity extends BaseActivity implements OnClickListener{
	private Button btnLeft,btnRight;
	private TextView tvTitle;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_creat_baby_account);
		findView();
		fillData();
		btnRight.setOnClickListener(this);
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
		btnRight.setText("提交");
		tvTitle.setText("创建婴儿账户");
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRight:
			this.finish();
			break;
		case R.id.btnLeft:
			this.finish();
			break;
		default:
			break;
		}
		
	}
}
