package com.keju.baby.activity.doctor;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;

/**
 * 搜索婴儿界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:58:32
 */
public class SearchActivity extends BaseActivity implements OnClickListener{
	private Button btnLeft,btnRight,btnSearch,btnClear;
	private TextView tvTitle;
protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		findView();
		fillData();		
        btnLeft.setOnClickListener(this);
	}
	private void findView() {
		
		btnLeft=(Button)findViewById(R.id.btnLeft);
		btnRight=(Button)findViewById(R.id.btnRight);
		btnSearch=(Button)findViewById(R.id.btnSearch);
		btnClear=(Button)findViewById(R.id.btnClear);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
	}
	/**
	 * 数据填充
	 */
	private void fillData() {
		
		btnLeft.setText("Back");
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("搜索");
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
