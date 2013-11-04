package com.baby.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baby.R;
import com.baby.activity.base.BaseActivity;
import com.baby.bean.MeetingNotifyBean;

/**
 * 会议通知
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:05:59
 */
public class MeetingNotifyAcitivity extends BaseActivity {
	private ListView lvMeetingNotify;
	private List<MeetingNotifyBean> list;
	private MeetingNotifyAdapter meetingNotifyAdapter;
	private long exitTime;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meeting_notify_activity);
		
		list=setdate();
		lvMeetingNotify=(ListView)findViewById(R.id.lvMeetingNotify);
		meetingNotifyAdapter=new MeetingNotifyAdapter(this,list);
		lvMeetingNotify.setAdapter(meetingNotifyAdapter);
		
	}
	private List<MeetingNotifyBean> setdate() {
		List<MeetingNotifyBean> list=new ArrayList<MeetingNotifyBean>();
		MeetingNotifyBean test=new MeetingNotifyBean(R.drawable.star," 尊敬的医生朋友，9.3日在上海XXXX医学中心将举行“惠氏奶粉 - 关爱新生儿”的医学讲座，期待您的参与！", "2013/9/1");
		list.add(test);
		test=new MeetingNotifyBean(R.drawable.ic_launcher," 尊敬的医生朋友，9.3日在上海XXXX医学中心将举行“惠氏奶粉 - 关爱新生儿”的医学讲座，期待您的参与！", "2013/9/1");
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
	class MeetingNotifyAdapter extends BaseAdapter{
		List<MeetingNotifyBean> list=new ArrayList<MeetingNotifyBean>();
		private LayoutInflater mInflater;
		public MeetingNotifyAdapter(Context context,List<MeetingNotifyBean> list) {
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
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.meeting_notify_item, null);
				holder.picture=(TextView)convertView.findViewById(R.id.tvPicture);
				holder.content=(TextView)convertView.findViewById(R.id.tvMeetingContent);
				holder.date=(TextView)convertView.findViewById(R.id.tvDate);
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
