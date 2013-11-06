package com.keju.baby.activity.baby;


import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.bean.FitmentBean;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;



/**
 * 育儿指南
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:27:27
 */
public class FitmentActivity extends BaseActivity {
	
	private ListView lvFitment;
	private List<FitmentBean> list;
	private FitmentAdapter fitmentAdapter;
	
	private long exitTime;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fitment_activity);
		
		list=setdate();
		lvFitment=(ListView)findViewById(R.id.lvFitment);
		fitmentAdapter=new FitmentAdapter(this,list);
		lvFitment.setAdapter(fitmentAdapter);
	}
	private List<FitmentBean> setdate() {
		List<FitmentBean> list=new ArrayList<FitmentBean>();
		FitmentBean test=new FitmentBean(R.drawable.star," 您的宝宝5周大，您的宝宝正在了解他有手、手指、脚和脚趾。直到现在，他还未知道她的手脚是她的身体的一部分，也不知道她......", "2013/9/2");
		list.add(test);
		test=new FitmentBean(R.drawable.ic_launcher," 您的宝宝5周大，您的宝宝正在了解他有手、手指、脚和脚趾。直到现在，他还未知道她的手脚是她的身体的一部分，也不知道她......", "2013/9/2");
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
   class FitmentAdapter extends BaseAdapter{
	   List<FitmentBean> list=new ArrayList<FitmentBean>();
		private LayoutInflater mInflater;
		public FitmentAdapter(Context context,List<FitmentBean> list) {
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
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.fitment_listview_item, null);
				holder.picture=(TextView)convertView.findViewById(R.id.tvImage);
				holder.content=(TextView)convertView.findViewById(R.id.tvFitmentContent);
				holder.date=(TextView)convertView.findViewById(R.id.tvFitmentDate);
				convertView.setTag(holder);
			}
			else{
				holder=(ViewHolder)convertView.getTag();
			}
			holder.picture.setCompoundDrawablesWithIntrinsicBounds(0, list.get(position).getPicture(), 0, 0);
			holder.content.setText(list.get(position).getContent());
			holder.date.setText(list.get(position).getDate());
			return convertView;
		}
	}
	
	class ViewHolder{
		public TextView picture;
		public TextView content;
		public TextView date;
	}
}
