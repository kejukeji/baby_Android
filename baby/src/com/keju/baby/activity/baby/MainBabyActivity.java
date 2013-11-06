package com.keju.baby.activity.baby;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.keju.baby.CommonApplication;
import com.keju.baby.R;
import com.keju.baby.activity.SettingActivity;

/**
 * 婴儿tabhost界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:13:38
 */
public class MainBabyActivity extends TabActivity implements OnCheckedChangeListener,OnClickListener {
	public static RadioButton rb_home, rb_account, rb_fitment, rb_setting;

	public static TabHost mth;
	private final static String HOME_TAB_ID = "rb_home";//首页
	private final static String ACCOUNT_TAB_ID = "rb_account";// 我的账号
	private final static String FITMENT_TAB_ID = "rb_fitment";// 育儿指南
	private final static String SETTING_TAB_ID = "rb_setting";// 设置
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_main_activity);
		findView();
		fillData();
		((CommonApplication) getApplication()).addActivity(this);
		
	}
	
	/**
	 * 查找控件
	 */
	private void findView() {
		mth = getTabHost();

		rb_home = (RadioButton) findViewById(R.id.rb_home);
		rb_account = (RadioButton) findViewById(R.id.rb_account);
		rb_fitment = (RadioButton) findViewById(R.id.rb_fitment);
		rb_setting = (RadioButton) findViewById(R.id.rb_setting);

		rb_home.setOnCheckedChangeListener(this);
		rb_account.setOnCheckedChangeListener(this);
		rb_fitment.setOnCheckedChangeListener(this);
		rb_setting.setOnCheckedChangeListener(this);

	}
	/**
	 * 数据填充
	 */
	private void fillData() {
		
		TabSpec ts1 = mth.newTabSpec(HOME_TAB_ID).setIndicator(HOME_TAB_ID);
		ts1.setContent(new Intent(this, BabyDetailActivity.class));
		mth.addTab(ts1);
		
		TabSpec ts2 = mth.newTabSpec(ACCOUNT_TAB_ID).setIndicator(ACCOUNT_TAB_ID);
		ts2.setContent(new Intent(this, BabyMyActivity.class));
		mth.addTab(ts2);
		
		TabSpec ts3 = mth.newTabSpec(FITMENT_TAB_ID).setIndicator(FITMENT_TAB_ID);
		ts3.setContent(new Intent(this, FitmentActivity.class));
		mth.addTab(ts3);

		TabSpec ts4 = mth.newTabSpec(SETTING_TAB_ID).setIndicator(SETTING_TAB_ID);
		ts4.setContent(new Intent(this, SettingActivity.class));
		mth.addTab(ts4);
		mth.setCurrentTabByTag(HOME_TAB_ID);
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
