/**
 * 
 */
package com.keju.baby.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 医生个人资料医院修改实体类
 * 
 * @author zhouyong
 * @data 创建时间：2013-11-28 下午1:28:47
 */
public class DoctorHospitalBean implements Serializable {
	private static final long serialVersionUID = -5178447442615478738L;

	private int hospitalId;
	private int hospitalProvinceId;
	private String hospitalName;

	public DoctorHospitalBean(JSONObject obj) throws JSONException {
		if (obj.has("id")) {
			this.hospitalId = obj.getInt("id");
		}
		if (obj.has("belong_province")) {
			this.hospitalProvinceId = obj.getInt("belong_province");
		}
		if (obj.has("name")) {
			this.hospitalName = obj.getString("name");
		}
	}

	/**
	 * 构建list
	 * 
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	public static List<DoctorHospitalBean> constractList(JSONArray array) throws JSONException {
		List<DoctorHospitalBean> list = new ArrayList<DoctorHospitalBean>();
		for (int i = 0; i < array.length(); i++) {
			DoctorHospitalBean bean = new DoctorHospitalBean(array.getJSONObject(i));
			list.add(bean);
		}
		return list;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public int getHospitalProvinceId() {
		return hospitalProvinceId;
	}

	public void setHospitalProvinceId(int hospitalProvinceId) {
		this.hospitalProvinceId = hospitalProvinceId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

}
