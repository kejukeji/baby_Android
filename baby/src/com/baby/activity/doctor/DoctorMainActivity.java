package com.baby.activity.doctor;

import com.baby.CommonApplication;
import com.baby.R;
import com.baby.activity.SettingActivity;

import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * 医生界面的tabhost
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:02:21
 */
public class DoctorMainActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_main);
		
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);  
		
		
        TabWidget tabWidget = tabHost.getTabWidget();  
        
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);  
        mLocalActivityManager.dispatchCreate(savedInstanceState);  
        tabHost.setup(mLocalActivityManager);  

        View mainTab=(View)LayoutInflater.from(this).inflate(R.layout.tabhost_item, null);
        TextView mainText=(TextView)mainTab.findViewById(R.id.tabhost_textview);
        mainText.setText("首页");
        
        View myTab=(View)LayoutInflater.from(this).inflate(R.layout.tabhost_item, null);
        TextView MyText=(TextView)myTab.findViewById(R.id.tabhost_textview);
        MyText.setText("我");
        
        View articleTab=(View)LayoutInflater.from(this).inflate(R.layout.tabhost_item, null);
        TextView articleText=(TextView)articleTab.findViewById(R.id.tabhost_textview);
        articleText.setText("文摘");
        
        View meetingTab=(View)LayoutInflater.from(this).inflate(R.layout.tabhost_item, null);
        TextView meetingText=(TextView)meetingTab.findViewById(R.id.tabhost_textview);
        meetingText.setText("会议");
        
        View setTab=(View)LayoutInflater.from(this).inflate(R.layout.tabhost_item, null);
        TextView setText=(TextView)setTab.findViewById(R.id.tabhost_textview);
        setText.setText("设置");
        
        tabHost.addTab(tabHost.newTabSpec("首页").setIndicator(mainTab)
        		.setContent(new Intent(this,DoctorHomeActivity.class))); 
        
        tabHost.addTab(tabHost.newTabSpec("我").setIndicator(myTab).setContent(new Intent(this,DoctorMyActivity.class)));  
        tabHost.addTab(tabHost.newTabSpec("学术文摘").setIndicator(articleTab).setContent(new Intent(this,AcademicAbstractsActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("会议通知").setIndicator(meetingTab).setContent(new Intent(this,MeetingNotifyAcitivity.class)));
        tabHost.addTab(tabHost.newTabSpec("设置").setIndicator(setTab)
        		.setContent(new Intent(this,SettingActivity.class)));
        
        final int tabs = tabWidget.getChildCount();  
        int tabWidth = CommonApplication.getInstance().getScreenWidth()/5;  
        int tabHeight =  (int) (tabWidth*0.75);  
         
        for (int i = 0; i < tabs; i++) {  
         View view = tabWidget.getChildAt(i);
         view.getLayoutParams().width = tabWidth; 
         view.getLayoutParams().height = tabHeight; 
       }
      
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
	}
}
