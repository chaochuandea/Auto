package com.zhang.utils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;


import com.zhang.annotations.AModel;
import com.zhang.annotations.AModelField;
import com.zhang.http.RequestParams;
import com.zhang.json.JSON;
import com.zhang.json.JSONObject;

public class ParamUtils {
	/**
	 * 把obj的数据放入multiRequestParams中去
	 * @param multiRequestParams
	 * @param obj
	 * @return multiRequestParams
	 */
	public static RequestParams getPrams(RequestParams multiRequestParams,Object obj){
//		Map<String, String> paramsMap = new HashMap<String, String>();
		JSONObject j = JSON.parseObject(JSON.toJSONString(obj));
		Set<Entry<String, Object>> t =j.entrySet();
		boolean isModel = obj.getClass().getAnnotation(AModel.class)!=null;
		for (Iterator<Entry<String, Object>> iterator = t.iterator(); iterator.hasNext();) {
			Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
			String key = entry.getKey();
			try {
			Field field = 	obj.getClass().getDeclaredField(entry.getKey());
			
			if(isModel){
				if (field.isAnnotationPresent(AModelField.class)) {
					AModelField fi = field.getAnnotation(AModelField.class);
					String uploadField = fi.uploadField();
					boolean upload = fi.upload();
					if(!uploadField.equals("")){
						upload = true;
						key = uploadField;
					}
					if(upload){
						if(!fi.isMultiPart()){
							multiRequestParams.put(key, entry.getValue().toString());
						}
					}
				}else{
					multiRequestParams.put(key, entry.getValue().toString());
				}
			}else{
				if (field.isAnnotationPresent(AModelField.class)) {
					AModelField fi = field.getAnnotation(AModelField.class);
					String uploadField = fi.uploadField();
					boolean upload = fi.upload();
					if(!uploadField.equals("")){
						upload = true;
						key = uploadField;
					}
					if(upload){
						if(!fi.isMultiPart()){
							multiRequestParams.put(key, entry.getValue().toString());
						}
					}
				}
			}
			
			
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return multiRequestParams;
	}
}
