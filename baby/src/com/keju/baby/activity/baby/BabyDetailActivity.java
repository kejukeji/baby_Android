package com.keju.baby.activity.baby;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.bean.BabyBean;

/**
 * 婴儿详情界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:29:41
 */
public class BabyDetailActivity extends BaseWebViewActivity {
	private Button btnLeft, btnRight;
	private TextView tvTitle;
	
	private BabyBean bean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent() != null){
			bean = (BabyBean) getIntent().getExtras().getSerializable(Constants.EXTRA_DATA);
		}
		findView();
		fillData();
	}

	private void findView() {
		btnLeft = (Button) this.findViewById(R.id.btnLeft);
		btnLeft.setVisibility(View.INVISIBLE);
		btnRight = (Button) this.findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(bean.getName());
		webView = (WebView) findViewById(R.id.webview);
	}

	private void fillData() {
		loadUrl(Constants.URL_GROW_LINE + bean.getId());
	}
}
