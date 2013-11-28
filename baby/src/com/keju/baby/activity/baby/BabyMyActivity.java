package com.keju.baby.activity.baby;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.keju.baby.AsyncImageLoader;
import com.keju.baby.AsyncImageLoader.ImageCallback;
import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.SystemException;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.AndroidUtil;
import com.keju.baby.util.ImageUtil;
import com.keju.baby.util.NetUtil;
import com.keju.baby.util.SharedPrefUtil;

/**
 * 宝宝的资料
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:25:43
 */
public class BabyMyActivity extends BaseActivity implements OnClickListener {
	private ImageView btnLeft, btnRight;
	private TextView tvTitle;

	private ImageView ivAvatar;
	private TextView tvId, tvRealName, tvGendar, tvPreproductions, tvHeight, tvWeight, tvHeadCircumference,
			tvDeliveryWay,tvComplication,tvApgar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_my_activity);
		findView();
		fillData();
	}

	private void findView() {

		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		tvId = (TextView) this.findViewById(R.id.tvId);
		tvRealName = (TextView) this.findViewById(R.id.tvRealName);
		tvGendar = (TextView) this.findViewById(R.id.tvGendar);
		tvPreproductions = (TextView) this.findViewById(R.id.tvPreproductions);
		tvHeight = (TextView) this.findViewById(R.id.tvHeight);
		tvWeight = (TextView) this.findViewById(R.id.tvWeight);
		tvHeadCircumference = (TextView) this.findViewById(R.id.tvHeadCircumference);
		tvDeliveryWay = (TextView) this.findViewById(R.id.tvDeliveryWay);
		tvComplication = (TextView) this.findViewById(R.id.tvComplication);
		tvApgar = (TextView) this.findViewById(R.id.tvApgar);

		ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
		ivAvatar.setOnClickListener(this);
		if (NetUtil.checkNet(this)) {
			new GetBabyInfor().execute();
		} else {
			showShortToast(R.string.NoSignalException);
		}
	}

	/**
	 * 数据填充
	 */
	private void fillData() {

		btnLeft.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("个人中心");
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
		case R.id.ivAvatar:
			openActivity(BabyInfoEditActivity.class);
			break;
		default:
			break;
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
				return new BusinessHelper().getBabyInfor(1);
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
				if(status==Constants.REQUEST_SUCCESS){
					if(result.has("baby_list")){
					JSONArray babyList = result.getJSONArray("baby_list");
					JSONObject babyBean = babyList.optJSONObject(0);
					tvId.setText(babyBean.getInt("id")+"");
				    tvRealName.setText(babyBean.getString("baby_name"));
				    tvGendar.setText(babyBean.getString("gender"));
				    
				    tvPreproductions.setText(babyBean.getString("due_date"));
				    tvHeight.setText(babyBean.getInt("born_height")+"cm");
				    tvWeight.setText(babyBean.getInt("born_weight")+"kg");
				    tvHeadCircumference.setText(babyBean.getInt("born_head")+"cm");
				    
				    tvDeliveryWay.setText(babyBean.getString("childbirth"));
				    tvComplication.setText(babyBean.getString("complication"));
				    tvApgar.setText(babyBean.getInt("apgar_score")+"");
				    
				    String photoUrl = BusinessHelper.PIC_URL +babyBean.getString("picture_path");
				    ivAvatar.setTag(photoUrl);
					Drawable cacheDrawble = AsyncImageLoader.getInstance().loadDrawable(photoUrl,
							new ImageCallback() {
								@Override
								public void imageLoaded(Drawable imageDrawable, String imageUrl) {
									ImageView image = (ImageView) ivAvatar.findViewWithTag(imageUrl);
									if (image != null) {
										if (imageDrawable != null) {
											image.setImageBitmap(ImageUtil.getRoundCornerBitmapWithPic(imageDrawable, 0.5f));
										} else {
											image.setImageBitmap(ImageUtil.getRoundCornerBitmapWithPic(getResources().getDrawable(R.drawable.item_lion), 0.5f));
										}
									}
								}
							});
					if (cacheDrawble != null) {
						ivAvatar.setImageBitmap(ImageUtil.getRoundCornerBitmapWithPic(cacheDrawble, 0.5f));
					} else {
						ivAvatar.setImageBitmap(ImageUtil.getRoundCornerBitmapWithPic(getResources().getDrawable(R.drawable.item_lion), 0.5f));
					}
					}else{
						showShortToast("数据加载失败");
					}
				}
			} catch (JSONException e) {
				showShortToast(R.string.json_exception);
			}
			}else{
				showShortToast("服务器请求失败");
			}
		}

	}
	
}
