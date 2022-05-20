package com.ovopark.dc.apigetway.sdk.config;

/**
 * @ClassName: OpenplatformConst
 * @Description:(常量定义)
 * @author: Remiel_Mercy
 * @date: 2019年4月30日 上午9:52:42
 * @Copyright: 2019 www.ovopark.com Inc. All rights reserved.
 */
public final class OpenplatformConst {
    /**
     * 版本号
     **/
    public static final String VERSION = "v1";

    public static final String JSON_DATA = "data";

    /**
     * @ClassName: Sm
     * @Description:(签名算法)
     * @author: Remiel_Mercy
     * @date: 2019年4月30日 上午9:53:30
     * @Copyright: 2019 www.ovopark.com Inc. All rights reserved.
     */
    public enum Sm {
        MD5("MD5", "md5"),
        SHA1("SHA1", "sha1"),
        SHA256("SHA256", "sha256"),
        RSA("RSA", "rsa");

        private String key;

        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        // 构造方法
        Sm(String key, String value) {
            this.key = key;
            this.value = value;
        }

        // 获取枚举定义的表达式
        public static String getValue(String key) {
            for (Sm e : Sm.values()) {
                if (e.getKey().equals(key)) {
                    return e.value;
                }
            }
            return null;
        }
    }

    /**
     * @ClassName: RequestMode
     * @Description:(请求方式)
     * @author: Remiel_Mercy
     * @date: 2019年4月30日 上午9:49:28
     * @Copyright: 2019 www.ovopark.com Inc. All rights reserved.
     */
    public enum RequestMode {
        POST("POST", "post"), GET("GET", "get"), PUT("PUT", "put"), DELETE("DELETE", "delete");
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        // 构造方法
        private RequestMode(String key, String value) {
            this.key = key;
            this.value = value;
        }

        // 获取枚举定义的表达式
        public static String getValue(String key) {
            for (RequestMode e : RequestMode.values()) {
                if (e.getKey().equals(key)) {
                    return e.value;
                }
            }
            return null;
        }
    }

    /**
     * @ClassName: Environment
     * @Description:(系统环境)
     * @author: Remiel_Mercy
     * @date: 2019年4月30日 上午9:49:16
     * @Copyright: 2019 www.ovopark.com Inc. All rights reserved.
     */
    public enum Environment {
        DEV("DEV", "dev"), TEST("TEST", "test"), FORMAL("FORMAL", "formal");
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        // 构造方法
        private Environment(String key, String value) {
            this.key = key;
            this.value = value;
        }

        // 获取枚举定义的表达式
        public static String getValue(String key) {
            for (Environment e : Environment.values()) {
                if (e.getKey().equals(key)) {
                    return e.value;
                }
            }
            return null;
        }
    }
}
