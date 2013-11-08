
package com.keju.baby.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.bean.MyCollectBean;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;



/**
 * 医生资料界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:53:44
 */
public class DoctorMyActivity extends BaseActivity implements OnCheckedChangeListener,OnClickListener{
	private RadioGroup doctorMyRadioGroup;
	private LinearLayout doctorMyLinearLayout;
	private ListView doctorMyListView;
	private MyCollectAdapter myCollectAdapter;
	private List<MyCollectBean> list;
	private long exitTime = 0;
	private Button btnLeft,btnRight,btnChangeInfo,btnChangePw;
	private TextView tvTitle;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_my_activity);
		
		list=setdata();
		doctorMyRadioGroup=(RadioGroup)findViewById(R.id.doctor_my_radiogroup);
		doctorMyLinearLayout=(LinearLayout)findViewById(R.id.doctor_my_linearlayout);
		doctorMyListView=(ListView)findViewById(R.id.doctor_my_listview);
		btnChangeInfo=(Button)findViewById(R.id.btnDoctor_my_change_inform);
		btnChangePw=(Button)findViewById(R.id.btnDoctor_my_change_pw);
		
        doctorMyRadioGroup.setOnCheckedChangeListener(this);
        myCollectAdapter=new MyCollectAdapter(this, list);
        btnChangeInfo.setOnClickListener(this);
        btnChangePw.setOnClickListener(this);
        
        doctorMyListView.setAdapter(myCollectAdapter);
        
        findView();
		fillData();		
         
	}
	private void findView() {
		
		btnLeft=(Button)findViewById(R.id.btnLeft);
		btnRight=(Button)findViewById(R.id.btnRight);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
	}
	/**
	 * 数据填充
	 */
	private void fillData() {
		
		btnLeft.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
		tvTitle.setText("个人中心");
	}
	private List<MyCollectBean> setdata() {
		List<MyCollectBean> list=new ArrayList<MyCollectBean>();
		MyCollectBean test=new MyCollectBean("新生儿的营养和需要量", " 新生儿出生后的2～4周内生长最快，按新生儿中等增长速度计算，每日增长体重在30克以上。新生儿补充营养的主要方式为：母乳喂养、混合喂养和人工喂养。新生儿期较其它各期相对营养素需要为高，为保证新生儿营养素的供给，减少或避免新生儿生理性体重减轻，应注意新生儿的营养供给量");
		list.add(test);
		
		test=new MyCollectBean("新生儿的营养和需要量", " 新生儿出生后的2～4周内生长最快，按新生儿中等增长速度计算，每日增长体重在30克以上。新生儿补充营养的主要方式为：母乳喂养、混合喂养和人工喂养......");
		list.add(test);
		
		return list;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            showLongToast("再按一次返回键退出");                             
	            exitTime = System.currentTimeMillis();   
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbDoctor_my_collect:
			doctorMyLinearLayout.setVisibility(View.GONE);
		    doctorMyListView.setVisibility(View.VISIBLE);
			break;
		case R.id.rbDoctor_my_inform:
			doctorMyLinearLayout.setVisibility(View.VISIBLE);
			doctorMyListView.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		
	}
	class MyCollectAdapter extends BaseAdapter{
		List<MyCollectBean> list=new ArrayList<MyCollectBean>();
		private LayoutInflater mInflater;
		public MyCollectAdapter(Context context,List<MyCollectBean> list) {
			this.mInflater = LayoutInflater.from(context);
			this.list=list;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder=null;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.doctor_my_collect_listitem, null);
				viewHolder.title=(TextView)convertView.findViewById(R.id.my_collect_title);
				viewHolder.content=(TextView)convertView.findViewById(R.id.my_collect_content);
				convertView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder)convertView.getTag();
			}
			viewHolder.title.setText(list.get(position).getTitle());
			viewHolder.content.setText(list.get(position).getContent());
			return convertView;
		}
		
	}
	class ViewHolder{
		public TextView title;
		public TextView content;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDoctor_my_change_inform:
			openActivity(DoctorInfoEditActivity.class);
			break;
		case R.id.btnDoctor_my_change_pw:
			
			break;
		
		default:
			break;
		}
		
	}
}

