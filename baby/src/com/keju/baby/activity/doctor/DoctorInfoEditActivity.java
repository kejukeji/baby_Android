package com.keju.baby.activity.doctor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.keju.baby.AsyncImageLoader;
import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.SystemException;
import com.keju.baby.AsyncImageLoader.ImageCallback;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.bean.DoctorBelongDepartmentBean;
import com.keju.baby.bean.DoctorDepartmentBean;
import com.keju.baby.bean.DoctorHospitalBean;
import com.keju.baby.bean.DoctorProvinceBean;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.ImageUtil;
import com.keju.baby.util.NetUtil;
import com.keju.baby.util.SharedPrefUtil;
import com.keju.baby.util.StringUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 医生信息编辑界面
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:56:27
 */
public class DoctorInfoEditActivity extends BaseActivity implements OnClickListener {

	private ImageView btnLeft, btnRight;
	private TextView tvTitle;

	private LinearLayout viewDoctorPhone;
	private ImageView ivDoctorPhone;
	private EditText etDoctorName, etDoctorEmil, etDoctorNumber;
	private TextView tvDoctorAddress, tvDoctorHospital, tvDoctorDepartment, tvJobTitle;
	private LinearLayout viewDoctorAddress, viewDoctorHospital, viewDoctorDepartment, viewJobTitle;
	private File mCurrentPhotoFile;// 照相机拍照得到的图片，临时文件
	private File avatarFile;// 头像文件
	private File PHOTO_DIR;// 照相机拍照得到的图片的存储位置
	static final int DATE_DIALOG_ID = 1;
	private Bitmap cameraBitmap;// 头像bitmap
	private long userId;

	private List<DoctorDepartmentBean> departmentList = new ArrayList<DoctorDepartmentBean>();// 科室list
	private List<DoctorHospitalBean> hospitalList = new ArrayList<DoctorHospitalBean>();// 医院list
	private List<DoctorProvinceBean> provinceList = new ArrayList<DoctorProvinceBean>();// 医生选择省得list
	private List<DoctorBelongDepartmentBean> positionList = new ArrayList<DoctorBelongDepartmentBean>();// 医生的职位list

	private ProviceAdapter adapter;
	private HospitalAdaper adapter1;
	private DepartmentAdaper adapter2;
	private PositionAdaper adapter3;
	
	private int proviceId; 
	private int hospitalId;
	private int departmentId;
	private int positionId;
	
