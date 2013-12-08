package com.zhang.utils;

import java.util.HashMap;
import java.util.Map;

import com.zhang.main.Auto;

public class AutoUtils {
	private static Map<String, Auto> autoMap = new HashMap<String, Auto>();
	public static void add(String activity_class_name,Auto auto){
			autoMap.put(activity_class_name, auto);
	}
	public static Auto get(String activity_class_name){
		return autoMap.get(activity_class_name);
	}
}
