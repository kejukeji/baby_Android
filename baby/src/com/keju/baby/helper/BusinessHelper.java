package com.keju.baby.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.keju.baby.Constants;
import com.keju.baby.SystemException;
import com.keju.baby.bean.AcademicAbstractBean;
import com.keju.baby.bean.BabyBean;
import com.keju.baby.bean.DoctorProvinceBean;
import com.keju.baby.bean.ResponseBean;
import com.keju.baby.bean.YardBean;
import com.keju.baby.internet.HttpClient;
import com.keju.baby.internet.PostParameter;

/**
 * 网络访问操作
 * 
 * @author Zhoujun 说明： 1、一些网络操作方法 2、访问系统业务方法，转换成json数据对象，或者业务对象。
 */
public class BusinessHelper {
	/**
	 * 网络访问路径
	 */
	private static final String BASE_URL = "http://baby.kejukeji.com/restful/";
//	private static final String BASE_URL = "http://192.168.1.125:7007/restful/";//向进服务器地址
	public static final String PIC_URL = "http://baby.kejukeji.com";// 头像地址
	
	public static final String BASE_URL1 = "http://42.121.108.142:7007/restful/";
	HttpClient httpClient = new HttpClient();

	
	/**
	 * 婴儿登录接口
	 * 
	 * @param loginName
	 * @param password
	 * @param remember  是否记住密码
	 * @param loginType  登录类型
	 * @return
	 * @throws SystemException
	 */
	public JSONObject login(String loginName,String loginPassword,String remember,String loginType) throws SystemException{
		return httpClient.post(BASE_URL +"html/do/login",new PostParameter[] { 
				new PostParameter("login_name"  ,loginName ),
				new PostParameter("login_pass"  ,loginPassword ),
				new PostParameter("remember"  ,remember ),
				new PostParameter("login_type"  ,loginType )} ).asJSONObject();
	}
	/**
	 * 注册
	 * 
	 * @param loginName
	 * @param password
	 * @return
	 * @throws SystemException
	 */
	public JSONObject register(String loginName, String password, String nickname) throws SystemException {
		return httpClient.post(
				BASE_URL + "regist.json",
				new PostParameter[] { new PostParameter("loginName", loginName),
						new PostParameter("password", password), new PostParameter("nickname", nickname) })
				.asJSONObject();
	}

	/**
	 * 获取婴儿列表
	 * 
	 * @param page
	 * @param doctor_id
	 * @return
	 */
	public JSONObject getBabyList1(int pageIndex, int doctor_id) throws SystemException {
			return httpClient.get(
					BASE_URL + "baby/list",
					new PostParameter[] { new PostParameter("page", pageIndex),
							new PostParameter("doctor_id", doctor_id) }).asJSONObject();
	}

	/**
	 * 获取婴儿列表
	 * 
	 * @param page
	 * @param doctor_id
	 * @return
	 */
	public ResponseBean<BabyBean> getBabyList(int pageIndex, int doctor_id) {
		ResponseBean<BabyBean> response = null;
		JSONObject obj;
		try {
			obj = httpClient.get(
					BASE_URL + "baby/list",
					new PostParameter[] { new PostParameter("page", pageIndex),
							new PostParameter("doctor_id", doctor_id) }).asJSONObject();
			int status = obj.getInt("code");
			if (status == Constants.REQUEST_SUCCESS) {
				response = new ResponseBean<BabyBean>(obj);
				List<BabyBean> list = null;
				if (!TextUtils.isEmpty(obj.getString("baby_list"))) {
					list = BabyBean.constractList(obj.getJSONArray("baby_list"));
				} else {
					list = new ArrayList<BabyBean>();
				}
				response.setObjList(list);
				response.setJsonData(obj.toString());
			} else {
				response = new ResponseBean<BabyBean>(Constants.REQUEST_FAILD, obj.getString("message"));
			}
		} catch (SystemException e1) {
			response = new ResponseBean<BabyBean>(Constants.REQUEST_FAILD, "服务器连接失败");
		} catch (JSONException e) {
			response = new ResponseBean<BabyBean>(Constants.REQUEST_FAILD, "json解析错误");
		}
		return response;
	}
	
	
	
