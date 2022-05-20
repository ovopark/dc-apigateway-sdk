package com.ovopark.dc.apigetway.sdk.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.ovopark.dc.apigetway.sdk.config.ApiConst;
import com.ovopark.dc.apigetway.sdk.kit.PoJoKit;
import com.ovopark.dc.apigetway.sdk.kit.RSASignatureKit;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * @author xuefei
 * @ClassName: SignUtils
 * @Description: (签名工具类)
 * @date 2016年12月13日 下午3:41:49
 */
public class SignUtils {

    public static final String _SM = "_sm";
    public static final String _SIG = "_sig";
    private static String debugInfo;

    static {
        debugInfo = "";
    }

    /**
     * 使用<code>secret</code>对paramValues按以下算法进行签名： <br/>
     * uppercase(hex(sha1(secretkey1value1key2value2...secret))
     *
     * @param paramNames  需要签名的参数名
     * @param paramValues 参数列表
     * @param sign        签名信息
     * @param sign_method 签名方法 SHA1,MD5
     * @return
     */
    public static String sign(Map<String, Object> paramValues, String sign, String sign_method) {
        return sign(paramValues, null, sign, sign_method);
    }

    public static String signJson(String json, String secret, String sign_method) {
        StringBuilder sb = new StringBuilder(secret);
        sb.append(json);
        sb.append(secret);
        return sign(sign_method, sb.toString());
    }

    public static String signPoJo(Object pojo, String secret, String sign_method) {
        ConcurrentMap<String, Object> paramValues = new ConcurrentHashMap<>();
        Class<?> clazz = pojo.getClass();
        Field[] fields = PoJoKit.getNeedFields(clazz);
        for (Field field : fields) {
            try {
                // 抑制Java对修饰符的检查
                field.setAccessible(true);
                JSONField jsonField = field.getAnnotation(JSONField.class);
                //如果配置了fastjson JSONField注解,获取注解name属性
                String key = jsonField == null ? field.getName() : jsonField.name();
                if (!key.equals(_SIG)) {
                    Object value = field.get(pojo);
                    if (value != null) {
                        if (key.equals(_SM)) {
                            sign_method = value.toString();
                        }
                        if (value instanceof List || value instanceof Map) {
                            value = JSON.toJSONString(value);
                        }
                        paramValues.putIfAbsent(key, value);
                    } else {
                        paramValues.putIfAbsent(key, "");
                    }
                }

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        try {
            StringBuilder sb = new StringBuilder();
            List<String> paramNames = new ArrayList<String>(paramValues.size());
            paramNames.addAll(paramValues.keySet());
            Collections.sort(paramNames);
            sb.append(secret);
            for (String paramName : paramNames) {
                String value = ConvertUtils.toStr(paramValues.get(paramName), "");
                sb.append(paramName).append(value);
            }
            sb.append(secret);
            debugInfo = sb.toString();
            System.err.println("pojo debugInfo:" + debugInfo);
            byte[] digests = null;
            sign_method = sign_method.toLowerCase();
            if (ApiConst.sign_method_SHA1.equals(sign_method)) {
                digests = getSHA1Digest(sb.toString());
            } else if (ApiConst.sign_method_SHA256.equals(sign_method)) {
                digests = getSHA256Digest(sb.toString());
            } else {
                digests = getMD5Digest(sb.toString());
            }
            return byte2hex(digests);
        } catch (IOException e) {
            throw new RuntimeException("apigetway sign error:" + e.getMessage());
        }
    }


    /**
     * 对paramValues进行签名，其中ignoreParamNames这些参数不参与签名
     *
     * @param paramValues
     * @param ignoreParamNames
     * @param secret
     * @return
     */
    public static String sign(Map<String, Object> paramValues, List<String> ignoreParamNames, String secret,
                              String sign_method) {
        try {
            StringBuilder sb = new StringBuilder();
            List<String> paramNames = new ArrayList<String>(paramValues.size());
            paramNames.addAll(paramValues.keySet());
            if (ignoreParamNames != null && ignoreParamNames.size() > 0) {
                for (String ignoreParamName : ignoreParamNames) {
                    paramNames.remove(ignoreParamName);
                }
            }
            Collections.sort(paramNames);
            sb.append(secret);
            for (String paramName : paramNames) {
                String value = ConvertUtils.toStr(paramValues.get(paramName), "");
                sb.append(paramName).append(value);
            }
            sb.append(secret);
            debugInfo = sb.toString();
            System.err.println("map  debugInfo:" + debugInfo);
            byte[] digests = null;
            sign_method = sign_method.toLowerCase();
            if (ApiConst.sign_method_SHA1.equals(sign_method)) {
                digests = getSHA1Digest(sb.toString());
            } else if (ApiConst.sign_method_SHA256.equals(sign_method)) {
                digests = getSHA256Digest(sb.toString());
            } else {
                digests = getMD5Digest(sb.toString());
            }
            return byte2hex(digests);
        } catch (IOException e) {
            throw new RuntimeException("apigetway sign error:" + e.getMessage());
        }
    }

    public static String utf8Encoding(String value, String sourceCharsetName) {
        try {
            return new String(value.getBytes(sourceCharsetName), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static byte[] getSHA1Digest(String data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            bytes = md.digest(data.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse);
        }
        return bytes;
    }

    private static byte[] getSHA256Digest(String data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            bytes = md.digest(data.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse);
        }
        return bytes;
    }

    private static byte[] getMD5Digest(String data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse);
        }
        return bytes;
    }


    /**
     * 二进制转十六进制字符串
     *
     * @param bytes
     * @return
     */
    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().toUpperCase();
    }

