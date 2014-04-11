package com.keju.baby.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.keju.baby.Constants;

/**
 * SharedPreferences工具类
 * 
 * @author Zhoujun 说明：SharedPreferences的操作工具类，需要缓存到SharedPreferences中的数据在此设置。
 */
public class SharedPrefUtil {

	public static final String IS_FIRST_LOGIN = "is_first_login";// 第一次进入
	public static final String DOCTORE_IS_FIRST_LOGIN = "doctor_is_first_login";//医生端是不是第一次登陆

	public static final String WEIBO_ACCESS_TOKEN = "weibo_access_token";// 新浪微博令牌
	public static final String WEIBO_EXPIRES_IN = "weibo_expires_in";// 新浪微博令牌时间
	public static final String WEIBO_ACCESS_CURR_TIME = "weibo_sccess_curr_time";// 新浪微博授权时间

	public static final String QQ_ACCESS_TOKEN = "qq_access_token";// 新浪微博令牌
	public static final String QQ_EXPIRES_IN = "qq_expires_in";// 新浪微博令牌时间
	public static final String QQ_OPENID = "qq_openid";
	public static final String QQ_ACCESS_CURR_TIME = "qq_sccess_curr_time";// 新浪微博授权时间

	public static final String UID = "uid";// 用户id；
	public static final String NAME = "name";// 用户名字；
	public static final String IS_LOGIN = "is_login";// 用户是否登录；
	public static final String USER_TYPE = "user_type";// 登录的用户；
	public static final String SEARCH_HISTORY = "search_history";// 搜索历史
	public static final String ISREMEMBER = "is_remember";// 是否记住密码

	public static final String BABY_NAME = "baby_name";// 婴儿登陆用户名
	public static final String BABY_PASSWORD = "baby_password";// 婴儿登陆密码

	public static final String DOCTOR_NAME = "doctor_name";// 医生登陆用户名
	public static final String DOCTOR_PASSWORD = "doctor_password";// 医生登陆密码

	/**
	 * 判断是否是第一次进入应用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFistLogin(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(IS_FIRST_LOGIN, true);
	}

	/**
	 * 如果已经进入应用，则设置第一次登录为false
	 * 
	 * @param context
	 */
	public static void setFistLogined(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(IS_FIRST_LOGIN, false);
		e.commit();
	}
	
	
	/**
	 * 判断医生端是否是第一次进入应用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isDoctorFistLogin(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(DOCTORE_IS_FIRST_LOGIN, true);
	}

	/**
	 * 如果已经进入应用，则设置第一次登录为false
	 * 
	 * @param context
	 */
	public static void setDoctorFistLogined(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(DOCTORE_IS_FIRST_LOGIN, false);
		e.commit();
	}

