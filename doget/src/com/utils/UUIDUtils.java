package com.utils;

import java.util.UUID;

/**
 * 生成随机的字符串的工具类：
 * @author admin
 *
 */
public class UUIDUtils {

	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static void main(String[] args) {
		System.out.println(UUIDUtils.getUUID());
	}
}
