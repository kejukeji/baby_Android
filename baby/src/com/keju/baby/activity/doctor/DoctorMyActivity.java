package com.keju.baby.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.keju.baby.AsyncImageLoader;
import com.keju.baby.AsyncImageLoader.ImageCallback;
import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.SystemException;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.bean.AcademicAbstractBean;
import com.keju.baby.bean.ResponseBean;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.AndroidUtil;
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
	private CollectionAdapter adapter;
	private List<AcademicAbstractBean> collectList;
	private long exitTime = 0;

	private String doctorName, doctorAddress, doctorHospital, doctorDepartment, doctorJop, doctorEmail, doctorPhone,
			avatarUrl;

	private View vFooter;
	private ProgressBar pbFooter;
	private TextView tvFooterMore;
	private boolean isLoad = false;// 是否正在加载数据
	private boolean isLoadMore = false;
	private boolean isComplete = false;// 是否加载完了；
	
	private int collectPageIndex = 1;

//	private boolean isRefresh = false;

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
		
		// 加载收藏更多footer
		vFooter = getLayoutInflater().inflate(R.layout.footer, null);
		pbFooter = (ProgressBar) vFooter.findViewById(R.id.progressBar);
		tvFooterMore = (TextView) vFooter.findViewById(R.id.tvMore);
		
		listView = (ListView) findViewById(R.id.listView);
		listView.addFooterView(vFooter);
		listView.setOnScrollListener(LoadListener);
		listView.setOnItemClickListener(itemListener);
	}

	/**
	 * 数据填充
	 */
	private void fillData() {
		btnLeft.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("个人中心");

		radio_group.setOnCheckedChangeListener(this);
		collectList = new ArrayList<AcademicAbstractBean>();
		adapter = new CollectionAdapter();

		listView.setAdapter(adapter);
		if (NetUtil.checkNet(this)) {
			new GetDoctorTask().execute();
			new GetCollectDataTask().execute();
		} else {
			String doctorInforStr = SharedPrefUtil.getDoctorInfor(DoctorMyActivity.this);
			JSONObject obj;
			try {
				obj = new JSONObject(doctorInforStr);
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
									ivAvatar.setImageDrawable(imageDrawable);
								} else {
									ivAvatar.setImageResource(R.drawable.item_lion);
								}
							}
						});
				if (cacheDrawable != null) {
					ivAvatar.setImageDrawable(cacheDrawable);
				} else {
					ivAvatar.setImageResource(R.drawable.item_lion);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
//			showShortToast(R.string.NoSignalException);
		
		}
	}
	/**
	 * 刷新学术收藏数据
	 */
	private void refreshCollect() {
		if (NetUtil.checkNet(this)) {
//			isRefresh = true;
			collectPageIndex = 1;
			new GetCollectDataTask().execute();
		} else {
			showShortToast(R.string.NoSignalException);
		}
	}

	/**
	 * listview点击事件
	 */
	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (collectList != null && collectList.size() > 0) {
				AcademicAbstractBean bean = collectList.get(arg2);
				Bundle b = new Bundle();
				b.putSerializable(Constants.EXTRA_DATA, bean);
				openActivity(AbstractDetailActivity.class, b);
			}
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
				if (NetUtil.checkNet(DoctorMyActivity.this)) {
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
			collectList.clear();
			refreshCollect();
			viewInfo.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			break;
		default:
			break;
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
			return collectList.size();
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
			final AcademicAbstractBean bean = collectList.get(position);
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
					if (NetUtil.checkNet(DoctorMyActivity.this)) {
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
			int doctorId = SharedPrefUtil.getUid(DoctorMyActivity.this);
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
							collectList.remove(bean);
						} else {
							showShortToast("收藏成功");
						}
						adapter.notifyDataSetChanged();
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
			return new BusinessHelper().getCollectAcademicAbstract(collectPageIndex, doctor_id);
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
					collectList.addAll(tempList);
					collectPageIndex++;
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
				if (collectPageIndex == 1 && tempList.size() == 0) {
					tvFooterMore.setText("");
				}

			} else {
				tvFooterMore.setText("");
				showShortToast(result.getError());
			}
			adapter.notifyDataSetChanged();
			isLoad = false;
		}
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
						SharedPrefUtil.setDoctorInfor(DoctorMyActivity.this, obj.toString());
						
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
											ivAvatar.setImageDrawable(imageDrawable);
										} else {
											ivAvatar.setImageResource(R.drawable.item_lion);
										}
									}
								});
						if (cacheDrawable != null) {
							ivAvatar.setImageDrawable(cacheDrawable);
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
