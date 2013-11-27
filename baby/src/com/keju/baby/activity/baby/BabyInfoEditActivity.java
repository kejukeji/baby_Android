package com.keju.baby.activity.baby;

import java.io.File;
import java.io.FileOutputStream;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.SystemException;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.ImageUtil;
import com.keju.baby.util.NetUtil;
import com.keju.baby.util.SharedPrefUtil;
import com.keju.baby.util.StringUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 宝宝资料编辑
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:16:24
 */
public class BabyInfoEditActivity extends BaseActivity implements OnClickListener {

	private Button btnLeft, btnRight;
	private TextView tvTitle;

	private LinearLayout viewUserPhone;// 用户头像
	private ImageView ivUserPhone;
	private EditText etParentNumber, etBabyName, etBabySix;
	private EditText etBabyProduction;// 宝宝的预产期
	private EditText etBabyWeight, etBabyHeight, etBabyHeadCircumference;// 宝宝的体重/身长
	private EditText etChildbirthWay, etComplication, etGrade;// 分娩方式/合并症/评分

	private File mCurrentPhotoFile;// 照相机拍照得到的图片，临时文件
	private File avatarFile;// 头像文件
	private File PHOTO_DIR;// 照相机拍照得到的图片的存储位置
	static final int DATE_DIALOG_ID = 1;
	private Bitmap cameraBitmap;// 头像bitmap
	private long userId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_info_edit_activity);

		findView();
		fillData();
		createPhotoDir();
	}

	private void findView() {

		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		viewUserPhone = (LinearLayout) findViewById(R.id.viewUserPhone);
		viewUserPhone.setOnClickListener(this);

		ivUserPhone = (ImageView) findViewById(R.id.ivUserPhone);
		etParentNumber = (EditText) findViewById(R.id.etParentNumber);
		etBabyName = (EditText) findViewById(R.id.etBabyName);
		etBabySix = (EditText) findViewById(R.id.etBabySix);
		etBabyProduction = (EditText) findViewById(R.id.etBabyProduction);
		etBabyWeight = (EditText) findViewById(R.id.etBabyWeight);
		etBabyHeight = (EditText) findViewById(R.id.etBabyHeight);
		etBabyHeadCircumference = (EditText) findViewById(R.id.etBabyHeadCircumference);
		etChildbirthWay = (EditText) findViewById(R.id.etChildbirthWay);
		etComplication = (EditText) findViewById(R.id.etComplication);
		etGrade = (EditText) findViewById(R.id.etGrade);

	}

	/**
	 * 数据填充
	 */
	private void fillData() {

		btnLeft.setText("Back");
		btnRight.setText("提交");
		tvTitle.setText("修改资料");

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
						ivUserPhone.setImageBitmap(cameraBitmap);
					}
					if (mCurrentPhotoFile != null && mCurrentPhotoFile.exists())
						mCurrentPhotoFile.delete();
				} catch (Exception e) {
					MobclickAgent.reportError(BabyInfoEditActivity.this, StringUtil.getExceptionInfo(e));
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
		case R.id.btnRight:
			String babyName = etBabyName.getText().toString().trim();
			String parentNumber = etParentNumber.getText().toString().trim();
			String babySix = etBabySix.getText().toString().trim();
			String babyProduction = etBabyProduction.getText().toString().trim();
			String babyWeight = etBabyWeight.getText().toString().trim();
			String babyHeight = etBabyHeight.getText().toString().trim();
			String babyHeadCircumference = etBabyHeadCircumference.getText().toString().trim();
			String childbirthWay = etChildbirthWay.getText().toString().trim();
			String complication = etComplication.getText().toString().trim();
			String grade = etGrade.getText().toString().trim();
			if (NetUtil.checkNet(this)) {
				new PostBabyInfor(babyName, parentNumber, babySix, babyProduction, babyWeight, babyHeight,
						babyHeadCircumference, childbirthWay, complication, grade).execute();
			} else {
				showShortToast(R.string.NoSignalException);
			}
			break;
		case R.id.viewUserPhone:
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
			MobclickAgent.reportError(BabyInfoEditActivity.this, StringUtil.getExceptionInfo(e));
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
	 * 提交Baby资料
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
			if (babyWeight.equals("") ) {
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
			int uid = SharedPrefUtil.getUid(BabyInfoEditActivity.this);
			String type ="update";
			try {
				return new BusinessHelper().addBabyInfor(1,type,babyName, parentNumber, babySex, babyProduction,
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
