package com.keju.baby.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
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
import com.keju.baby.activity.baby.BabyDetailActivity;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.bean.BabyBean;
import com.keju.baby.bean.ResponseBean;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.AndroidUtil;
import com.keju.baby.util.NetUtil;
import com.keju.baby.util.SharedPrefUtil;

/**
 * 医生首页界面
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:51:05
 */

public class DoctorHomeActivity extends BaseActivity implements OnCheckedChangeListener, OnItemClickListener,
		OnClickListener {
	private RadioGroup doctorHomeRadioGroup; // 主页面radiogroup
	private ListView listView; //
	private List<BabyBean> list; // adapter数据源
	private List<BabyBean> allList;//所有的list
	private boolean isAllLoad = false;// 是否正在加载数据
	private boolean isAllLoadMore = false;
	private boolean isAllComplete = false;// 是否加载完了；

	private int allPageIndex = 1;
	private List<BabyBean> collectList;//收藏的list
	private HomeAdapter adapter;
	private View vFooter;
	private ProgressBar pbFooter;
	private TextView tvFooterMore;
	
	private boolean isCollectLoad = false;// 是否正在加载数据
	private boolean isCollectLoadMore = false;
	private boolean isCollectComplete = false;// 是否加载完了；
	private int collectPageIndex = 1;
	
	
	private long exitTime;
	private ImageView btnLeft, btnRight;
	private TextView tvTitle;
	private boolean isRefresh = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_home);
		findView();
		fillData();
	}

	private void findView() {

		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnLeft.setImageResource(R.drawable.btn_create_account_selector);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		btnRight.setImageResource(R.drawable.btn_search_selector);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		btnRight.setOnClickListener(this);
		btnLeft.setOnClickListener(this);

		// 加载更多footer
		vFooter = getLayoutInflater().inflate(R.layout.footer, null);
		pbFooter = (ProgressBar) vFooter.findViewById(R.id.progressBar);
		tvFooterMore = (TextView) vFooter.findViewById(R.id.tvMore);
		listView = (ListView) findViewById(R.id.listView);
		
		doctorHomeRadioGroup = (RadioGroup) findViewById(R.id.dochome_radio_group);
		
	}
	
	/**
	 * 数据填充
	 */
	private void fillData() {
		tvTitle.setText("营养随访体系");
		
		adapter = new HomeAdapter();
		list = new ArrayList<BabyBean>();
		allList = new ArrayList<BabyBean>();
		collectList = new ArrayList<BabyBean>();
		listView.addFooterView(vFooter);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(LoadListener);
		listView.setOnItemClickListener(itemListener);
		doctorHomeRadioGroup.setOnCheckedChangeListener(this);
		
		if (NetUtil.checkNet(this)) {
			new GetBabyListTask().execute();
			new GetCollectBabyListTask().execute();
		} else {
			showShortToast(R.string.NoSignalException);
		}
	}
	/**
	 * 刷新数据
	 */
	private void refreshData() {
		if (NetUtil.checkNet(this)) {
			isRefresh = true;
			allPageIndex = 1;
			new GetBabyListTask().execute();
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
			if (list != null && list.size() > 0) {
				Bundle b = new Bundle();
				b.putSerializable(Constants.EXTRA_DATA, list.get(arg2));
				openActivity(BabyDetailActivity.class,b);
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
				isAllLoadMore = true;
				isCollectLoadMore = true;
			} else {
				isAllLoadMore = false;
				isCollectLoadMore = false;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// 滚动到最后，默认加载下一页
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && isAllLoadMore) {
				if (NetUtil.checkNet(context)) {
					if(doctorHomeRadioGroup.getCheckedRadioButtonId() == R.id.dochome_allbaby){
						if (!isAllLoad && !isAllComplete) {
							new GetBabyListTask().execute();
						}
					}else{
						if (!isCollectLoad && !isCollectComplete) {
							new GetCollectBabyListTask().execute();
						}
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
				AndroidUtil.exitApp(this);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.dochome_allbaby:
			list.clear();
			list.addAll(allList);
			adapter.notifyDataSetChanged();
			break;
		case R.id.dochome_mycollect:
			list.clear();
			list.addAll(collectList);
			adapter.notifyDataSetChanged();
			break;
		}
	}

	private class HomeAdapter extends BaseAdapter {

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
			final BabyBean bean = list.get(position);
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.doctor_home_item, null);
				holder.ivAvatar = (ImageView) convertView.findViewById(R.id.ivAvatar);
				holder.ivCollect = (ImageView) convertView.findViewById(R.id.ivCollect);
				holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
				holder.tvAge = (TextView) convertView.findViewById(R.id.tvAge);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String url = bean.getAvatarUrl();
			holder.ivAvatar.setTag(url);
			Drawable cacheDrawble = AsyncImageLoader.getInstance().loadDrawable(url, new ImageCallback() {

				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					ImageView image = (ImageView) listView.findViewWithTag(imageUrl);
					if (image != null) {
						if (imageDrawable != null) {
							image.setImageDrawable(imageDrawable);
						} else {
							image.setImageResource(R.drawable.item_lion);
						}
					}
				}
			});
			if (cacheDrawble != null) {
				holder.ivAvatar.setImageDrawable(cacheDrawble);
			} else {
				holder.ivAvatar.setImageResource(R.drawable.item_lion);
			}
			if(bean.isCollect()){
				holder.ivCollect.setImageResource(R.drawable.ic_collected);
			}else{
				holder.ivCollect.setImageResource(R.drawable.ic_collect_not);
			}
			holder.ivCollect.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(NetUtil.checkNet(DoctorHomeActivity.this)){
						new CollectTask(bean).execute();
					}else{
						showShortToast(R.string.NoSignalException);
					}
				}
			});
			holder.tvName.setText(bean.getName());
			holder.tvAge.setText(bean.getAge());
			return convertView;
		}

	}

	class ViewHolder {
		public ImageView ivAvatar,ivCollect;
		public TextView tvName, tvAge;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRight:
			openActivity(SearchActivity.class);
			break;
		case R.id.btnLeft:
			Intent intent = new Intent(this,DoctorCreatBabyAccountActivity.class);
			startActivityForResult(intent, Constants.REQUEST_CREATE_BABY);
			break;
		default:
			break;
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == Constants.REQUEST_CREATE_BABY ){
			refreshData();
		}
	}
	/**
	 * 获取婴儿列表
	 * @author Zhoujun
	 *
	 */
	private class GetBabyListTask extends AsyncTask<Void, Void, ResponseBean<BabyBean>> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(isAllLoadMore){
				isAllLoad = true;
				pbFooter.setVisibility(View.VISIBLE);
				tvFooterMore.setText(R.string.loading);
			}
			if(isRefresh){
				showPd(R.string.loading);
			}
		}

		@Override
		protected ResponseBean<BabyBean> doInBackground(Void... params) {
			int doctor_id = SharedPrefUtil.getUid(context);
			return new BusinessHelper().getBabyList(allPageIndex, doctor_id);
		}

		@Override
		protected void onPostExecute(ResponseBean<BabyBean> result) {
			super.onPostExecute(result);
			pbFooter.setVisibility(View.GONE);
			dismissPd();
			if (result.getStatus() == Constants.REQUEST_SUCCESS) {
				List<BabyBean> tempList = result.getObjList();
				boolean isLastPage = false;
				list.clear();
				if(isRefresh){
					allList.clear();
				}
				if (tempList.size() > 0) {
					allList.addAll(tempList);
					allPageIndex++;
				} else {
					isLastPage = true;
				}
				if (isLastPage) {
					pbFooter.setVisibility(View.GONE);
					tvFooterMore.setText(R.string.load_all);
					isAllComplete = true;
				} else {
					if (tempList.size() > 0 && tempList.size() < Constants.PAGE_SIZE) {
						pbFooter.setVisibility(View.GONE);
						tvFooterMore.setText("");
						isAllComplete = true;
					} else {
						pbFooter.setVisibility(View.GONE);
						tvFooterMore.setText("上拉查看更多");
					}
				}
				if (allPageIndex == 1 && tempList.size() == 0) {
					tvFooterMore.setText("");
				}

			} else {
				tvFooterMore.setText("");
				showShortToast(result.getError());
			}
			list.clear();
			list.addAll(allList);
			adapter.notifyDataSetChanged();
			isAllLoad = false;
			isRefresh = false;
		}

	}
	/**
	 * 获取收藏的婴儿列表
	 * @author Zhoujun
	 *
	 */
	private class GetCollectBabyListTask extends AsyncTask<Void, Void, ResponseBean<BabyBean>> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(isCollectLoadMore){
				isCollectLoad = true;
				pbFooter.setVisibility(View.VISIBLE);
				tvFooterMore.setText(R.string.loading);
			}
			if(isRefresh){
				showPd(R.string.loading);
			}
		}
		
		@Override
		protected ResponseBean<BabyBean> doInBackground(Void... params) {
			int doctor_id = SharedPrefUtil.getUid(context);
			return new BusinessHelper().getCollectBabyList(collectPageIndex, doctor_id);
		}
		
		@Override
		protected void onPostExecute(ResponseBean<BabyBean> result) {
			super.onPostExecute(result);
			pbFooter.setVisibility(View.GONE);
			dismissPd();
			if (result.getStatus() == Constants.REQUEST_SUCCESS) {
				List<BabyBean> tempList = result.getObjList();
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
					isCollectComplete = true;
				} else {
					if (tempList.size() > 0 && tempList.size() < Constants.PAGE_SIZE) {
						pbFooter.setVisibility(View.GONE);
						tvFooterMore.setText("");
						isCollectComplete = true;
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
			list.clear();
			list.addAll(collectList);
			adapter.notifyDataSetChanged();
			isCollectLoad = false;
			isRefresh = false;
		}
		
	}
	/**
	 * 收藏取消收藏
	 * @author Zhoujun
	 *
	 */
	private class CollectTask extends AsyncTask<Void, Void, JSONObject>{
		private BabyBean bean;
		
		public CollectTask(BabyBean bean) {
			super();
			this.bean = bean;
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			int doctorId = SharedPrefUtil.getUid(DoctorHomeActivity.this);
			try {
				return new BusinessHelper().collectBaby(bean.getId(), doctorId);
			} catch (SystemException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if(result != null){
				try {
					int status = result.getInt("code");
					if(status == Constants.REQUEST_SUCCESS){
						boolean isCollect = bean.isCollect();
						bean.setCollect(!isCollect);
						adapter.notifyDataSetChanged();
						if(isCollect){
							showShortToast("取消收藏成功");
						}else{
							showShortToast("收藏成功");
						}
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
