package com.keju.baby.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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
import android.widget.TextView;

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
 * 学术文摘界面
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:04:02
 */
public class AcademicAbstractsActivity extends BaseActivity {
	private ImageView btnLeft, btnRight;
	private TextView tvTitle;

	private ListView lvAcademic;
	private List<AcademicAbstractBean> list;
	private AcademicAdapter academicAdapter;
	private long exitTime;
	private View vFooter;
	private ProgressBar pbFooter;
	private TextView tvFooterMore;
	private boolean isLoad = false;// 是否正在加载数据
	private boolean isLoadMore = false;// 是否加载更多
	private boolean isComplete = false;// 是否加载完了；
	private int pageIndex = 1;
	
	private boolean isFilter = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.academic_abstract_activity);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		
		// 加载更多footer
		vFooter = getLayoutInflater().inflate(R.layout.footer, null);
		pbFooter = (ProgressBar) vFooter.findViewById(R.id.progressBar);
		tvFooterMore = (TextView) vFooter.findViewById(R.id.tvMore);
		lvAcademic = (ListView) findViewById(R.id.lvAcademic);
	}

	/**
	 * 数据填充
	 */
	private void fillData() {

		btnLeft.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("学术文摘");

		list = new ArrayList<AcademicAbstractBean>();
		academicAdapter = new AcademicAdapter();
		lvAcademic.addFooterView(vFooter);
		lvAcademic.setOnScrollListener(LoadListener);
		lvAcademic.setAdapter(academicAdapter);
		lvAcademic.setOnItemClickListener(clicklistener);
//		if(NetUtil.checkNet(this)){
//			new GetDataTask().execute();
//		}else{
//			showShortToast(R.string.NoSignalException);
//		}
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
						new GetDataTask().execute();
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


	/**
	 * 收藏文摘接口
	 * @author Zhoujun
	 * 
	 */
	private class GetDataTask extends AsyncTask<Void, Void, ResponseBean<AcademicAbstractBean>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(isLoadMore){
				isLoad = true;
				pbFooter.setVisibility(View.VISIBLE);
				tvFooterMore.setText(R.string.loading);
			}else{
				showPd(R.string.loading);
			}
			
		}

		@Override
		protected ResponseBean<AcademicAbstractBean> doInBackground(Void... params) {
			int doctor_id = SharedPrefUtil.getUid(context);
			return new BusinessHelper().getAcademicAbstract(pageIndex, doctor_id);
		}

		@Override
		protected void onPostExecute(ResponseBean<AcademicAbstractBean> result) {
			super.onPostExecute(result);
			pbFooter.setVisibility(View.GONE);
			dismissPd();
			if (isFilter) {
				list.clear();
				academicAdapter.notifyDataSetChanged();
			}
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
					if (tempList.size() > 0 && tempList.size() < Constants.PAGE_SIZE1) {
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
			academicAdapter.notifyDataSetChanged();
			isLoad = false;
			isFilter = false;
		}
	}

	/**
	 * 学术文摘适配器
	 * 
	 * @author Zhoujun
	 * 
	 */
	private class AcademicAdapter extends BaseAdapter {

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
			String content = bean.getContent();
			if(content.length() > 120){
				content = content.substring(0, 120) + "...";
			}
			viewHolder.content.setText(bean.getContent());
			if(bean.isCollect()){
				viewHolder.btnCollect.setText(R.string.doctor_my_collect_cancel);
			}else{
				viewHolder.btnCollect.setText(R.string.academic_abstract_collect);
			}
			viewHolder.btnCollect.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(NetUtil.checkNet(AcademicAbstractsActivity.this)){
						new CollectTask(bean).execute();
					}else{
						showShortToast(R.string.NoSignalException);
					}
				}
			});
			return convertView;
		}

	}

	class ViewHolder {
		public TextView title;
		public TextView content;
		public Button btnCollect;
	}
	/**
	 * 收藏取消收藏接口
	 * @author Zhoujun
	 *
	 */
	private class CollectTask extends AsyncTask<Void, Void, JSONObject>{
		private AcademicAbstractBean bean;
		
		public CollectTask(AcademicAbstractBean bean) {
			super();
			this.bean = bean;
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			int doctorId = SharedPrefUtil.getUid(AcademicAbstractsActivity.this);
			try {
				return new BusinessHelper().collectAbstract(bean.getId(), doctorId);
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
						academicAdapter.notifyDataSetChanged();
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
	@Override
	protected void onResume() {
		super.onResume();
		if(NetUtil.checkNet(this)){
			isFilter = true;
			isComplete = false;
			pageIndex  = 1;
			new GetDataTask().execute();
		}else{
			showShortToast(R.string.NoSignalException);
		}
		
	}
	
}
