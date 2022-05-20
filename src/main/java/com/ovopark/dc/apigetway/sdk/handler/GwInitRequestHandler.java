package com.ovopark.dc.apigetway.sdk.handler;


import com.ovopark.dc.apigetway.sdk.config.OpenplatformConst;
import com.ovopark.dc.apigetway.sdk.kit.DateKit;

import java.util.Date;
import java.util.UUID;


/**
 * 
 * @ClassName:  GwInitRequestHandler   
 * @Description:TODO(初始化RequestHandler )   
 * @author: Remiel_Mercy 
 * @date:   2022年5月19日 下午4:05:53   
 *     
 * @Copyright: 2022 www.ovopark.com Inc. All rights reserved.
 */
public class GwInitRequestHandler extends RequestHandler{

	public GwInitRequestHandler() {
		super();
	}
	public void init() {
		this.setParameter("_sm",OpenplatformConst.Sm.MD5.getValue());
		this.setParameter("_requestMode", OpenplatformConst.RequestMode.POST.getValue());
		this.setParameter("_version", OpenplatformConst.VERSION);
		this.setParameter("_timestamp", DateKit.DateTime2Str(new Date(), DateKit.getSampleTimeFormat()));
		//签名
		this.setParameter("_sig", "");
		this.setParameter("_nonce",UUID.randomUUID().toString());
		this.setParameter("_format", "json");
	}
	public void createSign() {
		super.createSign(false);
		this.setParameter("_sig", getParameter("_sig").toUpperCase());
	}



}
