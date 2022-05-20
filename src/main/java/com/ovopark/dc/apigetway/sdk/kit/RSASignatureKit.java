package com.ovopark.dc.apigetway.sdk.kit;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.ovopark.dc.apigetway.sdk.config.ApiConst;
import com.ovopark.dc.apigetway.sdk.utils.Base64Util;

/**
 * 
 * @ClassName:  RSASignatureKit   
 * @Description:(RSA签名验签类 )
 * @author: Remiel_Mercy 
 * @date:   2020年4月15日 下午1:45:48   
 *     
 * @Copyright: 2020 www.ovopark.com Inc. All rights reserved.
 */
public class RSASignatureKit {
	
	/**
	 * @Title: sign   
	 * @Description: (RSA签名)
	 * @param: @param content  待签名数据 
	 * @param: @param privateKey 商户私钥 
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String sign(String content, String privateKey) {
		return sign(content, privateKey, CharsetKit.UTF_8);
	}
	/**
	 * @Title: sign   
	 * @Description: (RSA签名)
	 * @param: @param content  待签名数据 
	 * @param: @param privateKey 商户私钥 
	 * @param: @param encode 字符集编码 
	 * @param: @return      
	 * @return: String   签名值    
	 * @throws
	 */
	public static String sign(String content, String privateKey, String encode) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Util.decode(privateKey));
			KeyFactory keyFactory = KeyFactory.getInstance(ApiConst.KEY_ALGORITHM);
			PrivateKey pk = keyFactory.generatePrivate(priPKCS8);
			Signature signature = Signature.getInstance(ApiConst.SIGN_ALGORITHMS);
			signature.initSign(pk);
			signature.update(content.getBytes());
			byte[] signedData = signature.sign();
			return Base64Util.encodeToString(signedData);
		} catch (Exception ignored) {

		}
		return null;
	}
	/**
	 * 
	 * @Title: doCheck   
	 * @Description: (RSA签名检查)
	 * @param: @param content 待签名数据 
	 * @param: @param sign 签名值 
	 * @param: @param publicKey 分配给开发商公钥 
	 * @param: @return      
	 * @return: boolean      
	 * @throws
	 */
	public static boolean doCheck(String content, String sign, String publicKey) {
		return doCheck(content,sign,publicKey, CharsetKit.UTF_8);
	}
	/**
	 * 
	 * @Title: doCheck   
	 * @Description: (RSA签名检查)
	 * @param: @param content 待签名数据 
	 * @param: @param sign 签名值 
	 * @param: @param publicKey 分配给开发商公钥 
	 * @param: @param encode 字符集编码 
	 * @param: @return      
	 * @return: boolean      
	 * @throws
	 */
	public static boolean doCheck(String content, String sign, String publicKey, String encode) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ApiConst.KEY_ALGORITHM);
			byte[] encodedKey = Base64Util.decode(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			Signature signature = Signature.getInstance(ApiConst.SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content.getBytes(encode));
			return signature.verify(Base64Util.decode(sign));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
