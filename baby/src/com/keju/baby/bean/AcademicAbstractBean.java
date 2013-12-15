package com.keju.baby.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AcademicAbstractBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4717738989456770009L;
	private int id;
	private String title;// 文摘标题
	private String content;// 文摘内容
	private boolean isCollect;// 是否收藏;

	
	public AcademicAbstractBean(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public AcademicAbstractBean(JSONObject obj) throws JSONException {
		if(obj.has("id")){
			this.id = obj.getInt("id");
		}
		if (obj.has("title")) {
			this.title = obj.getString("title");
		}
		if (obj.has("content")) {
			this.content = obj.getString("content");
		}
		if (obj.has("is_collect")) {
			this.isCollect = obj.getBoolean("is_collect");
		}
	}

	/**
	 * 构建一个list
	 * 
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	public static List<AcademicAbstractBean> constractList(JSONArray array) throws JSONException {
		List<AcademicAbstractBean> list = new ArrayList<AcademicAbstractBean>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			AcademicAbstractBean bean = new AcademicAbstractBean(obj);
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isCollect() {
		return isCollect;
	}

	public void setCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}
}
