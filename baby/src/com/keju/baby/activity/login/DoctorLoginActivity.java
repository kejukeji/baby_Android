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
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.activity.doctor.DoctorHomeActivity;
import com.keju.baby.activity.doctor.DoctorMainActivity;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.NetUtil;
import com.keju.baby.util.SharedPrefUtil;

/**
 * 医生登录界面
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:35:21
 */
public class DoctorLoginActivity extends BaseWebViewActivity implements OnClickListener {
	private EditText edDoctorUserName;
	private EditText edDoctorPassWord;
	private CheckBox cbIsRememberPassWord;
	private Button btnDoctorLogin, btnRegist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_login_activity);
		findView();
		fillData();
	}

	private void findView() {
		edDoctorUserName = (EditText) findViewById(R.id.etDocterLogin_username);
		edDoctorPassWord = (EditText) findViewById(R.id.etDocterLogin_password);
		cbIsRememberPassWord = (CheckBox) findViewById(R.id.cbIsRememberPassWord);
		btnDoctorLogin = (Button) findViewById(R.id.btnDoctorLogin_login);
		btnRegist = (Button) findViewById(R.id.btnDoctorLogin_register);
	}

	private void fillData() {
		btnDoctorLogin.setOnClickListener(this);
		btnRegist.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDoctorLogin_login:
			String doctorUserName = edDoctorUserName.getText().toString();
			if (TextUtils.isEmpty(doctorUserName)) {
				showShortToast("用户名不能为空");
				return;
			}
			String doctorPassWord = edDoctorPassWord.getText().toString();
			if (TextUtils.isEmpty(doctorPassWord)) {
				showShortToast("密码不能为空");
				return;
			}
			if (NetUtil.checkNet(DoctorLoginActivity.this)) {
				new LoginTask(doctorUserName, doctorPassWord).execute();
			} else {
				if (cbIsRememberPassWord.isChecked()) {
					SharedPrefUtil.setIsRemember(DoctorLoginActivity.this, 1);
					SharedPrefUtil.setUserType(DoctorLoginActivity.this, 1);
				} else {
					SharedPrefUtil.setIsRemember(DoctorLoginActivity.this, 2);
				}
				String doctorLoginName = SharedPrefUtil.getDoctorName(DoctorLoginActivity.this);
				String doctorLoginPassWord = SharedPrefUtil.getDoctorPassword(DoctorLoginActivity.this);
				if (doctorUserName.endsWith(doctorLoginName) && doctorPassWord.equals(doctorLoginPassWord)) {
					showShortToast("登陆成功");
					openActivity(DoctorMainActivity.class);
				} else {
					showShortToast("用户名和密码错误,检查后重新输入");
				}
			}
			break;
		case R.id.btnDoctorLogin_register:
			openActivity(DoctorRegisterActivity.class);
			break;

		default:
			break;
		}

	}

	/**
	 * 医生端登陆
	 * 
	 * @author Zhouyong
	 */
	public class LoginTask extends AsyncTask<Void, Void, JSONObject> {
		private String doctorUserName;
		private String doctorPassWord;

		/**
		 * @param doctorUserName
		 * @param doctorPassWord
		 */
		public LoginTask(String doctorUserName, String doctorPassWord) {
			this.doctorUserName = doctorUserName;
			this.doctorPassWord = doctorPassWord;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showPd("正在登录");
		}

		@Override
		protected JSONObject doInBackground(Void... arg0) {
			try {
				return new BusinessHelper().login(doctorUserName, doctorPassWord, "0", "doctor");
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			dismissPd();
			if (result != null) {
				try {
					int status = result.getInt("code");
					JSONObject userJson = null;
					if (status == Constants.REQUEST_SUCCESS && !result.getString("message").equals("登陆失败")) {
						if (result.has("doctor_list")) {
							userJson = result.getJSONObject("doctor_list");
						} else {
							showShortToast(result.getString("message"));
						}
						showShortToast("登陆成功");
						int uid = userJson.getInt("user_id");
						SharedPrefUtil.setDoctorName(DoctorLoginActivity.this, userJson.getString("doctor_name"));
						SharedPrefUtil.setDoctorPassword(DoctorLoginActivity.this, userJson.getString("doctor_pass"));
						SharedPrefUtil.setUid(DoctorLoginActivity.this, uid);
						SharedPrefUtil.setUserType(DoctorLoginActivity.this, 1);
						if (cbIsRememberPassWord.isChecked()) {
							SharedPrefUtil.setIsRemember(DoctorLoginActivity.this, 1);
							setResult(RESULT_OK);
						} else {
							SharedPrefUtil.setIsRemember(DoctorLoginActivity.this, 2);
						}
						openActivity(DoctorMainActivity.class);
						finish();
					} else {
						showShortToast("用户名或密码错误");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				showShortToast(R.string.connect_server_exception);
			}

		}
	}
}
