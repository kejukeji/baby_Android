package com.keju.baby.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.R.id;
import com.keju.baby.R.layout;
import com.keju.baby.activity.base.BaseActivity;

public class AddMilkActivity extends BaseActivity implements OnClickListener{
	private Button btnLeft,btnRight;
	private TextView tvTitle;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_milk_activity);
        findView();
		fillData();
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
		
			break;

		default:
			break;
		}
		
	}
}
