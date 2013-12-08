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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.NewAddBabyRecordActivity;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.util.AndroidUtil;
import com.keju.baby.util.SharedPrefUtil;

public class BabyHomeActivity extends BaseWebViewActivity implements OnClickListener, OnTouchListener {

	private View viewWebTab;
	private HorizontalScrollView viewTab;
	private TextView tvVisit, tvGrowLine, tvGrowRate, tvInfo;
	private ImageView ivLeft, ivRight;
	private Vector<Boolean> isClick = new Vector<Boolean>();
	private List<TextView> tvList = new ArrayList<TextView>();
	private int lastPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft.setVisibility(View.INVISIBLE);
		// btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnRight.setImageResource(R.drawable.btn_add_record_selector);
		btnRight.setOnClickListener(this);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(SharedPrefUtil.getName(this));

		viewTab = (HorizontalScrollView) findViewById(R.id.viewTab);
		viewWebTab = findViewById(R.id.viewWebTab);
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
		tvInfo.setVisibility(View.GONE);
	}

	private void fillData() {
		loadUrl(Constants.URL_VISIT_RECORD + SharedPrefUtil.getUid(this) + "?entrance_type=baby");
		tvTitle.setText(SharedPrefUtil.getName(this));
	}

	@Override
	protected void onResume() {
		super.onResume();
		webView.reload();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRight:
			Bundle b = new Bundle();
			b.putInt(Constants.EXTRA_DATA, SharedPrefUtil.getUid(this));
			Intent intent = new Intent(this, NewAddBabyRecordActivity.class);
			intent.putExtras(b);
			startActivityForResult(intent, Constants.REQUEST_NEW_ADD_VISIT_CODE);
			break;
		case R.id.tvVisit:
			if (isClick.get(0)) {
				return;
			}
			setPositionUnCheck(0);
			loadUrl(Constants.URL_VISIT_RECORD + SharedPrefUtil.getUid(this));
			break;
		case R.id.tvGrowLine:
			if (isClick.get(1)) {
				return;
			}
			setPositionUnCheck(1);
			viewTab.fullScroll(View.FOCUS_LEFT);
			loadUrl(Constants.URL_GROW_LINE + SharedPrefUtil.getUid(this));
			break;
		case R.id.tvGrowRate:
			if (isClick.get(2)) {
				return;
			}
			setPositionUnCheck(2);
			viewTab.fullScroll(View.FOCUS_RIGHT);
			loadUrl(Constants.URL_GROW_RATE + SharedPrefUtil.getUid(this));
			break;
		case R.id.tvInfo:
			if (isClick.get(3)) {
				return;
			}
			setPositionUnCheck(3);
			loadUrl(Constants.URL_BABY_DETAIL + SharedPrefUtil.getUid(this));
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
		isClick.set(lastPosition, false);
		tvList.get(lastPosition).setBackgroundDrawable(null);
		tvList.get(lastPosition).setTextColor(getResources().getColor(R.color.gold));
		lastPosition = position;
		tvList.get(position).setBackgroundResource(R.drawable.bg_web_tab_sel);
		tvList.get(position).setTextColor(getResources().getColor(R.color.white));
		tvList.get(position).setPadding(15, 5, 15, 5);
		tvTitle.setText(SharedPrefUtil.getName(this));
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_NEW_ADD_VISIT_CODE) {
			webView.reload();
		}
	}

	private long exitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (webView.getUrl().contains(Constants.URL_NEED)) {
				if (webView.canGoBack()) {
					webView.goBack();
					return true;
				}
			} else {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					showShortToast(R.string.try_again_logout);
					exitTime = System.currentTimeMillis();
				} else {
					AndroidUtil.exitApp(this);
					finish();
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
