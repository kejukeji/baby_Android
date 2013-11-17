package com.keju.baby.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;

/**
 * 选择配方奶（妈妈进入的时候，不可以新增配方奶）
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:34:41
 */
public class ChooseMilkActivity extends BaseActivity implements OnClickListener{
	private Button btnLeft,btnRight,btnAddMilk,btnCheck;
	private TextView tvTitle;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_milk_activity);
        findView();
		fillData();
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		btnAddMilk.setOnClickListener(this);
		btnCheck.setOnClickListener(this);
	}
	private void findView() {
		
		btnLeft=(Button)findViewById(R.id.btnLeft);
		btnRight=(Button)findViewById(R.id.btnRight);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
		btnAddMilk=(Button)findViewById(R.id.btnAddMilk);
		btnCheck=(Button)findViewById(R.id.btnMilkCheck);
	}
	/**
	 * 数据填充
	 */
	private void fillData() {
		
		btnLeft.setText("Back");
		btnRight.setText("确定");
		tvTitle.setText("配方奶");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			this.finish();
			break;
		case R.id.btnRight:
			//this.finish();
			break;
		case R.id.btnAddMilk:
			openActivity(AddMilkActivity.class);
			break;
		case R.id.btnMilkCheck:
			
			break;

		default:
			break;
		}
		
	}
}
