package com.baby.bean;

public class FitmentBean {
	private int picture;//管理员相片
	private String content;//育儿指南
	private String date;//日期
	
	public FitmentBean(int picture,String content,String date){
		this.picture=picture;
		this.content=content;
		this.date=date;
	}
	
	public int getPicture() {
		return picture;
	}
	public void setPicture(int picture) {
		this.picture = picture;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
