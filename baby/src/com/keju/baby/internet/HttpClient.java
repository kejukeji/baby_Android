
package com.keju.baby.internet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.Header;

import com.keju.baby.Configuration;
import com.keju.baby.SystemException;


/**
 * 管理  HTTP request/response.
 * @author Zhoujun
 */
public class HttpClient implements java.io.Serializable {
    private static final int OK = 200;// OK: 服务器成功返回
//    private static final int NOT_MODIFIED = 304;// Not Modified(未修改): 没有返回响应时无新数据返回
//    private static final int BAD_REQUEST = 400;// Bad Request(错误请求): 请求不合法，服务器不理解请求的语法。
//    private static final int NOT_AUTHORIZED = 401;// Not Authorized(未授权): 请求要求身份验证。
//    private static final int FORBIDDEN = 403;// Forbidden(禁止): 服务器拒绝请求。
//    private static final int NOT_FOUND = 404;// Not Found(未找到): 服务器找不到请求的资源。
//    private static final int NOT_ACCEPTABLE = 406;// Not Acceptable(不接受): 无法使用请求的内容特性响应请求的资源
    private static final int INTERNAL_SERVER_ERROR = 500;// Internal Server Error(服务器内部错误): 服务器遇到错误，无法完成请求。 
//    private static final int BAD_GATEWAY = 502;// Bad Gateway(错误网关): 服务器作为网关或代理，从上游服务器收到无效响应。
//    private static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable(服务不可用): 服务器目前无法使用（由于超载或停机维护）。 通常，这只是暂时状态。请重试。

    private final static boolean DEBUG = Configuration.getDebug();

    private String basic;
    private int retryCount = Configuration.getRetryCount();//重试次数
    private int retryIntervalMillis = Configuration.getRetryIntervalSecs() * 1000;//重试间隔时间
    private int connectionTimeout = Configuration.getConnectionTimeout();//网络连接超时时间
    private int readTimeout = Configuration.getReadTimeout();
    private static final long serialVersionUID = 808018030183407996L;
    private static boolean isJDK14orEarlier = false;
    private Map<String, String> requestHeaders = new HashMap<String, String>();

    static {
        try {
            String versionStr = System.getProperty("java.specification.version");
            if (null != versionStr) {
                isJDK14orEarlier = 1.5d > Double.parseDouble(versionStr);
            }
        } catch (AccessControlException ace) {
            isJDK14orEarlier = true;
        }
    }

