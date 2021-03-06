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
	 * requestCode
	 */
	public static final int REQUEST_CREATE_BABY = 5;//创建婴儿账户
	public static final int REQUEST_NEW_ADD_VISIT_CODE = 6;//新增随访记录
	public static final int REQUEST_NEW_ADD_MILK = 7;//新增随访记录
	public static final int REQUEST_COMPLICATION = 8;//合并症
	/**
	 * 分页数据
	 */
	public static final int PAGE_SIZE = 6;
	public static final int PAGE_SIZE1 = 3;
	/**
	 * 登录用户类型；
	 */
	public static final int USER_DOCTOR = 1;//医生
	public static final int USER_MOTHER = 2;//妈妈
	/**
	 * html url
	 */
	public static final String URL_BASE_HTML = "http://baby.kejukeji.com/html/";
	public static final String URL_DOCTOR_LOGIN = "login.html";//医生登录
	public static final String URL_BABY_LOGIN = "mummy/login.html";//婴儿登录
	public static final String URL_REGISTER = "register.html";//注册
	public static final String URL_FITMENT_LIST = "raise_dir.html";//育儿指南列表
	public static final String URL_FITMENT_DETAIL = "raise.html";//育儿指南详情
	
	public static final String URL_VISIT_RECORD = "visit_record.html/";//随访记录
	public static final String URL_GROW_LINE = "grow_line.html/";//生长曲线
	public static final String URL_GROW_LINE_NINE = "grow_line_nine.html/";//九城生长曲线
	public static final String URL_GROW_LINE_FEN_TONG = "grow_line_fen_tong.html/";//fentong生长曲线
	public static final String URL_GROW_RATE = "grow_rate.html/";//生长速率
	public static final String URL_BABY_DETAIL = "baby_detail.html/";//婴儿详细资料
	public static final String URL_NEED = "grow_bar.html/";//营养摄入需求
	
	public static final String URL_CREATE_BABY = "create_baby.html";//医生端创建婴儿账号
	public static final String URL_ADD_FOLLOW_UP = "add_follow-up.html/";//添加随访记录
	public static final String URL_FORMULA = "formula.html";//新增配方奶
	public static final String URL_MEETING_NOTIFY_DETAIL = "meeting.html";//会议通知详情
	public static final String URL_MEETING_NOTIFY_LIST = "meeting_notice.html";//会议通知列表
	public static final String URL_CHANGE_PASSWORD = "password.html";//修改密码
	
	/** 
	 * 用来标识请求照相功能的activity
	 */
	public static final int CAMERA_WITH_DATA = 3023;
	/** 
	 * 用来标识请求gallery的activity 
	 * */
	public static final int PHOTO_PICKED_WITH_DATA = 3021;

}
