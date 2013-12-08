package com.zhang.util.core;

import java.util.HashMap;
import java.util.Map;

public class KeyDoubleValueMap <K1,V1,V2>{
	Map<K1, V1> views = new HashMap<K1, V1>();
	Map<K1, V2> agss = new HashMap<K1, V2>();
	public void put(K1 key,V1  view,V2 ags){
		if(view != null){
			views.put(key, view);
		}
		if(ags !=null){
			agss.put(key, ags);
		}
		
	}
	public V1 getView(K1 key){
		return views.get(key);
	}
	
	public V2 getAgs(K1 key){
		return agss.get(key);
	}
}
