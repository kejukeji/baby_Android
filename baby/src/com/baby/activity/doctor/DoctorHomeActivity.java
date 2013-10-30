package com.baby.activity.doctor;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baby.R;
import com.baby.activity.base.BaseActivity;
import com.baby.bean.BabyInformationBean;

/**
 * 医生首页界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:51:05
 */
public class DoctorHomeActivity extends BaseActivity implements OnCheckedChangeListener{
	RadioGroup doctorHomeRadioGroup;
	GridView homeGridView;
	List<BabyInformationBean> list;
	
	HomeGridViewAdapter homeGridViewAdapter,homeGridViewAdapter2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_home);
		list=setdata();
		 //测试用
		doctorHomeRadioGroup=(RadioGroup)findViewById(R.id.dochome_radio_group);
		homeGridView=(GridView)findViewById(R.id.dochome_gridview);
		
		homeGridViewAdapter=new HomeGridViewAdapter(this,list);
		list =setdata2();
		homeGridViewAdapter2=new HomeGridViewAdapter(this, list);
		
		homeGridView.setAdapter(homeGridViewAdapter);
		doctorHomeRadioGroup.setOnCheckedChangeListener(this);
	}

	private List<BabyInformationBean> setdata2() {
		// TODO Auto-generated method stub
		List<BabyInformationBean> list=new ArrayList<BabyInformationBean>();
		
		BabyInformationBean test=new BabyInformationBean(R.drawable.ic_launcher,"1","baby1","1");
		list.add(test);
		
		test=new BabyInformationBean(R.drawable.ic_launcher,"2","baby2","2");
		list.add(test);
		
		return list;
	}

	private List<BabyInformationBean> setdata() {
		// TODO Auto-generated method stub
		List<BabyInformationBean> list=new ArrayList<BabyInformationBean>();
		BabyInformationBean test=new BabyInformationBean(R.drawable.ic_launcher,"3","baby3","3");
		list.add(test);
		
		test=new BabyInformationBean(R.drawable.ic_launcher,"4","baby4","4");
		list.add(test);
		
		test=new BabyInformationBean(R.drawable.ic_launcher,"5","baby5","5");
		list.add(test);
		
		return list;
		
		
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
