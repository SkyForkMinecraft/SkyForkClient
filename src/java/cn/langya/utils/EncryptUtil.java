package cn.langya.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author LangYa466
 * @date 2024/5/6 16:54
 */

public class EncryptUtil  {

	public static String key = "别破解了哥";

	/***
	 * @param orignal 原文
	 * @return 加密后的字符
	 */
	public static String encrypt(String orignal) {
		char[] chars = orignal.toCharArray();
		StringBuilder buffer = new StringBuilder();
		for(char aChar : chars) {
			int asciiCode = aChar;
			asciiCode += Arrays.hashCode(key.toCharArray());
			char result = (char)asciiCode;
			buffer.append(result);
		}


		return buffer.toString();
	}
	
	/**
	 *
	 * @param encryptedData :密文
	 * @return : 源数据
	 */
	public static String decrypt(String encryptedData) {
	    char[] chars = encryptedData.toCharArray();
	    StringBuilder sb = new StringBuilder();
	    for (char aChar : chars) {
	        int asciiCode = aChar;
	        asciiCode -= Arrays.hashCode(key.toCharArray());
	        char result = (char) asciiCode;
	        sb.append(result);
	    }
 
	    return sb.toString();
	}

}
 