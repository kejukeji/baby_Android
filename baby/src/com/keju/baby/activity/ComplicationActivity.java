package com.keju.baby.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
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
	private CheckBox rbHuangDan, rbLuNei, rbYingZhong, rbShiWangMo, rbDongMaiGuan, rbXinZangBing;
	private CheckBox rbHuXi, rbHuXiShuaiJie, rbChangeYan, rbFeiYan, rbNaoMoYan, rbBaiXueZheng;
	private CheckBox rbGaoXueTang, rbDaiXie, rbNo;
	private CheckBox[] rbs;
	
	private String complicationStr = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent().getExtras() != null){
			complicationStr = getIntent().getExtras().getString(Constants.EXTRA_DATA);
		}
		setContentView(R.layout.complication);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		rbHuangDan = (CheckBox) findViewById(R.id.rbHuangDan);
		rbLuNei = (CheckBox) findViewById(R.id.rbLuNei);
		rbYingZhong = (CheckBox) findViewById(R.id.rbYingZhong);
		rbShiWangMo = (CheckBox) findViewById(R.id.rbShiWangMo);
		rbDongMaiGuan = (CheckBox) findViewById(R.id.rbDongMaiGuan);
		rbXinZangBing = (CheckBox) findViewById(R.id.rbXinZangBing);

		rbHuXi = (CheckBox) findViewById(R.id.rbHuXi);
		rbHuXiShuaiJie = (CheckBox) findViewById(R.id.rbHuXiShuaiJie);
		rbChangeYan = (CheckBox) findViewById(R.id.rbChangeYan);
		rbFeiYan = (CheckBox) findViewById(R.id.rbFeiYan);
		rbNaoMoYan = (CheckBox) findViewById(R.id.rbNaoMoYan);
		rbBaiXueZheng = (CheckBox) findViewById(R.id.rbBaiXueZheng);

		rbGaoXueTang = (CheckBox) findViewById(R.id.rbGaoXueTang);
		rbDaiXie = (CheckBox) findViewById(R.id.rbDaiXie);
		rbNo = (CheckBox) findViewById(R.id.rbNo);
	}

	private void fillData() {
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnLeft.setOnClickListener(this);
		btnRight.setImageResource(R.drawable.btn_commit_selector);
		btnRight.setOnClickListener(this);
		tvTitle.setText("合并症");
		rbs = new CheckBox[] { rbHuangDan, rbLuNei, rbYingZhong, rbShiWangMo, rbDongMaiGuan, rbXinZangBing, rbHuXi,
				rbHuXiShuaiJie, rbChangeYan, rbFeiYan, rbNaoMoYan, rbBaiXueZheng, rbGaoXueTang, rbDaiXie, rbNo };
		
		if(complicationStr != null){
			String[] str = complicationStr.split(",");
			for (int i = 0; i < str.length; i++) {
				for (int j = 0; j < rbs.length; j++) {
					if(str[i].equals(rbs[j].getText().toString())){
						rbs[j].setChecked(true);
						break;
					}
				}
			}
		}
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