	private String doctorName,doctorAddress,doctorHospital,
    doctorDepartment,doctorJop,doctorEmail,doctorPhone,avatarUrl;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_info_edit_activity);
         
		if(getIntent()!=null){
		 doctorName =getIntent().getExtras().getString("DCNAME");
		 doctorAddress =getIntent().getExtras().getString("DCADDRESS"); 
		 doctorHospital =getIntent().getExtras().getString("DCHOSPITAL"); 
		 doctorDepartment =getIntent().getExtras().getString("DCDEPARTMENT"); 
		 doctorJop =getIntent().getExtras().getString("DCJOP"); 
		 doctorEmail =getIntent().getExtras().getString("DCEMAIL");
		 doctorPhone =getIntent().getExtras().getString("DCPHONE"); 
		 avatarUrl =getIntent().getExtras().getString("DCURL"); 
		}
		findView();
		fillData();
		createPhotoDir();
	}

	private void findView() {

		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		btnRight.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		viewDoctorPhone = (LinearLayout) this.findViewById(R.id.viewDoctorPhone);
		viewDoctorPhone.setOnClickListener(this);

		ivDoctorPhone = (ImageView) this.findViewById(R.id.ivDoctorPhone);
		etDoctorName = (EditText) this.findViewById(R.id.etDoctorName);
		tvDoctorAddress = (TextView) this.findViewById(R.id.tvDoctorAddress);
		tvDoctorHospital = (TextView) this.findViewById(R.id.tvDoctorHospital);
		tvDoctorDepartment = (TextView) this.findViewById(R.id.tvDoctorDepartment);
		tvJobTitle = (TextView) this.findViewById(R.id.tvJobTitle);
		etDoctorEmil = (EditText) this.findViewById(R.id.etDoctorEmil);
		etDoctorNumber = (EditText) this.findViewById(R.id.etDoctorNumber);
		
        etDoctorName.setText(doctorName);
        tvDoctorAddress.setText(doctorAddress);
        tvDoctorHospital.setText(doctorHospital);
        tvDoctorDepartment.setText(doctorDepartment);
        tvJobTitle.setText(doctorJop);
        etDoctorEmil.setText(doctorEmail);
        etDoctorNumber.setText(doctorPhone);
        Drawable cacheDrawable = AsyncImageLoader.getInstance().loadAsynLocalDrawable(avatarUrl, new ImageCallback() {
			
			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				if(imageDrawable != null){
					Bitmap bitmap = ImageUtil.getRoundCornerBitmapWithPic(imageDrawable, 0.5f);
					ivDoctorPhone.setImageBitmap(bitmap);
				}else{
					ivDoctorPhone.setImageResource(R.drawable.item_lion);
				}
			}
		});
		if(cacheDrawable != null){
			Bitmap bitmap = ImageUtil.getRoundCornerBitmapWithPic(cacheDrawable, 0.5f);
			ivDoctorPhone.setImageBitmap(bitmap);
		}else{
			ivDoctorPhone.setImageResource(R.drawable.item_lion);
		}
		viewDoctorAddress = (LinearLayout) this.findViewById(R.id.viewDoctorAddress);
		viewDoctorAddress.setOnClickListener(this);
		viewDoctorHospital = (LinearLayout) this.findViewById(R.id.viewDoctorHospital);
		viewDoctorHospital.setOnClickListener(this);
		viewDoctorDepartment = (LinearLayout) this.findViewById(R.id.viewDoctorDepartment);
		viewDoctorDepartment.setOnClickListener(this);
		viewJobTitle = (LinearLayout) this.findViewById(R.id.viewJobTitle);
		viewJobTitle.setOnClickListener(this);
	}

	/**
	 * 数据填充
	 */
	private void fillData() {

		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnRight.setImageResource(R.drawable.btn_commit_selector);
		tvTitle.setText("修改资料");
		if (NetUtil.checkNet(this)) {
			new PostDoctorInforTask().execute();
		} else {
			showShortToast(R.string.NoSignalException);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constants.PHOTO_PICKED_WITH_DATA:// 相册
				cameraBitmap = data.getParcelableExtra("data");
				if (cameraBitmap == null) {
					Uri dataUri = data.getData();
					Intent intent = getCropImageIntent(dataUri);
					startActivityForResult(intent, Constants.PHOTO_PICKED_WITH_DATA);
				}

				try {
					// 保存缩略图
					FileOutputStream out = null;
					File file = new File(PHOTO_DIR, ImageUtil.createAvatarFileName(String.valueOf(userId)));
					if (file != null && file.exists()) {
						file.delete();
					}
					avatarFile = new File(PHOTO_DIR, ImageUtil.createAvatarFileName(String.valueOf(userId)));
					out = new FileOutputStream(avatarFile, false);

					if (cameraBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
						out.flush();
						out.close();
					}
					if (avatarFile != null) {
						ivDoctorPhone.setImageBitmap(cameraBitmap);
					}
					if (mCurrentPhotoFile != null && mCurrentPhotoFile.exists())
						mCurrentPhotoFile.delete();
				} catch (Exception e) {
					MobclickAgent.reportError(DoctorInfoEditActivity.this, StringUtil.getExceptionInfo(e));
				}
				break;
			case Constants.CAMERA_WITH_DATA:// 拍照
				doCropPhoto(mCurrentPhotoFile);
				break;
			}
		}
	}
	private Dialog dialogProvince;
	private Dialog dialogDoctorHospital;
	private Dialog dialogDoctorDepartment;
	private Dialog dialogJobTitle;
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;
		case R.id.viewDoctorPhone:
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.user_image_changing, null); //
			TextView tvTakePhoto = (TextView) layout.findViewById(R.id.tvtakephoto);
			TextView tvGetPicture = (TextView) layout.findViewById(R.id.tvgetpicture);
			TextView tvCancel = (TextView) layout.findViewById(R.id.tvcancel);
			final Dialog dialog = new Dialog(this, R.style.dialog);
			dialog.setContentView(layout); // 将取得布局文件set进去
			dialog.show(); // 显示
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.width = (int) (display.getWidth() - 40); // 设置宽度
			lp.gravity = Gravity.BOTTOM;
			dialog.getWindow().setAttributes(lp);
			tvCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			tvTakePhoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
						doTakePhoto();// 用户点击了从照相机获取
					} else {
						showShortToast("请检查SD卡是否正常");
					}
					dialog.dismiss();
				}
			});
			tvGetPicture.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					intent.putExtra("crop", "true");
					intent.putExtra("aspectX", 1);
					intent.putExtra("aspectY", 1);
					intent.putExtra("outputX", 200);
					intent.putExtra("outputY", 200);
					intent.putExtra("return-data", true);
					startActivityForResult(intent, Constants.PHOTO_PICKED_WITH_DATA);
					dialog.dismiss();

				}
			});
			break;
		case R.id.viewDoctorAddress:
			LayoutInflater inflater1 = getLayoutInflater();
			View layout1 = inflater1.inflate(R.layout.dialog_doctor_list, null);
			ListView addressList = (ListView) layout1.findViewById(R.id.doctorListView);
			adapter = new ProviceAdapter();
			addressList.setAdapter(adapter);
			addressList.setOnItemClickListener(itemListener);
			dialogProvince = new Dialog(this, R.style.dialog);
			dialogProvince.setContentView(layout1); // 将取得布局文件set进去
			dialogProvince.show(); // 显示
			WindowManager windowManager1 = getWindowManager();
			Display display1 = windowManager1.getDefaultDisplay();
			WindowManager.LayoutParams lp1 = dialogProvince.getWindow().getAttributes();
			lp1.width = (int) (display1.getWidth() - 20); // 设置宽度
			// lp1.gravity = Gravity.BOTTOM;
			dialogProvince.getWindow().setAttributes(lp1);
			break;
		case R.id.viewDoctorHospital:
			LayoutInflater inflater2 = getLayoutInflater();
			View layout2 = inflater2.inflate(R.layout.dialog_doctor_list, null);
			ListView hospitalList = (ListView) layout2.findViewById(R.id.doctorListView);
			adapter1 = new HospitalAdaper();
			hospitalList.setAdapter(adapter1);
			hospitalList.setOnItemClickListener(itemListener1);
			dialogDoctorHospital = new Dialog(this, R.style.dialog);
			dialogDoctorHospital.setContentView(layout2); // 将取得布局文件set进去
			dialogDoctorHospital.show(); // 显示
			WindowManager windowManager2 = getWindowManager();
			Display display2 = windowManager2.getDefaultDisplay();
			WindowManager.LayoutParams lp2 = dialogDoctorHospital.getWindow().getAttributes();
			lp2.width = (int) (display2.getWidth() - 20); // 设置宽度
			// lp1.gravity = Gravity.BOTTOM;
			dialogDoctorHospital.getWindow().setAttributes(lp2);
			break;
		case R.id.viewDoctorDepartment:
			LayoutInflater inflater3 = getLayoutInflater();
			View layout3 = inflater3.inflate(R.layout.dialog_doctor_list, null);
			ListView departmentList = (ListView) layout3.findViewById(R.id.doctorListView);
			adapter2 = new DepartmentAdaper();
			departmentList.setAdapter(adapter2);
			departmentList.setOnItemClickListener(itemListener2);
			dialogDoctorDepartment = new Dialog(this, R.style.dialog);
			dialogDoctorDepartment.setContentView(layout3); // 将取得布局文件set进去
			dialogDoctorDepartment.show(); // 显示
			WindowManager windowManager3 = getWindowManager();
			Display display3 = windowManager3.getDefaultDisplay();
			WindowManager.LayoutParams lp3 = dialogDoctorDepartment.getWindow().getAttributes();
			lp3.width = (int) (display3.getWidth() - 20); // 设置宽度
			// lp1.gravity = Gravity.BOTTOM;
			dialogDoctorDepartment.getWindow().setAttributes(lp3);
			break;
		case R.id.viewJobTitle:
			LayoutInflater inflater4 = getLayoutInflater();
			View layout4 = inflater4.inflate(R.layout.dialog_doctor_list, null);
			ListView jobTitleList = (ListView) layout4.findViewById(R.id.doctorListView);
			adapter3 = new PositionAdaper();
			jobTitleList.setAdapter(adapter3);
			jobTitleList.setOnItemClickListener(itemListener3);
			dialogJobTitle = new Dialog(this, R.style.dialog);
			dialogJobTitle.setContentView(layout4); // 将取得布局文件set进去
			dialogJobTitle.show(); // 显示
			WindowManager windowManager4 = getWindowManager();
			Display display4 = windowManager4.getDefaultDisplay();
			WindowManager.LayoutParams lp4 = dialogJobTitle.getWindow().getAttributes();
			lp4.width = (int) (display4.getWidth() - 20); // 设置宽度
			dialogJobTitle.getWindow().setAttributes(lp4);
			break;
		case R.id.btnRight:
			String doctorName = etDoctorName.getText().toString().trim();
			String doctorEmil = etDoctorEmil.getText().toString().trim();
			String doctorNumber = etDoctorNumber.getText().toString().trim();
			if (NetUtil.checkNet(this)) {
				new PostDoctorInfor(doctorName,doctorEmil,doctorNumber).execute();
			} else {
				showShortToast(R.string.NoSignalException);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 城市选择 listview 点击
	 */
	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (arg2 >= provinceList.size()) {
				return;
			}
			DoctorProvinceBean bean = provinceList.get(arg2);
			tvDoctorAddress.setText(bean.getProvinceName());
			proviceId = bean.getProvinceId();
			dialogProvince.dismiss();
		}
	};
	/**
	 * 医院选择 listview 点击
	 */
	OnItemClickListener itemListener1 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (arg2 >= hospitalList.size()) {
				return;
			}
			DoctorHospitalBean bean = hospitalList.get(arg2);
			tvDoctorHospital.setText(bean.getHospitalName());
			hospitalId = bean.getHospitalId();
			dialogDoctorHospital.dismiss();
		}
	};
	/**
	 * 科室选择 listview 点击
	 */
	OnItemClickListener itemListener2 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (arg2 >= departmentList.size()) {
				return;
			}
			DoctorDepartmentBean bean = departmentList.get(arg2);
			tvDoctorDepartment.setText(bean.getDepartmentName());
			departmentId = bean.getDepartmentId();
			dialogDoctorDepartment.dismiss();
		}
	};
	/**
	 * 职位选择 listview 点击
	 */
	OnItemClickListener itemListener3 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (arg2 >= positionList.size()) {
				return;
			}
			DoctorBelongDepartmentBean bean = positionList.get(arg2);
			tvJobTitle.setText(bean.getPositionName());
			positionId = bean.getPositionId();
			dialogJobTitle.dismiss();
		}
	};

	private void createPhotoDir() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_DIR_NAME + "/");
			if (!PHOTO_DIR.exists()) {
				// 创建照片的存储目录
				PHOTO_DIR.mkdirs();
			}
		} else {
			showShortToast("请检查SD卡是否正常");
		}
	}

	public void StartCamera() {
		try {
			mCurrentPhotoFile = new File(PHOTO_DIR, ImageUtil.getPhotoFileName());// 给新照的照片文件命名
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			startActivityForResult(intent, Constants.CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "拍照出错", Toast.LENGTH_LONG).show();
		}
	}

	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	/**
	 * 拍照获取图片
	 * 
	 */
	protected void doTakePhoto() {
		try {
			mCurrentPhotoFile = new File(PHOTO_DIR, ImageUtil.getPhotoFileName());// 给新照的照片文件命名
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			startActivityForResult(intent, Constants.CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			showShortToast("拍照出错");
		}
	}

	/**
	 * 调用图片剪辑程序去剪裁图片
	 * 
	 * @param f
	 */
	protected void doCropPhoto(File f) {
		try {
			// 启动gallery去剪辑这个照片
			final Intent intent = getCropImageIntent(Uri.fromFile(f));
			startActivityForResult(intent, Constants.PHOTO_PICKED_WITH_DATA);

		} catch (Exception e) {
			MobclickAgent.reportError(DoctorInfoEditActivity.this, StringUtil.getExceptionInfo(e));
			showShortToast("照片裁剪出错");
		}
	}

	/**
	 * 调用图片剪辑程序
	 */
	public static Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		return intent;
	}

	/***
	 * 医生个人资料修改
	 */
	private class PostDoctorInfor extends AsyncTask<Void, Void, JSONObject> {
		private String doctorName;
		private String doctorEmil;
		private String doctorNumber;

		/**
		 * @param doctorName
		 * @param doctorAddress
		 * @param doctorHospital
		 * @param doctorDepartment
		 * @param jobTitle
		 * @param doctorEmil
		 * @param doctorNumber
		 */
		public PostDoctorInfor(String doctorName,String doctorEmil,String doctorNumber) {
			this.doctorName = doctorName;
			this.doctorEmil = doctorEmil;
			this.doctorNumber = doctorNumber;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showPd("正在修改...");
		}
		@Override
		protected JSONObject doInBackground(Void... params) {
			int uid = SharedPrefUtil.getUid(DoctorInfoEditActivity.this);
			String type ="update";
			try {
				return new BusinessHelper().addDoctorInfor(uid,type,doctorName, proviceId, hospitalId,
						departmentId, positionId, doctorEmil, doctorNumber, avatarFile);
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
					if (status == Constants.REQUEST_SUCCESS) {
						showShortToast("资料修改成功");
					} else {
						showShortToast("资料设置失败");
					}
				} catch (JSONException e) {
					showShortToast(R.string.json_exception);
				}
			} else {
	//			showShortToast("服务连接失败");
			}
		}

	}

	/**
	 * 获取医生的相关数据
	 * 
	 * */
	private class PostDoctorInforTask extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Void... params) {
			try {
				return new BusinessHelper().getDoctorData();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					int status = result.getInt("code");
					if (status == Constants.REQUEST_SUCCESS) {
						// 医生的科室
						JSONArray departmentArrList = result.getJSONArray("department_list");
						if (departmentArrList != null) {
							ArrayList<DoctorDepartmentBean> departmentBean = (ArrayList<DoctorDepartmentBean>) DoctorDepartmentBean
									.constractList(departmentArrList);
							departmentList.addAll(departmentBean);
						}
						// 医生的医院
						JSONArray hospitalArrList = result.getJSONArray("hospital_list");
						if (hospitalArrList != null) {
							ArrayList<DoctorHospitalBean> hospitalBean = (ArrayList<DoctorHospitalBean>) DoctorHospitalBean
									.constractList(hospitalArrList);
							hospitalList.addAll(hospitalBean);
						}
						// 医生的职位
						JSONArray positionArrList = result.getJSONArray("position_list");
						if (positionArrList != null) {
							ArrayList<DoctorBelongDepartmentBean> positionBean = (ArrayList<DoctorBelongDepartmentBean>) DoctorBelongDepartmentBean
									.constractList(positionArrList);
							positionList.addAll(positionBean);
						}
						// 医生的所在省
						JSONArray provinceArrList = result.getJSONArray("province_list");
						if (provinceArrList != null) {
							ArrayList<DoctorProvinceBean> provinceBean = (ArrayList<DoctorProvinceBean>) DoctorProvinceBean
									.constractList(provinceArrList);
							provinceList.addAll(provinceBean);
						}
					} else {
						showShortToast(result.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				showShortToast("服务器连接失败");
			}
		}
	}
	/**
	 * 获取医生城市的适配器
	 * 
	 * */
	private class ProviceAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return provinceList.size();
		}

		@Override
		public Object getItem(int position) {
			return provinceList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			DoctorProvinceBean bean = provinceList.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.dialog_doctor_item, null);
				holder.tvProvinceName = (TextView) convertView.findViewById(R.id.tvDoctorInfor);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvProvinceName.setText(bean.getProvinceName());
			return convertView;
		}

		class ViewHolder {
			private TextView tvProvinceName;
		}

	}
	/**
	 * 获取医生医院的适配器
	 * 
	 * */
	private class HospitalAdaper extends BaseAdapter {

		@Override
		public int getCount() {
			return hospitalList.size();
		}

		@Override
		public Object getItem(int position) {
			return hospitalList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			DoctorHospitalBean bean = hospitalList.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.dialog_doctor_item, null);
				holder.tvHospitalName = (TextView) convertView.findViewById(R.id.tvDoctorInfor);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvHospitalName.setText(bean.getHospitalName());
			return convertView;
		}

		class ViewHolder {
			private TextView tvHospitalName;
		}

	}

	/**
	 * 获取医生科室的适配器
	 * 
	 * */
	private class DepartmentAdaper extends BaseAdapter {

		@Override
		public int getCount() {
			return departmentList.size();
		}

		@Override
		public Object getItem(int position) {
			return departmentList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			DoctorDepartmentBean bean = departmentList.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.dialog_doctor_item, null);
				holder.tvDepartmenName = (TextView) convertView.findViewById(R.id.tvDoctorInfor);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvDepartmenName.setText(bean.getDepartmentName());
			return convertView;
		}

		class ViewHolder {
			private TextView tvDepartmenName;
		}

	}

	/**
	 * 获取医生职位的适配器
	 * 
	 * */
	private class PositionAdaper extends BaseAdapter {

		@Override
		public int getCount() {
			return positionList.size();
		}

		@Override
		public Object getItem(int position) {
			return positionList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			DoctorBelongDepartmentBean bean = positionList.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.dialog_doctor_item, null);
				holder.tvPositionName = (TextView) convertView.findViewById(R.id.tvDoctorInfor);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvPositionName.setText(bean.getPositionName());
			return convertView;
		}

		class ViewHolder {
			private TextView tvPositionName;
		}

	}


}
