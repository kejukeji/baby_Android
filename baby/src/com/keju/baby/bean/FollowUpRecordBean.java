package com.keju.baby.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.keju.baby.helper.BusinessHelper;

public class FollowUpRecordBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5178447442615478738L;
	/**
	 * 随访记录表bean 说明： 1、属性命名使用驼峰命名法 2、属性需要添加注释，说明属性的意义
	 */
	private int babyId;
	private String measure_data;// 测量日期
	private String weight;
	private String height;
	private String head_size;
	private String Hospital_within;
	private String breast_feeding;
	private String brand;
	private String kind;
	private String recipe_milk;
	public String getMeasure_data() {
		return measure_data;
	}
	public void setMeasure_data(String measure_data) {
		this.measure_data = measure_data;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getHead_size() {
		return head_size;
	}
	public void setHead_size(String head_size) {
		this.head_size = head_size;
	}
	public String getHospital_within() {
		return Hospital_within;
	}
	public void setHospital_within(String hospital_within) {
		Hospital_within = hospital_within;
	}
	public String getBreast_feeding() {
		return breast_feeding;
	}
	public void setBreast_feeding(String breast_feeding) {
		this.breast_feeding = breast_feeding;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getRecipe_milk() {
		return recipe_milk;
	}
	public void setRecipe_milk(String recipe_milk) {
		this.recipe_milk = recipe_milk;
	}
	public int getBabyId() {
		return babyId;
	}
	public void setBabyId(int babyId) {
		this.babyId = babyId;
	}
	

}
