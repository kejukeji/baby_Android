package com.keju.baby.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 奶粉种类
 * @author Zhoujun
 * @version 创建时间：2013-12-10 下午12:35:24
 */
public class KindBean {
	private String name;
	private int id;
	private String energy;
	private String protein;
	private String carbohydrate;
	private String fat;
	
	public KindBean(){
		
	}
	public KindBean (JSONObject obj) throws JSONException{
		if(obj.has("id")){
			this.id = obj.getInt("id");
		}
		if(obj.has("type")){
			this.name = obj.getString("type");
		}
		if(obj.has("energy")){
			this.energy = obj.getString("energy");
		}
		if(obj.has("protein")){
			this.protein = obj.getString("protein");
		}
		if(obj.has("carbon_compound")){
			this.carbohydrate = obj.getString("carbon_compound");
		}
		if(obj.has("axunge")){
			this.fat = obj.getString("axunge");
		}
	}
	/**
	 * 构建个list
	 * @param array
	 * @return
	 * @throws JSONException 
	 */
	public static List<KindBean> constractList(JSONArray array) throws JSONException{
		List<KindBean> list = new ArrayList<KindBean>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			KindBean bean = new KindBean(obj);
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
	public String getEnergy() {
		return energy;
	}
	public void setEnergy(String energy) {
		this.energy = energy;
	}
	public String getProtein() {
		return protein;
	}
	public void setProtein(String protein) {
		this.protein = protein;
	}
	public String getCarbohydrate() {
		return carbohydrate;
	}
	public void setCarbohydrate(String carbohydrate) {
		this.carbohydrate = carbohydrate;
	}
	public String getFat() {
		return fat;
	}
	public void setFat(String fat) {
		this.fat = fat;
	}
	@Override
	public String toString() {
		return name;
	}
}
