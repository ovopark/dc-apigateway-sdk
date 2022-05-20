package com.ovopark.dc.apigetway.sdk.config;
/**
    * @ClassName: ApiConst
    * @Description: (网关常量)
    * @author Remiel_Mercy xuefei_fly@126.com
    * @date 2017年9月11日 下午4:19:40
 */
public interface ApiConst {
	//debug 模式下识别http header中dubbo.version参数,将请求路由到指定的dubbo服务上
    String       DEBUG_DUBBOVERSION      = "DUBBO-VERSION";
   //debug 模式下识别http header中dubbo.service.ip参数,将请求路由到指定的dubbo服务上
    String       DEBUG_DUBBOSERVICE_URL  = "DUBBO-SERVICE-URL";
    String       FORMAT_XML              = "xml";
    String       FORMAT_JSON             = "json";
    String       FORMAT_PLAINTEXT        = "plaintext";
    String       SERVER_ADDRESS          = "a:";
    String       THREADID                = "t:";
    String       SPLIT                   = "|";
    String       REQ_TAG                 = "s:";
	String       CONTENT_TYPE_FORM       = "application/x-www-form-urlencoded;charset=utf-8";
    String       CONTENT_TYPE_XML        = "application/xml;charset=utf-8";
    String       CONTENT_TYPE_JSON       = "application/json;charset=utf-8";
    String       CONTENT_TYPE_JAVASCRIPT = "application/javascript;charset=utf-8";
    String       CONTENT_TYPE_PLAINTEXT  = "text/plain";
    String       JSONARRAY_PREFIX        = "[";
    String       JSONARRAY_SURFIX        = "]";
    String       USER_AGENT              = "User-Agent";
    String       REFERER                 = "Referer";
    String       DEBUG_AGENT             = "coco.tester";
    
	String sign_method_MD5 = "md5";
	String sign_method_SHA1 = "sha1";
	String sign_method_SHA256 = "sha256";
	String sign_method_RSA = "rsa";
	
	String KEY_ALGORITHM = "RSA";
	String SIGN_ALGORITHMS = "SHA1WithRSA";
}
