package com.keju.baby.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;

/**
 * 选择合并症界面
 * 
 * @author Zhoujun
 * @version 创建时间：2013-12-11 下午3:32:13
 */
public class ComplicationActivity extends BaseActivity implements OnClickListener {
	private ImageView btnLeft, btnRight;
	private TextView tvTitle;
	private RadioButton rbHuangDan, rbLuNei, rbYingZhong, rbShiWangMo, rbDongMaiGuan, rbXinZangBing;
	private RadioButton rbHuXi, rbHuXiShuaiJie, rbChangeYan, rbFeiYan, rbNaoMoYan, rbBaiXueZheng;
	private RadioButton rbGaoXueTang, rbDaiXie, rbNo;
	private RadioButton[] rbs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complication);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		rbHuangDan = (RadioButton) findViewById(R.id.rbHuangDan);
		rbLuNei = (RadioButton) findViewById(R.id.rbLuNei);
		rbYingZhong = (RadioButton) findViewById(R.id.rbYingZhong);
		rbShiWangMo = (RadioButton) findViewById(R.id.rbShiWangMo);
		rbDongMaiGuan = (RadioButton) findViewById(R.id.rbDongMaiGuan);
		rbXinZangBing = (RadioButton) findViewById(R.id.rbXinZangBing);

		rbHuXi = (RadioButton) findViewById(R.id.rbHuXi);
		rbHuXiShuaiJie = (RadioButton) findViewById(R.id.rbHuXiShuaiJie);
		rbChangeYan = (RadioButton) findViewById(R.id.rbChangeYan);
		rbFeiYan = (RadioButton) findViewById(R.id.rbFeiYan);
		rbNaoMoYan = (RadioButton) findViewById(R.id.rbNaoMoYan);
		rbBaiXueZheng = (RadioButton) findViewById(R.id.rbBaiXueZheng);

		rbGaoXueTang = (RadioButton) findViewById(R.id.rbGaoXueTang);
		rbDaiXie = (RadioButton) findViewById(R.id.rbDaiXie);
		rbNo = (RadioButton) findViewById(R.id.rbNo);
	}

	private void fillData() {
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnLeft.setOnClickListener(this);
		btnRight.setImageResource(R.drawable.btn_commit_selector);
		btnRight.setOnClickListener(this);
		tvTitle.setText("合并症");
		rbs = new RadioButton[] { rbHuangDan, rbLuNei, rbYingZhong, rbShiWangMo, rbDongMaiGuan, rbXinZangBing, rbHuXi,
				rbHuXiShuaiJie, rbChangeYan, rbFeiYan, rbNaoMoYan, rbBaiXueZheng, rbGaoXueTang, rbDaiXie, rbNo };
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;
		case R.id.btnRight:
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < rbs.length; i++) {
				if(rbs[i].isChecked()){
					sb.append(rbs[i].getText().toString() + ",");
				}
			}
			if(sb.length() <= 0){
				showShortToast("请至少选中一项保存");
				return;
			}
			String complicationStr = sb.substring(0, sb.length() - 1);
			Intent intent = getIntent();
			intent.putExtra(Constants.EXTRA_DATA, complicationStr);
			setResult(RESULT_OK,intent);
			finish();
			break;
		default:
			break;
		}
	}
}