	/**
	 * 获取收藏的婴儿列表
	 * @param pageIndex
	 * @param doctor_id
	 * @return
	 */
	public ResponseBean<BabyBean> getCollectBabyList(int pageIndex, int doctor_id) {
		ResponseBean<BabyBean> response = null;
		JSONObject obj;
		try {
			obj = httpClient.get(
					BASE_URL + "baby/collect/list",
					new PostParameter[] { new PostParameter("page", pageIndex),
							new PostParameter("doctor_id", doctor_id) }).asJSONObject();
			int status = obj.getInt("code");
			if (status == Constants.REQUEST_SUCCESS) {
				response = new ResponseBean<BabyBean>(obj);
				List<BabyBean> list = null;
				if (!TextUtils.isEmpty(obj.getString("baby_list"))) {
					list = BabyBean.constractList(obj.getJSONArray("baby_list"));
				} else {
					list = new ArrayList<BabyBean>();
				}
				response.setObjList(list);
				response.setJsonData(obj.toString());
			} else {
				response = new ResponseBean<BabyBean>(Constants.REQUEST_FAILD, obj.getString("message"));
			}
		} catch (SystemException e1) {
			response = new ResponseBean<BabyBean>(Constants.REQUEST_FAILD, "服务器连接失败");
		} catch (JSONException e) {
			response = new ResponseBean<BabyBean>(Constants.REQUEST_FAILD, "json解析错误");
		}
		return response;
	}

	/***
	 * 获取Baby个人资料
	 * 
	 * @param uid
	 *            用户id
	 * @return
	 */
	public JSONObject getBabyInfor(int uid) throws SystemException {
		return httpClient.post(BASE_URL + "baby/info", new PostParameter[] { new PostParameter("baby_id", uid) })
				.asJSONObject();
	}

	/**
	 * 修改Baby资料 图片上传 此方案即可上传文件 又可以不传
	 * 
	 * @param uid
	 * @param babyName
	 * @param parentNumber
	 * @param babySex
	 * @param babyProduction
	 * @param babyWeight
	 * @param babyHeight
	 * @param babyHeadCircumference
	 * @param childbirthWay
	 * @param complication
	 * @param grade
	 * @param grade
	 * @return
	 * @throws avatarFile
	 */
	public JSONObject addBabyInfor(int uid, String type, String babyName, String parentNumber, String babySex,
			String babyProduction, int babyWeight, int babyHeight, int babyHeadCircumference, String childbirthWay,
			String complication, int grade, File avatarFile) throws SystemException {
		List<PostParameter> params = new ArrayList<PostParameter>();
		params.add(new PostParameter("baby_id", uid));
		params.add(new PostParameter("type", type));
		if (!babyName.equals("")) {
			params.add(new PostParameter("baby_name", babyName));
		}
		if (!parentNumber.equals("")) {
			params.add(new PostParameter("patriarch_tel", parentNumber));
		}
		if (!babySex.equals("")) {
			params.add(new PostParameter("gender", babySex));
		}
		if (!babyProduction.equals("")) {
			params.add(new PostParameter("due_date", babyProduction));
		}
		if (babyWeight != 0) {
			params.add(new PostParameter("born_weight", babyWeight));
		}
		if (babyHeight != 0) {
			params.add(new PostParameter("born_weight", babyHeight));
		}
		if (babyHeadCircumference != 0) {
			params.add(new PostParameter("born_head", babyHeadCircumference));
		}
		if (!childbirthWay.equals("")) {
			params.add(new PostParameter("childbirth", childbirthWay));
		}
		if (!complication.equals("")) {
			params.add(new PostParameter("complication", complication));
		}
		if (grade != 0) {
			params.add(new PostParameter("apgar_score", grade));
		}
		if (avatarFile != null) {
			return httpClient.multPartURL("upload_image", BASE_URL + "baby/info",
					params.toArray(new PostParameter[params.size()]), avatarFile).asJSONObject();
		} else {
			return httpClient.post(BASE_URL + "baby/info", params.toArray(new PostParameter[params.size()]))
					.asJSONObject();
		}
	}

