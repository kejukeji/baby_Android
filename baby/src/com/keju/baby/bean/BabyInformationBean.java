package com.keju.baby.bean;

public class BabyInformationBean {
	/**
	 * 婴儿数据bean
	 * 说明：	1、属性命名使用驼峰命名法
	 * 		2、属性需要添加注释，说明属性的意义
	 */
	private int picture;//头像id
	private String id;//编号
	private String name;//姓名
	private String day; //出生日期
	
	public BabyInformationBean(int picture,String id,String name,String day){
		this.picture=picture;
		this.id=id;
		this.name=name;
		this.day=day;
	}
	
	public int getPicture() {
		return picture;
	}
	public void setPicture(int picture) {
		this.picture = picture;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}

}
