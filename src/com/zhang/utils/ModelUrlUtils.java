package com.zhang.utils;

import java.util.HashMap;
import java.util.Map;


public class ModelUrlUtils {
	static Map<String, String> urlverablemap = new HashMap<String, String>();
	public static void setUrlVerable(String urlverablekey,String urlverablevalue){
		urlverablemap.put(urlverablekey, urlverablevalue);
	}
	public static String getUrlVerable(String urlverablekey){
		return urlverablemap.get(urlverablekey);
	}
}
