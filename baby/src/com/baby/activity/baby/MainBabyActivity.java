package com.baby.activity.baby;

import com.baby.CommonApplication;
import com.baby.R;
import com.baby.activity.SettingActivity;
import com.baby.activity.doctor.AcademicAbstractsActivity;
import com.baby.activity.doctor.DoctorHomeActivity;
import com.baby.activity.doctor.DoctorMyActivity;

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
 * 婴儿tabhost界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:13:38
 */
public class MainBabyActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_main_activity);
		
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
        
        View guideTab=(View)LayoutInflater.from(this).inflate(R.layout.tabhost_item, null);
        TextView articleText=(TextView)guideTab.findViewById(R.id.tabhost_textview);
        articleText.setText("育儿指南");
        
        View setTab=(View)LayoutInflater.from(this).inflate(R.layout.tabhost_item, null);
        TextView setText=(TextView)setTab.findViewById(R.id.tabhost_textview);
        setText.setText("设置");
        
        tabHost.addTab(tabHost.newTabSpec("首页").setIndicator(mainTab)
        		.setContent(new Intent(this,SettingActivity.class))); 
        
        tabHost.addTab(tabHost.newTabSpec("我").setIndicator(myTab).setContent(new Intent(this,BabyMyActivity.class)));  
        tabHost.addTab(tabHost.newTabSpec("育儿指南").setIndicator(guideTab).setContent(new Intent(this,FitmentActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("设置").setIndicator(setTab)
        		.setContent(new Intent(this,SettingActivity.class)));
        
        final int tabs = tabWidget.getChildCount();  
        int tabWidth = CommonApplication.getInstance().getScreenWidth()/4;  
        int tabHeight =  (int) (tabWidth*0.6);  
         
        for (int i = 0; i < tabs; i++) {  
         View view = tabWidget.getChildAt(i);
         view.getLayoutParams().width = tabWidth; 
         view.getLayoutParams().height = tabHeight; 
       }
       
		
	}
}