	/**
	 * 修改医生资料 图片上传 此方案即可上传文件 又可以不传
	 * 
	 * @param uid
	 * @param doctorName
	 * @param doctorAddress
	 * @param doctorHospital
	 * @param doctorDepartment
	 * @param jobTitle
	 * @param doctorEmil
	 * @param doctorNumber
	 * @param avatarFile
	 * @return
	 * @throws
	 */
	public JSONObject addDoctorInfor(int uid, String type, String doctorName, int doctorAddressId,
			int doctorHospitalId, int doctorDepartmentId, int jobTitleId, String doctorEmil, String doctorNumber,
			File avatarFile) throws SystemException {
		List<PostParameter> params = new ArrayList<PostParameter>();
		params.add(new PostParameter("doctor_id", uid));
		params.add(new PostParameter("type", type));
		if (!doctorName.equals("")) {
			params.add(new PostParameter("real_name", doctorName));
		}
		if (doctorAddressId != 0) {
			params.add(new PostParameter("patriarch_tel", doctorAddressId));
		}
		if (doctorHospitalId != 0) {
			params.add(new PostParameter("belong_hospital", doctorHospitalId));
		}
		if (doctorDepartmentId != 0) {
			params.add(new PostParameter("belong_department", doctorDepartmentId));
		}
		if (jobTitleId != 0) {
			params.add(new PostParameter("position", jobTitleId));
		}
		if (!doctorEmil.equals("")) {
			params.add(new PostParameter("email", doctorEmil));
		}
		if (!doctorNumber.equals("")) {
			params.add(new PostParameter("tel", doctorNumber));
		}
		if (avatarFile != null) {
			return httpClient.multPartURL("upload_image", BASE_URL + "doctor/info",
					params.toArray(new PostParameter[params.size()]), avatarFile).asJSONObject();
		} else {
			return httpClient.post(BASE_URL + "doctor/info", params.toArray(new PostParameter[params.size()]))
					.asJSONObject();
		}
	}

	/**
	 * 获取医生信息
	 * 
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	public JSONObject getDoctorInfo(int id) throws SystemException {
		return httpClient.post(BASE_URL + "doctor/info", new PostParameter[] { new PostParameter("doctor_id", id) })
				.asJSONObject();
	}

	/**
	 * 搜索婴儿
	 * 
	 * @param keyword
	 * @return
	 */
	public ResponseBean<BabyBean> searchBaby(String keyword,String startTime,String endTime) {
		ResponseBean<BabyBean> response = null;
		List<PostParameter> p = new ArrayList<PostParameter>();
		if(!TextUtils.isEmpty(startTime)){
			p.add(new PostParameter("start_birthday_time", startTime));
		}
		if(!TextUtils.isEmpty(endTime)){
			p.add(new PostParameter("end_birthday_time", endTime));
		}
		p.add(new PostParameter("keyword", keyword));
		JSONObject obj;
		try {
			obj = httpClient.get(BASE_URL + "doctor/search",p.toArray(new PostParameter[p.size()])).asJSONObject();
			int status = obj.getInt("code");
			if (status == Constants.REQUEST_SUCCESS) {
				response = new ResponseBean<BabyBean>(obj);
				List<BabyBean> list = null;
				if (!TextUtils.isEmpty(obj.getString("baby_list"))) {
					list = BabyBean.constractList(obj.getJSONArray("baby_list"));
				} else {
					list = new ArrayList<BabyBean>();
				}
				response.setObjList(list);
			} else {
				response = new ResponseBean<BabyBean>(Constants.REQUEST_FAILD, obj.getString("message"));
			}
		} catch (SystemException e1) {
			response = new ResponseBean<BabyBean>(Constants.REQUEST_FAILD, "服务器连接失败");
		} catch (JSONException e) {
			response = new ResponseBean<BabyBean>(Constants.REQUEST_FAILD, "json解析错误");
		}
		return response;
	}

	/**
	 * 获取医生所在的地区，医院，科室，等信息
	 * 
	 * @param id
	 * @return
	 * @throws SystemException
	 */
//	public JSONObject getDoctorData() throws SystemException {
//		return httpClient.get(BASE_URL + "html/register/data").asJSONObject();
//	}
//	
	
