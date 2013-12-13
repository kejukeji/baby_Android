package com.keju.baby.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 奶粉品牌bean
 * @author Zhoujun
 * @version 创建时间：2013-12-10 下午12:34:26
 */
public class BrandBean {
	private int id;
	private String name;
	private List<KindBean> list;
	
	public BrandBean (JSONObject obj) throws JSONException{
		if(obj.has("id")){
			this.id = obj.getInt("id");
		}
		if(obj.has("name")){
			this.name = obj.getString("name");
		}
		if(obj.has("sub_kind")){
			if(!TextUtils.isEmpty(obj.getString("sub_kind"))){
				this.list = KindBean.constractList(obj.getJSONArray("sub_kind"));
			}else{
				this.list = new ArrayList<KindBean>();
			}
		}
	}
	/**
	 * 构建一个list
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	public static List<BrandBean> constractList(JSONArray array) throws JSONException{
		List<BrandBean> list = new ArrayList<BrandBean>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			BrandBean bean = new BrandBean(obj);
			list.add(bean);
		}
		return list;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<KindBean> getList() {
		return list;
	}
	public void setList(List<KindBean> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return name;
	}
}
