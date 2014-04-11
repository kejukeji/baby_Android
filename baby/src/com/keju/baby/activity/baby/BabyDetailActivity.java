package com.keju.baby.activity.baby;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.keju.baby.CommonApplication;
import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.SystemException;
import com.keju.baby.activity.NewAddBabyRecordActivity;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.bean.BabyBean;
import com.keju.baby.bean.FollowUpRecordBean;
import com.keju.baby.db.DataBaseAdapter;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.NetUtil;
import com.keju.baby.util.SharedPrefUtil;

/**
 * 婴儿详情界面
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:29:41
 */
public class BabyDetailActivity extends BaseWebViewActivity implements OnClickListener, OnTouchListener {

	private BabyBean bean;
	private HorizontalScrollView viewTab;
	private TextView tvVisit, tvGrowLine, tvGrowRate, tvInfo;
	private ImageView ivLeft, ivRight;
	private Vector<Boolean> isClick = new Vector<Boolean>();
	private List<TextView> tvList = new ArrayList<TextView>();
	private int lastPosition = 0;

	/**
	 * 数据库操作对象
	 */
	private DataBaseAdapter dba;
	public static final String TABLE_NAME_FOLLOW_UP_RECORD = "follow_up_record";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dba = ((CommonApplication) getApplicationContext()).getDbAdapter();
		if (getIntent() != null) {
			bean = (BabyBean) getIntent().getExtras().getSerializable(Constants.EXTRA_DATA);
		}
		findView();
		fillData();
	}

	private void findView() {
		btnLeft.setVisibility(View.VISIBLE);
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnRight.setImageResource(R.drawable.btn_add_record_selector);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(bean.getName());
		webView = (WebView) findViewById(R.id.webview);

		viewTab = (HorizontalScrollView) findViewById(R.id.viewTab);
		viewWebTab.setVisibility(View.VISIBLE);
		tvVisit = (TextView) findViewById(R.id.tvVisit);
		tvGrowLine = (TextView) findViewById(R.id.tvGrowLine);
		tvGrowRate = (TextView) findViewById(R.id.tvGrowRate);
		tvInfo = (TextView) findViewById(R.id.tvInfo);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivRight = (ImageView) findViewById(R.id.ivRight);
		ivLeft.setOnTouchListener(this);
		ivRight.setOnTouchListener(this);
		tvVisit.setOnClickListener(this);
		tvGrowLine.setOnClickListener(this);
		tvGrowRate.setOnClickListener(this);
		tvInfo.setOnClickListener(this);
		isClick.add(true);
		isClick.add(false);
		isClick.add(false);
		isClick.add(false);
		tvList.add(tvVisit);
		tvList.add(tvGrowLine);
		tvList.add(tvGrowRate);
		tvList.add(tvInfo);

		if (NetUtil.checkNet(BabyDetailActivity.this)) {
			List<FollowUpRecordBean> followUpRecordList = new ArrayList<FollowUpRecordBean>();
			if (dba.tabbleIsExist(TABLE_NAME_FOLLOW_UP_RECORD) == true) {
				followUpRecordList = dba.findAllFollow();
				for (FollowUpRecordBean bean : followUpRecordList) {
					if(bean.getBabyId()== 0){
					 showShortToast("无网络下新添加的婴儿，无对应的id无法添加随访记录");
					}else{
					new AddBabyRecordTask(bean.getBabyId(), bean.getMeasure_data(), bean.getWeight(), bean.getHeight(),
							bean.getHead_size(), bean.getBreast_feeding(), bean.getHospital_within(), bean.getBrand(),
							bean.getKind(), bean.getRecipe_milk()).execute();
					}
				}
				if (dba.clearTableData(TABLE_NAME_FOLLOW_UP_RECORD)) {
					// showShortToast("新增婴儿表已清空");
				}
			}

		}
	}

	private void fillData() {

		loadUrl(Constants.URL_VISIT_RECORD + bean.getId());
		webView.addJavascriptInterface(new Object() {
			public void webviewAddVisit(int code) {
				if (code == 200) {
					if (webView.getUrl() == null) {
						return;
					}
					if (webView.canGoBack()) {
						webView.goBack();
						webView.reload();
					}
				}
			}
		}, "app");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_NEW_ADD_VISIT_CODE) {
			webView.reload();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			if (webView.getUrl() == null) {
				return;
			}
			if (webView.getUrl().contains(Constants.URL_ADD_FOLLOW_UP) || webView.getUrl().contains(Constants.URL_NEED)) {
				webView.goBack();
			} else {
				finish();
			}
			break;
		case R.id.btnRight:
			Bundle b = new Bundle();
			b.putInt(Constants.EXTRA_DATA, bean.getId());
			b.putBoolean("isMother", false);
			Intent intent = new Intent(this, NewAddBabyRecordActivity.class);
			intent.putExtras(b);
			startActivityForResult(intent, Constants.REQUEST_NEW_ADD_VISIT_CODE);
			break;
		case R.id.tvVisit:
			if (isClick.get(0)) {
				return;
			}
			setPositionUnCheck(0);
			loadUrl(Constants.URL_VISIT_RECORD + bean.getId());
			break;
		case R.id.tvGrowLine:
			if (isClick.get(1)) {
				return;
			}
			setPositionUnCheck(1);
			viewTab.fullScroll(View.FOCUS_LEFT);
			loadUrl(Constants.URL_GROW_LINE + bean.getId() + "?select_type=doctor");
			break;
		case R.id.tvGrowRate:
			if (isClick.get(2)) {
				return;
			}
			setPositionUnCheck(2);
			viewTab.fullScroll(View.FOCUS_RIGHT);
			loadUrl(Constants.URL_GROW_RATE + bean.getId());
			break;
		case R.id.tvInfo:
			if (isClick.get(3)) {
				return;
			}
			setPositionUnCheck(3);
			loadUrl(Constants.URL_BABY_DETAIL + bean.getId());
			break;
		default:
			break;
		}
	}

	/**
	 * 设置上次
	 * 
	 * @param position
	 */
	private void setPositionUnCheck(int position) {
		int bottom = tvList.get(lastPosition).getPaddingBottom();
		int top = tvList.get(lastPosition).getPaddingTop();
		int right = tvList.get(lastPosition).getPaddingRight();
		int left = tvList.get(lastPosition).getPaddingLeft();
		isClick.set(lastPosition, false);
		tvList.get(lastPosition).setBackgroundDrawable(null);
		tvList.get(lastPosition).setTextColor(getResources().getColor(R.color.gold));

		lastPosition = position;
		tvList.get(position).setBackgroundResource(R.drawable.bg_web_tab_sel);
		tvList.get(position).setTextColor(getResources().getColor(R.color.white));
		tvList.get(position).setPadding(left, top, right, bottom);
		tvTitle.setText(bean.getName());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.ivLeft:
			viewTab.fullScroll(View.FOCUS_LEFT);
			break;
		case R.id.ivRight:
			viewTab.fullScroll(View.FOCUS_RIGHT);
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (webView.getUrl() == null) {
				return super.onKeyDown(keyCode, event);
			}
			if (webView.getUrl().contains(Constants.URL_NEED)) {
				if (webView.canGoBack()) {
					webView.goBack();
					return true;
				}
			} else if ((webView.getUrl().contains(Constants.URL_GROW_LINE)
					|| webView.getUrl().contains(Constants.URL_GROW_LINE) || webView.getUrl().contains(
					Constants.URL_GROW_LINE_FEN_TONG))
					&& titleBar.getVisibility() == View.GONE) {
				titleDown();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 * @author Zhoujun
	 * 
	 */
	private class AddBabyRecordTask extends AsyncTask<Void, Void, JSONObject> {
		private int id;
		private String due_date;
		private String weight;
		private String height;
		private String head;
		private String breastfeeding;
		private String location;
		private String brand;
		private String kind;
		private String nutrition;

		public AddBabyRecordTask(int babyId, String due_date, String weight, String height, String head,
				String breastfeeding, String location, String brand, String kind, String nutrition) {
			super();
			this.id  = babyId;
			this.due_date = due_date;
			this.weight = weight;
			this.height = height;
			this.head = head;
			this.breastfeeding = breastfeeding;
			this.location = location;
			this.brand = brand;
			this.kind = kind;
			this.nutrition = nutrition;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showPd("添加记录中...");
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			String add_type;
			if (SharedPrefUtil.getUserType(BabyDetailActivity.this) == Constants.USER_DOCTOR) {
				add_type = "doctor";
			} else {
				add_type = "baby";
			}
			try {
				return new BusinessHelper().addVisit(id, due_date, weight, height, head, breastfeeding, location,
						brand, kind, nutrition, add_type);
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
						showShortToast("添加随访记录成功");
						setResult(RESULT_OK);
						fillData();
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
