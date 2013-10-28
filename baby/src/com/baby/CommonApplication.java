package com.baby;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.baby.db.DataBaseAdapter;
/**
 * 应用全局变量
 * @author Zhoujun
 * 说明：	1、可以缓存一些应用全局变量。比如数据库操作对象
 */
public class CommonApplication extends Application {
	/**
	 * Singleton pattern
	 */
	private static CommonApplication instance;
	/**
	 * 数据库操作类
	 * @return
	 */
	private DataBaseAdapter dataBaseAdapter;
	
	/**
	 * 屏幕的长和宽
	 * @return
	 */
	private int screen_width;
	private int screen_height;
	
	public static CommonApplication getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		dataBaseAdapter = new DataBaseAdapter(this);
		dataBaseAdapter.open();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		WindowManager window=(WindowManager)(this.getSystemService(Context.WINDOW_SERVICE));
		window.getDefaultDisplay().getMetrics(displaymetrics);
		screen_width=displaymetrics.widthPixels;
		screen_height=displaymetrics.heightPixels;
		
	}
	
	/**
	 * 获得数据库操作对象
	 * @return
	 */
	public DataBaseAdapter getDbAdapter(){
		return this.dataBaseAdapter;
	}
	
	/**
	 * 缓存activity对象索引
	 */
	public List<Activity> activities = new ArrayList<Activity>();;
	public List<Activity> getActivities(){
		return activities;
	}
	public void addActivity(Activity mActivity) {
		activities.add(mActivity);
	}
	public int getScreenWidth(){
		return this.screen_width;
		
	}
	public int getScreenHeight(){
		return this.screen_height;
	}
}
