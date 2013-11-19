package com.keju.baby.activity.baby;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;

/**
 * 婴儿状态总结
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:30:57
 */
public class BabyStatusActivity extends BaseActivity {
	private Button btnLeft,btnRight;
	private TextView tvTitle;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_status_activity);
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
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("营养摄入状况");
	}

}
