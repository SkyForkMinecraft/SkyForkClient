package com.skyfork.api.langya.utils;

import java.util.Arrays;

/**
 * @author LangYa466
 * @since 2024/5/6 16:54
 */

public class EncryptUtil  {

	public static String key = "别破解了哥";

	/**
	 * @param text 加密前的字符
	 * @return 加密后的字符
	 */
	public static String encrypt(String text) {
		char[] chars = text.toCharArray();
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
	 * @param text 加密后的字符
	 * @return : 加密前的字符
	 */
	public static String decrypt(String text) {
	    char[] chars = text.toCharArray();
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
 