	public ResponseBean<DoctorProvinceBean > getDoctorData(){
		ResponseBean<DoctorProvinceBean> bean ;
		try {
			JSONObject obj = httpClient.get(BASE_URL + "html/register/data").asJSONObject();
			List<DoctorProvinceBean> list = DoctorProvinceBean.constractList(obj.getJSONArray("total"));
			bean = new ResponseBean<DoctorProvinceBean>(obj);
			bean.setObjList(list);
		} catch (SystemException e) {
			bean = new ResponseBean<DoctorProvinceBean>(Constants.REQUEST_FAILD, "服务器连接失败");
		} catch (JSONException e) {
			bean = new ResponseBean<DoctorProvinceBean>(Constants.REQUEST_FAILD, "json解析错误");
		}
		return bean;
	}

	/**
	 * 收藏婴儿
	 * 
	 * @param babyId
	 * @param doctorId
	 * @return
	 * @throws SystemException
	 */
	public JSONObject collectBaby(int babyId, int doctorId) throws SystemException {
		return httpClient.get(
				BASE_URL + "doctor/collect",
				new PostParameter[] { new PostParameter("type", "baby"), new PostParameter("baby_id", babyId),
						new PostParameter("doctor_id", doctorId) }).asJSONObject();
	}

	/**
	 * 收藏文摘
	 * 
	 * @param abstractId
	 * @param doctorId
	 * @return
	 * @throws SystemException
	 */
	public JSONObject collectAbstract(int abstractId, int doctorId) throws SystemException {
		return httpClient.get(
				BASE_URL + "doctor/collect",
				new PostParameter[] { new PostParameter("type", "abstract"),
						new PostParameter("abstract_id", abstractId), new PostParameter("doctor_id", doctorId) })
				.asJSONObject();
	}

