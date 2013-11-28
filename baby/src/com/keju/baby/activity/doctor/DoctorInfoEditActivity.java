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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.SystemException;
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
	private EditText etDoctorName, etDoctorAddress, etDoctorHospital, etDoctorDepartment, etJobTitle, etDoctorEmil,
			etDoctorNumber;

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_info_edit_activity);

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
		etDoctorAddress = (EditText) this.findViewById(R.id.etDoctorAddress);
		etDoctorHospital = (EditText) this.findViewById(R.id.etDoctorHospital);
		etDoctorDepartment = (EditText) this.findViewById(R.id.etDoctorDepartment);
		etJobTitle = (EditText) this.findViewById(R.id.etJobTitle);
		etDoctorEmil = (EditText) this.findViewById(R.id.etDoctorEmil);
		etDoctorNumber = (EditText) this.findViewById(R.id.etDoctorNumber);

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

		tvTitle.setText("修改资料");
		if (NetUtil.checkNet(this)) {
			new PostDoctorInforTask().execute();
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
			lp.width = (int) (display.getWidth()); // 设置宽度
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
			View layout1= inflater1.inflate(R.layout.dialog_doctor_list, null); //
			ListView addressList =(ListView)layout1.findViewById(R.id.doctorListView);
			adapter = new ProviceAdapter();
			addressList.setAdapter(adapter); 
			final Dialog dialog1 = new Dialog(this, R.style.dialog);
			dialog1.setContentView(layout1); // 将取得布局文件set进去
			dialog1.show(); // 显示
			WindowManager windowManager1 = getWindowManager();
			Display display1 = windowManager1.getDefaultDisplay();
			WindowManager.LayoutParams lp1 = dialog1.getWindow().getAttributes();
			lp1.width = (int) (display1.getWidth()); // 设置宽度
			lp1.gravity = Gravity.BOTTOM;
			dialog1.getWindow().setAttributes(lp1);
			break;
		case R.id.viewDoctorHospital:
			break;
		case R.id.viewDoctorDepartment:
			break;
		case R.id.viewJobTitle:
			break;
		case R.id.btnRight:
			String doctorName = etDoctorName.getText().toString().trim();
			String doctorAddress = etDoctorAddress.getText().toString().trim();
			String doctorHospital = etDoctorHospital.getText().toString().trim();
			String doctorDepartment = etDoctorDepartment.getText().toString().trim();
			String jobTitle = etJobTitle.getText().toString().trim();
			String doctorEmil = etDoctorEmil.getText().toString().trim();
			String doctorNumber = etDoctorNumber.getText().toString().trim();
			if (NetUtil.checkNet(this)) {
				new PostDoctorInfor(doctorName, doctorAddress, doctorHospital, doctorDepartment, jobTitle, doctorEmil,
						doctorNumber).execute();
			} else {
				showShortToast(R.string.NoSignalException);
			}
			break;
		default:
			break;
		}

	}

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
		private String doctorAddress;
		private String doctorHospital;
		private String doctorDepartment;
		private String jobTitle;
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
		public PostDoctorInfor(String doctorName, String doctorAddress, String doctorHospital, String doctorDepartment,
				String jobTitle, String doctorEmil, String doctorNumber) {
			this.doctorName = doctorName;
			this.doctorAddress = doctorAddress;
			this.doctorHospital = doctorHospital;
			this.doctorDepartment = doctorDepartment;
			this.jobTitle = jobTitle;
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
			try {
				return new BusinessHelper().addDoctorInfor(uid, doctorName, doctorAddress, doctorHospital,
						doctorDepartment, jobTitle, doctorEmil, doctorNumber, avatarFile);
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
					int status = result.getInt("status");
					if (status == Constants.REQUEST_SUCCESS) {
					} else {
						showShortToast("资料设置失败");
					}
				} catch (JSONException e) {
					showShortToast(R.string.json_exception);
				}
			} else {
				showShortToast("服务连接失败");
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
	
	private class ProviceAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return null;
		}
		
	}
	
	
	
}
