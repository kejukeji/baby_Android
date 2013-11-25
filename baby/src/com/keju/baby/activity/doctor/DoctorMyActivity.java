package com.keju.baby.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.bean.MyCollectBean;
import com.keju.baby.util.AndroidUtil;

/**
 * 医生资料界面
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:53:44
 */
public class DoctorMyActivity extends BaseActivity implements OnCheckedChangeListener, OnClickListener {
	private Button btnLeft, btnRight;
	private TextView tvTitle;
	private RadioGroup radio_group;
	private View viewInfo;
	private TextView tvId,tvName,tvArea,tvHospital,tvDepartment,tvJob,tvEmail,tvPhone;
	
	private ListView listView;
	private Adapter adapter;
	private List<MyCollectBean> list;
	private long exitTime = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_my_activity);
		findView();
		fillData();
	}

	private void findView() {
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnRight = (Button) findViewById(R.id.btnRight);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		radio_group = (RadioGroup) findViewById(R.id.radio_group);
		viewInfo = findViewById(R.id.viewInfo);
		
		tvId = (TextView) findViewById(R.id.tvId);
		tvName = (TextView) findViewById(R.id.tvName);
		tvArea = (TextView) findViewById(R.id.tvArea);
		tvHospital = (TextView) findViewById(R.id.tvHospital);
		tvDepartment = (TextView) findViewById(R.id.tvDepartment);
		tvJob = (TextView) findViewById(R.id.tvJob);
		tvEmail = (TextView) findViewById(R.id.tvEmail);
		tvPhone = (TextView) findViewById(R.id.tvPhone);
		
		
		listView = (ListView) findViewById(R.id.listView);
	}

	/**
	 * 数据填充
	 */
	private void fillData() {
		btnLeft.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("个人中心");

		radio_group.setOnCheckedChangeListener(this);
		list = new ArrayList<MyCollectBean>();
		adapter = new Adapter();

		listView.setAdapter(adapter);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				showShortToast(R.string.try_again_logout);
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				AndroidUtil.exitApp(this);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_info:
			viewInfo.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			break;
		case R.id.rb_collect:
			viewInfo.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}

	class Adapter extends BaseAdapter {
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
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.doctor_my_collect_listitem, null);
				viewHolder.title = (TextView) convertView.findViewById(R.id.my_collect_title);
				viewHolder.content = (TextView) convertView.findViewById(R.id.my_collect_content);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.title.setText(list.get(position).getTitle());
			viewHolder.content.setText(list.get(position).getContent());
			return convertView;
		}

	}

	class ViewHolder {
		public TextView title;
		public TextView content;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}

	}
}
