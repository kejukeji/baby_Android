/**
 * 
 */
package com.keju.baby.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.keju.baby.bean.AddBabyBean;
import com.keju.baby.bean.BabyBean;
import com.keju.baby.bean.BrandBean;
import com.keju.baby.bean.FollowUpRecordBean;
import com.keju.baby.bean.KindBean;
import com.keju.baby.bean.YardBean;

/**
 * 数据库操作类
 * 
 * @author Zhoujun 说明： 1、数据库操作类 2、定义好数据表名，数据列，数据表创建语句 3、操作表的方法紧随其后
 */
public class DataBaseAdapter {
	/**
	 * 数据库版本
	 */
	private static final int DATABASE_VERSION = 1;
	/**
	 * 数据库名称
	 */
	private static final String DATABASE_NAME = "baby.db";
	/**
	 * 数据库表id
	 */
	public static final String RECORD_ID = "_id";

	private SQLiteDatabase db;
	private ReaderDbOpenHelper dbOpenHelper;

	public DataBaseAdapter(Context context) {
		this.dbOpenHelper = new ReaderDbOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void open() {
		this.db = dbOpenHelper.getWritableDatabase();
	}

	public void close() {
		if (db != null) {
			db.close();
		}
		if (dbOpenHelper != null) {
			dbOpenHelper.close();
		}
	}

	private class ReaderDbOpenHelper extends SQLiteOpenHelper {

		public ReaderDbOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			// 创建表
			_db.execSQL(CREATE_SQL_FOLLOW_UP);
			_db.execSQL(CREATE_SQL_HOSPITAL);
			_db.execSQL(CREATE_SQL_BARAND);
			_db.execSQL(CREATE_SQL_KIND);
			_db.execSQL(CREATE_SQL_ADD_BABY);
			_db.execSQL(CREATE_SQL_BABY_LIST);

		}

		/**
		 * 升级应用时，有数据库改动在此方法中修改。
		 */
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {

		}
	}

	/************************************************** 用户 ********************************************************/
	/**
	 * 随访记录表
	 */
	public static final String TABLE_NAME_FOLLOW_UP_RECORD = "follow_up_record";

	/**
	 * 随访记录表中的列定义
	 * 
	 */
	public interface FollowColumns {
		public static final String BABYID = "babyId";//婴儿id
		public static final String MEASURE_DATA = "data";// 测量日期
		public static final String WEIGHT = "weight";// 体重
		public static final String HEIGHT = "height";// 身长
		public static final String HEAD_SIZE = "head_size";// 头围
		public static final String BREAST_FEEDING = "breast"; // 母乳喂养量
		public static final String HOSPITAl_WITHIN = "Hospital";// 医院内外
		public static final String BRAND = "brand";// 品牌
		public static final String KIND = "kind";// 种类
		public static final String RECIPE_MILK = "recipemilk";// 配方奶喂养量

	}

	/**
	 * 随访记录表查询列
	 */
	public static final String[] PROJECTION_FOLLOW_UP = new String[] { RECORD_ID, FollowColumns.BABYID,FollowColumns.MEASURE_DATA,
			FollowColumns.WEIGHT, FollowColumns.HEIGHT, FollowColumns.HEAD_SIZE, FollowColumns.BREAST_FEEDING,
			FollowColumns.HOSPITAl_WITHIN, FollowColumns.BRAND, FollowColumns.KIND, FollowColumns.RECIPE_MILK };

	/**
	 * 随访记录表的创建语句
	 */
	public static final String CREATE_SQL_FOLLOW_UP = "create table " + TABLE_NAME_FOLLOW_UP_RECORD + " (" + RECORD_ID
			+ " integer primary key autoincrement," + FollowColumns.BABYID + " integer ,"+ FollowColumns.MEASURE_DATA + " text," + FollowColumns.WEIGHT
			+ " text," + FollowColumns.HEIGHT + " text," + FollowColumns.HEAD_SIZE + " text,"
			+ FollowColumns.BREAST_FEEDING + " text," + FollowColumns.HOSPITAl_WITHIN + " text," + FollowColumns.BRAND
			+ " text," + FollowColumns.KIND + " text," + FollowColumns.RECIPE_MILK + " text " + ");";

