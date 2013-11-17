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
	public static final String BASE_HTML_URL = "http://42.121.108.142:7007/html/";
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
	 * 登录用户类型；
	 */
	public static final int USER_DOCTOR = 1;//医生
	public static final int USER_MOTHER = 2;//妈妈
}
