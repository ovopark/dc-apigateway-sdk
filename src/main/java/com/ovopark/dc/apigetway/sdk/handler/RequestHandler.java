package com.ovopark.dc.apigetway.sdk.handler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.StringEscapeUtils;

import com.ovopark.dc.apigetway.sdk.config.ApiConst;
import com.ovopark.dc.apigetway.sdk.config.OpenplatformConst;
import com.ovopark.dc.apigetway.sdk.kit.CharsetKit;
import com.ovopark.dc.apigetway.sdk.kit.PoJoKit;
import com.ovopark.dc.apigetway.sdk.kit.RSASignatureKit;
import com.ovopark.dc.apigetway.sdk.kit.StrKit;
import com.ovopark.dc.apigetway.sdk.kit.UrlKit;
import com.ovopark.dc.apigetway.sdk.utils.OvoParkUtils;
import com.ovopark.dc.apigetway.sdk.utils.SignUtils;



public class RequestHandler {
	private HttpServletRequest request;
	private HttpServletResponse response;
	/** 请求的参数 */
	private final SortedMap<String,Object> parameters;
	/** 网关地址 */
	private String gateUrl;
	/** 方法名 */
	private String method;
	/**应用id**/
	private String appId;
	
	/** 密匙 */
	private String applicationKey;
	/** 秘钥 */
	private String applicationSecret;
	/** debug信息 */
	private String debugInfo;
	/**是否要去除json转义 */
	private boolean jsonUnescape;
	/**版本号**/
	private String version;
	/**请求方式post,get,put,delete**/
	private String requestMode;
	/**签名方法**/
	private String signatureMethod;
	/**rsa私钥**/
	private String rsaPrivateKey;
	/**是否忽略签名**/
	private boolean ignoreSignature;
	
	public RequestHandler() {
		this.gateUrl = "";
		this.method = "";
		this.appId="";
		this.applicationKey = "";
		this.applicationSecret = "";
		this.parameters = new TreeMap<>();
		this.debugInfo = "";
		this.jsonUnescape=false;
		this.ignoreSignature=false;
	}
	
	/**
	 * 获取参数值
	 * @param parameter 参数名称
	 * @return String 
	 */
	public String getParameter(String parameter) {
		String s = (String)this.parameters.get(parameter); 
		return (null == s) ? "" : s;
	}
	