	// -----------------------------新浪微博验证信息-----------------
	/**
	 * 设置微博绑定信息
	 * 
	 * @param context
	 * @param access_token
	 * @param expires_in
	 */
	public static void setWeiboInfo(Context context, String access_token, String expires_in, String access_curr_time) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(WEIBO_ACCESS_TOKEN, access_token);
		e.putString(WEIBO_EXPIRES_IN, expires_in);
		e.putString(WEIBO_ACCESS_CURR_TIME, access_curr_time);
		e.commit();
	}

	/**
	 * 清除微博绑定
	 * 
	 * @param context
	 * @return
	 */
	public static void clearWeiboBind(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().remove(WEIBO_ACCESS_TOKEN).remove(WEIBO_EXPIRES_IN).commit();
	}

	public static String getWeiboAccessToken(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(WEIBO_ACCESS_TOKEN, null);
	}

	public static String getWeiboExpiresIn(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(WEIBO_EXPIRES_IN, null);
	}

	public static String getWeiboAccessCurrTime(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(WEIBO_ACCESS_CURR_TIME, null);
	}

	/**
	 * 检测新浪微博是否绑定
	 */
	public static boolean checkWeiboBind(Context context) {
		String WeiboAccessToken = getWeiboAccessToken(context);
		String WeiboExpiresIn = getWeiboExpiresIn(context);
		String weiboAccessCurrTime = getWeiboAccessCurrTime(context);
		if (WeiboAccessToken == null || WeiboExpiresIn == null || weiboAccessCurrTime == null) {
			return false;
		} else {
			long currTime = System.currentTimeMillis();
			long accessCurrTime = Long.parseLong(weiboAccessCurrTime);
			long expiresIn = Long.parseLong(WeiboExpiresIn);
			if ((currTime - accessCurrTime) / 1000 > expiresIn) {
				return false;
			} else {
				return true;
			}
		}
	}

	// -----------------------------腾讯微博验证信息-----------------
	/**
	 * 设置腾讯微博信息
	 * 
	 * @param context
	 * @param access_token
	 * @param expires_in
	 * @param access_curr_time
	 */
	public static void setQQInfo(Context context, String access_token, String expires_in, String openid,
			String access_curr_time) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(QQ_ACCESS_TOKEN, access_token);
		e.putString(QQ_EXPIRES_IN, expires_in);
		e.putString(QQ_OPENID, openid);
		e.putString(QQ_ACCESS_CURR_TIME, access_curr_time);
		e.commit();
	}

	public static String getQQAccessToken(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(QQ_ACCESS_TOKEN, null);
	}

	public static String getQQExpiresIn(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(QQ_EXPIRES_IN, null);
	}

	public static String getQQOpenid(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(QQ_OPENID, null);
	}

	public static String getQQAccessCurrTime(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(QQ_ACCESS_CURR_TIME, null);
	}

	public static void clearQQBind(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().remove(QQ_ACCESS_TOKEN).remove(QQ_EXPIRES_IN).remove(QQ_OPENID).remove(QQ_ACCESS_CURR_TIME).commit();
	}

	/**
	 * 检查腾讯微博是否绑定
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkQQBind(Context context) {
		String qqAccessToken = getQQAccessToken(context);
		String qqExpiresIn = getQQExpiresIn(context);
		String qqAccessCurrTime = getQQAccessCurrTime(context);
		if (qqAccessToken == null || qqExpiresIn == null || qqAccessCurrTime == null) {
			return false;
		} else {
			long currTime = System.currentTimeMillis();
			long accessCurrTime = Long.parseLong(qqAccessCurrTime);
			long expiresIn = Long.parseLong(qqExpiresIn);
			if ((currTime - accessCurrTime) / 1000 > expiresIn) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 获得检测间隔
	 * 
	 * @param con
	 * @return
	 */
	public static final String CHECK_UPDATE_TIME_KEY = "check_update_time_key";// 轮询时间

	public static long getUpdateInterval(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getLong(CHECK_UPDATE_TIME_KEY, 5 * 60 * 1000);
	}

	/**
	 * 判断是否登录
	 * 
	 * @param context
	 * @return true表示登录
	 */
	public static boolean isLogin(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(IS_LOGIN, false);
	}

	/**
	 * 记住登录
	 * 
	 * @param context
	 * @return true表示登录
	 */
	public static void setIsLogin(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(IS_LOGIN, true);
		e.commit();
	}

	/**
	 * 获取用户的uid
	 * 
	 * @param context
	 * @return
	 */
	public static int getUid(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(UID, 0);
	}

	/**
	 * 保存用户的uid
	 * 
	 * @param context
	 * @param uid
	 */
	public static void setUid(Context context, int uid) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putInt(UID, uid);
		e.commit();
	}

	/**
	 * 获取用户名
	 * 
	 * @param context
	 * @return
	 */
	public static String getName(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(NAME, "");
	}

	/**
	 * 保存用户的名字
	 * 
	 * @param context
	 * @param uid
	 */
	public static void setName(Context context, String name) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(NAME, name);
		e.commit();
	}

	/**
	 * 获取用户类型
	 * 
	 * @param context
	 * @return
	 */
	public static int getUserType(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(USER_TYPE, Constants.USER_DOCTOR);
	}

	/**
	 * 保存用户类型
	 * 
	 * @param context
	 * @param uid
	 */
	public static void setUserType(Context context, int userType) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putInt(USER_TYPE, userType);
		e.commit();
	}

	/**
	 * 清除用户信息
	 * 
	 * @param context
	 */
	public static void clearUserinfo(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.remove(UID).remove(USER_TYPE).remove(IS_LOGIN).remove(NAME).remove(SEARCH_HISTORY);
		e.commit();
	}

	/**
	 * 获取用户名
	 * 
	 * @param context
	 * @return
	 */
	public static String getSearchHistory(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(SEARCH_HISTORY, "");
	}

	/**
	 * 保存用户的名字
	 * 
	 * @param context
	 * @param uid
	 */
	public static void setSearchHistory(Context context, String history) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(SEARCH_HISTORY, history);
		e.commit();
	}

	/**
	 * 获取用户是否记录密码
	 * 
	 * @param context
	 * @return
	 */
	public static int getIsRemember(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(ISREMEMBER, 0);
	}

	/**
	 * 保存用户是否记录密码
	 * 
	 * @param context
	 * @param uid
	 */
	public static void setIsRemember(Context context, int uid) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putInt(ISREMEMBER, uid);
		e.commit();
	}

	/**
	 * 获取婴儿登陆用户名
	 * 
	 * @param context
	 * @return
	 */
	public static String getBabyName(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(BABY_NAME, "");
	}

	/**
	 * 保存婴儿用户登陆的名字
	 * 
	 * @param context
	 * @param uid
	 */
	public static void setBabyName(Context context, String name) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(BABY_NAME, name);
		e.commit();
	}

	/**
	 * 获取婴儿登陆密码
	 * 
	 * @param context
	 * @return
	 */
	public static String getBabyPassword(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(BABY_PASSWORD, "");
	}

	/**
	 * 保存婴儿用户登陆的密码
	 * 
	 * @param context
	 * @param uid
	 */
	public static void setBabyPassword(Context context, String password) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(BABY_PASSWORD, password);
		e.commit();
	}

	/**
	 * 获取医生登陆用户名
	 * 
	 * @param context
	 * @return
	 */
	public static String getDoctorName(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(DOCTOR_NAME, "");
	}

	/**
	 * 保存医生用户登陆的名字
	 * 
	 * @param context
	 * @param uid
	 */
	public static void setDoctorName(Context context, String name) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(DOCTOR_NAME, name);
		e.commit();
	}

	/**
	 * 获取医生登陆密码
	 * 
	 * @param context
	 * @return
	 */
	public static String getDoctorPassword(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(DOCTOR_PASSWORD, "");
	}

	/**
	 * 保存医生用户登陆的密码
	 * 
	 * @param context
	 * @param uid
	 */
	public static void setDoctorPassword(Context context, String password) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(DOCTOR_PASSWORD, password);
		e.commit();
	}
	
	/***************************    缓存            ************************/
	
	public static final String COLLECTBABYLIST="collectbabyList"; //收藏婴儿列表
	
	/**
	 * 保存收藏婴儿列表数据
	 * @param context
	 * @param categoryStr
	 */
	public static void setCollectBaby(Context context,String collectBabyListStr){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(COLLECTBABYLIST, collectBabyListStr);
		e.commit();
	}
	/**
	 * 获取收藏婴儿列表数据
	 * @param context
	 * @param type
	 */
	public static String getCollectBaby(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(COLLECTBABYLIST, null);
	}
	
	public static String MILKDATA = "milKData"; //牛奶的数据
	/**
	 * 保持奶粉所以数据list
	 * @param context
	 * @param categoryStr
	 */
	public static void setMilkData(Context context,String babyListStr){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(MILKDATA, babyListStr);
		e.commit();
	}
	/**
	 * 获取奶粉数据list
	 * @param context
	 * @param type
	 */
	public static String getMilkData(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(MILKDATA, null);
	}
	
	public static String DOCTORINFOR = "doctor_infor"; //医生个人信息
	
	/**
	 * 医生个人信息
	 * @param context
	 * @param categoryStr
	 */
	public static void setDoctorInfor(Context context,String doctorInfor){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(DOCTORINFOR, doctorInfor);
		e.commit();
	}
	/**
	 * 获取医生个人信息
	 * @param context
	 * @param type
	 */
	public static String getDoctorInfor(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(DOCTORINFOR, null);
	}
	
}
