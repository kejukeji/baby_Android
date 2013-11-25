package com.keju.baby.bean;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
/**
 *	与服务器请求结果
 * @author Zhoujun
 * @version 创建时间：2013-3-27 上午9:33:14 
 */
public class ResponseBean<T> {
	private Integer status;
	private String error;
	private T obj;
	private List<T> objList;
	private Map<String, List<T>> objMap;
	private String jsonData;
	
	public ResponseBean(JSONObject obj) throws JSONException{
		if(obj!=null){
			if(obj.has("code")){
				this.status = obj.getInt("code");
			}
			if(obj.has("message")){
				this.error = obj.getString("message");
			}
			this.jsonData = obj.toString();
		}
	}
	public ResponseBean(Integer status, String error){
		this.status = status;
		this.error = error;
	}
	
	public ResponseBean(){
	}
	
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return the info
	 */
	public String getError() {
		return error;
	}
	/**
	 * @param info the info to set
	 */
	public void setError(String error) {
		this.error = error;
	}
	/**
	 * @return the obj
	 */
	public T getObj() {
		return obj;
	}
	/**
	 * @param obj the obj to set
	 */
	public void setObj(T obj) {
		this.obj = obj;
	}
	/**
	 * @return the objList
	 */
	public List<T> getObjList() {
		return objList;
	}
	/**
	 * @param objList the objList to set
	 */
	public void setObjList(List<T> objList) {
		this.objList = objList;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public Map<String, List<T>> getObjMap() {
		return objMap;
	}

	public void setObjMap(Map<String, List<T>> objMap) {
		this.objMap = objMap;
	}

}


