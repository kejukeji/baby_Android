package com.keju.baby.activity.doctor;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.bean.AcademicAbstractBean;

/**
 * 学术文摘详情
 * @author Zhoujun
 * @version 创建时间：2013-12-13 下午2:22:48
 */
public class AbstractDetailActivity extends BaseActivity {
	private ImageView btnLeft, btnRight;
	private TextView tvTitle;
	
	private TextView tvContentTitle, tvContent;
	private AcademicAbstractBean bean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent().getExtras() != null){
			bean = (AcademicAbstractBean) getIntent().getExtras().get(Constants.EXTRA_DATA);
		}
		setContentView(R.layout.abstract_detail);
		findView();
		fillData();
	}
	private void findView(){
		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvContentTitle = (TextView) findViewById(R.id.tvContentTitle);
		tvContent = (TextView) findViewById(R.id.tvContent);
	}
	private void fillData(){
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnRight.setVisibility(View.INVISIBLE);
		tvTitle.setText("学术文摘详情");
		btnLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		if(bean != null){
			tvContentTitle.setText(bean.getTitle());
			tvContent.setText(bean.getContent());
		}
	}
}
