package com.keju.baby.activity;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.SystemException;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.bean.BrandBean;
import com.keju.baby.bean.KindBean;
import com.keju.baby.bean.ResponseBean;
import com.keju.baby.bean.YardBean;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.DateUtil;
import com.keju.baby.util.NetUtil;
import com.keju.baby.util.SharedPrefUtil;

/**
 * 新增宝宝记录(妈妈新增的时候，不可以新增配方奶，传boolean判断)
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:32:11
 */
public class NewAddBabyRecordActivity extends BaseActivity implements OnClickListener {
	private ImageView btnLeft, btnRight;
	private TextView tvTitle;

	private TextView tvTime, tvYard, tvBrand, tvKind;
	private EditText etWeight, etHeight, etHead, etFeed, etMilk;
	private Button btnCheck, btnNewAddMilk;
	private View viewNutrition;
	private TextView tvEnergy, tvProtein, tvCarbohydrate, tvFat;

	private int id;
	private boolean isMother = false;
	private boolean isRefresh = false;
	private List<YardBean> yardList = new ArrayList<YardBean>();
	private List<BrandBean> brandList = new ArrayList<BrandBean>();
	private List<KindBean> kindList = new ArrayList<KindBean>();
	private int yardId = 1;
	private int brandId = 1;
	private int kindId = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().getExtras() != null) {
			id = getIntent().getExtras().getInt(Constants.EXTRA_DATA);
			isMother = getIntent().getExtras().getBoolean("isMother");
		}
		setContentView(R.layout.new_add_baby_record);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnLeft.setOnClickListener(this);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		btnRight.setImageResource(R.drawable.btn_commit_selector);
		btnRight.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		tvTime = (TextView) findViewById(R.id.tvTime);
		tvTime.setOnClickListener(this);
		tvYard = (TextView) findViewById(R.id.tvYard);
		tvBrand = (TextView) findViewById(R.id.tvBrand);
		tvKind = (TextView) findViewById(R.id.tvKind);
		tvYard.setOnClickListener(this);
		tvBrand.setOnClickListener(this);
		tvKind.setOnClickListener(this);

		etWeight = (EditText) findViewById(R.id.etWeight);
		etHeight = (EditText) findViewById(R.id.etHeight);
		etHead = (EditText) findViewById(R.id.etHead);
		etFeed = (EditText) findViewById(R.id.etFeed);
		etMilk = (EditText) findViewById(R.id.etMilk);

		btnCheck = (Button) findViewById(R.id.btnCheck);
		btnNewAddMilk = (Button) findViewById(R.id.btnNewAddMilk);
		btnCheck.setOnClickListener(this);
		btnNewAddMilk.setOnClickListener(this);

		viewNutrition = findViewById(R.id.viewNutrition);
		tvEnergy = (TextView) findViewById(R.id.tvEnergy);
		tvProtein = (TextView) findViewById(R.id.tvProtein);
		tvCarbohydrate = (TextView) findViewById(R.id.tvCarbohydrate);
		tvFat = (TextView) findViewById(R.id.tvFat);
	}

	private void fillData() {
		tvTitle.setText("新增随访记录");
		if (isMother) {
			btnNewAddMilk.setVisibility(View.INVISIBLE);
		}
		if (NetUtil.checkNet(this)) {
			new GetMilkDataTask().execute();
		} else {
			showShortToast(R.string.NoSignalException);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_NEW_ADD_MILK) {
			if (NetUtil.checkNet(this)) {
				isRefresh = true;
				new GetMilkDataTask().execute();
			} else {
				showShortToast(R.string.NoSignalException);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;
		case R.id.btnRight:
			String time = tvTime.getText().toString();
			if (TextUtils.isEmpty(time)) {
				showShortToast("请填写测量日期");
				return;
			}
			String weight = etWeight.getText().toString();
			if (TextUtils.isEmpty(weight)) {
				showShortToast("请填写体重");
				return;
			}
			String height = etHeight.getText().toString();
			if (TextUtils.isEmpty(height)) {
				showShortToast("请填写身高");
				return;
			}
			String head = etHead.getText().toString();
			if (TextUtils.isEmpty(head)) {
				showShortToast("请填写头围");
				return;
			}
			String feed = etFeed.getText().toString();
			if (TextUtils.isEmpty(feed)) {
				showShortToast("请填写母乳喂养量");
				return;
			}
			String yard = tvYard.getText().toString();
			if (TextUtils.isEmpty(yard)) {
				showShortToast("请选择院内外");
				return;
			}
			String brand = tvBrand.getText().toString();
			if (TextUtils.isEmpty(brand)) {
				showShortToast("请选择品牌");
				return;
			}
			String kind = tvKind.getText().toString();
			if (TextUtils.isEmpty(kind)) {
				showShortToast("请选择种类");
				return;
			}
			String milk = etMilk.getText().toString();
			if (TextUtils.isEmpty(milk)) {
				showShortToast("请填写配方奶喂养量");
				return;
			}
			if (NetUtil.checkNet(this)) {
				new AddBabyRecordTask(time, weight, height, head, feed, yardId + "", brandId + "", kindId + "", milk)
						.execute();
			} else {
				showShortToast(R.string.NoSignalException);
			}
			break;
		case R.id.tvTime:
			showDateDialog();
			break;
		case R.id.tvYard:
			dialogType = "yard";
			showDialog(yardList);
			break;
		case R.id.tvBrand:
			dialogType = "brand";
			showDialog(brandList);
			break;
		case R.id.tvKind:
			if (kindList.size() == 0) {
				KindBean bean = new KindBean();
				bean.setId(0);
				bean.setName("暂无此记录");
				kindList.add(bean);
			}
			dialogType = "kind";
			showDialog(kindList);
			break;
		case R.id.btnCheck:
			String yardStr = tvYard.getText().toString();
			if (TextUtils.isEmpty(yardStr)) {
				showShortToast("请选择院内/外");
				return;
			}
			String brandStr = tvBrand.getText().toString();
			if (TextUtils.isEmpty(brandStr)) {
				showShortToast("请选择奶粉品牌");
				return;
			}
			String kindStr = tvKind.getText().toString();
			if (TextUtils.isEmpty(kindStr)) {
				showShortToast("请选择奶粉种类");
				return;
			}
			viewNutrition.setVisibility(View.VISIBLE);
			break;
		case R.id.btnNewAddMilk:
			startActivityForResult(new Intent(this, AddMilkActivity.class), Constants.REQUEST_NEW_ADD_MILK);
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
		tvTime.setText(DateUtil.dateToString("yyyy-MM-dd", cal.getTime()));
	}

	private ListAdapter adapter;
	private Dialog dialog;
	private String dialogType;

	/**
	 * 显示dialog
	 */
	private void showDialog(List list) {
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
		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setAttributes(lp1);
	}

	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (dialogType.equals("yard")) {
				brandList.clear();
				brandList.addAll(yardList.get(arg2).getList());
				tvYard.setText(yardList.get(arg2).getName());
				tvBrand.setText("");
				tvKind.setText("");
				yardId = yardList.get(arg2).getId();
			} else if (dialogType.equals("brand")) {
				kindList.clear();
				kindList.addAll(brandList.get(arg2).getList());
				tvBrand.setText(brandList.get(arg2).getName());
				if (brandList.get(arg2).getName().equals("其他")) {
					btnCheck.setVisibility(View.GONE);
					viewNutrition.setVisibility(View.GONE);
				} else {
					btnCheck.setVisibility(View.VISIBLE);
				}
				tvKind.setText("");
				brandId = brandList.get(arg2).getId();
			} else if (dialogType.equals("kind")) {
				if (kindList.get(arg2).getId() == 0) {
					dialog.dismiss();
					return;
				}
				tvKind.setText(kindList.get(arg2).getName());

				tvEnergy.setText(kindList.get(arg2).getEnergy());
				tvProtein.setText(kindList.get(arg2).getProtein());
				tvCarbohydrate.setText(kindList.get(arg2).getCarbohydrate());
				tvFat.setText(kindList.get(arg2).getFat());
				kindId = kindList.get(arg2).getId();
			}
			dialog.dismiss();
		}
	};

	/**
	 * dialog listview 适配器
	 * 
	 * */
	private class ListAdapter extends BaseAdapter {
		private List<Object> list;

		public ListAdapter(List<Object> list) {
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
			String name = list.get(position).toString();
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
	 * 
	 * @author Zhoujun
	 * 
	 */
	private class AddBabyRecordTask extends AsyncTask<Void, Void, JSONObject> {
		private String due_date;
		private String weight;
		private String height;
		private String head;
		private String breastfeeding;
		private String location;
		private String brand;
		private String kind;
		private String nutrition;

		public AddBabyRecordTask(String due_date, String weight, String height, String head, String breastfeeding,
				String location, String brand, String kind, String nutrition) {
			super();
			this.due_date = due_date;
			this.weight = weight;
			this.height = height;
			this.head = head;
			this.breastfeeding = breastfeeding;
			this.location = location;
			this.brand = brand;
			this.kind = kind;
			this.nutrition = nutrition;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showPd("添加记录中...");
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			String add_type;
			if(SharedPrefUtil.getUserType(NewAddBabyRecordActivity.this) == Constants.USER_DOCTOR){
				add_type = "doctor";
			}else{
				add_type = "baby";
			}
			try {
				return new BusinessHelper().addVisit(id, due_date, weight, height, head, breastfeeding, location,
						brand, kind, nutrition,add_type);
			} catch (SystemException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			dismissPd();
			if (result != null) {
				try {
					int status = result.getInt("code");
					if (status == Constants.REQUEST_SUCCESS) {
						showShortToast("添加随访记录成功");
						setResult(RESULT_OK);
						finish();
					} else {
						showShortToast(result.getString("message"));
					}
				} catch (JSONException e) {
					showShortToast(R.string.json_exception);
				}
			} else {
				showShortToast(R.string.connect_server_exception);
			}
		}

	}

	/**
	 * 获取奶粉数据
	 * 
	 * @author Zhoujun
	 * 
	 */
	private class GetMilkDataTask extends AsyncTask<Void, Void, ResponseBean<YardBean>> {

		@Override
		protected ResponseBean<YardBean> doInBackground(Void... params) {
			return new BusinessHelper().getMilkData();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showPd(R.string.loading);
		}

		@Override
		protected void onPostExecute(ResponseBean<YardBean> result) {
			super.onPostExecute(result);
			dismissPd();
			if (result.getStatus() == Constants.REQUEST_SUCCESS) {
				List<YardBean> tempList = result.getObjList();
				if (tempList.size() > 0) {
					yardList.clear();
					brandList.clear();
					kindList.clear();
				}
				yardList.addAll(tempList);
				if (yardList.size() > 0) {
					tvYard.setText(yardList.get(0).getName());
					brandList.addAll(yardList.get(0).getList());
					if (brandList.size() > 0) {
						tvBrand.setText(brandList.get(0).getName());
						kindList.addAll(brandList.get(0).getList());
						if (kindList.size() > 0) {
							tvKind.setText(kindList.get(0).getName());
							tvEnergy.setText(kindList.get(0).getEnergy());
							tvProtein.setText(kindList.get(0).getProtein());
							tvCarbohydrate.setText(kindList.get(0).getCarbohydrate());
							tvFat.setText(kindList.get(0).getFat());
						}
					}
				}
			} else {
				showShortToast(result.getError());
			}
			isRefresh = false;
		}

	}
}