	/**
	 * 创建婴儿账户
	 * 
	 * @param baby_name
	 * @param baby_pass
	 * @param patriarch_tel
	 * @param gender
	 * @param due_date
	 * @param born_birthday
	 * @param born_weight
	 * @param born_height
	 * @param born_head
	 * @param childbirth_style
	 * @param complication_id
	 * @return
	 * @throws SystemException
	 */
	public JSONObject creatBabyAccount(int doctor_id,String baby_name, String baby_pass, String patriarch_tel, String gender,
			String due_date, String born_birthday, String born_weight, String born_height, String born_head,
			String childbirth_style, String complication_id,String growth_standard) throws SystemException {
		return httpClient.post(
				BASE_URL + "html/create/baby",
				new PostParameter[] { new PostParameter("doctor_id", doctor_id),new PostParameter("baby_name", baby_name),
						new PostParameter("baby_pass", baby_pass), new PostParameter("patriarch_tel", patriarch_tel),
						new PostParameter("gender", gender), new PostParameter("due_date", due_date),
						new PostParameter("born_birthday", born_birthday),
						new PostParameter("born_weight", born_weight), new PostParameter("born_height", born_height),
						new PostParameter("born_head", born_head),
						new PostParameter("childbirth_style", childbirth_style),
						new PostParameter("complication_id", complication_id),new PostParameter("growth_standard", growth_standard) }).asJSONObject();
	}
	/**
	 * 添加随访记录
	 * @param due_date
	 * @param weight
	 * @param height
	 * @param head
	 * @param breastfeeding
	 * @param location
	 * @param brand
	 * @param kind
	 * @param nutrition
	 * @param add_type
	 * @return
	 * @throws SystemException 
	 */
	public JSONObject addVisit(int id,String due_date, String weight, String height, String head, String breastfeeding,
			String location, String brand, String kind, String nutrition,String add_type) throws SystemException {
		return httpClient.post(BASE_URL + "html/add/visit", new PostParameter[] {new PostParameter("baby_id", id),new PostParameter("measure_date", due_date),
				new PostParameter("weight", weight),new PostParameter("height", height),new PostParameter("head", head),new PostParameter("breastfeeding", breastfeeding),
				new PostParameter("location", location),new PostParameter("brand", brand),new PostParameter("kind", kind),
				new PostParameter("nutrition", nutrition),new PostParameter("add_type", add_type)}).asJSONObject();
	}
	/**
	 * 获取奶粉数据；
	 * @return
	 */
	public ResponseBean<YardBean> getMilkData(){
		ResponseBean<YardBean> bean ;
		try {
			JSONObject obj = httpClient.get(BASE_URL + "record/data").asJSONObject();
			List<YardBean> list = YardBean.constractList(obj.getJSONArray("total"));
			bean = new ResponseBean<YardBean>(obj);
			bean.setObjList(list);
			bean.setJsonData(obj.toString());
		} catch (SystemException e) {
			bean = new ResponseBean<YardBean>(Constants.REQUEST_FAILD, "服务器连接失败");
		} catch (JSONException e) {
			bean = new ResponseBean<YardBean>(Constants.REQUEST_FAILD, "json解析错误");
		}
		return bean;
	}
	/**
	 * 获取学术文摘列表
	 * @return
	 */
	public ResponseBean<AcademicAbstractBean> getAcademicAbstract(int pageIndex,int doctor_id){
		ResponseBean<AcademicAbstractBean> bean ;
		try {
			JSONObject obj = httpClient.get(BASE_URL + "academic",
					new PostParameter[]{new PostParameter("doctor_id", doctor_id),
					new PostParameter("page", pageIndex)}).asJSONObject();
			int status = obj.getInt("code");
			if (status == Constants.REQUEST_SUCCESS) {
				bean = new ResponseBean<AcademicAbstractBean>(obj);
				List<AcademicAbstractBean> list = null;
				if (!TextUtils.isEmpty(obj.getString("academic_list"))) {
					list = AcademicAbstractBean.constractList(obj.getJSONArray("academic_list"));
				} else {
					list = new ArrayList<AcademicAbstractBean>();
				}
				bean.setObjList(list);
			} else {
				bean = new ResponseBean<AcademicAbstractBean>(Constants.REQUEST_FAILD, obj.getString("message"));
			}
		} catch (SystemException e) {
			bean = new ResponseBean<AcademicAbstractBean>(Constants.REQUEST_FAILD, "服务器连接失败");
		} catch (JSONException e) {
			bean = new ResponseBean<AcademicAbstractBean>(Constants.REQUEST_FAILD, "json解析错误");
		}
		return bean;
	}
	/**
	 * 获取收藏的学术文摘
	 * @param pageIndex
	 * @param doctor_id
	 * @return
	 */
	public ResponseBean<AcademicAbstractBean> getCollectAcademicAbstract(int pageIndex,int doctor_id){
		ResponseBean<AcademicAbstractBean> bean ;
		try {
			JSONObject obj = httpClient.get(BASE_URL + "doctor/collect/academic",new PostParameter[]{new PostParameter("doctor_id", doctor_id),
					new PostParameter("page", pageIndex)}).asJSONObject();
			int status = obj.getInt("code");
			if (status == Constants.REQUEST_SUCCESS) {
				bean = new ResponseBean<AcademicAbstractBean>(obj);
				List<AcademicAbstractBean> list = null;
				if (!TextUtils.isEmpty(obj.getString("academic"))) {
					list = AcademicAbstractBean.constractList(obj.getJSONArray("academic"));
				} else {
					list = new ArrayList<AcademicAbstractBean>();
				}
				bean.setObjList(list);
			} else {
				bean = new ResponseBean<AcademicAbstractBean>(Constants.REQUEST_FAILD, obj.getString("message"));
			}
		} catch (SystemException e) {
			bean = new ResponseBean<AcademicAbstractBean>(Constants.REQUEST_FAILD, "服务器连接失败");
		} catch (JSONException e) {
			bean = new ResponseBean<AcademicAbstractBean>(Constants.REQUEST_FAILD, "json解析错误");
		}
		return bean;
	}
	/**
	 * 收藏或取消收藏 学术文摘接口
	 * @param academic_id
	 * @param doctorId
	 * @return
	 * @throws SystemException
	 */
	public JSONObject collectAcademic(int academic_id, int doctorId) throws SystemException {
		return httpClient.get(
				BASE_URL + "doctor/collect",
				new PostParameter[] { new PostParameter("type", "abstract"), new PostParameter("abstract_id", academic_id),
						new PostParameter("doctor_id", doctorId) }).asJSONObject();
	}
}
