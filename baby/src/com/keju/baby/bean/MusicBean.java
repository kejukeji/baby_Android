package com.keju.baby.bean;

import java.io.Serializable;
/**
 * 音乐bean
 * @author Zhoujun
 * 说明：	1、属性命名使用驼峰命名法
 * 		2、属性需要添加注释，说明属性的意义
 * 
 */
public class MusicBean implements Serializable {
	private static final long serialVersionUID = 3546317456463328864L;
	private int id;
	private String title;//音乐标题
	private int duration;//音乐时长
	private String data;//音乐路径
	
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
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
