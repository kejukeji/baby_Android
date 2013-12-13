package com.keju.baby.activity.doctor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.SystemException;
import com.keju.baby.activity.ComplicationActivity;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.DateUtil;
import com.keju.baby.util.NetUtil;
import com.keju.baby.util.StringUtil;

/**
 * 创建婴儿账户
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:57:49
 */

public class DoctorCreatBabyAccountActivity extends BaseActivity implements OnClickListener{
	private ImageView btnLeft,btnRight;
	private TextView tvTitle;
	
	private EditText etPhone,etName,etPassword,etPasswordConfirm,etWeight,etHeight,etHead;
	private TextView tvGender,tvPreProduction,tvBirthday,tvWay,tvComplication;
	
	private List<String> list = new ArrayList<String>();
	private String dateType;
	private String complicationStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_creat_baby_account);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnLeft.setOnClickListener(this);
		btnRight.setImageResource(R.drawable.btn_commit_selector);
		btnRight.setOnClickListener(this);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText("创建婴儿账户");
		
		etPhone = (EditText) findViewById(R.id.etPhone);
		etName = (EditText) findViewById(R.id.etName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
		
		tvGender = (TextView) findViewById(R.id.tvGender);
		tvPreProduction = (TextView) findViewById(R.id.tvPreProduction);
		tvBirthday = (TextView) findViewById(R.id.tvBirthday);
		etWeight = (EditText) findViewById(R.id.etWeight);
		etHeight = (EditText) findViewById(R.id.etHeight);
		etHead = (EditText) findViewById(R.id.etHead);
		tvWay = (TextView) findViewById(R.id.tvWay);
		tvComplication = (TextView) findViewById(R.id.tvComplication);
	}

	private void fillData() {
		tvGender.setOnClickListener(this);
		tvPreProduction.setOnClickListener(this);
		tvBirthday.setOnClickListener(this);
		tvWay.setOnClickListener(this);
		tvComplication.setOnClickListener(this);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == Constants.REQUEST_COMPLICATION){
			complicationStr = data.getStringExtra(Constants.EXTRA_DATA);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;
		case R.id.btnRight:
			String phone = etPhone.getText().toString();
			if(!StringUtil.isMobile(phone)){
				showShortToast("手机号码格式不正确");
				return;
			}
			String name = etName.getText().toString();
			if(TextUtils.isEmpty(name)){
				showShortToast("婴儿名不能为空");
				return;
			}
			String password = etPassword.getText().toString();
			if(TextUtils.isEmpty(password)){
				showShortToast("密码不能为空");
				return;
			}
			String passwordConfirm = etPasswordConfirm.getText().toString();
			if(!passwordConfirm.equals(password)){
				showShortToast("两次输入的密码不一致");
				return;
			}
			String gender = tvGender.getText().toString();
			String preproduction = tvPreProduction.getText().toString();
			if(TextUtils.isEmpty(preproduction)){
				showShortToast("请填写预产期");
				return;
			}
			String birthday = tvBirthday.getText().toString();
			if(TextUtils.isEmpty(birthday)){
				showShortToast("请填写出生日期");
				return;
			}
			String weight = etWeight.getText().toString();
			if(TextUtils.isEmpty(weight)){
				showShortToast("请填写体重");
				return;
			}
			String height = etHeight.getText().toString();
			if(TextUtils.isEmpty(height)){
				showShortToast("请填写身高");
				return;
			}
			String head = etHead.getText().toString();
			if(TextUtils.isEmpty(head)){
				showShortToast("请填写头围");
				return;
			}
			String way = tvWay.getText().toString();
			if(TextUtils.isEmpty(way)){
				showShortToast("请选择分娩方式");
				return;
			}
			String complication = tvComplication.getText().toString();
			if(TextUtils.isEmpty(complication)){
				showShortToast("请选择合并症");
				return;
			}
			if(complication.equals("有")){
				complication = complicationStr;
			}
			if(!NetUtil.checkNet(this)){
				showShortToast(R.string.NoSignalException);
			}
			new CreatBabyAccountTask(name, password, phone, gender, preproduction, birthday, weight, height, head, way, complication).execute();
			break;
		case R.id.tvGender:
			dialogType = "gender";
			list.clear();
			list.add("男");
			list.add("女");
			showDialog(list);
			break;
		case R.id.tvPreProduction:
			dateType = "preproduction";
			showDateDialog();
			break;
		case R.id.tvBirthday:
			dateType = "birthday";
			showDateDialog();
			break;
		case R.id.tvWay:
			dialogType = "diliveryWay";
			list.clear();
			list.add("剖腹产");
			list.add("顺产");
			showDialog(list);
			break;
		case R.id.tvComplication:
			dialogType = "complication";
			list.clear();
			list.add("有");
			list.add("没有");
			showDialog(list);
			break;
		default:
			break;
		}
	}
	Calendar cal = Calendar.getInstance();

	/**
	 * 显示日期选项dialog；
	 */
	private void showDateDialog() {
		new DatePickerDialog(this, listener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH)).show();
	}

	private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDate();
		}
	};

	private void updateDate() {
		if(dateType.equals("preproduction")){
			tvPreProduction.setText(DateUtil.dateToString("yyyy-MM-dd", cal.getTime()));
		}else if(dateType.equals("birthday")){
			tvBirthday.setText(DateUtil.dateToString("yyyy-MM-dd", cal.getTime()));
		}
	}
	private ListAdapter adapter;
	private Dialog dialog;
	private String dialogType;

	/**
	 * 显示dialog
	 */
	private void showDialog(List<String> list) {
		View view = getLayoutInflater().inflate(R.layout.dialog_doctor_list, null);
		ListView addressList = (ListView) view.findViewById(R.id.doctorListView);
		adapter = new ListAdapter(list);
		addressList.setAdapter(adapter);
		addressList.setOnItemClickListener(itemListener);
		dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(view); // 将取得布局文件set进去
		dialog.show(); // 显示
		WindowManager windowManager1 = getWindowManager();
		Display display1 = windowManager1.getDefaultDisplay();
		WindowManager.LayoutParams lp1 = dialog.getWindow().getAttributes();
		lp1.width = (int) (display1.getWidth() - 20); // 设置宽度
		dialog.getWindow().setAttributes(lp1);
	}
	/**
	 * 城市选择 listview 点击
	 */
	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (dialogType.equals("gender")) {
				tvGender.setText((String) adapter.getItem(arg2));
			} else if (dialogType.equals("diliveryWay")) {
				tvWay.setText((String) adapter.getItem(arg2));
			}else if(dialogType.equals("complication")){
				tvComplication.setText((String) adapter.getItem(arg2));
				if(((String) adapter.getItem(arg2)).equals("有")){
					startActivityForResult(new Intent(DoctorCreatBabyAccountActivity.this, ComplicationActivity.class), Constants.REQUEST_COMPLICATION);
				}
			}
			dialog.dismiss();
		}
	};
	/**
	 * 获取医生城市的适配器
	 * 
	 * */
	private class ListAdapter extends BaseAdapter {
		private List<String> list;

		public ListAdapter(List<String> list) {
			super();
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			String name = list.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.dialog_doctor_item, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.tvDoctorInfor);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvName.setText(name);
			return convertView;
		}

		class ViewHolder {
			private TextView tvName;
		}

	}
	/**
	 * 创建婴儿账户
	 * @author Zhoujun
	 *
	 */
	private class CreatBabyAccountTask extends AsyncTask<Void, Void, JSONObject>{
		
		private String baby_name;
		private String baby_pass;
		private String patriarch_tel;
		private String gender;
		private String due_date;
		private String born_birthday;
		private String born_weight;
		private String born_height;
		private String born_head;
		private String childbirth_style;
		private String complication_id;
		
		public CreatBabyAccountTask(String baby_name, String baby_pass, String patriarch_tel, String gender,
				String due_date, String born_birthday, String born_weight, String born_height, String born_head,
				String childbirth_style, String complication_id) {
			super();
			this.baby_name = baby_name;
			this.baby_pass = baby_pass;
			this.patriarch_tel = patriarch_tel;
			this.gender = gender;
			this.due_date = due_date;
			this.born_birthday = born_birthday;
			this.born_weight = born_weight;
			this.born_height = born_height;
			this.born_head = born_head;
			this.childbirth_style = childbirth_style;
			this.complication_id = complication_id;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showPd("正在提交中...");
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			try {
				return new BusinessHelper().creatBabyAccount(baby_name, baby_pass, patriarch_tel, gender, due_date, born_birthday, born_weight, born_height, born_head, childbirth_style, complication_id);
			} catch (SystemException e) {
				return null;
			}
		}
		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			dismissPd();
			if(result != null){
				try {
					int status = result.getInt("code");
					if(status == Constants.REQUEST_SUCCESS){
						showShortToast("创建婴儿账户成功");
						setResult(RESULT_OK);
						finish();
					}else{
						showShortToast(result.getString("message"));
					}
				} catch (JSONException e) {
					showShortToast(R.string.json_exception);
				}
			}else{
				showShortToast(R.string.connect_server_exception);
			}
		}
	}
}
