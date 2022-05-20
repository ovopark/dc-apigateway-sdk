package com.ovopark.dc.apigetway.sdk.utils;

import java.util.zip.CRC32;

public class CRC32Utils {

    /**
     * 冗余校验码,产生一个bai32bit（8位十六进制数）的校验值
     *
     * @param key
     * @return
     */
    public static Long getCRC32(String key) {
        CRC32 crc = new CRC32();
        crc.update(key.getBytes());
        return crc.getValue();
    }

}
