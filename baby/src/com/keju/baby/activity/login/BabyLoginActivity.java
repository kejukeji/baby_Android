package com.keju.baby.activity.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.SystemException;
import com.keju.baby.activity.baby.BabyMainActivity;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.NetUtil;
import com.keju.baby.util.SharedPrefUtil;

/**
 * 婴儿登录
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:35:48
 */
public class BabyLoginActivity extends BaseWebViewActivity implements OnClickListener {
	private EditText edBabyUserName, edBabyPassword;
	private CheckBox cbIsRememberPassWord;
	private Button btnBabyLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_login_activity);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft.setVisibility(View.INVISIBLE);
		btnRight.setVisibility(View.INVISIBLE);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText("登录");

		edBabyUserName = (EditText) this.findViewById(R.id.etBabyLogin_username);
		edBabyPassword = (EditText) this.findViewById(R.id.etBabyLogin_password);
		cbIsRememberPassWord = (CheckBox) this.findViewById(R.id.cbIsRememberPassWord);
		btnBabyLogin = (Button) this.findViewById(R.id.btnBabyLogin);
	}

	private void fillData() {
		btnBabyLogin.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBabyLogin:
			String babyUserName = edBabyUserName.getText().toString();
			if (TextUtils.isEmpty(babyUserName)) {
				showShortToast("用户名不能为空");
				return;
			}
			String babyPassWord = edBabyPassword.getText().toString();
			if (TextUtils.isEmpty(babyPassWord)) {
				showShortToast("密码不能为空");
				return;
			}
			
			if (NetUtil.checkNet(BabyLoginActivity.this)) {
				new LoginTask(babyUserName,babyPassWord).execute();
			} else {
				if(cbIsRememberPassWord.isChecked()){
					SharedPrefUtil.setIsRemember(BabyLoginActivity.this, 1);
					SharedPrefUtil.setUserType(BabyLoginActivity.this, 2);
				}else{
					SharedPrefUtil.setIsRemember(BabyLoginActivity.this, 2);
				}
				String babyLoginName = SharedPrefUtil.getBabyName(BabyLoginActivity.this);
				String babyLoginPassWord = SharedPrefUtil.getBabyPassword(BabyLoginActivity.this);
				if(babyUserName.endsWith(babyLoginName)&&babyPassWord.equals(babyLoginPassWord)){
					showShortToast("登陆成功");
					openActivity(BabyMainActivity.class);
				}else{
					showShortToast("用户名和密码错误,检查后重新输入");
				}
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 登陆
	 * 
	 * @author Zhouyong
	 */
	public class LoginTask extends AsyncTask<Void, Void, JSONObject> {
		private  String babyUserName;
		private  String babyPassWord;
		/**
		 * @param babyUserName
		 * @param babyPassWord
		 */
		public LoginTask(String babyUserName, String babyPassWord) {
			this.babyUserName = babyUserName;
			this.babyPassWord = babyPassWord;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showPd("正在登录");
		}

		@Override
		protected JSONObject doInBackground(Void... arg0) {
			try {
				return new BusinessHelper().login(babyUserName,babyPassWord,"0","mummy");
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			dismissPd();
			if(result!=null){
				try {
					int status = result.getInt("code");
					JSONObject userJson = null;
					if (status == Constants.REQUEST_SUCCESS &&!result.getString("message").equals("登陆失败")){
						if(result.has("doctor_list")){
							 userJson = result.getJSONObject("doctor_list");
						}else{
							showShortToast(result.getString("message"));
						}
						showShortToast("登陆成功");
						int uid = userJson.getInt("user_id");
						SharedPrefUtil.setUid(BabyLoginActivity.this, uid);
						SharedPrefUtil.setUserType(BabyLoginActivity.this, Constants.USER_MOTHER);
						SharedPrefUtil.setBabyName(BabyLoginActivity.this, userJson.getString("patriarch_tel"));
						SharedPrefUtil.setBabyPassword(BabyLoginActivity.this, userJson.getString("baby_pass"));
						if(cbIsRememberPassWord.isChecked()){
							SharedPrefUtil.setIsRemember(BabyLoginActivity.this, 1);
							setResult(RESULT_OK);	
						}else{
							SharedPrefUtil.setIsRemember(BabyLoginActivity.this, 2);
						}
						openActivity(BabyMainActivity.class);
						finish();
					}else{
						showShortToast("用户名或密码错误");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}else{
				showShortToast(R.string.connect_server_exception);
			}
			
		}
	}
}
