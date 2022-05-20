package com.ovopark.dc.apigetway.sdk.utils;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ovopark.dc.apigetway.sdk.config.ApiConst;
import com.ovopark.dc.apigetway.sdk.kit.CurrentTimeMillisClockKit;


public class OvoParkUtils {
    /**
     * 获取编码字符集
     *
     * @param request
     * @param response
     * @return String
     */
    public static String getCharacterEncoding(HttpServletRequest request,
                                              HttpServletResponse response) {
        if (null == request || null == response) {
            return "UTF-8";
        }
        String enc = request.getCharacterEncoding();
        if (null == enc || "".equals(enc)) {
            enc = response.getCharacterEncoding();
        }
        if (null == enc || "".equals(enc)) {
            enc = "UTF-8";
        }
        return enc;
    }


    public static String getCid() {
        // *md5hash(server ip address)，用来串联_cid,_cid=address|thread|time*
        String serverAddress = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            serverAddress = Md5Util.computeToHex(addr.getHostAddress().getBytes(StandardCharsets.UTF_8))
                    .substring(0, 6);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiConst.SERVER_ADDRESS + serverAddress + ApiConst.SPLIT + ApiConst.THREADID
                + Thread.currentThread().getId() + ApiConst.SPLIT + ApiConst.REQ_TAG + CurrentTimeMillisClockKit.getInstance().now();
    }


}
