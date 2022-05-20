package com.ovopark.dc.apigetway.sdk.entity;


/**
    * @ClassName: GetWayResponse
    * @Description: (网关相应对象)
    * @author Remiel_Mercy xuefei_fly@126.com
    * @date 2017年9月17日 下午4:02:12
 */
public class GetWayResponse {

	/**相应状态对象**/
	public ResponseStat stat;
	/**api服务层调用结果说明**/
	public String result;
	/**返回结果字符串(一般为json格式，子应用系统自定义格式)**/
	public Object data;
}
