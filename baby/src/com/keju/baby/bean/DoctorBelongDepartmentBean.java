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
 * 医生个人资料职位修改实体类
 * 
 * @author zhouyong
 * @data 创建时间：2013-11-28 下午1:28:47
 */
public class DoctorBelongDepartmentBean implements Serializable {
	private static final long serialVersionUID = -5178447442615478738L;

	private int PositionId;
	private String PositionName;

	public DoctorBelongDepartmentBean(JSONObject obj) throws JSONException {
		if (obj.has("id")) {
			this.PositionId = obj.getInt("id");
		}
		if (obj.has("name")) {
			this.PositionName = obj.getString("name");
		}
	}

	/**
	 * 构建list
	 * 
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	public static List<DoctorBelongDepartmentBean> constractList(JSONArray array) throws JSONException {
		List<DoctorBelongDepartmentBean> list = new ArrayList<DoctorBelongDepartmentBean>();
		for (int i = 0; i < array.length(); i++) {
			DoctorBelongDepartmentBean bean = new DoctorBelongDepartmentBean(array.getJSONObject(i));
			list.add(bean);
		}
		return list;
	}

	public int getPositionId() {
		return PositionId;
	}

	public void setPositionId(int positionId) {
		PositionId = positionId;
	}

	public String getPositionName() {
		return PositionName;
	}

	public void setPositionName(String positionName) {
		PositionName = positionName;
	}

}
