package com.keju.baby.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.util.AndroidUtil;
import com.keju.baby.util.NetUtil;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

/**
 * 设置界面
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:06:51
 */
public class SettingActivity extends BaseActivity implements OnClickListener{
	private long exitTime;
	private Button btnLeft, btnRight,btnLogout;
	private TextView tvTitle;
	
	private View viewGrade, viewRecommend,viewVersion, viewAbout,viewChangePassword;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnLogout=(Button)findViewById(R.id.btnLogout);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		viewGrade=findViewById(R.id.viewGrade);
		viewRecommend=findViewById(R.id.viewRecommend);
		viewVersion=findViewById(R.id.viewVersion);
		viewAbout=findViewById(R.id.viewAbout);
		viewChangePassword=findViewById(R.id.viewChangePassword);
		
		viewGrade.setOnClickListener(this);
		viewRecommend.setOnClickListener(this);
		viewVersion.setOnClickListener(this);
		viewAbout.setOnClickListener(this);
		viewChangePassword.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
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
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				showShortToast(R.string.try_again_logout);
				exitTime = System.currentTimeMillis();
			} else {
				AndroidUtil.exitApp(this);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.viewGrade:
			AndroidUtil.lanuchMarket(this);
			break;
		case R.id.viewRecommend:
			
			break;
		case R.id.viewVersion:
			if(NetUtil.checkNet(this)){
				UmengUpdateAgent.update(this);
				UmengUpdateAgent.setUpdateOnlyWifi(false);
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				        @Override
				        public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
				            switch (updateStatus) {
				            case 0: // has update
				                UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
				                break;
				            case 1: // has no update
				                showShortToast("已经是最新版本");
				                break;
				            case 2: // none wifi
				            	showShortToast("没有wifi连接， 只在wifi下更新");
				                break;
				            case 3: // time out
				            	showShortToast("连接服务器超时");
				                break;
				            }
				        }
				});
			}else{
				showShortToast(R.string.NoSignalException);
			}
			break;	
		case R.id.viewAbout:
			
			break;
		case R.id.viewChangePassword:
			openActivity(ChangePasswordActivity.class);
			break;
		case R.id.btnLogout://二次提示，跳转到登录界面
			
			break;
		default:
			break;
		}
		
	}
}
