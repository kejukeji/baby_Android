package com.keju.baby.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;

/**
 * 设置界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:06:51
 */
public class SettingActivity extends BaseActivity {
	 private long exitTime;
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.setting_activity);
	    }
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
		        if((System.currentTimeMillis()-exitTime) > 2000){  
		            showLongToast("再按一次返回键退出");                             
		            exitTime = System.currentTimeMillis();   
		        } else {
		            finish();
		            System.exit(0);
		        }
		        return true;   
		    }
			return super.onKeyDown(keyCode, event);
		}
}
