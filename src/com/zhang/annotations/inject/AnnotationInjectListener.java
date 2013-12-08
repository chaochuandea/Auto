package com.zhang.annotations.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.zhang.main.Auto;
import com.zhang.main.Auto.InjectListener;
import com.zhang.utils.AutoUtils;

public class AnnotationInjectListener implements InjectListener{
	private Method afterInjectmethod;
	private Object handler;
	public AnnotationInjectListener(Object handler){
		this.handler = handler;
	}
	public AnnotationInjectListener afterInject(Method method){
		this.afterInjectmethod = method;
		return this;
	}
	public static void setListener(Object activity,Annotation annotation,Method method){
		Auto auto = AutoUtils.get(activity.getClass().getCanonicalName());
		if(auto!=null){
			Class<?> type = annotation.annotationType();
			if(type.equals(AfterActivityToModel.class)){
				auto.setAfterActivityToModelListener(new AnnotationInjectListener(activity).afterInject(method));
			}else if(type.equals(AfterLayoutToActivity.class)){
				auto.setAfterLayoutToActivityListener(new AnnotationInjectListener(activity).afterInject(method));
			}else if(type.equals(AfterModelToActivity.class)){
				auto.setAfterModelToActivityListener(new AnnotationInjectListener(activity).afterInject(method));
			}
		}
	}
	@Override
	public void afterInject() {
		try {
			afterInjectmethod.invoke(handler);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
