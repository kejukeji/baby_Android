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
public class DoctorProvinceBean implements Serializable {
	private static final long serialVersionUID = -5178447442615478738L;

	private int ProvinceId;
	private String ProvinceName;

	public DoctorProvinceBean(JSONObject obj) throws JSONException {
		if (obj.has("id")) {
			this.ProvinceId = obj.getInt("id");
		}
		if (obj.has("name")) {
			this.ProvinceName = obj.getString("name");
		}
	}

	/**
	 * 构建list
	 * 
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	public static List<DoctorProvinceBean> constractList(JSONArray array) throws JSONException {
		List<DoctorProvinceBean> list = new ArrayList<DoctorProvinceBean>();
		for (int i = 0; i < array.length(); i++) {
			DoctorProvinceBean bean = new DoctorProvinceBean(array.getJSONObject(i));
			list.add(bean);
		}
		return list;
	}

	public int getProvinceId() {
		return ProvinceId;
	}

	public void setProvinceId(int provinceId) {
		ProvinceId = provinceId;
	}

	public String getProvinceName() {
		return ProvinceName;
	}

	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}

}
