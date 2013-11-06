package com.keju.baby.bean;

public class MeetingNotifyBean {
	private int picture;//管理员相片
	private String content;//会议内容
	private String date;//日期
	
	public MeetingNotifyBean(int picture,String content,String date){
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
