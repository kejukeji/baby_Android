package com.baby.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.mime.content.ContentBody;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baby.R;
import com.baby.activity.base.BaseActivity;
import com.baby.activity.doctor.DoctorHomeActivity.ViewHolder;
import com.baby.bean.AcademicAbstractBean;
import com.baby.bean.BabyInformationBean;

/**
 * 学术文摘界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:04:02
 */
public class AcademicAbstractsActivity extends BaseActivity {
	private Button btnCollect,btnCOllectCancel;
	private ListView lvAcademic;
	private List<AcademicAbstractBean> list;
	private AcademicAdapter academicAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.academic_abstract_activity);
		
		list=setdata();
		lvAcademic=(ListView)findViewById(R.id.lvAcademic);
		academicAdapter=new AcademicAdapter(this,list);
		lvAcademic.setAdapter(academicAdapter);
		//lvAcademic.setVisibility(View.v)
	}

	private List<AcademicAbstractBean> setdata() {
		List<AcademicAbstractBean> list=new ArrayList<AcademicAbstractBean>();
		AcademicAbstractBean test=new AcademicAbstractBean("新生儿的营养和需要量", " 新生儿出生后的2～4周内生长最快，按新生儿中等增长速度计算，每日增长体重在30克以上。新生儿补充营养的主要方式为：母乳喂养、混合喂养和人工喂养。新生儿期较其它各期相对营养素需要为高，为保证新生儿营养素的供给，减少或避免新生儿生理性体重减轻，应注意新生儿的营养供给量");
		list.add(test);
		
		test=new AcademicAbstractBean("新生儿的营养和需要量", " 新生儿出生后的2～4周内生长最快，按新生儿中等增长速度计算，每日增长体重在30克以上。新生儿补充营养的主要方式为：母乳喂养、混合喂养和人工喂养。新生儿期较其它各期相对营养素需要为高，为保证新生儿营养素的供给，减少或避免新生儿生理性体重减轻，应注意新生儿的营养供给量");
		list.add(test);
		
		return list;
	}
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
	}
	class AcademicAdapter extends BaseAdapter{

		List<AcademicAbstractBean> list=new ArrayList<AcademicAbstractBean>();
		private LayoutInflater mInflater;
		public AcademicAdapter(Context context,List<AcademicAbstractBean> list) {
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
				convertView=mInflater.inflate(R.layout.academic_abstract_item, null);
				viewHolder.title=(TextView)convertView.findViewById(R.id.academic_title);
				viewHolder.content=(TextView)convertView.findViewById(R.id.academic_content);
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
	
}