	/**
	 * 设置参数值
	 * @param parameter 参数名称
	 * @param parameterValue 参数值
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if(null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}
	/**
	    * @Title: setPoJo
	    * @Description: (PoJo类参数反射成map key value)
	    * @param @param pojo    参数
	    * @return void    返回类型
	    * @throws
	 */
	public void setPoJo(Object pojo) {
		Class<?> clazz = pojo.getClass();
		Field[] fields = PoJoKit.getNeedFields(clazz);
		for (Field field : fields) {
		      try {
		    	String v = "";
				// 抑制Java对修饰符的检查
			    field.setAccessible(true);
			    String key=field.getName();
			    Object value=field.get(pojo);
				if(null != value) {
					v = value.toString().trim();
				}
				this.parameters.put(key, v);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public String getGateUrl() {
		return gateUrl;
	}
	public void setGateUrl(String gateUrl) {
		this.gateUrl = gateUrl;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getApplicationKey() {
		return applicationKey;
	}
	public void setApplicationKey(String applicationKey) {
		this.applicationKey = applicationKey;
	}
	public String getApplicationSecret() {
		return applicationSecret;
	}
	public void setApplicationSecret(String applicationSecret) {
		this.applicationSecret = applicationSecret;
	}
	protected void ignoreSign() {
		parameters.put("_mt", this.getMethod());
		parameters.put("_aid", this.getAppId());
	}
	protected void createSign(boolean unescape) {
		StringBuilder sb = new StringBuilder();
		String signatureMethod=this.getSignatureMethod();
		if(!StrKit.isBlank(signatureMethod)) {
			this.setParameter("_sm",signatureMethod);
		}else {
			signatureMethod=this.getParameter("_sm");
		}
		//对称加密方式,添加开发者秘钥,签名字符串首位添加秘钥
		if(!ApiConst.sign_method_RSA.equals(signatureMethod)) {
			sb.append( this.getApplicationSecret());
			parameters.put("_akey", this.getApplicationKey());
		}

		parameters.put("_aid", this.getAppId());
		parameters.put("_mt", this.getMethod());

		String version=this.getVersion();
		if(!StrKit.isBlank(version)) {
			parameters.put("_version", version);
		}
		String requestMode=this.getRequestMode();
		if(!StrKit.isBlank(requestMode)) {
			parameters.put("_requestMode",requestMode);
		}
		Set<Entry<String, Object>> es = this.parameters.entrySet();
		for (Entry<String, Object> entry : es) {
			String k = entry.getKey();
			String v = (String) entry.getValue();
			if (unescape && k.equals(OpenplatformConst.JSON_DATA)) {
				v = StringEscapeUtils.unescapeJava(v);
				v = UrlKit.encode(v, CharsetKit.UTF_8);
			}
			if (!"_sig".equals(k)) {
				sb.append(k).append(v);
			}
		}
		String sign="";
		//判断是否是RSA签名
		if(ApiConst.sign_method_RSA.equals(signatureMethod)) {
			sign =RSASignatureKit.sign(sb.toString(), this.getRsaPrivateKey());
		}else {
			//对称加密方式签名字符串首位添加秘钥
			sb.append( this.getApplicationSecret());
			sign =SignUtils.sign(signatureMethod, sb.toString());
		}
		this.setParameter("_sig", sign);
		//debug信息
		this.setDebugInfo(sb.toString() + " => sign:" + sign);
	}
	
	
	
	/**
	 * 获取带参数的请求URL
	 * @return String
	 * @throws UnsupportedEncodingException 
	 */
	public String getRequestURL(){
		boolean flag = isJsonUnescape();
		if (isIgnoreSignature()) {
			ignoreSign();
		} else {
			this.createSign(flag);
		}
		StringBuilder sb = new StringBuilder();
		String enc = getCharset();
		Set<Entry<String, Object>> es = this.parameters.entrySet();
		for (Entry<String, Object> entry : es) {
			String k = entry.getKey();
			String v = (String) entry.getValue();
			if (flag) {
				k = StringEscapeUtils.unescapeJava(k);
				v = StringEscapeUtils.unescapeJava(v);
			}
			try {
				sb.append(k).append("=").append(URLEncoder.encode(v, enc)).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		// 去掉最后一个&
		String reqPars = sb.substring(0, sb.lastIndexOf("&"));
		return this.getGateUrl() + "?" + reqPars;
	}
	
	protected String getCharset() {
		return OvoParkUtils.getCharacterEncoding(this.request, this.response);
	}
	/**
	*设置debug信息
	*/
	protected void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}
	public SortedMap<String, Object> getAllParameters() {		
		return this.parameters;
	}
	/**
	*获取debug信息
	*/
	public String getDebugInfo() {
		return debugInfo;
	}

	public boolean isJsonUnescape() {
		return jsonUnescape;
	}

	public void setJsonUnescape(boolean jsonUnescape) {
		this.jsonUnescape = jsonUnescape;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRequestMode() {
		return requestMode;
	}

	public void setRequestMode(String requestMode) {
		this.requestMode = requestMode;
	}

	public String getSignatureMethod() {
		return signatureMethod;
	}

	public void setSignatureMethod(String signatureMethod) {
		this.signatureMethod = signatureMethod;
	}

	public String getRsaPrivateKey() {
		return rsaPrivateKey;
	}

	public void setRsaPrivateKey(String rsaPrivateKey) {
		this.rsaPrivateKey = rsaPrivateKey;
	}

	public boolean isIgnoreSignature() {
		return ignoreSignature;
	}

	public void setIgnoreSignature(boolean ignoreSignature) {
		this.ignoreSignature = ignoreSignature;
	}
	
	
}
