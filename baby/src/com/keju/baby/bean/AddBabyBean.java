package com.keju.baby.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.keju.baby.helper.BusinessHelper;

public class AddBabyBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5178447442615478738L;
	/**
	 * 增加婴儿bean 说明： 1、属性命名使用驼峰命名法 2、属性需要添加注释，说明属性的意义
	 */
	private String baby_name;// 婴儿名字
	private String patriarch_tel;// 父母号码
	private String incepting_password;// 初始密码
	private String again_password;// 确认密码
	private String sex;// 性别
	private String expected_data;// 预产期
	private String born_data;// 出生日期
	private String weight; // 出生体重
	private String height;// 出生身高
	private String head_size;// 出生体重
	private String delivery_way;// 分娩方式
	private String complication;// 有无合并症
	private String standardStr;//身长的标准

	
	public String getStandardStr() {
		return standardStr;
	}

	public void setStandardStr(String standardStr) {
		this.standardStr = standardStr;
	}

	public String getPatriarch_tel() {
		return patriarch_tel;
	}

	public void setPatriarch_tel(String patriarch_tel) {
		this.patriarch_tel = patriarch_tel;
	}
	public String getBaby_name() {
		return baby_name;
	}

	public void setBaby_name(String baby_name) {
		this.baby_name = baby_name;
	}

	public String getIncepting_password() {
		return incepting_password;
	}

	public void setIncepting_password(String incepting_password) {
		this.incepting_password = incepting_password;
	}

	public String getAgain_password() {
		return again_password;
	}

	public void setAgain_password(String again_password) {
		this.again_password = again_password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getExpected_data() {
		return expected_data;
	}

	public void setExpected_data(String expected_data) {
		this.expected_data = expected_data;
	}

	public String getBorn_data() {
		return born_data;
	}

	public void setBorn_data(String born_data) {
		this.born_data = born_data;
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

	public String getDelivery_way() {
		return delivery_way;
	}

	public void setDelivery_way(String delivery_way) {
		this.delivery_way = delivery_way;
	}

	public String getComplication() {
		return complication;
	}

	public void setComplication(String complication) {
		this.complication = complication;
	}

}
