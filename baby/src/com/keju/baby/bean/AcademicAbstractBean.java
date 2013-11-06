package com.keju.baby.bean;

public class AcademicAbstractBean {
	private String title;//文摘标题
	private String content;//文摘内容
	/*private String day;*/ 
	
	public AcademicAbstractBean(String title,String content){
		this.title=title;
		this.content=content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title=title;
	}
	public String getContent(){
		return content;
	}
	public void setContent(String content) {
		this.content=content;
	}	
}