	/**
	 * 插入数据
	 * 
	 * @param list
	 */
	public synchronized Boolean inserData(FollowUpRecordBean bean) {
		SQLiteDatabase localDb = db;
		Boolean isInser = false;
		try {
			localDb.beginTransaction();
//			localDb.delete(TABLE_NAME_FOLLOW_UP_RECORD, null, null);
//			for (FollowUpRecordBean bean : list) {
				String sql = "insert into " + TABLE_NAME_FOLLOW_UP_RECORD + " (" 
                        + FollowColumns.BABYID + ","
                        + FollowColumns.MEASURE_DATA + ","
						+ FollowColumns.WEIGHT + ","
                        + FollowColumns.HEIGHT + "," 
						+ FollowColumns.HEAD_SIZE + ","
						+ FollowColumns.BREAST_FEEDING + ","
						+ FollowColumns.HOSPITAl_WITHIN + ","
						+ FollowColumns.BRAND + "," 
						+ FollowColumns.KIND + "," 
						+ FollowColumns.RECIPE_MILK
						+ ") values(?,?,?,?,?,?,?,?,?,?)";
				localDb.execSQL(sql,
						new Object[] {bean.getBabyId(),bean.getMeasure_data(), bean.getWeight(), bean.getHeight(), bean.getHead_size(),
								bean.getBreast_feeding(), bean.getHospital_within(), bean.getBrand(), bean.getKind(),
								bean.getRecipe_milk() });
//			}
			
			localDb.setTransactionSuccessful();
			isInser = true;
		} finally {
			localDb.endTransaction();
		}
		return isInser;
	}

	/**
	 * 获取随访记录数据
	 * 
	 * @return
	 */
	public List<FollowUpRecordBean> findAllFollow() {
		List<FollowUpRecordBean> followList = new ArrayList<FollowUpRecordBean>();
		Cursor c = db.query(TABLE_NAME_FOLLOW_UP_RECORD, PROJECTION_FOLLOW_UP, null, null, null, null, RECORD_ID);
		while (c.moveToNext()) {
			FollowUpRecordBean followBean = new FollowUpRecordBean();
			followBean.setBabyId(c.getInt(c.getColumnIndex(FollowColumns.BABYID)));
			followBean.setMeasure_data(c.getString(c.getColumnIndex(FollowColumns.MEASURE_DATA)));
			followBean.setWeight(c.getString(c.getColumnIndex(FollowColumns.WEIGHT)));
			followBean.setHeight(c.getString(c.getColumnIndex(FollowColumns.HEIGHT)));
			followBean.setHead_size(c.getString(c.getColumnIndex(FollowColumns.HEAD_SIZE)));
			followBean.setBreast_feeding(c.getString(c.getColumnIndex(FollowColumns.BREAST_FEEDING)));
			followBean.setHospital_within(c.getString(c.getColumnIndex(FollowColumns.HOSPITAl_WITHIN)));
			followBean.setBrand(c.getString(c.getColumnIndex(FollowColumns.BRAND)));
			followBean.setKind(c.getString(c.getColumnIndex(FollowColumns.KIND)));
			followBean.setRecipe_milk(c.getString(c.getColumnIndex(FollowColumns.RECIPE_MILK)));
			followList.add(followBean);
		}
		c.close();
		return followList;
	}

	/************************************************** 新增婴儿 ********************************************************/
	/**
	 * 新增婴儿表
	 * 
	 * */
	public static final String TABLE_NAME_ADDBABY = "addBaby";

	/**
	 * 新增婴儿表中的定义
	 * 
	 * */