    public HttpClient() {
        this.basic = null;
        setRequestHeader("Accept-Encoding","gzip");
    }

   
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * 当发送一个网络请求连接时使用
     * @param connectionTimeout - 连接超时毫秒数
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = Configuration.getConnectionTimeout(connectionTimeout);

    }
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * Sets the read timeout to a specified timeout, in milliseconds. System property -Dsinat4j.http.readTimeout overrides this attribute.
     * @param readTimeout - an int that specifies the timeout value to be used in milliseconds
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = Configuration.getReadTimeout(readTimeout);
    }


    public void setRetryCount(int retryCount) {
        if (retryCount >= 0) {
            this.retryCount = Configuration.getRetryCount(retryCount);
        } else {
            throw new IllegalArgumentException("RetryCount cannot be negative.");
        }
    }
    
    public void setRetryIntervalSecs(int retryIntervalSecs) {
        if (retryIntervalSecs >= 0) {
            this.retryIntervalMillis = Configuration.getRetryIntervalSecs(retryIntervalSecs) * 1000;
        } else {
            throw new IllegalArgumentException(
                    "RetryInterval cannot be negative.");
        }
    }
    
    public Response multPartURL(String url,  PostParameter[] params,ImageItem item) throws SystemException{
  		PostMethod post = new PostMethod(url);
    	try {
    		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
    		long t = System.currentTimeMillis();
    		Part[] parts=null;
    		if(params==null){
    			parts=new Part[1];
    		}else{
    			parts=new Part[params.length+1];
    		}
    		if (params != null ) {
    			int i=0;
      			for (PostParameter entry : params) {
      				parts[i++]=new StringPart( entry.getName(),(String)entry.getValue(),"UTF-8");
    			}
      			parts[parts.length-1]=new ByteArrayPart(item.getContent(), item.getName(), item.getImageType());
      		}
    		post.setRequestEntity( new MultipartRequestEntity(parts, post.getParams()) );
    		 List<Header> headers = new ArrayList<Header>();

    	    client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
    		client.executeMethod(post);

    		Response response=new Response();
    		response.setResponseAsString(post.getResponseBodyAsString());
    		response.setStatusCode(post.getStatusCode());

    		log("multPartURL URL:" + url + ", result:" + response + ", time:" + (System.currentTimeMillis() - t));
        	return response;
    	} catch (Exception ex) {
    		 throw new SystemException(ex.getMessage(), ex, -1);
    	} finally {
    		post.releaseConnection();
    	}
  	}
    public Response multPartURL(String fileParamName,String url,  PostParameter[] params,File file) throws SystemException{
  		PostMethod post = new PostMethod(url);
  		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
    	try {
    		long t = System.currentTimeMillis();
    		Part[] parts=null;
    		if(params==null){
    			parts=new Part[1];
    		}else{
    			parts=new Part[params.length+1];
    		}
    		if (params != null ) {
    			int i=0;
      			for (PostParameter entry : params) {
      				parts[i++]=new StringPart( entry.getName(),(String)entry.getValue(),"UTF-8");
    			}
      		}
    		FilePart filePart=new FilePart(fileParamName,file.getName(), file,"multipart/form-data","UTF-8");
    		filePart.setTransferEncoding("binary");
    		parts[parts.length-1]= filePart;

    		post.setRequestEntity( new MultipartRequestEntity(parts, post.getParams()) );
    		 List<Header> headers = new ArrayList<Header>();

    	    client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
    		client.executeMethod(post);

    		Response response=new Response();
    		response.setResponseAsString(post.getResponseBodyAsString());
    		response.setStatusCode(post.getStatusCode());

    		log("multPartURL URL:" + url + ", result:" + response + ", time:" + (System.currentTimeMillis() - t));
        	return response;
    	} catch (Exception ex) {
    		 throw new SystemException(ex.getMessage(), ex, -1);
    	} finally {
    		post.releaseConnection();
    		client=null;
    	}
  	}
 	private static class ByteArrayPart extends PartBase {
		private byte[] mData;
		private String mName;
		public ByteArrayPart(byte[] data, String name, String type) throws IOException {
			super(name, type, "UTF-8", "binary");
			mName = name;
			mData = data;
		}
		protected void sendData(OutputStream out) throws IOException {
			out.write(mData);
		}
		protected long lengthOfData() throws IOException {
			return mData.length;
		}
	    protected void sendDispositionHeader(OutputStream out) throws IOException {
	    	super.sendDispositionHeader(out);
	    	StringBuilder buf = new StringBuilder();
	    	buf.append("; filename=\"").append(mName).append("\"");
	    	out.write(buf.toString().getBytes());
	    }
	}
    
    /**
     * 将一个数组值拷贝到另一个数组
     * @param <T>
     * @param source
     * @return
     */
    @SuppressWarnings("unchecked")
	private final <T> T[] copy(T[] source) {
        Class type = source.getClass().getComponentType();
        T[] target = (T[])Array.newInstance(type, source.length+1);
        System.arraycopy(source, 0, target, 0, source.length);
        return target;
     }

    /**
     * 删除请求
     * @param url
     * @return
     * @throws SystemException
     */
    public Response delete(String url) throws SystemException {
    	return httpRequest(url, null,  "DELETE");
    }
    /**
     *  带额外参数的post请求
     * @param url
     * @param PostParameters
     * @return
     * @throws SystemException
     */
    public Response post(String url, PostParameter[] PostParameters) throws SystemException {
        return httpRequest(url, PostParameters);
    }
    /**
     * post 带默认参数请求  默认参数是版本号
     * @param url
     * @return
     * @throws SystemException
     */
    public Response post(String url) throws SystemException {
        return httpRequest(url, new PostParameter[0]);
    }
    /**
     * get 请求，不带参数
     * @param url
     * @return
     * @throws SystemException
     */
    public Response get(String url) throws SystemException {
        return get(url, null);
    }
    
    /**
     * get 请求,带参数
     * @param url
     * @return
     * @throws SystemException
     */
    public Response get(String url,PostParameter[] params) throws SystemException {
//    	if (url.indexOf("?") == -1) {
//			url += "?PCode=" + Constants.PRODCODE;
//		} else if (url.indexOf("version") == -1) {
//			url += "&PCode=" + Constants.PRODCODE;
//		}
    	if (null != params && params.length > 0) {
    		if (url.indexOf("?") == -1) {
    			url += "?" + HttpClient.encodeParameters(params);
    		}else{
    			url += "&" + HttpClient.encodeParameters(params);
    		}
		}
        return httpRequest(url, null);
    }
    /**
     * 设置统一参数
     * @param url
     * @param postParams
     * @return
     * @throws SystemException
     */
    protected Response httpRequest(String url, PostParameter[] postParams) throws SystemException {
    	//可以添加统一参数version
		PostParameter[] newPostParameters = postParams;
    	String method = "GET";
    	if (postParams != null) {
    		method = "POST";
//			newPostParameters = copy(postParams);
//			newPostParameters[postParams.length] = new PostParameter("PCode",Constants.PRODUCT_ID);
    	}
    	return httpRequest(url, newPostParameters, method);
    }
    /**
     * 发送  http 请求
     * @param url
     * @param postParams
     * @param httpMethod
     * @return
     * @throws SystemException
     */
    public Response httpRequest(String url, PostParameter[] postParams, String httpMethod) throws SystemException {
        int retriedCount;
        int retry = retryCount + 1;
        Response res = null;
        
        for (retriedCount = 0; retriedCount < retry; retriedCount++) {
            int responseCode = -1;
            try {
                HttpURLConnection con = null;
                OutputStream osw = null;
                try {
                    con = getConnection(url);
                    con.setDoInput(true);
                    setHeaders(url, postParams, con, httpMethod);
                    if (null != postParams || "POST".equals(httpMethod)) {
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Content-Type",
                                "application/x-www-form-urlencoded");
                        con.setDoOutput(true);
                        String postParam = "";
                        if (postParams != null) {
                        	postParam = encodeParameters(postParams);
                        }
                        log("Post Params: ", postParam);
                        byte[] bytes = postParam.getBytes("UTF-8");

                        con.setRequestProperty("Content-Length",
                                Integer.toString(bytes.length));
                        osw = con.getOutputStream();
                        osw.write(bytes);
                        osw.flush();
                        osw.close();
                    } else {
                        con.setRequestMethod("GET");
                    }
                    res = new Response(con);
                    responseCode = con.getResponseCode();
                    if(DEBUG){
                        log("Response: ");
                        Map<String, List<String>> responseHeaders = con.getHeaderFields();
                        for (String key : responseHeaders.keySet()) {
                            List<String> values = responseHeaders.get(key);
                            for (String value : values) {
                                if(null != key){
                                    log(key + ": " + value);
                                }else{
                                    log(value);
                                }
                            }
                        }
                    }
                    if (responseCode != OK) {
                        if (responseCode < INTERNAL_SERVER_ERROR || retriedCount == retryCount) {
                            //throw new SystemException(getCause(responseCode) + "\n" + res.asString(), responseCode);
                        	throw new SystemException("Http connection failed!" + "\n" + res.asString(), responseCode);
                        }
                        // will retry if the status code is INTERNAL_SERVER_ERROR
                    } else {
                        break;
                    }
                } finally {
                    try {
                        osw.close();
                    } catch (Exception ignore) {
                    }
                }
            } catch (IOException ioe) {
                // connection timeout or read timeout
                if (retriedCount == retryCount) {
                    throw new SystemException(ioe.getMessage(), ioe, responseCode);
                }
            }
            try {
                if(DEBUG && null != res){
                    res.asString();
                }
                log("Sleeping " + retryIntervalMillis +" millisecs for next retry.");
                Thread.sleep(retryIntervalMillis);
            } catch (InterruptedException ignore) {
                //nothing to do
            }
        }
        return res;
    }
    /**
     * 格式化请求参数
     * @param postParams
     * @return
     */
    public static String encodeParameters(PostParameter[] postParams) {
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < postParams.length; j++) {
            if (j != 0) {
                buf.append("&");
            }
//            buf.append(postParams[j].name)
//    			.append("=").append(postParams[j].value);
            try {
                buf.append(URLEncoder.encode(postParams[j].name, "UTF-8"))
                        .append("=").append(URLEncoder.encode(postParams[j].value, "UTF-8"));
            } catch (java.io.UnsupportedEncodingException neverHappen) {
            }
        }
        return buf.toString();

    }

    /**
     * 设置 HTTP headers 请求头信息
     *
     * @param connection    HttpURLConnection
     * @param authenticated boolean
     */
    private void setHeaders(String url, PostParameter[] params, HttpURLConnection connection, String httpMethod) {
        log("Request: ");
        log(httpMethod + " ", url);
        
        for (String key : requestHeaders.keySet()) {
            connection.addRequestProperty(key, requestHeaders.get(key));
            log(key + ": " + requestHeaders.get(key));
        }
    }

    public void setRequestHeader(String name, String value) {
        requestHeaders.put(name, value);
    }

    public String getRequestHeader(String name) {
        return requestHeaders.get(name);
    }

    private HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        if (connectionTimeout > 0 && !isJDK14orEarlier) {
            con.setConnectTimeout(connectionTimeout);
        }
        if (readTimeout > 0 && !isJDK14orEarlier) {
            con.setReadTimeout(readTimeout);
        }
        return con;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpClient)) return false;

        HttpClient that = (HttpClient) o;

        if (connectionTimeout != that.connectionTimeout) return false;
        if (readTimeout != that.readTimeout) return false;
        if (retryCount != that.retryCount) return false;
        if (retryIntervalMillis != that.retryIntervalMillis) return false;
        if (basic != null ? !basic.equals(that.basic) : that.basic != null)
            return false;
        if (!requestHeaders.equals(that.requestHeaders)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = basic != null ? basic.hashCode() : 0;
        result = 31 * result + retryCount;
        result = 31 * result + retryIntervalMillis;
        result = 31 * result + connectionTimeout;
        result = 31 * result + readTimeout;
        result = 31 * result + requestHeaders.hashCode();
        return result;
    }

    private static void log(String message) {
        if (DEBUG) {
            System.out.println("[" + new java.util.Date() + "]" + message);
        }
    }

    private static void log(String message, String message2) {
        if (DEBUG) {
            log(message + message2);
        }
    }
    /**
     * 获得网络返回状态码对应的原因
     * @param statusCode 网络返回状态码
     * @return
     */
//    private static String getCause(int statusCode){
//        String cause = null;
//        switch(statusCode){
//            case NOT_MODIFIED:
//                break;
//            case BAD_REQUEST:
//                cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
//                break;
//            case NOT_AUTHORIZED:
//                cause = "Authentication credentials were missing or incorrect.";
//                break;
//            case FORBIDDEN:
//                cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
//                break;
//            case NOT_FOUND:
//                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
//                break;
//            case NOT_ACCEPTABLE:
//                cause = "Returned by the Search API when an invalid format is specified in the request.";
//                break;
//            case INTERNAL_SERVER_ERROR:
//                cause = "Something is broken.  Please post to the group so the team can investigate.";
//                break;
//            case BAD_GATEWAY:
//                cause = "Server is down or being upgraded.";
//                break;
//            case SERVICE_UNAVAILABLE:
//                cause = "Service Unavailable: The servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
//                break;
//            default:
//                cause = "";
//        }
//        return statusCode + ":" + cause;
//    }
}
