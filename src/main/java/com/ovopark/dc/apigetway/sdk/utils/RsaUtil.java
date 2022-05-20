package com.ovopark.dc.apigetway.sdk.utils;

import com.ovopark.dc.apigetway.sdk.config.ApiConst;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: RsaUtil
 * @Description:(Rsa 工具类)
 * @author: Remiel_Mercy
 * @date: 2020年4月15日 上午10:14:06
 * @Copyright: 2020 www.ovopark.com Inc. All rights reserved.
 */
public class RsaUtil {

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * @throws
     * @Title: createKeys
     * @Description: (创建公钥私钥)
     * @param: @return
     * @return: ConcurrentHashMap<String, String>
     */
    public static ConcurrentHashMap<String, String> createKeys() {
        ConcurrentHashMap<String, String> data = new ConcurrentHashMap<String, String>();
        try {
            KeyPairGenerator keyPairGeno = KeyPairGenerator.getInstance(ApiConst.KEY_ALGORITHM);
            keyPairGeno.initialize(1024);
            KeyPair keyPair = keyPairGeno.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            String publicKey = Base64Util.encodeToString(rsaPublicKey.getEncoded());
            String privateKey = Base64Util.encodeToString(rsaPrivateKey.getEncoded());
            System.out.println("公钥：" + publicKey);
            System.out.println("私钥：" + privateKey);
            data.put(PUBLIC_KEY, publicKey);
            data.put(PRIVATE_KEY, privateKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * @throws
     * @Title: getPublicKey
     * @Description: (获取公钥对象)
     * @param: @param pubKeyData
     * @param: @return
     * @param: @throws Exception
     * @return: RSAPublicKey
     */
    public static RSAPublicKey getPublicKey(String pubKey) {
        return getPublicKey(Base64Util.decode(pubKey));

    }

    public static RSAPublicKey getPublicKey(byte[] pubKeyData) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKeyData);
        try {
            KeyFactory factory = KeyFactory.getInstance(ApiConst.KEY_ALGORITHM);
            return (RSAPublicKey) factory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @throws
     * @Title: getPrivateKey
     * @Description: (获取私钥对象)
     * @param: @param priKey
     * @param: @return
     * @param: @throws Exception
     * @return: RSAPrivateKey
     */
    public static RSAPrivateKey getPrivateKey(String priKey) {
        return getPrivateKey(Base64Util.decode(priKey));
    }

    public static RSAPrivateKey getPrivateKey(byte[] keyBytes) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(ApiConst.KEY_ALGORITHM);
            return (RSAPrivateKey) factory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * @throws
     * @Title: encryptByPublicKey
     * @Description: (公钥加密)
     * @param: @param data 待加密数据
     * @param: @param publicKey  私钥
     * @param: @return  返回密文
     * @return: String
     */
    public static String encryptByPublicKey(String data, String publicKey) {
        return encryptByPublicKey(data, getPublicKey(publicKey));
    }

    public static String encryptByPublicKey(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(ApiConst.KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64Util.encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @throws
     * @Title: decryptByPrivateKey
     * @Description: (私钥解密)
     * @param: @param data 待解密内容
     * @param: @param rsaPublicKey  公钥
     * @param: @return
     * @return: String   返回明文
     */

    public static String decryptByPrivateKey(String data, String privateKey) {
        return decryptByPrivateKey(data, getPrivateKey(privateKey));
    }

    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(ApiConst.KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] inputData = Base64Util.decode(data);
            byte[] bytes = cipher.doFinal(inputData);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
