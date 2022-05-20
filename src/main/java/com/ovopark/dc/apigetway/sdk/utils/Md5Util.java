package com.ovopark.dc.apigetway.sdk.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: Md5Util
 * @Description:(md5工具类)
 * @author: Remiel_Mercy
 * @date: 2020年4月3日 下午3:04:59
 * @Copyright: 2020 www.ovopark.com Inc. All rights reserved.
 */
public class Md5Util {
    public static byte[] compute(byte[] content) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(content);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String computeToHex(byte[] content) {
        return HexStringUtil.toHexString(compute(content));
    }

    public static String computeToBase64(byte[] content) {
        return Base64Util.encodeToString(compute(content));
    }
}