	public interface AddBabyColumns {
		public static final String PATRIARCH_TEL = "patriarch_tel";// 父母号码
		public static final String BABYNAME = "baby_name";// 婴儿名字
		public static final String INCEPTING_PASSWORD = "incepting_password";// 初始密码
		public static final String AGAIN_PASSWORD = "again_password";// 确认密码
		public static final String SEX = "sex";// 性别
		public static final String EXPECTED_DATA = "expected_data";// 预产期
		public static final String BORN_DATA = "born_data";// 出生日期
		public static final String WEIGHT = "weight"; // 出生体重
		public static final String HEIGHT = "height";// 出生身高
		public static final String HEAD_SIZE = "head_size";// 出生体重
		public static final String DELIVERY_WAY = "delivery_way";// 分娩方式
		public static final String COMPLICATION = "complication";// 有无合并症
		public static final String STANDARDSTR = "standardStr"; // 生长的标准
	}

	/**
	 * 新增婴儿表查询列
	 */
	public static final String[] PROJECTION_ADDBABY = new String[] { RECORD_ID, AddBabyColumns.PATRIARCH_TEL,
			AddBabyColumns.BABYNAME, AddBabyColumns.INCEPTING_PASSWORD, AddBabyColumns.AGAIN_PASSWORD,
			AddBabyColumns.SEX, AddBabyColumns.EXPECTED_DATA, AddBabyColumns.BORN_DATA, AddBabyColumns.WEIGHT,
			AddBabyColumns.HEIGHT, AddBabyColumns.HEAD_SIZE, AddBabyColumns.DELIVERY_WAY, AddBabyColumns.COMPLICATION,
			AddBabyColumns.STANDARDSTR };

	/**
	 * 新增婴儿表的创建语句
	 */
	public static final String CREATE_SQL_ADD_BABY = "create table "+ TABLE_NAME_ADDBABY+ 
		" (" + RECORD_ID + " integer primary key autoincrement,"
			+AddBabyColumns.PATRIARCH_TEL + " text,"
			+ AddBabyColumns.BABYNAME + " text,"
			+ AddBabyColumns.INCEPTING_PASSWORD + " text,"
			+ AddBabyColumns.AGAIN_PASSWORD + " text,"
			+ AddBabyColumns.SEX + " text," 
			+ AddBabyColumns.EXPECTED_DATA + " text," 
			+ AddBabyColumns.BORN_DATA+ " text," 
			+ AddBabyColumns.WEIGHT + " text,"
			+ AddBabyColumns.HEIGHT + " text,"
			+ AddBabyColumns.HEAD_SIZE+ " text,"
			+ AddBabyColumns.DELIVERY_WAY + " text," 
			+ AddBabyColumns.COMPLICATION + " text,"
			+ AddBabyColumns.STANDARDSTR + " text " + 
		");";

