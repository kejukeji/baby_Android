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
	public static final String APP_DIR_NAME = "ele4android";
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
	 * 腾讯微博配置
	 */
	public static final String TENCENT_APP_ID = "801237383";//app id
	public static final String TENCENT_APP_KEY = "65a1355f898df7f19a6ccd47bdb45d92";//app key
	public static final String TENCENT_REDIRECT_URL = "http://www.eemedia.cn/app_download.aspx";
	
	/**
	 * 人人网
	 */
	public static final String RENREN_APP_ID = "211176";//app id
	public static final String RENREN_API_KEY = "979175fc39c14a8eba6ea78f6e876c01";//api key
	public static final String RENREN_SECRET_KEY = "a113d3aa3cde431eb499f6fc37ff1e30";//secret key
}
