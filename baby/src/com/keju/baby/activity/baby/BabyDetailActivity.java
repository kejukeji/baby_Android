package com.keju.baby.activity.baby;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
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

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.NewAddBabyRecordActivity;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.bean.BabyBean;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			loadUrl(Constants.URL_GROW_LINE + bean.getId() + "?select_type=doctor" );
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
}
