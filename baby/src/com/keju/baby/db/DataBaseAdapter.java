/**
 * 
 */
package com.keju.baby.db;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.keju.baby.bean.MusicBean;

/**
 * 数据库操作类
 * @author Zhoujun
 * 说明：	1、数据库操作类
 * 		2、定义好数据表名，数据列，数据表创建语句
 * 		3、操作表的方法紧随其后
 */
public class DataBaseAdapter {
	/**
	 * 数据库版本
	 */
	private static final int DATABASE_VERSION = 1;
	/**
	 * 数据库名称
	 */
	private static final String DATABASE_NAME = "ele4android.db";
	/**
	 * 数据库表id
	 */
	public static final String RECORD_ID = "_id";
	
	private SQLiteDatabase db;		
	private ReaderDbOpenHelper dbOpenHelper;

	public DataBaseAdapter(Context context){
		this.dbOpenHelper = new ReaderDbOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
	}

	public void open(){
		this.db = dbOpenHelper.getWritableDatabase();
	}

	public void close(){
		if(db!=null){
			db.close();
		}
		if(dbOpenHelper!=null){
			dbOpenHelper.close();
		}
	}

	private class ReaderDbOpenHelper extends SQLiteOpenHelper{

		public ReaderDbOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			//创建表
			_db.execSQL(CREATE_SQL_MUSIC);
		}
		/**
		 * 升级应用时，有数据库改动在此方法中修改。
		 */
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
			
		}
	}

	/**************************************************用户********************************************************/
	/**
	 * 音乐表
	 */
	public static final String TABLE_NAME_MUSIC = "t_music";
	/**
	 * 音乐表中的列定义
	 * @author Aizhimin
	 */
	public interface MusicColumns {
		public static final String TITLE = "title";
		public static final String DURATION = "duration";
		public static final String DATA = "data";
	}
	/**
	 * 音乐表查询列
	 */
	public static final String[] PROJECTION_MUSIC = new String[] {
		RECORD_ID, MusicColumns.TITLE,MusicColumns.DURATION,MusicColumns.DATA};
	/**
	 * 音乐表的创建语句
	 */
	public static final String CREATE_SQL_MUSIC = "create table "+TABLE_NAME_MUSIC+
			" ("+
				RECORD_ID+" integer primary key autoincrement,"+
				MusicColumns.TITLE+" text, "+
				MusicColumns.DURATION+" integer, "+
				MusicColumns.DATA+" text "+
			");";
	
	/**
	 * 批量插入频道信息
	 * @param scbList
	 */
	public synchronized void bantchMusic(List<MusicBean> musicList) {
		SQLiteDatabase localDb = db;
		try {
			localDb.beginTransaction();
			localDb.delete(TABLE_NAME_MUSIC, null, null);
			for (MusicBean music:musicList) {
				String sql = "insert into "+TABLE_NAME_MUSIC+" ("
						+MusicColumns.TITLE+","
						+MusicColumns.DURATION+","
						+MusicColumns.DATA+","
						+") values(?,?,?)";
				localDb.execSQL(sql,new Object[]{music.getTitle(),music.getDuration(),music.getData()});
			}
			localDb.setTransactionSuccessful();
		}finally{
			localDb.endTransaction();
		}
	}
	
}
