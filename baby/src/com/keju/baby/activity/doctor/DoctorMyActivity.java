package com.keju.baby.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.keju.baby.AsyncImageLoader;
import com.keju.baby.AsyncImageLoader.ImageCallback;
import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.SystemException;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.bean.MyCollectBean;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.AndroidUtil;
import com.keju.baby.util.ImageUtil;
import com.keju.baby.util.NetUtil;
import com.keju.baby.util.SharedPrefUtil;

/**
 * 医生资料界面
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:53:44
 */
public class DoctorMyActivity extends BaseActivity implements OnCheckedChangeListener, OnClickListener {
	private ImageView btnLeft, btnRight;
	private TextView tvTitle;
	private RadioGroup radio_group;
	private View viewInfo;
	private ImageView ivAvatar;
	private TextView tvName, tvId, tvRealName, tvArea, tvHospital, tvDepartment, tvJob, tvEmail, tvPhone;

	private ListView listView;
	private Adapter adapter;
	private List<MyCollectBean> list;
	private long exitTime = 0;

	private String doctorName, doctorAddress, doctorHospital, doctorDepartment, doctorJop, doctorEmail, doctorPhone,
			avatarUrl;;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_my_activity);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		radio_group = (RadioGroup) findViewById(R.id.radio_group);
		viewInfo = findViewById(R.id.viewInfo);

		ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
		ivAvatar.setOnClickListener(this);
		tvId = (TextView) findViewById(R.id.tvId);
		tvName = (TextView) findViewById(R.id.tvName);
		tvRealName = (TextView) findViewById(R.id.tvRealName);
		tvArea = (TextView) findViewById(R.id.tvArea);
		tvHospital = (TextView) findViewById(R.id.tvHospital);
		tvDepartment = (TextView) findViewById(R.id.tvDepartment);
		tvJob = (TextView) findViewById(R.id.tvJob);
		tvEmail = (TextView) findViewById(R.id.tvEmail);
		tvPhone = (TextView) findViewById(R.id.tvPhone);

		listView = (ListView) findViewById(R.id.listView);
	}

	/**
	 * 数据填充
	 */
	private void fillData() {
		btnLeft.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("个人中心");

		radio_group.setOnCheckedChangeListener(this);
		list = new ArrayList<MyCollectBean>();
		adapter = new Adapter();

		listView.setAdapter(adapter);
		if (NetUtil.checkNet(this)) {
			new GetDoctorTask().execute();
		} else {
			showShortToast(R.string.NoSignalException);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				showShortToast(R.string.try_again_logout);
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				AndroidUtil.exitApp(this);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_info:
			viewInfo.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			break;
		case R.id.rb_collect:
			viewInfo.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}

	class Adapter extends BaseAdapter {
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
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.doctor_my_collect_listitem, null);
				viewHolder.title = (TextView) convertView.findViewById(R.id.my_collect_title);
				viewHolder.content = (TextView) convertView.findViewById(R.id.my_collect_content);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.title.setText(list.get(position).getTitle());
			viewHolder.content.setText(list.get(position).getContent());
			return convertView;
		}

	}

	class ViewHolder {
		public TextView title;
		public TextView content;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivAvatar:
			Bundle b = new Bundle();
			b.putString("DCNAME", doctorName);
			b.putString("DCADDRESS", doctorAddress);
			b.putString("DCHOSPITAL", doctorHospital);
			b.putString("DCDEPARTMENT", doctorDepartment);
			b.putString("DCJOP", doctorJop);
			b.putString("DCEMAIL", doctorEmail);
			b.putString("DCPHONE", doctorPhone);
			b.putString("DCURL", avatarUrl);
			openActivity(DoctorInfoEditActivity.class, b);
			break;
		default:
			break;
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
		protected JSONObject doInBackground(Void... params) {
			int id = SharedPrefUtil.getUid(DoctorMyActivity.this);
			try {
				return new BusinessHelper().getDoctorInfo(id);
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
						JSONObject obj = result.getJSONArray("doctor_list").getJSONObject(0);
						tvId.setText(obj.getInt("id") + "");

						tvName.setText(obj.getString("doctor_name"));
						doctorName = obj.getString("real_name");
						tvRealName.setText(doctorName);
						doctorAddress = obj.getString("province");
						tvArea.setText(doctorAddress);
						doctorHospital = obj.getString("hospital");
						tvHospital.setText(doctorHospital);
						doctorDepartment = obj.getString("department");
						tvDepartment.setText(doctorDepartment);
						doctorJop = obj.getString("positions");
						tvJob.setText(doctorJop);
						doctorEmail = obj.getString("email");
						tvEmail.setText(doctorEmail);
						doctorPhone = obj.getString("tel");
						tvPhone.setText(doctorPhone);
						avatarUrl = BusinessHelper.PIC_URL + obj.getString("picture_path");
						Drawable cacheDrawable = AsyncImageLoader.getInstance().loadAsynLocalDrawable(avatarUrl,
								new ImageCallback() {

									@Override
									public void imageLoaded(Drawable imageDrawable, String imageUrl) {
										if (imageDrawable != null) {
											Bitmap bitmap = ImageUtil.getRoundCornerBitmapWithPic(imageDrawable, 0.5f);
											ivAvatar.setImageBitmap(bitmap);
										} else {
											ivAvatar.setImageResource(R.drawable.item_lion);
										}
									}
								});
						if (cacheDrawable != null) {
							Bitmap bitmap = ImageUtil.getRoundCornerBitmapWithPic(cacheDrawable, 0.5f);
							ivAvatar.setImageBitmap(bitmap);
						} else {
							ivAvatar.setImageResource(R.drawable.item_lion);
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
}
