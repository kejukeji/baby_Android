package com.keju.baby.activity.doctor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.util.NetUtil;

/**
 * 搜索婴儿界面
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:58:32
 */
public class SearchActivity extends BaseActivity implements OnClickListener {
	private ImageView btnLeft, btnRight;
	private TextView tvTitle;
	private Button btnSearch, btnClear;
	private EditText etKeyword;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		findView();
		fillData();
		btnLeft.setOnClickListener(this);
	}

	private void findView() {

		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);
		btnClear = (Button) findViewById(R.id.btnClear);
		btnClear.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		etKeyword = (EditText) findViewById(R.id.etKeyword);
	}

	/**
	 * 数据填充
	 */
	private void fillData() {
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("搜索");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			this.finish();
			break;
		case R.id.btnSearch:
			String keyword = etKeyword.getText().toString();
			if (TextUtils.isEmpty(keyword)) {
				showShortToast("搜索的关键字不能为空");
				return;
			}
			if (!NetUtil.checkNet(this)) {
				showShortToast(R.string.NoSignalException);
			}
			Bundle b = new Bundle();
			b.putString(Constants.EXTRA_DATA, keyword);
			openActivity(SearchResultActivity.class, b);
			break;
		case R.id.btnClear:
			break;
		default:
			break;
		}

	}
}
