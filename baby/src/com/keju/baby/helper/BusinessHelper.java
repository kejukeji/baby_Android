package com.keju.baby.helper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.keju.baby.Constants;
import com.keju.baby.SystemException;
import com.keju.baby.bean.BabyBean;
import com.keju.baby.bean.ResponseBean;
import com.keju.baby.internet.HttpClient;
import com.keju.baby.internet.PostParameter;
/**
 * 网络访问操作
 * @author Zhoujun
 * 说明：	1、一些网络操作方法
 * 		2、访问系统业务方法，转换成json数据对象，或者业务对象。
 */
public class BusinessHelper {
	/**
	 * 网络访问路径
	 */
	private static final String BASE_URL = "http://42.121.108.142:7007/restful/";
	public static final String PIC_URL = "http://42.121.108.142:7007";//头像地址
	HttpClient httpClient = new HttpClient();
	
	/**
	 * 注册
	 * 
	 * @param loginName
	 * @param password
	 * @return
	 * @throws SystemException
	 */
	public JSONObject register(String loginName, String password,
			String nickname) throws SystemException {
		return httpClient.post(
				BASE_URL + "regist.json",
				new PostParameter[] {
						new PostParameter("loginName", loginName),
						new PostParameter("password", password),
						new PostParameter("nickname", nickname) })
				.asJSONObject();
	}
	/**
	 * 获取婴儿列表
	 * @param page
	 * @param doctor_id
	 * @return
	 */
	public ResponseBean<BabyBean> getBabyList(int pageIndex,int doctor_id){
		ResponseBean<BabyBean> response = null;
		JSONObject obj;
		try {
			obj = httpClient.get(BASE_URL + "baby/list", new PostParameter[] { new PostParameter("page", pageIndex),
					new PostParameter("doctor_id", doctor_id)}).asJSONObject();
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
	
}
