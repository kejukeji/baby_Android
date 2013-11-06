package com.keju.baby.helper;

import org.json.JSONObject;

import com.keju.baby.SystemException;
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
	private static final String BASE_URL = "http://222.73.182.110:8080/bukuai/";
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
	
	
	
}
