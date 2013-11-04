package com.baby.activity.baby;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baby.R;
import com.baby.activity.base.BaseActivity;

/**
 * 宝宝资料编辑
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:16:24
 */
public class BabyInfoEditActivity extends BaseActivity implements OnClickListener{
	private Button btnBack;
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_info_edit_activity);
		
		btnBack=(Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			openActivity(BabyMyActivity.class);
			break;

		default:
			break;
		}
		
	}
}
