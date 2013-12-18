package com.keju.baby.activity.baby;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.keju.baby.AsyncImageLoader;
import com.keju.baby.AsyncImageLoader.ImageCallback;
import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.SystemException;
import com.keju.baby.activity.ComplicationActivity;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.AndroidUtil;
import com.keju.baby.util.DateUtil;
import com.keju.baby.util.ImageUtil;
import com.keju.baby.util.NetUtil;
import com.keju.baby.util.SharedPrefUtil;
import com.keju.baby.util.StringUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 宝宝的资料
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:25:43
 */
public class BabyMyActivity extends BaseActivity implements OnClickListener {
	private ImageView btnLeft, btnRight;
	private TextView tvTitle;

	private TextView tvId, tvGendar, tvPreproductions, tvDeliveryWay, tvComplication, tvApgar;
	private EditText etRealName, etHeight, etWeight, etHeadCircumference;

	private ImageView ivAvatar;
	private File mCurrentPhotoFile;// 照相机拍照得到的图片，临时文件
	private File avatarFile;// 头像文件
	private File PHOTO_DIR;// 照相机拍照得到的图片的存储位置
	private Bitmap cameraBitmap;// 头像bitmap

	private String dialogType;
	private List<String> list = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_my_activity);
		findView();
		fillData();
		createPhotoDir();
	}

	private void findView() {

		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		tvId = (TextView) this.findViewById(R.id.tvId);
		etRealName = (EditText) this.findViewById(R.id.etRealName);
		tvGendar = (TextView) this.findViewById(R.id.tvGendar);
		tvPreproductions = (TextView) this.findViewById(R.id.tvPreproductions);
		etHeight = (EditText) this.findViewById(R.id.etHeight);
		etWeight = (EditText) this.findViewById(R.id.etWeight);
		etHeadCircumference = (EditText) this.findViewById(R.id.etHeadCircumference);
		tvDeliveryWay = (TextView) this.findViewById(R.id.tvDeliveryWay);
		tvComplication = (TextView) this.findViewById(R.id.tvComplication);
		tvApgar = (TextView) this.findViewById(R.id.tvApgar);

		ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
		ivAvatar.setOnClickListener(this);

		tvGendar.setOnClickListener(this);
		tvPreproductions.setOnClickListener(this);
		etHeight.setOnClickListener(this);
		etWeight.setOnClickListener(this);
		etHeadCircumference.setOnClickListener(this);
		tvDeliveryWay.setOnClickListener(this);
		tvComplication.setOnClickListener(this);
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
						ivAvatar.setImageBitmap(ImageUtil.getRoundedCornerBitmapWithPic(cameraBitmap, 0.5f));
					} else {
						ivAvatar.setImageBitmap(ImageUtil.getRoundCornerBitmapWithPic(
								getResources().getDrawable(R.drawable.item_lion), 0.5f));
					}
					if (mCurrentPhotoFile != null && mCurrentPhotoFile.exists())
						mCurrentPhotoFile.delete();
				} catch (Exception e) {

				}
				break;
			case Constants.CAMERA_WITH_DATA:// 拍照
				doCropPhoto(mCurrentPhotoFile);
				break;
			case Constants.REQUEST_COMPLICATION:
				tvComplication.setText(data.getStringExtra(Constants.EXTRA_DATA));
				break;
			}
		}
	}

	/**
	 * 数据填充
	 */
	private void fillData() {

		btnLeft.setVisibility(View.GONE);
		btnRight.setBackgroundResource(R.drawable.btn_commit_selector);
		btnRight.setOnClickListener(this);
		tvTitle.setText("个人中心");
		if (NetUtil.checkNet(this)) {
			new GetBabyInfor().execute();
		} else {
			showShortToast(R.string.NoSignalException);
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
			getParent().startActivityForResult(intent, Constants.PHOTO_PICKED_WITH_DATA);

		} catch (Exception e) {
			MobclickAgent.reportError(this, StringUtil.getExceptionInfo(e));
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRight:
			String babyName = etRealName.getText().toString().trim();
			String parentNumber = tvId.getText().toString().trim();
			String babySix = tvGendar.getText().toString().trim();
			String babyProduction = tvPreproductions.getText().toString().trim();
			String babyWeight = etWeight.getText().toString().trim();
			String babyHeight = etHeight.getText().toString().trim();
			String babyHeadCircumference = etHeadCircumference.getText().toString().trim();
			String childbirthWay = tvDeliveryWay.getText().toString().trim();
			String complication = tvComplication.getText().toString().trim();
			String grade = tvApgar.getText().toString().trim();
			if (NetUtil.checkNet(this)) {
				new PostBabyInfor(babyName, parentNumber, babySix, babyProduction, babyWeight, babyHeight,
						babyHeadCircumference, childbirthWay, complication, grade).execute();
			} else {
				showShortToast(R.string.NoSignalException);
			}
			break;
		case R.id.tvGendar:
			dialogType = "gender";
			list.clear();
			list.add("男");
			list.add("女");
			showDialog(list);
			break;
		case R.id.tvPreproductions:
			showDateDialog();
			break;
		case R.id.tvDeliveryWay:
			dialogType = "diliveryWay";
			list.clear();
			list.add("剖腹产");
			list.add("顺产");
			showDialog(list);
			break;
		case R.id.tvComplication:
			Bundle b= new Bundle();
			b.putString(Constants.EXTRA_DATA, tvComplication.getText().toString());
			Intent intent = new Intent(this, ComplicationActivity.class);
			intent.putExtras(b);
			startActivityForResult(intent, Constants.REQUEST_COMPLICATION);
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
		tvPreproductions.setText(DateUtil.dateToString("yyyy-MM-dd", cal.getTime()));
	}

	private ProviceAdapter adapter;
	private Dialog dialog;

	/**
	 * 显示dialog
	 */
	private void showDialog(List<String> list) {
		View view = getLayoutInflater().inflate(R.layout.dialog_doctor_list, null);
		ListView addressList = (ListView) view.findViewById(R.id.doctorListView);
		adapter = new ProviceAdapter(list);
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
				tvGendar.setText((String) adapter.getItem(arg2));
			} else if (dialogType.equals("height")) {
				etHeight.setText((String) adapter.getItem(arg2));
			} else if (dialogType.equals("weight")) {
				etWeight.setText((String) adapter.getItem(arg2));
			} else if (dialogType.equals("head")) {
				etHeadCircumference.setText((String) adapter.getItem(arg2));
			} else if (dialogType.equals("diliveryWay")) {
				tvDeliveryWay.setText((String) adapter.getItem(arg2));
			}
			dialog.dismiss();
		}
	};

	/**
	 * 获取医生城市的适配器
	 * 
	 * */
	private class ProviceAdapter extends BaseAdapter {
		private List<String> list;

		public ProviceAdapter(List<String> list) {
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

	/***
	 * 获得Baby资料
	 */
	private class GetBabyInfor extends AsyncTask<Void, Void, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showPd("正在加载...");
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			int uid = SharedPrefUtil.getUid(BabyMyActivity.this);
			try {
				return new BusinessHelper().getBabyInfor(uid);
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
						if (result.has("baby_list")) {
							JSONArray babyList = result.getJSONArray("baby_list");
							JSONObject babyBean = babyList.optJSONObject(0);
							tvId.setText(babyBean.getString("patriarch_tel"));
							etRealName.setText(babyBean.getString("baby_name"));
							tvGendar.setText(babyBean.getString("gender"));
							tvPreproductions.setText(babyBean.getString("due_date"));
							etHeight.setText(babyBean.getInt("born_height") + "");
							etWeight.setText(babyBean.getInt("born_weight") + "");
							etHeadCircumference.setText(babyBean.getInt("born_head") + "");
							tvDeliveryWay.setText(babyBean.getString("childbirth_style"));
							tvComplication.setText(babyBean.getString("complication"));
							tvApgar.setText(babyBean.getInt("apgar_score") + "");
							String photoUrl = BusinessHelper.PIC_URL + babyBean.getString("picture_path");
							ivAvatar.setTag(photoUrl);
							Drawable cacheDrawble = AsyncImageLoader.getInstance().loadDrawable(photoUrl,
									new ImageCallback() {
										@Override
										public void imageLoaded(Drawable imageDrawable, String imageUrl) {
											ImageView image = (ImageView) ivAvatar.findViewWithTag(imageUrl);
											if (image != null) {
												if (imageDrawable != null) {
													image.setImageBitmap(ImageUtil.getRoundCornerBitmapWithPic(
															imageDrawable, 0.5f));
												} else {
													image.setImageBitmap(ImageUtil.getRoundCornerBitmapWithPic(
															getResources().getDrawable(R.drawable.item_lion), 0.5f));
												}
											}
										}
									});
							if (cacheDrawble != null) {
								ivAvatar.setImageBitmap(ImageUtil.getRoundCornerBitmapWithPic(cacheDrawble, 0.5f));
							} else {
								ivAvatar.setImageBitmap(ImageUtil.getRoundCornerBitmapWithPic(getResources()
										.getDrawable(R.drawable.item_lion), 0.5f));
							}
						} else {
							showShortToast("数据加载失败");
						}
					}
				} catch (JSONException e) {
					showShortToast(R.string.json_exception);
				}
			} else {
				showShortToast("服务器请求失败");
			}
		}
	}

	/**
	 * 修改婴儿资料
	 * 
	 * @author Zhoujun
	 * 
	 */
	private class PostBabyInfor extends AsyncTask<Void, Void, JSONObject> {
		private String babyName = null;
		private String parentNumber = null;
		private String babySex = null;
		private String babyProduction = null;
		private int babyWeight = 0;
		private int babyHeight = 0;
		private int babyHeadCircumference = 0;
		private String childbirthWay = null;
		private String complication = null;
		private int grade = 0;

		/**
		 * @param babyName
		 * @param parentNumber
		 * @param babySex
		 * @param babyProduction
		 * @param babyWeight
		 * @param babyHeight
		 * @param babyHeadCircumference
		 * @param childbirthWay
		 * @param complication
		 * @param grade
		 */
		public PostBabyInfor(String babyName, String parentNumber, String babySex, String babyProduction,
				String babyWeight, String babyHeight, String babyHeadCircumference, String childbirthWay,
				String complication, String grade) {

			this.babyName = babyName;
			this.parentNumber = parentNumber;
			this.babySex = babySex;
			this.babyProduction = babyProduction;
			if (babyWeight.equals("")) {
				this.babyWeight = 0;
			} else {
				this.babyWeight = Integer.parseInt(babyWeight);
			}
			if (babyHeight.equals("")) {
				this.babyHeight = 0;
			} else {
				this.babyHeight = Integer.parseInt(babyHeight);
			}
			if (babyHeadCircumference.equals("")) {
				this.babyHeadCircumference = 0;
			} else {
				this.babyHeadCircumference = Integer.parseInt(babyHeadCircumference);
			}
			this.childbirthWay = childbirthWay;
			this.complication = complication;
			if (grade.equals("")) {
				this.grade = 0;
			} else {
				this.grade = Integer.parseInt(grade);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showShortToast("正在修改...");
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			int uid = SharedPrefUtil.getUid(BabyMyActivity.this);
			String type = "update";
			try {
				return new BusinessHelper().addBabyInfor(uid, type, babyName, parentNumber, babySex, babyProduction,
						babyWeight, babyHeight, babyHeadCircumference, childbirthWay, complication, grade, avatarFile);
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
				showShortToast("服务连接失败");
			}
		}

	}
}
