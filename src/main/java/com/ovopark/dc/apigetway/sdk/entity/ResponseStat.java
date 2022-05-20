package com.ovopark.dc.apigetway.sdk.entity;

import java.util.Map;

/**
    * @ClassName: ResponseStat
    * @Description: (响应状态)
    * @author Remiel_Mercy xuefei_fly@126.com
    * @date 2017年9月17日 下午4:35:31
 */
public class ResponseStat {
	/**当前系统时间**/
	private long systime;
	/**调用返回值**/
	private int code;
	/**返回值名称**/
	private String codename;
	/**调用标识符**/
	private String cid;
	
	/**
	 * 一些网关参数.
	 */
	private Map<String,Object> gatewayParam;
	
	public Map<String, Object> getGatewayParam() {
		return gatewayParam;
	}
	
	public void setGatewayParam(Map<String, Object> gatewayParam) {
		this.gatewayParam = gatewayParam;
	}
	
	public long getSystime() {
		return systime;
	}
	public void setSystime(long systime) {
		this.systime = systime;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	
	public String getCodename() {
		return codename;
	}
	public void setCodename(String codename) {
		this.codename = codename;
	}
	public ResponseStat(long systime, int code, String cid,String codename ,Map<String,Object> gatewayParam) {
		super();
		this.systime = systime;
		this.code = code;
		this.cid = cid;
		this.codename=codename;
		this.gatewayParam=gatewayParam;
	}
	public ResponseStat() {
		super();
	}
	
	@Override
	public String toString() {
		return "ResponseStat{" + "systime=" + systime + ", code=" + code + ", codename='" + codename + '\'' + ", cid='"
				+ cid + '\'' + ", gatewayParam=" + gatewayParam + '}';
	}
}
