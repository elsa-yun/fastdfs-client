package com.lineteam.util;

import org.apache.commons.lang.ArrayUtils;

public class T {

	public static void main(String[] args) {
		t("");
	}

	public static void t(String a, String... arr) {
		System.out.println(arr);
		String[] as=new String[0];
		if (null == arr) {
			System.out.println("++++++++");
		}
		ArrayUtils.isEmpty(as);
		System.out.println(as.length);
	}

}
