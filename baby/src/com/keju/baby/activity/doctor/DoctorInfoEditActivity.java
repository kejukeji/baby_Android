package com.keju.baby.activity.doctor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.keju.baby.AsyncImageLoader;
import com.keju.baby.AsyncImageLoader.ImageCallback;
import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.SystemException;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.bean.AcademicAbstractBean;
import com.keju.baby.bean.DoctorBelongDepartmentBean;
import com.keju.baby.bean.DoctorDepartmentBean;
import com.keju.baby.bean.DoctorHospitalBean;
import com.keju.baby.bean.DoctorProvinceBean;
import com.keju.baby.bean.ResponseBean;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.AndroidUtil;
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
public class DoctorInfoEditActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

	private ImageView btnLeft, btnRight;
	private TextView tvTitle;

	private EditText etDoctorName, etDoctorEmil, etDoctorNumber;
	private TextView tvName, tvDoctorAddress, tvDoctorHospital, tvDoctorDepartment, tvJobTitle;
	private LinearLayout viewDoctorAddress, viewDoctorHospital, viewDoctorDepartment, viewJobTitle;

	private ImageView ivAvatar;
	private File mCurrentPhotoFile;// 照相机拍照得到的图片，临时文件
	private File avatarFile;// 头像文件
	private File PHOTO_DIR;// 照相机拍照得到的图片的存储位置
	private Bitmap cameraBitmap;// 头像bitmap

	private RadioGroup radio_group;
	private View viewInfo;

	private List<DoctorProvinceBean> provinceList = new ArrayList<DoctorProvinceBean>();// 医生选择省得list
	private List<DoctorHospitalBean> hospitalList = new ArrayList<DoctorHospitalBean>();// 医院list
	private List<DoctorDepartmentBean> departmentList = new ArrayList<DoctorDepartmentBean>();// 科室list
	private List<DoctorBelongDepartmentBean> positionList = new ArrayList<DoctorBelongDepartmentBean>();// 医生的职位list

	private ListView listView;
	private CollectionAdapter collectionAdapter;
	private List<AcademicAbstractBean> list;
	private int proviceId;
	private int hospitalId;
	private int departmentId;
	private int positionId;
	private View vFooter;
	private ProgressBar pbFooter;
	private TextView tvFooterMore;
	private boolean isLoad = false;// 是否正在加载数据
	private boolean isLoadMore = false;
	private boolean isComplete = false;// 是否加载完了；
	private int pageIndex = 1;

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

		radio_group = (RadioGroup) findViewById(R.id.radio_group);
		radio_group.setOnCheckedChangeListener(this);
		viewInfo = findViewById(R.id.viewInfo);
		tvName = (TextView) findViewById(R.id.tvName);
		ivAvatar = (ImageView) this.findViewById(R.id.ivAvatar);
		ivAvatar.setOnClickListener(this);
		etDoctorName = (EditText) this.findViewById(R.id.etDoctorName);

		tvDoctorAddress = (TextView) this.findViewById(R.id.tvDoctorAddress);
		tvDoctorHospital = (TextView) this.findViewById(R.id.tvDoctorHospital);
		tvDoctorDepartment = (TextView) this.findViewById(R.id.tvDoctorDepartment);
		tvJobTitle = (TextView) this.findViewById(R.id.tvJobTitle);

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

		// 加载更多footer
		vFooter = getLayoutInflater().inflate(R.layout.footer, null);
		pbFooter = (ProgressBar) vFooter.findViewById(R.id.progressBar);
		tvFooterMore = (TextView) vFooter.findViewById(R.id.tvMore);
		listView = (ListView) findViewById(R.id.listView);
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
					File file = new File(PHOTO_DIR, ImageUtil.createAvatarFileName(String.valueOf(SharedPrefUtil
							.getUid(this))));
					if (file != null && file.exists()) {
						file.delete();
					}
					avatarFile = new File(PHOTO_DIR, ImageUtil.createAvatarFileName(String.valueOf(SharedPrefUtil
							.getUid(this))));
					out = new FileOutputStream(avatarFile, false);

					if (cameraBitmap.compress(Bitmap.CompressFormat.PNG, 80, out)) {
						out.flush();
						out.close();
					}
					if (avatarFile != null) {
						ivAvatar.setImageBitmap(cameraBitmap);
					} else {
						ivAvatar.setImageResource(R.drawable.doctor_default);
					}
					if (mCurrentPhotoFile != null && mCurrentPhotoFile.exists())
						mCurrentPhotoFile.delete();
				} catch (Exception e) {

				}
				break;
			case Constants.CAMERA_WITH_DATA:// 拍照
				doCropPhoto(mCurrentPhotoFile);
				break;
			}
		}
	}

	/**
	 * 数据填充
	 */
	private void fillData() {

		btnLeft.setVisibility(View.INVISIBLE);
		btnRight.setImageResource(R.drawable.btn_commit_selector);
		tvTitle.setText("个人中心");

		list = new ArrayList<AcademicAbstractBean>();
		collectionAdapter = new CollectionAdapter();

		listView.addFooterView(vFooter);
		listView.setOnScrollListener(LoadListener);
		listView.setAdapter(collectionAdapter);
		listView.setOnItemClickListener(clicklistener);
		listView.setAdapter(collectionAdapter);
		if (NetUtil.checkNet(this)) {
			new PostDoctorInforTask().execute();
			new GetDoctorTask().execute();
			new GetCollectDataTask().execute();
		} else {
			String doctorInforStr = SharedPrefUtil.getDoctorInfor(DoctorInfoEditActivity.this);
			if(doctorInforStr != null){
				JSONObject obj = null;
				try {
					obj = new JSONObject(doctorInforStr); 
					etDoctorName.setText(obj.getString("real_name"));
					tvDoctorAddress.setText(obj.getString("province"));
					tvDoctorHospital.setText(obj.getString("hospital"));
					tvDoctorDepartment.setText(obj.getString("department"));
					tvJobTitle.setText(obj.getString("positions"));
					etDoctorEmil.setText(obj.getString("email"));
					etDoctorNumber.setText(obj.getString("tel"));
					tvName.setText(obj.getString("doctor_name"));

					String avatarUrl = BusinessHelper.PIC_URL + obj.getString("picture_path");
					ivAvatar.setTag(avatarUrl);
					Drawable cacheDrawable = AsyncImageLoader.getInstance().loadAsynLocalDrawable(avatarUrl,
							new ImageCallback() {
								@Override
								public void imageLoaded(Drawable imageDrawable, String imageUrl) {
									ImageView image = (ImageView) ivAvatar.findViewWithTag(imageUrl);
									if (image != null) {
										if (imageDrawable != null) {
											ivAvatar.setImageDrawable(imageDrawable);
										} else {
											ivAvatar.setImageResource(R.drawable.doctor_default);
										}
									}
								}
							});
					if (cacheDrawable != null) {
						ivAvatar.setImageDrawable(cacheDrawable);
					} else {
						ivAvatar.setImageResource(R.drawable.doctor_default);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		}

	}

	/**
	 * 刷新学术收藏数据
	 */
	private void refreshCollect() {
		if (NetUtil.checkNet(this)) {
			// isRefresh = true;
			pageIndex = 1;
			new GetCollectDataTask().execute();
		} else {
			showShortToast(R.string.NoSignalException);
		}
	}

	OnItemClickListener clicklistener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			AcademicAbstractBean bean = list.get(position);
			Bundle b = new Bundle();
			b.putSerializable(Constants.EXTRA_DATA, bean);
			openActivity(AbstractDetailActivity.class, b);
		}
	};
	/**
	 * 滚动监听器
	 */
	OnScrollListener LoadListener = new OnScrollListener() {
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem + visibleItemCount == totalItemCount) {
				isLoadMore = true;
			} else {
				isLoadMore = false;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// 滚动到最后，默认加载下一页
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && isLoadMore) {
				if (NetUtil.checkNet(context)) {
					if (!isLoad && !isComplete) {
						new GetCollectDataTask().execute();
					}
				} else {
					showShortToast(R.string.NoSignalException);
				}
			} else {

			}
		}
	};

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_info:
			btnRight.setVisibility(View.VISIBLE);
			viewInfo.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			break;
		case R.id.rb_collect:
			list.clear();
			refreshCollect();
			btnRight.setVisibility(View.INVISIBLE);
			viewInfo.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;
		case R.id.ivAvatar:
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
			dialogType = "province";
			showDialog(provinceList);
			break;
		case R.id.viewDoctorHospital:
			dialogType = "hospital";
			showDialog(hospitalList);
			break;
		case R.id.viewDoctorDepartment:
			dialogType = "department";
			showDialog(departmentList);
			break;
		case R.id.viewJobTitle:
			dialogType = "job";
			showDialog(positionList);
			break;
		case R.id.btnRight:
			String doctorName = etDoctorName.getText().toString().trim();
			String doctorEmil = etDoctorEmil.getText().toString().trim();
			String doctorNumber = etDoctorNumber.getText().toString().trim();
			if (StringUtil.isBlank(doctorName) || StringUtil.isBlank(doctorEmil) || StringUtil.isBlank(doctorNumber)) {
				showShortToast("请输入完整的信息");
				return;
			} else {
				if (NetUtil.checkNet(this)) {
					new PostDoctorInfor(doctorName, doctorEmil, doctorNumber).execute();
				} else {
					showShortToast(R.string.NoSignalException);
				}
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
		public PostDoctorInfor(String doctorName, String doctorEmil, String doctorNumber) {
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
			String type = "update";
			try {
				return new BusinessHelper().addDoctorInfor(uid, type, doctorName, proviceId, hospitalId, departmentId,
						positionId, doctorEmil, doctorNumber, avatarFile);
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
						showShortToast("资料修改失败");
					}
				} catch (JSONException e) {
					showShortToast(R.string.json_exception);
				}
			} else {
				// showShortToast("服务连接失败");
			}
		}

	}

	/**
	 * 获取医生的相关数据
	 * 
	 * */
	private class PostDoctorInforTask extends AsyncTask<Void, Void, ResponseBean<DoctorProvinceBean>> {

		@Override
		protected ResponseBean<DoctorProvinceBean> doInBackground(Void... params) {
			return new BusinessHelper().getDoctorData();
		}

		@Override
		protected void onPostExecute(ResponseBean<DoctorProvinceBean> result) {
			super.onPostExecute(result);
			dismissPd();
			if (result.getStatus() == Constants.REQUEST_SUCCESS) {
				List<DoctorProvinceBean> tempList = result.getObjList();
				provinceList.addAll(tempList);
				if (provinceList.size() > 0) {
					hospitalList.addAll(provinceList.get(0).getList());
					if (hospitalList.size() > 0) {
						departmentList.addAll(hospitalList.get(0).getList());
						if (departmentList.size() > 0) {
							positionList.addAll(departmentList.get(0).getList());
						}
					}
				}
			} else {
				showShortToast(result.getError());
			}
		}
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
			if (dialogType.equals("province")) {
				hospitalList.clear();
				hospitalList.addAll(provinceList.get(arg2).getList());
				tvDoctorAddress.setText(provinceList.get(arg2).getProvinceName());
				tvDoctorHospital.setText("");
				tvDoctorDepartment.setText("");
				tvJobTitle.setText("");
				proviceId = provinceList.get(arg2).getProvinceId();
			} else if (dialogType.equals("hospital")) {
				departmentList.clear();
				departmentList.addAll(hospitalList.get(arg2).getList());
				tvDoctorHospital.setText(hospitalList.get(arg2).getHospitalName());
				tvDoctorDepartment.setText("");
				tvJobTitle.setText("");
				hospitalId = hospitalList.get(arg2).getHospitalId();
			} else if (dialogType.equals("department")) {
				positionList.clear();
				positionList.addAll(departmentList.get(arg2).getList());
				tvDoctorDepartment.setText(departmentList.get(arg2).getDepartmentName());
				tvJobTitle.setText("");
				departmentId = departmentList.get(arg2).getDepartmentId();
			} else if (dialogType.equals("job")) {
				tvJobTitle.setText(positionList.get(arg2).getPositionName());
				positionId = positionList.get(arg2).getPositionId();
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
			DoctorProvinceBean bean =null;
			DoctorHospitalBean bean1 =null;
			DoctorDepartmentBean bean2 =null;
			DoctorBelongDepartmentBean bean3 =null;
//			String name = list.get(position).toString();
			if (dialogType.equals("province")) {
				 bean = (DoctorProvinceBean) list.get(position);
			}else if(dialogType.equals("hospital")) {
				 bean1 =(DoctorHospitalBean) list.get(position);
			} else if (dialogType.equals("department")){
				 bean2  = (DoctorDepartmentBean) list.get(position);
			}else if(dialogType.equals("job")){
				 bean3 =(DoctorBelongDepartmentBean) list.get(position);
			}
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.dialog_doctor_item, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.tvDoctorInfor);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (dialogType.equals("province")) {
				holder.tvName.setText(bean.getProvinceName());
			}else if(dialogType.equals("hospital")) {
				holder.tvName.setText(bean1.getHospitalName());
			} else if (dialogType.equals("department")){
				holder.tvName.setText(bean2.getDepartmentName());
			}else if(dialogType.equals("job")){
				holder.tvName.setText(bean3.getPositionName());
			}
			
			return convertView;
		}

		class ViewHolder {
			private TextView tvName;
		}

	}

	/**
	 * 收藏适配
	 * 
	 * @author Zhoujun
	 * 
	 */
	private class CollectionAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			final AcademicAbstractBean bean = list.get(position);
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.academic_abstract_item, null);
				viewHolder.title = (TextView) convertView.findViewById(R.id.academic_title);
				viewHolder.content = (TextView) convertView.findViewById(R.id.academic_content);
				viewHolder.btnCollect = (Button) convertView.findViewById(R.id.btnCollect);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.title.setText(bean.getTitle());
			viewHolder.content.setText(bean.getContent());
			String content = bean.getContent();
			if (content.length() > 120) {
				content = content.substring(0, 120) + "...";
			}
			viewHolder.content.setText(bean.getContent());
			if (bean.isCollect()) {
				viewHolder.btnCollect.setText(R.string.doctor_my_collect_cancel);
			} else {
				viewHolder.btnCollect.setText(R.string.academic_abstract_collect);
			}
			viewHolder.btnCollect.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (NetUtil.checkNet(DoctorInfoEditActivity.this)) {
						new CollectTask(bean).execute();
					} else {
						showShortToast(R.string.NoSignalException);
					}
				}
			});
			return convertView;
		}

		class ViewHolder {
			public TextView title;
			public TextView content;
			public Button btnCollect;
		}

	}

	/**
	 * 收藏取消收藏接口
	 * 
	 * @author Zhoujun
	 * 
	 */
	private class CollectTask extends AsyncTask<Void, Void, JSONObject> {
		private AcademicAbstractBean bean;

		public CollectTask(AcademicAbstractBean bean) {
			super();
			this.bean = bean;
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			int doctorId = SharedPrefUtil.getUid(DoctorInfoEditActivity.this);
			try {
				return new BusinessHelper().collectAbstract(bean.getId(), doctorId);
			} catch (SystemException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					int status = result.getInt("code");
					if (status == Constants.REQUEST_SUCCESS) {
						boolean isCollect = bean.isCollect();
						bean.setCollect(!isCollect);

						if (isCollect) {
							showShortToast("取消收藏成功");
							list.remove(bean);
						} else {
							showShortToast("收藏成功");
						}
						collectionAdapter.notifyDataSetChanged();
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
	 * 
	 * @author Zhoujun
	 * 
	 */
	private class GetCollectDataTask extends AsyncTask<Void, Void, ResponseBean<AcademicAbstractBean>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (isLoadMore) {
				isLoad = true;
				pbFooter.setVisibility(View.VISIBLE);
				tvFooterMore.setText(R.string.loading);
			} else {
				showPd(R.string.loading);
			}

		}

		@Override
		protected ResponseBean<AcademicAbstractBean> doInBackground(Void... params) {
			int doctor_id = SharedPrefUtil.getUid(context);
			return new BusinessHelper().getCollectAcademicAbstract(pageIndex, doctor_id);
		}

		@Override
		protected void onPostExecute(ResponseBean<AcademicAbstractBean> result) {
			super.onPostExecute(result);
			pbFooter.setVisibility(View.GONE);
			dismissPd();
			if (result.getStatus() == Constants.REQUEST_SUCCESS) {
				List<AcademicAbstractBean> tempList = result.getObjList();
				boolean isLastPage = false;
				if (tempList.size() > 0) {
					list.addAll(tempList);
					pageIndex++;
				} else {
					isLastPage = true;
				}
				if (isLastPage) {
					pbFooter.setVisibility(View.GONE);
					tvFooterMore.setText(R.string.load_all);
					isComplete = true;
				} else {
					if (tempList.size() > 0 && tempList.size() < Constants.PAGE_SIZE) {
						pbFooter.setVisibility(View.GONE);
						tvFooterMore.setText("");
						isComplete = true;
					} else {
						pbFooter.setVisibility(View.GONE);
						tvFooterMore.setText("上拉查看更多");
					}
				}
				if (pageIndex == 1 && tempList.size() == 0) {
					tvFooterMore.setText("");
				}

			} else {
				tvFooterMore.setText("");
				showShortToast(result.getError());
			}
			collectionAdapter.notifyDataSetChanged();
			isLoad = false;
		}
	}

	/**
	 * 获取医生详情
	 * 
	 * @author Zhoujun
	 * 
	 */
	private class GetDoctorTask extends AsyncTask<Void, Void, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showPd(R.string.loading);
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			int id = SharedPrefUtil.getUid(DoctorInfoEditActivity.this);
			try {
				return new BusinessHelper().getDoctorInfo(id);
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
						JSONObject obj = result.getJSONArray("doctor_list").getJSONObject(0);
						SharedPrefUtil.setDoctorInfor(DoctorInfoEditActivity.this, obj.toString());

						etDoctorName.setText(obj.getString("real_name"));
						tvDoctorAddress.setText(obj.getString("province"));
						tvDoctorHospital.setText(obj.getString("hospital"));
						tvDoctorDepartment.setText(obj.getString("department"));
						tvJobTitle.setText(obj.getString("positions"));
						etDoctorEmil.setText(obj.getString("email"));
						etDoctorNumber.setText(obj.getString("tel"));
						tvName.setText(obj.getString("doctor_name"));

						String avatarUrl = BusinessHelper.PIC_URL + obj.getString("picture_path");
						ivAvatar.setTag(avatarUrl);
						Drawable cacheDrawable = AsyncImageLoader.getInstance().loadAsynLocalDrawable(avatarUrl,
								new ImageCallback() {

									@Override
									public void imageLoaded(Drawable imageDrawable, String imageUrl) {
										ImageView image = (ImageView) ivAvatar.findViewWithTag(imageUrl);
										if (image != null) {
											if (imageDrawable != null) {
												ivAvatar.setImageDrawable(imageDrawable);
											} else {
												ivAvatar.setImageResource(R.drawable.doctor_default);
											}
										}
									}
								});
						if (cacheDrawable != null) {
							ivAvatar.setImageDrawable(cacheDrawable);
						} else {
							ivAvatar.setImageResource(R.drawable.doctor_default);
						}
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

	private long exitTime;

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
}
