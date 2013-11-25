package com.keju.baby.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.keju.baby.helper.BusinessHelper;

public class BabyBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5178447442615478738L;
	/**
	 * 婴儿数据bean
	 * 说明：	1、属性命名使用驼峰命名法
	 * 		2、属性需要添加注释，说明属性的意义
	 */
	private int id;//编号
	private String name;//姓名
	private String age; //出生日期
	private String avatarUrl;//头像地址
	private boolean isCollect;//是否收藏
	
	public BabyBean (JSONObject obj) throws JSONException{
		if(obj.has("id")){
			this.id = obj.getInt("id");
		}
		if(obj.has("baby_name")){
			this.name = obj.getString("baby_name");
		}
		if(obj.has("time")){
			this.age = obj.getString("time");
		}
		if(obj.has("picture_path")){
			this.avatarUrl = BusinessHelper.PIC_URL + obj.getString("picture_path");
		}
		if(obj.has("is_collect")){
			this.isCollect = obj.getInt("is_collect") == 1 ? true : false;
		}
	}
	/**
	 * 构建list
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	public static List<BabyBean> constractList(JSONArray array) throws JSONException{
		List<BabyBean> list = new ArrayList<BabyBean>();
		for (int i = 0; i < array.length(); i++) {
			BabyBean bean = new BabyBean(array.getJSONObject(i));
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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public boolean isCollect() {
		return isCollect;
	}
	public void setCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}
	
}
