package com.keju.baby.activity.doctor;

import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

import com.keju.baby.CommonApplication;
import com.keju.baby.R;
import com.keju.baby.activity.SettingActivity;
import com.keju.baby.activity.baby.BabyDetailActivity;
import com.keju.baby.activity.baby.BabyMyActivity;
import com.keju.baby.activity.baby.FitmentActivity;

/**
 * 医生界面的tabhost
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:02:21
 */
public class DoctorMainActivity extends TabActivity implements OnCheckedChangeListener{
	public static RadioButton rb_home, rb_account, rb_academic, rb_meeting,rb_setting;

	public static TabHost mth;
	private final static String HOME_TAB_ID = "rb_home";//首页
	private final static String ACCOUNT_TAB_ID = "rb_account";// 我的账号
	private final static String ACADEMIC_TAB_ID = "rb_academic";// 学术文摘
	private final static String MEETING_TAB_ID="rb_meeting";//会议通知
	private final static String SETTING_TAB_ID = "rb_setting";// 设置
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_main_activity);
		findView();
		fillData();
		((CommonApplication) getApplication()).addActivity(this);

		LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);  
        mLocalActivityManager.dispatchCreate(savedInstanceState);  
        mth.setup(mLocalActivityManager); 
	}
	
	/**
	 * 查找控件
	 */
	private void findView() {
		mth = getTabHost();

		rb_home = (RadioButton) findViewById(R.id.rb_doctor_home);
		rb_account = (RadioButton) findViewById(R.id.rb_doctor_account);
		rb_academic = (RadioButton) findViewById(R.id.rb_doctor_academic);
		rb_meeting=(RadioButton)findViewById(R.id.rb_doctor_meeting);
		rb_setting = (RadioButton) findViewById(R.id.rb_doctor_setting);

		rb_home.setOnCheckedChangeListener(this);
		rb_account.setOnCheckedChangeListener(this);
		rb_academic.setOnCheckedChangeListener(this);
		rb_meeting.setOnCheckedChangeListener(this);
		rb_setting.setOnCheckedChangeListener(this);

	}
	/**
	 * 数据填充
	 */
	private void fillData() {
		
		TabSpec ts1 = mth.newTabSpec(ACCOUNT_TAB_ID).setIndicator(ACCOUNT_TAB_ID);
		ts1.setContent(new Intent(this, DoctorMyActivity.class));
		mth.addTab(ts1);
		
		TabSpec ts2 = mth.newTabSpec(HOME_TAB_ID).setIndicator(HOME_TAB_ID);
		ts2.setContent(new Intent(this, DoctorHomeActivity.class));
		mth.addTab(ts2);
				
		TabSpec ts3 = mth.newTabSpec(ACADEMIC_TAB_ID).setIndicator(ACADEMIC_TAB_ID);
		ts3.setContent(new Intent(this, AcademicAbstractsActivity.class));
		mth.addTab(ts3);
		
		TabSpec ts4 = mth.newTabSpec(MEETING_TAB_ID).setIndicator(MEETING_TAB_ID);
		ts4.setContent(new Intent(this, MeetingNotifyAcitivity.class));
		mth.addTab(ts4);

		TabSpec ts5 = mth.newTabSpec(SETTING_TAB_ID).setIndicator(SETTING_TAB_ID);
		ts5.setContent(new Intent(this, SettingActivity.class));
		mth.addTab(ts5);
		mth.setCurrentTabByTag(ACCOUNT_TAB_ID);
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.rb_doctor_home:
			if(isChecked){
				mth.setCurrentTabByTag(HOME_TAB_ID);
			}
			break;
		case R.id.rb_doctor_account:
			if(isChecked){
				mth.setCurrentTabByTag(ACCOUNT_TAB_ID);
			}
			break;
		case R.id.rb_doctor_academic:
			if(isChecked){
				mth.setCurrentTabByTag(ACADEMIC_TAB_ID);
			}
			break;
		case R.id.rb_doctor_meeting:
			if(isChecked){
				mth.setCurrentTabByTag(MEETING_TAB_ID);
			}
			break;
		case R.id.rb_doctor_setting:
			if(isChecked){
				mth.setCurrentTabByTag(SETTING_TAB_ID);
			}
			break;
		default:
			break;
		}
	}
}
