package com.keju.baby.activity.baby;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.ChooseMilkActivity;
import com.keju.baby.activity.base.BaseActivity;

public class AddVisitRecordActivity extends BaseActivity implements OnClickListener{
	private Button btnLeft,btnRight,btnChange;
	private TextView tvTitle;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_visit_record);
		findView();
		fillData();
		btnRight.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
		btnChange.setOnClickListener(this);
	}
	private void findView() {
		
		btnLeft=(Button)findViewById(R.id.btnLeft);
		btnRight=(Button)findViewById(R.id.btnRight);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
		btnChange=(Button)findViewById(R.id.btnAddVisitMilk);
	}
	/**
	 * 数据填充
	 */
	private void fillData() {
		
		btnLeft.setText("Back");
		btnRight.setText("提交");
		tvTitle.setText("新增随访记录");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			this.finish();
			break;
		case R.id.btnAddVisitMilk:
			openActivity(ChooseMilkActivity.class);
			break;
		default:
			break;
		}
		
	}

}
