package com.zhang.annotations.http;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import com.zhang.main.Auto;
import com.zhang.main.Auto.LoadErrorListener;
import com.zhang.main.Auto.LoadFinishListener;
import com.zhang.main.Auto.LoadProgressListener;
import com.zhang.main.Auto.LoadStartListener;
import com.zhang.main.Auto.LoadSuccessListener;
import com.zhang.main.Auto.UpLoadErrorListener;
import com.zhang.main.Auto.UpLoadFinishListener;
import com.zhang.main.Auto.UpLoadProgressListener;
import com.zhang.main.Auto.UpLoadStartListener;
import com.zhang.main.Auto.UpLoadSuccessListener;
import com.zhang.utils.AutoUtils;
import com.zhang.utils.LogUtils;

public class AnnotationsHttpListener implements UpLoadErrorListener,UpLoadFinishListener,UpLoadProgressListener,UpLoadStartListener,UpLoadSuccessListener,LoadStartListener,LoadSuccessListener,LoadErrorListener,LoadFinishListener,LoadProgressListener{
	 private Object handler;
	
	 private Method onErrorMethod;
	 private Method onStartMethod;
	 private Method onSuccessMethod;
	 private Method onProgressMethod;
	 private Method onFinishMethod;
	 
	 public AnnotationsHttpListener(Object handler){
		 this.handler = handler;
	 }
	 public AnnotationsHttpListener onError(Method method){
		 this.onErrorMethod = method;
		 return this;
	 }
	 public AnnotationsHttpListener onStart(Method method){
		 this.onStartMethod = method;
		 return this;
	 }
	 public AnnotationsHttpListener onSuccess(Method method){
		 this.onSuccessMethod = method;
		 return this;
	 }
	 public AnnotationsHttpListener onProgress(Method method){
		 this.onProgressMethod = method;
		 return this;
	 }
	 public AnnotationsHttpListener onFinish(Method method){
		 this.onFinishMethod = method;
		 return this;
	 }
	@Override
	public void onFinish() {
		try {
			if(onFinishMethod!=null){
				onFinishMethod.invoke(handler);
			}
		} catch (Throwable e) {
            LogUtils.i(e.getMessage(), e);
        }
	}

	@Override
	public void onStart() {
		try {
			if(onStartMethod!=null){
				onStartMethod.invoke(handler);
			}
		} catch (Throwable e) {
            LogUtils.i(e.getMessage(), e);
        }
		
	}

	@Override
	public String onSuccess(String content) {
		try {
			if(onSuccessMethod!=null){
				Object obj =   onSuccessMethod.invoke(handler, content);
				return obj.toString();
			}
		}
		catch (Throwable e) {
            LogUtils.i(e.getMessage(), e);
        }
		 return content;
	}

	@Override
	public void onError(int statusCode, byte[] responseBody) {
		try {
			if(onErrorMethod!=null){
				onErrorMethod.invoke(handler, statusCode,responseBody);
			}
		} catch (Throwable e) {
            LogUtils.i(e.getMessage(), e);
        }
	}

	@Override
	public void onProgress(int bytesWritten, int totalSize) {
		try {
			if(onProgressMethod!=null){
				onProgressMethod.invoke(handler, bytesWritten,totalSize);
			}
		} catch (Throwable e) {
            LogUtils.i(e.getMessage(), e);
        }
	}
	public static void setListener(Object activity,Annotation annotation,Method method){
		Auto auto = AutoUtils.get(activity.getClass().getCanonicalName());
		if(auto!=null){
			Class<?> type = annotation.annotationType();
			if(type.equals(OnLoadError.class)){
				auto.setLoaderror(new AnnotationsHttpListener(activity).onError(method));
			}else if(type.equals(OnLoadFinish.class)){
				auto.setLoadfinish(new AnnotationsHttpListener(activity).onFinish(method));
			}else if(type.equals(OnLoadProgress.class)){
				auto.setLoadprogress(new AnnotationsHttpListener(activity).onProgress(method));
			}else if(type.equals(OnLoadStart.class)){
				auto.setLoadstrart(new AnnotationsHttpListener(activity).onStart(method));
			}else if(type.equals(OnLoadSuccess.class)){
				auto.setLoadsuccess(new AnnotationsHttpListener(activity).onSuccess(method));
			}else if(type.equals(OnUpLoadError.class)){
				auto.setUploaderror(new AnnotationsHttpListener(activity).onError(method));
			}else if(type.equals(OnUpLoadFinish.class)){
				auto.setUploadfinish(new AnnotationsHttpListener(activity).onFinish(method));
			}else if(type.equals(OnUpLoadProgress.class)){
				auto.setUploadprogress(new AnnotationsHttpListener(activity).onProgress(method));
			}else if(type.equals(OnUpLoadStart.class)){
				auto.setUploadstrart(new AnnotationsHttpListener(activity).onStart(method));
			}else if(type.equals(OnUpLoadSuccess.class)){
				auto.setUploadsuccess(new AnnotationsHttpListener(activity).onSuccess(method));
			}
		}
			
	}
}
