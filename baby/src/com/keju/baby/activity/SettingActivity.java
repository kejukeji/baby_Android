package com.keju.baby.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;

/**
 * 设置界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:06:51
 */
public class SettingActivity extends BaseActivity {
	 private long exitTime;
	 private Button btnLeft,btnRight;
	 private TextView tvTitle;
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.setting_activity);
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
			
			btnLeft.setVisibility(View.GONE);
			btnRight.setVisibility(View.GONE);
			tvTitle.setText("设置");
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
