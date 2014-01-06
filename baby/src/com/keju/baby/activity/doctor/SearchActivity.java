package com.keju.baby.activity.doctor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.util.DateUtil;
import com.keju.baby.util.NetUtil;
import com.keju.baby.util.SharedPrefUtil;
import com.keju.baby.view.GridViewInScrollView;

/**
 * 搜索婴儿界面
 * 
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:58:32
 */
public class SearchActivity extends BaseActivity implements OnClickListener {
	private ImageView btnLeft, btnRight;
	private TextView tvTitle;
	private TextView tvTimeStart, tvTimeEnd;
	private Button btnSearch, btnClear;
	private EditText etKeyword;
	private GridViewInScrollView gvHistory;
	private Adapter adapter;
	private List<String> list;
	
	private String dateType;
	private int screenWidth;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		Display display = this.getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
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
		tvTimeStart = (TextView) findViewById(R.id.tvTimeStart);
		tvTimeStart.setOnClickListener(this);
		tvTimeEnd = (TextView) findViewById(R.id.tvTimeEnd);
		tvTimeEnd.setOnClickListener(this);
		
		gvHistory = (GridViewInScrollView) findViewById(R.id.gvSearchHistory);
	}

	/**
	 * 数据填充
	 */
	private void fillData() {
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("搜索");
		
		list = new ArrayList<String>();
		adapter = new Adapter();
		gvHistory.setAdapter(adapter);
	}
	private void fillSearchHistory(){
		list.clear();
		String[] historys = SharedPrefUtil.getSearchHistory(this).split(",");
		for (int i = 0; i < historys.length; i++) {
			list.add(historys[i]);
		}
		adapter.notifyDataSetChanged();
	}
	@Override
	protected void onResume() {
		super.onResume();
		fillSearchHistory();
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
			saveHistory();
			Bundle b = new Bundle();
			b.putString(Constants.EXTRA_DATA, keyword);
			b.putString("startTime", tvTimeStart.getText().toString());
			b.putString("endTime", tvTimeEnd.getText().toString());
			openActivity(SearchResultActivity.class, b);
			break;
		case R.id.btnClear:
			SharedPrefUtil.setSearchHistory(this, "");
			fillSearchHistory();
			break;
		case R.id.tvTimeStart:
			dateType = "start";
			showDateDialog();
			break;
		case R.id.tvTimeEnd:
			dateType = "end";
			showDateDialog();
			break;
		default:
			break;
		}

	}
	/**
	 * 保存搜索历史
	 */
	private void saveHistory() {
		String text = etKeyword.getText().toString();
		String save_Str = SharedPrefUtil.getSearchHistory(this);
		String[] hisArrays = save_Str.split(",");
		for (int i = 0; i < hisArrays.length; i++) {
			if (hisArrays[i].equals(text)) {
				return;
			}
		}
		StringBuilder sb = new StringBuilder(save_Str);
		sb.append(text + ",");
		SharedPrefUtil.setSearchHistory(this, sb.toString());
	}
	private Calendar cal = Calendar.getInstance();
	/**
	 * 显示日期选项dialog；
	 */
	private void showDateDialog() {
		new DatePickerDialog(this, listener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH)).show();
	}

	private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDate();
		}
	};

	private void updateDate() {
		if(dateType.equals("start")){
			tvTimeStart.setText(DateUtil.dateToString("yyyy-MM-dd", cal.getTime()));
		}else if(dateType.equals("end")){
			tvTimeEnd.setText(DateUtil.dateToString("yyyy-MM-dd", cal.getTime()));
		}
	}
	/**
	 * 适配器
	 * 
	 * @author Zhoujun
	 * 
	 */
	private class Adapter extends BaseAdapter {
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final String name = list.get(position);
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.history_item, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			int itemWidth = (screenWidth - 2 * 10 - 20) / 3;
			LayoutParams params = holder.tvName.getLayoutParams();
			params.width = itemWidth;
			params.height = itemWidth/3;
			holder.tvName.setLayoutParams(params);
			holder.tvName.setText(name);
			holder.tvName.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					b.putString(Constants.EXTRA_DATA, name);
					openActivity(SearchResultActivity.class, b);
				}
			});
			return convertView;
		}

		class ViewHolder {
			private TextView tvName;
		}
	}
}
