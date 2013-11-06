package com.keju.baby.activity.doctor;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.bean.BabyInformationBean;

/**
 * 医生首页界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:51:05
 */
public class DoctorHomeActivity extends BaseActivity implements OnCheckedChangeListener,OnItemClickListener{
	private RadioGroup doctorHomeRadioGroup; //主页面radiogroup
	private GridView homeGridView; //主页面gridview
	private List<BabyInformationBean> list; // 数据源
	private HomeGridViewAdapter homeGridViewAdapter,homeGridViewAdapter2;// 所有baby适配器。 收藏适配器
	private long exitTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_home);
		
		
		doctorHomeRadioGroup=(RadioGroup)findViewById(R.id.dochome_radio_group);
		homeGridView=(GridView)findViewById(R.id.dochome_gridview);
		
		 //测试用
		list=setdata();
		homeGridViewAdapter=new HomeGridViewAdapter(this,list);
		list =setdata2();
		homeGridViewAdapter2=new HomeGridViewAdapter(this, list);
		
		
		homeGridView.setAdapter(homeGridViewAdapter);
		homeGridView.setOnItemClickListener(this);
		doctorHomeRadioGroup.setOnCheckedChangeListener(this);
	}

	private List<BabyInformationBean> setdata2() {
		// TODO Auto-generated method stub
		List<BabyInformationBean> list=new ArrayList<BabyInformationBean>();
		
		BabyInformationBean test=new BabyInformationBean(R.drawable.pic,"1000001","baby1","1个月");
		list.add(test);
		
		test=new BabyInformationBean(R.drawable.pic,"1000002","baby2","2个月");
		list.add(test);
		
		return list;
	}

	private List<BabyInformationBean> setdata() {
		// TODO Auto-generated method stub
		List<BabyInformationBean> list=new ArrayList<BabyInformationBean>();
		BabyInformationBean test=new BabyInformationBean(R.drawable.pic,"1000003","baby3","3个月");
		list.add(test);
		
		test=new BabyInformationBean(R.drawable.pic,"1000004","baby4","4个月");
		list.add(test);
		
		test=new BabyInformationBean(R.drawable.pic,"1000005","baby5","5个月");
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.dochome_allbaby:
			homeGridView.setAdapter(homeGridViewAdapter);
			break;
		case R.id.dochome_mycollect:
			homeGridView.setAdapter(homeGridViewAdapter2);
			break;
		}
	}
	class HomeGridViewAdapter extends BaseAdapter{
		//适配器
		List<BabyInformationBean> list= new ArrayList<BabyInformationBean>();
		private LayoutInflater mInflater;
		 public HomeGridViewAdapter(Context context,List<BabyInformationBean> list){
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
			// TODO Auto-generated method stub
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.doctor_home_gridview_item, null);
				holder.pricture=(ImageView)convertView.findViewById(R.id.gridview_picture);
				holder.littleStar=(ImageView)convertView.findViewById(R.id.gridview_littlestar);
				holder.id=(TextView)convertView.findViewById(R.id.gridview_id);
				holder.name=(TextView)convertView.findViewById(R.id.gridview_name);
				holder.day=(TextView)convertView.findViewById(R.id.gridview_day);
				convertView.setTag(holder);
			}
			else{
				holder=(ViewHolder)convertView.getTag();
			}
			holder.pricture.setBackgroundResource(list.get(position).getPicture());
			holder.littleStar.setBackgroundResource(R.drawable.star);
			holder.id.setText(list.get(position).getId());
			holder.name.setText(list.get(position).getName());
			holder.day.setText(list.get(position).getDay());
			return convertView;
		}

	}
	
	class ViewHolder{
		public ImageView pricture;
		public ImageView littleStar;
		public TextView id;
		public TextView name;
		public TextView day;
	}

	

}
