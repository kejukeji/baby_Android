package com.keju.baby.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;


/**
 * 奶粉院内外bean
 * @author Zhoujun
 * @version 创建时间：2013-12-10 下午12:32:24
 */
public class YardBean{
	private String name;
	private int id;
	private List<BrandBean> list;
	
	public YardBean (JSONObject obj) throws JSONException{
		if(obj.has("id")){
			this.id = obj.getInt("id");
		}
		if(obj.has("type")){
			this.name = obj.getString("type");
		}
		if(obj.has("sub_brand")){
			if(!TextUtils.isEmpty(obj.getString("sub_brand"))){
				this.list = BrandBean.constractList(obj.getJSONArray("sub_brand"));
			}else{
				this.list = new ArrayList<BrandBean>();
			}
		}
	}
	/**
	 * 
	 */
	public YardBean() {
		super();
	}
	/**
	 * 构建一个list
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	public static List<YardBean> constractList(JSONArray array) throws JSONException{
		List<YardBean> list = new ArrayList<YardBean>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			YardBean bean = new YardBean(obj);
			list.add(bean);
		}
		return list;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<BrandBean> getList() {
		return list;
	}
	public void setList(List<BrandBean> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return name;
	}
}
