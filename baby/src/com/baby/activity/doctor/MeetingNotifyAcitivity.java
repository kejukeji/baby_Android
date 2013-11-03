package com.baby.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
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
		MeetingNotifyBean test=new MeetingNotifyBean(R.drawable.ic_launcher," 尊敬的医生朋友，9.3日在上海XXXX医学中心将举行“惠氏奶粉 - 关爱新生儿”的医学讲座，期待您的参与！", "2013/9/1");
		list.add(test);
		test=new MeetingNotifyBean(R.drawable.ic_launcher," 尊敬的医生朋友，9.3日在上海XXXX医学中心将举行“惠氏奶粉 - 关爱新生儿”的医学讲座，期待您的参与！", "2013/9/1");
		list.add(test);
		
		return list;
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
			holder.picture.setText(list.get(position).getPicture());
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
