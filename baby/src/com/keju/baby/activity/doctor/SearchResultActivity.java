package com.keju.baby.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.keju.baby.AsyncImageLoader;
import com.keju.baby.AsyncImageLoader.ImageCallback;
import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.baby.BabyDetailActivity;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.bean.BabyBean;
import com.keju.baby.bean.ResponseBean;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.NetUtil;

/**
 * 搜索结果
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:00:32
 */
public class SearchResultActivity extends BaseActivity implements OnClickListener {
	private ImageView btnLeft, btnRight;
	private TextView tvTitle;

	private ListView listView; //
	private List<BabyBean> list; // 数据源
	private HomeAdapter adapter;

	private String keyword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent() != null) {
			keyword = getIntent().getExtras().getString(Constants.EXTRA_DATA);
		}
		setContentView(R.layout.search_result);
		findView();
		fillData();
	}

	private void findView() {

		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnLeft.setBackgroundResource(android.R.drawable.btn_default);
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		btnLeft.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.listView);

	}

	/**
	 * 数据填充
	 */
	private void fillData() {
		tvTitle.setText("搜索结果");

		adapter = new HomeAdapter();
		list = new ArrayList<BabyBean>();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(itemListener);
		if (NetUtil.checkNet(this)) {
			new GetSearchBabyListTask().execute();
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
				openActivity(BabyDetailActivity.class, b);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;

		default:
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
			BabyBean bean = list.get(position);
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
			if (bean.isCollect()) {
				holder.ivCollect.setImageResource(R.drawable.ic_collected);
			} else {
				holder.ivCollect.setImageResource(R.drawable.ic_collect_not);
			}
			holder.tvName.setText(bean.getName());
			holder.tvAge.setText(bean.getAge());
			return convertView;
		}

	}

	class ViewHolder {
		public ImageView ivAvatar, ivCollect;
		public TextView tvName, tvAge;
	}

	/**
	 * 获取搜索的婴儿列表
	 * 
	 * @author Zhoujun
	 * 
	 */
	private class GetSearchBabyListTask extends AsyncTask<Void, Void, ResponseBean<BabyBean>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showPd(R.string.loading);
		}

		@Override
		protected ResponseBean<BabyBean> doInBackground(Void... params) {
			return new BusinessHelper().searchBaby(keyword);
		}

		@Override
		protected void onPostExecute(ResponseBean<BabyBean> result) {
			super.onPostExecute(result);
			dismissPd();
			if (result.getStatus() == Constants.REQUEST_SUCCESS) {
				List<BabyBean> tempList = result.getObjList();
				list.addAll(tempList);
				adapter.notifyDataSetChanged();
			} else {
				showShortToast(result.getError());
			}
		}

	}
}
