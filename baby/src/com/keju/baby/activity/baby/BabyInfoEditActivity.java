package com.keju.baby.activity.baby;


import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



/**
 * 宝宝资料编辑
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:16:24
 */
public class BabyInfoEditActivity extends BaseActivity implements OnClickListener{
	private Button btnBack;
	private Button btnLeft,btnRight;
	private TextView tvTitle;
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_info_edit_activity);
		
		btnBack=(Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
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
		tvTitle.setText("修改资料");
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
