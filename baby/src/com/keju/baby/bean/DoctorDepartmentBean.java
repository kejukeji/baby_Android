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
 * 医生个人资料科室修改实体类
 * 
 * @author zhouyong
 * @data 创建时间：2013-11-28 下午1:28:47
 */
public class DoctorDepartmentBean implements Serializable {
	private static final long serialVersionUID = -5178447442615478738L;

	private int DepartmentId;
	private String DepartmentName;

	public DoctorDepartmentBean(JSONObject obj) throws JSONException {
		if (obj.has("id")) {
			this.DepartmentId = obj.getInt("id");
		}
		if (obj.has("name")) {
			this.DepartmentName = obj.getString("name");
		}
	}

	/**
	 * 构建list
	 * 
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	public static List<DoctorDepartmentBean> constractList(JSONArray array) throws JSONException {
		List<DoctorDepartmentBean> list = new ArrayList<DoctorDepartmentBean>();
		for (int i = 0; i < array.length(); i++) {
			DoctorDepartmentBean bean = new DoctorDepartmentBean(array.getJSONObject(i));
			list.add(bean);
		}
		return list;
	}

	public int getDepartmentId() {
		return DepartmentId;
	}

	public void setDepartmentId(int departmentId) {
		DepartmentId = departmentId;
	}

	public String getDepartmentName() {
		return DepartmentName;
	}

	public void setDepartmentName(String departmentName) {
		DepartmentName = departmentName;
	}

}
