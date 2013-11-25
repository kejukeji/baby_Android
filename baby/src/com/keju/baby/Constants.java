package com.keju.baby;


/**
 * 常量类
 * @author Zhoujun
 * 说明：	1、一些应用常量在此定义
 * 		2、常量包括：一些类型的定义，在其他程序中不能够出现1 2 3之类的值。
 */
public class Constants {
	/**
	 * 应用文件存放目录
	 */
	public static final String APP_DIR_NAME = "baby";
	/**
	 * 微博绑定类型，点击账号绑定和新浪微博
	 */
	public static final String EXTRA_BIND_FROM="extra_bind_from";
	public static final String BIND_WEIBO="bind_weibo";//微博
	public static final String BIND_RENREN="bind_renren";//绑定人人
	
	/**
	 * 微博绑定的request code
	 */
	public static final int REQUEST_CODE_BIND_WEIBO = 11;
	public static final int REQUEST_CODE_BIND_RENREN = 12;
	
	/**
	 * 新浪微博配置
	 */
	public static final String WEIBO_CONSUMER_KEY = "3955024569";// 替换为开发者的appkey，例如"1646212960";
	public static final String WEIBO_CONSUMER_SECRET = "b2071565ef1f2514504ccefb16866b25";// 替换为开发者的appkey，例如"94098772160b6f8ffc1315374d8861f9";
	public static final String WEIBO_REDIRECT_URL = "http://www.eemedia.cn/";//微博应用回调地址
	public static final String WEIBO_USER_UID = "1291843462";
	/**
	 * intent code
	 */
	public static final String EXTRA_DATA = "extra_data";//跳转绑定的数据；
	/**
	 * 网络请求状态码
	 */
	public static final int REQUEST_SUCCESS = 200;//网络请求成功
	public static final int REQUEST_FAILD = 0;//网络请求成功
	/**
	 * 分页数据
	 */
	public static final int PAGE_SIZE = 10;
	/**
	 * 登录用户类型；
	 */
	public static final int USER_DOCTOR = 1;//医生
	public static final int USER_MOTHER = 2;//妈妈
	/**
	 * html url
	 */
	public static final String URL_BASE_HTML = "http://42.121.108.142:7007/html/";
	public static final String URL_DOCTOR_LOGIN = "login.html";//医生登录
	public static final String URL_BABY_LOGIN = "login.html";//婴儿登录
	public static final String URL_FITMENT = "raise.html";//育儿指南
	public static final String URL_MEETING_NOTIFY = "meeting.html";//会议通知
	public static final String URL_CHANGE_PASSWORD = "password.html";//修改密码
	
	/** 
	 * 用来标识请求照相功能的activity
	 */
	public static final int CAMERA_WITH_DATA = 3023;
	/** 
	 * 用来标识请求gallery的activity 
	 * */
	public static final int PHOTO_PICKED_WITH_DATA = 3021;

	public static final String URL_GROW_LINE = "grow_line.html?user_id=";//生长曲线

}