    /**
     * @param @param  sign 签名字符串
     * @param @param  applicationSecret 秘钥
     * @param @param  signatureMethod	签名方法
     * @param @param  request
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: checkSign
     * @Description: (检查签名的有效性)
     */
    public static boolean checkSign(String sign, String applicationSecret, String signatureMethod, HttpServletRequest request) {
        HashMap<String, Object> needSignParams = new HashMap<>();
        Enumeration<String> enume = request.getParameterNames();
        while (enume.hasMoreElements()) {
            String name = enume.nextElement();
            //添加所有除_sig的参数,用做于签名参数
            if (!name.equals(_SIG)) {
                String value = ConvertUtils.toStr(request.getParameter(name), "");
                needSignParams.put(name, value);
            }
        }
        // 签名
        String signValue = SignUtils.sign(needSignParams, applicationSecret, signatureMethod);
        return signValue.equals(sign);
    }

    public static boolean checkSign(String sign, String applicationSecret, String signatureMethod, Object pojo) {
        //签名
        String signValue = SignUtils.signPoJo(pojo, applicationSecret, signatureMethod);
        return signValue.equals(sign);
    }

    /**
     * @param @param  sign 签名字符串
     * @param @param  applicationSecret 秘钥
     * @param @param  signatureMethod	签名方法
     * @param @param  requestMap 请求map
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: checkSign
     * @Description: (检查签名的有效性)
     */
    public static boolean checkSign(String sign, String applicationSecret, String signatureMethod, ConcurrentHashMap<String, String> requestMap) {
        HashMap<String, Object> needSignParams = new HashMap<>();
        for (Map.Entry<String, String> entry : requestMap.entrySet()) {
            String name = entry.getKey();
            //添加所有除_sig的参数,用做于签名参数
            if (!name.equals(_SIG)) {
                needSignParams.put(name, entry.getValue());
            }
        }
        // 签名
        String signValue = SignUtils.sign(needSignParams, applicationSecret, signatureMethod);
        return signValue.equals(sign);
    }

    public static String sign(String sign_method, String parmsvalue) {
        try {
            byte[] digests = null;
            sign_method = sign_method.toLowerCase();
            if (ApiConst.sign_method_SHA1.equals(sign_method)) {
                digests = getSHA1Digest(parmsvalue);
            } else if (ApiConst.sign_method_SHA256.equals(sign_method)) {
                digests = getSHA256Digest(parmsvalue);
            } else {
                digests = getMD5Digest(parmsvalue);
            }
            return byte2hex(digests);
        } catch (IOException e) {
            throw new RuntimeException("apigetway sign error:" + e.getMessage());
        }
    }

    public static String getDebugInfo() {
        return debugInfo;
    }

    /**
     * @throws
     * @Title: checkRsaSign
     * @Description: (检查RSA签名)
     * @param: @param sign  签名token
     * @param: @param publicKey 公钥
     * @param: @param requestMap 请求参数
     * @param: @return
     * @return: boolean
     */
    public static boolean checkRsaSign(String sign, String publicKey, ConcurrentHashMap<String, String> requestMap) {
        HashMap<String, Object> needSignParams = new HashMap<>();
        for (Map.Entry<String, String> entry : requestMap.entrySet()) {
            String name = entry.getKey();
            //添加所有除_sig的参数,用做于签名参数
            if (!name.equals(_SIG)) {
                needSignParams.put(name, entry.getValue());
            }
        }
        StringBuilder sb = new StringBuilder();
        List<String> paramNames = new ArrayList<String>(needSignParams.size());
        paramNames.addAll(needSignParams.keySet());
        Collections.sort(paramNames);
        for (String paramName : paramNames) {
            String value = ConvertUtils.toStr(needSignParams.get(paramName), "");
            sb.append(paramName).append(value);
        }
        String content = sb.toString();
        debugInfo = content;
        System.err.println("rsa  debugInfo:" + debugInfo);
        return RSASignatureKit.doCheck(content, sign, publicKey);

    }
}
