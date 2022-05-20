package com.ovopark.dc.apigetway.sdk.kit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.ovopark.dc.apigetway.sdk.annotation.IgnoreSign;



public class PoJoKit {
	
	/**
	    * @Title: getNeedFields
	    * @Description: (获取新增的ORM字段)
	    * @param @param clazz
	    * @param @return    参数
	    * @return Field[]    返回类型
	    * @throws
	 */
	public static Field[] getNeedFields(Class<?> clazz) {
	    Map<String,Object> list = new HashMap<>();
	    while ((null != clazz) && (clazz != Object.class)) {
	      Field[] fs = clazz.getDeclaredFields();
			for (Field f : fs) {
				if (isIgnoredField(f)) {
					continue;
				}
				if (list.containsKey(f.getName())) {
					continue;
				}
				list.put(f.getName(), f);
			}
	      clazz = clazz.getSuperclass();
	    }
	    return (Field[]) list.values().toArray(new Field[0]);
	}
	/**
	    * @Title: isIgnoredField
	    * @Description: (忽略的字段)
	    * @param @param f
	    * @param @return    参数
	    * @return boolean    返回类型
	    * @throws
	 */
	public static boolean isIgnoredField(Field f) {
	    if (Modifier.isStatic(f.getModifiers()))
	      return true;
	    if (Modifier.isFinal(f.getModifiers())) 
	      return true;
	    //是否忽略签名字段
	    IgnoreSign ignore = f.getAnnotation(IgnoreSign.class);
		return ignore != null;
	}
}
