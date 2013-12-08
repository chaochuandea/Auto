package com.zhang.utils;

import java.util.HashMap;
import java.util.Map;

import com.zhang.main.Model;


public class ModelUtils {
	private static Map<String, Model> autoMap = new HashMap<String, Model>();
	public static void add(String activity_class_name,Model model){
			autoMap.put(activity_class_name, model);
	}
	public static Model get(String activity_class_name){
		return autoMap.get(activity_class_name);
	}
}
