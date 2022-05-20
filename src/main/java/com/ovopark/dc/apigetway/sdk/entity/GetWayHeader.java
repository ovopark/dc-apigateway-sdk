package com.ovopark.dc.apigetway.sdk.entity;

import java.util.Date;

import com.ovopark.dc.apigetway.sdk.kit.DateKit;

/**
 * @ClassName:  GetWayHeader   
 * @Description:(报文头)
 * @author: Remiel_Mercy 
 * @date:   2020年6月28日 下午2:50:45   
 *     
 * @Copyright: 2020 www.ovopark.com Inc. All rights reserved.
 */
public class GetWayHeader {
	private String aid;
	
	private String akey;
	
	private String mt;
	
	private String sm;
	
	private String requestMode;
	
	private String version;
	
	private String timestamp;
	
	private String format;

	public GetWayHeader() {
		super();
		this.aid = "S107";
		this.sm="md5";
		this.requestMode = "post";
		this.version = "v1";
		this.timestamp = DateKit.DateTime2Str(new Date(), DateKit.getSampleTimeFormat());
		this.format = "json";
	}
	
	
	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getAkey() {
		return akey;
	}

	public void setAkey(String akey) {
		this.akey = akey;
	}

	public String getMt() {
		return mt;
	}

	public void setMt(String mt) {
		this.mt = mt;
	}

	public String getSm() {
		return sm;
	}

	public void setSm(String sm) {
		this.sm = sm;
	}

	public String getRequestMode() {
		return requestMode;
	}

	public void setRequestMode(String requestMode) {
		this.requestMode = requestMode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	
}
