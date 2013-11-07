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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;
import com.keju.baby.bean.AcademicAbstractBean;

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
	private long exitTime;
	private Button btnLeft,btnRight;
	private TextView tvTitle;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.academic_abstract_activity);
		
		list=setdata();
		lvAcademic=(ListView)findViewById(R.id.lvAcademic);
		academicAdapter=new AcademicAdapter(this,list);
		lvAcademic.setAdapter(academicAdapter);
		
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
		tvTitle.setText("学术文摘");
	}
	private List<AcademicAbstractBean> setdata() {
		List<AcademicAbstractBean> list=new ArrayList<AcademicAbstractBean>();
		AcademicAbstractBean test=new AcademicAbstractBean("新生儿的营养和需要量", " 新生儿出生后的2～4周内生长最快，按新生儿中等增长速度计算，每日增长体重在30克以上。新生儿补充营养的主要方式为：母乳喂养、混合喂养和人工喂养。新生儿期较其它各期相对营养素需要为高，为保证新生儿营养素的供给，减少或避免新生儿生理性体重减轻，应注意新生儿的营养供给量");
		list.add(test);
		
		test=new AcademicAbstractBean("新生儿的营养和需要量", " 新生儿出生后的2～4周内生长最快，按新生儿中等增长速度计算，每日增长体重在30克以上。新生儿补充营养的主要方式为：母乳喂养、混合喂养和人工喂养。新生儿期较其它各期相对营养素需要为高，为保证新生儿营养素的供给，减少或避免新生儿生理性体重减轻，应注意新生儿的营养供给量");
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
