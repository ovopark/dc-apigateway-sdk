package com.ovopark.dc.apigetway.sdk.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import com.alibaba.fastjson.JSON;

/**
 * @author Remiel_Mercy xuefei_fly@126.com
 * @ClassName: HttpClientUtil
 * @Description: (
 *Http客户端工具类 < br / >
 *这是内部调用类 ， 请不要在外部调用 。)
 * @date 2017年10月30日 上午9:41:22
 */
public class HttpClientUtil {
    /**
     * @param @param  strUrl
     * @param @return 参数
     * @return String    返回类型
     * @throws
     * @Title: getURL
     * @Description: (获取不带查询串的url)
     */
    public static String getURL(String strUrl) {
        if (null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if (-1 != indexOf) {
                return strUrl.substring(0, indexOf);
            }

            return strUrl;
        }
        return strUrl;
    }

    /**
     * @param @param  strUrl
     * @param @return 参数
     * @return String    返回类型
     * @throws
     * @Title: getQueryString
     * @Description: (获取查询串)
     */
    public static String getQueryString(String strUrl) {
        if (null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if (-1 != indexOf) {
                return strUrl.substring(indexOf + 1);
            }
            return "";
        }
        return null;
    }

    public static String getJson(String strUrl) {
        String s = getQueryString(strUrl);
        Map<String, String> map = new HashMap<String, String>();
        String[] arrys = s.split("&");
        for (String arry : arrys) {
            String[] kv = arry.split("=");
            map.put(kv[0], kv[1]);
        }
        String json = JSON.toJSONString(map);
        System.out.println("request json:" + json);
        return json;
    }

    public static HttpURLConnection getHttpURLConnection(String strUrl)
            throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        return httpURLConnection;
    }

    /**
     * @param @param  out
     * @param @param  data
     * @param @param  len
     * @param @throws IOException    参数
     * @return void    返回类型
     * @throws
     * @Title: doOutput
     * @Description: (
     *处理输出
     *注意 : 流关闭需要自行处理)
     */
    public static void doOutput(OutputStream out, byte[] data, int len)
            throws IOException {
        int dataLen = data.length;
        int off = 0;
        while (off < data.length) {
            if (len >= dataLen) {
                out.write(data, off, dataLen);
                off += dataLen;
            } else {
                out.write(data, off, len);
                off += len;
                dataLen -= len;
            }
            // 刷新缓冲区
            out.flush();
        }
    }

    /**
     * InputStream转换成String
     * 注意:流关闭需要自行处理
     *
     * @param in
     * @param encoding 编码
     * @return String
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in, String encoding) throws IOException {
        return new String(InputStreamTOByte(in), encoding);
    }

    /**
     * InputStream转换成Byte
     * 注意:流关闭需要自行处理
     *
     * @param in
     * @return byte
     * @throws Exception
     */
    public static byte[] InputStreamTOByte(InputStream in) throws IOException {
        int BUFFER_SIZE = 4096;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
            outStream.write(data, 0, count);
        }
        data = null;
        byte[] outByte = outStream.toByteArray();
        outStream.close();
        return outByte;
    }
}