	/**
	 * 插入新增婴儿数据
	 * 
	 * @param list
	 */
	public synchronized Boolean inserAddBabyData(AddBabyBean bean) {
		SQLiteDatabase localDb = db;
		try {
			localDb.beginTransaction();
			// localDb.delete(TABLE_NAME_ADDBABY, null, null);//增加表数据会被删除（ 覆盖）
			// for(AddBabyBean bean:list){
			String sql = "insert into " + TABLE_NAME_ADDBABY + 
				" (" 
			        + AddBabyColumns.PATRIARCH_TEL + ","
					+ AddBabyColumns.BABYNAME + ","
			        + AddBabyColumns.INCEPTING_PASSWORD + ","
					+ AddBabyColumns.AGAIN_PASSWORD + "," 
			        + AddBabyColumns.SEX + "," 
					+ AddBabyColumns.EXPECTED_DATA + ","
					+ AddBabyColumns.BORN_DATA + "," 
					+ AddBabyColumns.WEIGHT + "," 
					+ AddBabyColumns.HEIGHT + ","
					+ AddBabyColumns.HEAD_SIZE + "," 
					+ AddBabyColumns.DELIVERY_WAY + "," 
					+ AddBabyColumns.COMPLICATION + ","
					+ AddBabyColumns.STANDARDSTR + 
				 ") values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			localDb.execSQL(sql,new Object[] { bean.getPatriarch_tel(), bean.getBaby_name(), bean.getIncepting_password(),
							bean.getAgain_password(), bean.getSex(), bean.getExpected_data(), bean.getBorn_data(),
							bean.getWeight(), bean.getHeight(), bean.getHead_size(), bean.getDelivery_way(),
							bean.getComplication(), bean.getStandardStr() });
			localDb.setTransactionSuccessful();
			// }
			return true;
		} catch (SQLException e) {
			return false;
		} finally {
			localDb.endTransaction();
		}
	}

	/**
	 * 获取新增婴儿单个数据
	 * 
	 * @return
	 */
	public AddBabyBean getNewBabyData() {
		Cursor c = db.query(TABLE_NAME_ADDBABY, PROJECTION_ADDBABY, null, null, null, null, RECORD_ID);
		AddBabyBean addBabyBean = null;
		while (c.moveToNext()) {
			addBabyBean = new AddBabyBean();
			addBabyBean.setBaby_name(c.getString(c.getColumnIndex(AddBabyColumns.BABYNAME)));
			addBabyBean.setBorn_data(c.getString(c.getColumnIndex(AddBabyColumns.BORN_DATA)));
		}
		c.close();
		return addBabyBean;
	}

	/**
	 * 获取新增婴儿整个list数据
	 * 
	 * @return
	 */
	public List<AddBabyBean> getNewBabyList() {
		List<AddBabyBean> newBabyList = new ArrayList<AddBabyBean>();
		Cursor c = db.query(TABLE_NAME_ADDBABY, PROJECTION_ADDBABY, null, null, null, null, RECORD_ID);
		while (c.moveToNext()) {
			AddBabyBean newBabyBean = new AddBabyBean();
			newBabyBean.setPatriarch_tel(c.getString(c.getColumnIndex(AddBabyColumns.PATRIARCH_TEL)));
			newBabyBean.setBaby_name(c.getString(c.getColumnIndex(AddBabyColumns.BABYNAME)));
			newBabyBean.setIncepting_password(c.getString(c.getColumnIndex(AddBabyColumns.INCEPTING_PASSWORD)));
			newBabyBean.setAgain_password(c.getString(c.getColumnIndex(AddBabyColumns.AGAIN_PASSWORD)));
			newBabyBean.setSex(c.getString(c.getColumnIndex(AddBabyColumns.SEX)));
			newBabyBean.setExpected_data(c.getString(c.getColumnIndex(AddBabyColumns.EXPECTED_DATA)));
			newBabyBean.setBorn_data(c.getString(c.getColumnIndex(AddBabyColumns.BORN_DATA)));
			newBabyBean.setWeight(c.getString(c.getColumnIndex(AddBabyColumns.WEIGHT)));
			newBabyBean.setHeight(c.getString(c.getColumnIndex(AddBabyColumns.HEIGHT)));
			newBabyBean.setHead_size(c.getString(c.getColumnIndex(AddBabyColumns.HEAD_SIZE)));
			newBabyBean.setDelivery_way(c.getString(c.getColumnIndex(AddBabyColumns.DELIVERY_WAY)));
			newBabyBean.setComplication(c.getString(c.getColumnIndex(AddBabyColumns.COMPLICATION)));
			newBabyBean.setStandardStr(c.getString(c.getColumnIndex(AddBabyColumns.STANDARDSTR)));
			newBabyList.add(newBabyBean);
		}
		c.close();
		return newBabyList;
	}

	/************************************************** 婴儿列表表 ********************************************************/
	/**
	 * 婴儿列表表
	 * 
	 * */
	public static final String TABLE_NAME_BABYLIST = "baby_list";

	/**
	 * 婴儿列表表中的定义
	 * 
	 * */

	public interface BabyListColumns {
		public static final String BABYID = "baby_id"; // 婴儿的id
		public static final String BABYNAME = "baby_name";// 婴儿名字
		public static final String BABY_PICTURE = "baby_picture";// 时间
		public static final String INDEX = "page_index";//分页；

	}

	/**
	 * 婴儿列表表查询列
	 */
	public static final String[] PROJECTION_BABY = new String[] { RECORD_ID, BabyListColumns.BABYID,BabyListColumns.BABYNAME,
			BabyListColumns.BABY_PICTURE,BabyListColumns.INDEX };

	/**
	 * 婴儿列表表的创建语句
	 */
	public static final String CREATE_SQL_BABY_LIST = "create table " + TABLE_NAME_BABYLIST + " (" + RECORD_ID
			+ " integer primary key autoincrement,"
			+ BabyListColumns.BABYID + " integer ," 
			+ BabyListColumns.BABYNAME + " text," 
			+ BabyListColumns.BABY_PICTURE+ " text, "
			+ BabyListColumns.INDEX+ " integer "
			+ ");";

	/**
	 * 批量插入婴儿列表数据
	 * 
	 * @param list
	 */
	public synchronized void inserBabyListData(List<BabyBean> list,int index) {
		SQLiteDatabase localDb = db;
		try {
			localDb.beginTransaction();
//			localDb.delete(TABLE_NAME_BABYLIST, null, null);// 增加表数据会被删除（ 覆盖）
			for (BabyBean bean : list) {
				String sql = "insert into " + TABLE_NAME_BABYLIST + " ("
						                    + BabyListColumns.BABYID + ","
			                               + BabyListColumns.BABYNAME + ","
						                   + BabyListColumns.BABY_PICTURE + ","
						                   + BabyListColumns.INDEX +") values(?,?,?,?)";
				localDb.execSQL(sql, new Object[] { bean.getId(),bean.getName(), bean.getAvatarUrl(),index});

			}
			localDb.setTransactionSuccessful();// 关闭数据库
		} finally {
			localDb.endTransaction();
		}
	}
	/**
	 * 查询是否已缓存
	 * @param index
	 * @return true 表示存在缓存
	 */
	public boolean qryCacheIsExist(int index){
		SQLiteDatabase localDb = db;
		boolean isExist = false;
		Cursor cur = localDb.rawQuery(
				"select * from baby_list where  page_index = ?",
				new String[] { index + "" });
		while (cur.moveToNext()) {
			isExist = true;
			break;
		}
		cur.close();
		return isExist;
	}
	
	/**
	 * 更新缓存
	 * @param index
	 */
	public void updateCache( List<BabyBean> list, int index){
		for (BabyBean bean : list) {
		String sql = "update "+TABLE_NAME_BABYLIST+" SET "
				+BabyListColumns.BABYID + "= ? "
				+ " where " + BabyListColumns.BABYNAME + "= ? and "
				+BabyListColumns.BABY_PICTURE + "= ? "+ "= ? and "
				+ BabyListColumns.INDEX + "= ? ;";
		db.execSQL(sql,new Object[]{bean.getId(), bean.getName(), bean.getAvatarUrl(),index});
		}
	}
	
	/**
	 * 单条插入婴儿列表数据
	 * 
	 * @param bean
	 */
	public synchronized void inserSingleBabyListData(BabyBean bean) {
		SQLiteDatabase localDb = db;
		try {
			localDb.beginTransaction();
			String sql = "insert into " + TABLE_NAME_BABYLIST + " (" + BabyListColumns.BABYNAME + ","
					+ BabyListColumns.BABY_PICTURE + ") values(?,?)";
			localDb.execSQL(sql, new Object[] { bean.getName(), bean.getAvatarUrl()});

			localDb.setTransactionSuccessful();
		} finally {
			localDb.endTransaction();
		}
	}

	/**
	 * 获取婴儿列表数据
	 * 
	 * @return
	 */
	public List<BabyBean> getBabyListData() {
		List<BabyBean> BabyList = new ArrayList<BabyBean>();
		Cursor c = db.query(TABLE_NAME_BABYLIST, PROJECTION_BABY, null, null, null, null, RECORD_ID);
		while (c.moveToNext()) {
			BabyBean babyBean = new BabyBean();
			babyBean.setId(c.getInt(c.getColumnIndex(BabyListColumns.BABYID)));
			babyBean.setName(c.getString(c.getColumnIndex(BabyListColumns.BABYNAME)));
			babyBean.setAvatarUrl(c.getString(c.getColumnIndex(BabyListColumns.BABY_PICTURE)));
			BabyList.add(babyBean);
		}
		c.close();
		return BabyList;
	}

	/************************************************** 院内外表 ********************************************************/

	private static final String TABLE_NAME_HOSPITAL = "hospital";

	/**
	 * 院内外表中的定义
	 * 
	 * */
	public interface HospitalColumns {
		public static final String HOSPITALID = "hospitalId";
		public static final String HOSPITALNAME = "hospitalName";
	}

	/**
	 * 院内外表查询列
	 */
	public static final String[] PROJECTION_HOSPITAL = new String[] { RECORD_ID, HospitalColumns.HOSPITALID,
			HospitalColumns.HOSPITALNAME };

	/**
	 * 院内外表的建立
	 */

	public static final String CREATE_SQL_HOSPITAL = "create table " + TABLE_NAME_HOSPITAL + " (" + RECORD_ID
			+ " integer primary key autoincrement," + HospitalColumns.HOSPITALID + " integer, "
			+ HospitalColumns.HOSPITALNAME + " text " + ");";

	/**
	 * 插入数据
	 * 
	 * @param list
	 */
	public synchronized void inserHospitalData(List<YardBean> yardList) {
		SQLiteDatabase localDb = db;
		try {
			localDb.beginTransaction();
			localDb.delete(TABLE_NAME_HOSPITAL, null, null);
			for (YardBean yardBean : yardList) {
				// String sql = "insert into " + TABLE_NAME_HOSPITAL + " ("
				// + HospitalColumns.HOSPITALID + ","
				// + HospitalColumns.HOSPITALNAME
				// + ") values(?,?)";
				// localDb.execSQL(sql, new Object[]
				// {yardBean.getId(),yardBean.getName()});
				//
				ContentValues cv = new ContentValues();
				cv.put(HospitalColumns.HOSPITALID, yardBean.getId());
				cv.put(HospitalColumns.HOSPITALNAME, yardBean.getName());
				localDb.insert(TABLE_NAME_HOSPITAL, RECORD_ID, cv);

			}
			localDb.setTransactionSuccessful();
		} finally {
			localDb.endTransaction();
		}
	}

	/**
	 * 获取医院数据
	 * 
	 * @return
	 */
	public List<YardBean> findAllHospital() {
		List<YardBean> yardList = new ArrayList<YardBean>();
		Cursor c = db.query(TABLE_NAME_HOSPITAL, PROJECTION_HOSPITAL, null, null, null, null, RECORD_ID);
		while (c.moveToNext()) {
			YardBean yardBean = new YardBean();
			yardBean.setId(c.getInt(1));
			yardBean.setName(c.getString(2));
			yardList.add(yardBean);
		}
		c.close();
		return yardList;
	}

	/************************************************** 品牌表 ********************************************************/

	/**
	 * 品牌表
	 * */
	private static final String TABLE_NAME_BARAND = "brand";

	/**
	 * 品牌表中的定义
	 * 
	 * */
	public interface BrandColumns {
		public static final String BARANDID = "brandId";
		public static final String BARANDNAME = "brandName";
	}

	/**
	 * 品牌表查询列
	 */
	public static final String[] PROJECTION_BARANDL = new String[] { RECORD_ID, BrandColumns.BARANDID,
			BrandColumns.BARANDNAME };

	/**
	 * 品牌表的建立
	 */

	public static final String CREATE_SQL_BARAND = "create table " + TABLE_NAME_BARAND + " (" + RECORD_ID
			+ " integer primary key autoincrement," + BrandColumns.BARANDID + " integer, " + BrandColumns.BARANDNAME
			+ " text " + ");";

	/**
	 * 插入品牌数据
	 * 
	 * @param list
	 */
	public synchronized void inserBarandData(List<BrandBean> baradList) {
		SQLiteDatabase localDb = db;
		try {
			localDb.beginTransaction();
			localDb.delete(TABLE_NAME_BARAND, null, null);
			for (BrandBean baradBean : baradList) {
				// String sql = "insert into " + TABLE_NAME_BARAND + " ("
				// + BrandColumns.BARANDID + ","
				// + BrandColumns.BARANDNAME
				// + ") values(?,?)";
				// localDb.execSQL(sql, new Object[]
				// {baradBean.getId(),baradBean.getName()});
				//
				ContentValues cv = new ContentValues();
				cv.put(BrandColumns.BARANDID, baradBean.getId());
				cv.put(BrandColumns.BARANDNAME, baradBean.getName());
				localDb.insert(TABLE_NAME_BARAND, RECORD_ID, cv);
			}
			localDb.setTransactionSuccessful();
		} finally {
			localDb.endTransaction();
		}
	}

	/**
	 * 获取医院数据
	 * 
	 * @return
	 */
	public List<BrandBean> findAllBarand() {
		List<BrandBean> barndList = new ArrayList<BrandBean>();
		Cursor c = db.query(TABLE_NAME_BARAND, PROJECTION_BARANDL, null, null, null, null, RECORD_ID);
		while (c.moveToNext()) {
			BrandBean brandBean = new BrandBean();
			brandBean.setId(c.getInt(1));
			brandBean.setName(c.getString(2));
			barndList.add(brandBean);
		}
		c.close();
		return barndList;
	}

	/************************************************** 种类表 ********************************************************/

	/**
	 * 种类表
	 * */
	private static final String TABLE_NAME_KIND = "kind";

	/**
	 * 种类表中的定义
	 * 
	 * */
	public interface KindColumns {
		public static final String KINDID = "kindId";
		public static final String KINDNAME = "kindName";
	}

	/**
	 * 种类表查询列
	 */
	public static final String[] PROJECTION_KIND = new String[] { RECORD_ID, KindColumns.KINDID, KindColumns.KINDNAME };

	/**
	 * 种类表的建立
	 */

	public static final String CREATE_SQL_KIND = "create table " + TABLE_NAME_KIND + " (" + RECORD_ID
			+ " integer primary key autoincrement," + KindColumns.KINDID + " integer, " + KindColumns.KINDNAME
			+ " text " + ");";

	/**
	 * 插入种类数据
	 * 
	 * @param list
	 */
	public synchronized void inserKindData(List<KindBean> kindList) {
		SQLiteDatabase localDb = db;
		try {
			localDb.beginTransaction();
			localDb.delete(TABLE_NAME_KIND, null, null);
			for (KindBean kindBean : kindList) {
				// String sql = "insert into " + TABLE_NAME_KIND + " ("
				// + KindColumns.KINDID + ","
				// + KindColumns.KINDNAME
				// + ") values(?,?)";
				// localDb.execSQL(sql, new Object[]
				// {kindBean.getId(),kindBean.getName()});
				//
				ContentValues cv = new ContentValues();
				cv.put(KindColumns.KINDID, kindBean.getId());
				cv.put(KindColumns.KINDNAME, kindBean.getName());
				localDb.insert(TABLE_NAME_KIND, RECORD_ID, cv);
			}
			localDb.setTransactionSuccessful();
		} finally {
			localDb.endTransaction();
		}
	}

	/**
	 * 判断数据库中的表是否为空数据
	 * 
	 * @param list
	 */
	public boolean tabbleIsExist(String tableName) {
		boolean is_true = false;
		if (tableName == null) {
			return false;
		}
		Cursor cursor = null;
		String sql = "select * from " + tableName + "";
		cursor = db.rawQuery(sql, null);
		while(cursor.moveToNext()){
			is_true = true ;
		}
    return is_true;
	}

	/**
	 * 清空表数据
	 * 
	 * @param list
	 */
	public boolean clearTableData(String tableName) {
		boolean is_crear = false;
		SQLiteDatabase localDb = db;
		if(tableName==null){
			is_crear = false;
		}
		try {
			localDb.delete(tableName, null, null);
			is_crear = true;
		} finally {
			
		}
		return is_crear;
	}
	
}
