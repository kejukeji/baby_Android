package com.keju.baby.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.keju.baby.Constants;

/**
 * SharedPreferences工具类
 * @author Zhoujun
 * 说明：SharedPreferences的操作工具类，需要缓存到SharedPreferences中的数据在此设置。
 */
public class SharedPrefUtil {
	
	public static final String IS_FIRST_LOGIN = "is_first_login";//第一次进入
	
	public static final String WEIBO_ACCESS_TOKEN = "weibo_access_token";//新浪微博令牌
	public static final String WEIBO_EXPIRES_IN = "weibo_expires_in";//新浪微博令牌时间
	public static final String WEIBO_ACCESS_CURR_TIME = "weibo_sccess_curr_time";//新浪微博授权时间
	
	public static final String QQ_ACCESS_TOKEN = "qq_access_token";//新浪微博令牌
	public static final String QQ_EXPIRES_IN = "qq_expires_in";//新浪微博令牌时间
	public static final String QQ_OPENID = "qq_openid";
	public static final String QQ_ACCESS_CURR_TIME = "qq_sccess_curr_time";//新浪微博授权时间
	
	public static final String UID = "uid";//用户id；
	public static final String USER_TYPE = "user_type";//登录的用户；
	
	/**
	 * 判断是否是第一次进入应用
	 * @param context
	 * @return
	 */
	public static boolean isFistLogin(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(IS_FIRST_LOGIN, true);
	}
	/**
	 * 如果已经进入应用，则设置第一次登录为false
	 * @param context
	 */
	public static void setFistLogined(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(IS_FIRST_LOGIN, false);
		e.commit();
	}

	//-----------------------------新浪微博验证信息-----------------
	/**
	 * 设置微博绑定信息
	 * @param context
	 * @param access_token
	 * @param expires_in
	 */
	public static void setWeiboInfo(Context context,String access_token,String expires_in,  String access_curr_time){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(WEIBO_ACCESS_TOKEN, access_token);
		e.putString(WEIBO_EXPIRES_IN, expires_in);
		e.putString(WEIBO_ACCESS_CURR_TIME, access_curr_time);
		e.commit();
	}
	/**
	 * 清除微博绑定
	 * @param context
	 * @return
	 */
	public static void clearWeiboBind(Context context){
		SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().remove(WEIBO_ACCESS_TOKEN).remove(WEIBO_EXPIRES_IN).commit();
	}
	public static String getWeiboAccessToken(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(WEIBO_ACCESS_TOKEN, null);
	}
	public static String getWeiboExpiresIn(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(WEIBO_EXPIRES_IN, null);
	}
	public static String getWeiboAccessCurrTime(Context context){
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
			if((currTime-accessCurrTime)/1000>expiresIn){
				return false;
			}else{
				return true;
			}
		}
	}
	//-----------------------------腾讯微博验证信息-----------------
	/**
	 * 设置腾讯微博信息
	 * @param context
	 * @param access_token
	 * @param expires_in
	 * @param access_curr_time
	 */
	public static void setQQInfo(Context context,String access_token,String expires_in,String openid,  String access_curr_time){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(QQ_ACCESS_TOKEN, access_token);
		e.putString(QQ_EXPIRES_IN, expires_in);
		e.putString(QQ_OPENID, openid);
		e.putString(QQ_ACCESS_CURR_TIME, access_curr_time);
		e.commit();
	}
	public static String getQQAccessToken(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(QQ_ACCESS_TOKEN, null);
	}
	public static String getQQExpiresIn(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(QQ_EXPIRES_IN, null);
	}
	public static String getQQOpenid(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(QQ_OPENID, null);
	}
	public static String getQQAccessCurrTime(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(QQ_ACCESS_CURR_TIME, null);
	}
	public static void clearQQBind(Context context){
		SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().remove(QQ_ACCESS_TOKEN).remove(QQ_EXPIRES_IN).remove(QQ_OPENID).remove(QQ_ACCESS_CURR_TIME).commit();
	}
	
	/**
	 * 检查腾讯微博是否绑定
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
			if((currTime-accessCurrTime)/1000>expiresIn){
				return false;
			}else{
				return true;
			}
		}
	}
	
	/**
	 * 获得检测间隔
	 * @param con
	 * @return
	 */
	public static final String CHECK_UPDATE_TIME_KEY = "check_update_time_key";//轮询时间
	public static long getUpdateInterval(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getLong(CHECK_UPDATE_TIME_KEY, 5*60*1000);
	}
	
	/**
	 * 判断是否登录
	 * @param context
	 * @return true表示登录
	 */
	public static boolean isLogin(Context context){
		return getUid(context) > 0;
	}
	/**
	 * 获取用户的uid
	 * @param context
	 * @return
	 */
	public static int getUid(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(UID, 0);
	}
	/**
	 * 保存用户的uid
	 * @param context
	 * @param uid
	 */
	public static void setUid(Context context, int uid){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putInt(UID, uid);
		e.commit();
	}
	/**
	 * 获取用户类型
	 * @param context
	 * @return
	 */
	public static int getUserType(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(USER_TYPE, Constants.USER_DOCTOR);
	}
	/**
	 * 保存用户类型
	 * @param context
	 * @param uid
	 */
	public static void setUserType(Context context, int userType){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putInt(USER_TYPE, userType);
		e.commit();
	}
}
