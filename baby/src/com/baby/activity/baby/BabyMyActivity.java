package com.baby.activity.baby;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baby.R;
import com.baby.activity.base.BaseActivity;

/**
 * 宝宝的资料
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:25:43
 */
public class BabyMyActivity extends BaseActivity implements OnClickListener{
	private Button btnChangePassword,btnChangeInfo;
	
protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_my_activity);
		
		btnChangePassword=(Button)findViewById(R.id.btnBabyChangePw);
		btnChangeInfo=(Button)findViewById(R.id.btnBabyChangeInform);
		btnChangePassword.setOnClickListener(this);
		btnChangeInfo.setOnClickListener(this);
		}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBabyChangePw:
			
			
			break;
		case R.id.btnBabyChangeInform:
			openActivity(BabyInfoEditActivity.class);			
			break;
		default:
			break;
		}
	
	}

}
