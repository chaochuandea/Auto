package com.zhang.remoteconverter;

import com.zhang.json.JSON;

public class FastJsonConverter implements ARemoteConverter{

	@Override
	public Object getModel(String str, Class<?> clas) {
		Object obj = JSON.parseObject(str, clas);
		return obj;
	}

	@Override
	public String toJson(Object obj) {
		String js = JSON.toJSONString(obj);
		return js;
	}

}
