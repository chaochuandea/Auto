package com.zhang.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;

import com.zhang.annotations.AData;
import com.zhang.annotations.AModel;
import com.zhang.annotations.AURLVerable;
import com.zhang.http.AsyncHttpClient;
import com.zhang.http.AsyncHttpResponseHandler;
import com.zhang.http.RequestParams;
import com.zhang.main.Auto.InjectListener;
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
import com.zhang.remoteconverter.ARemoteConverter;
import com.zhang.remoteconverter.FastJsonConverter;
import com.zhang.utils.ModelUrlUtils;
import com.zhang.utils.ModelUtils;
import com.zhang.utils.ParamUtils;
import com.zhang.utils.StringUtils;
import com.zhang.utils.ViewUtils;

/**
 * 这个类是处理一个页面有多次提交的问题的；
 * 如果：new Model(Object container,Object model)  他是会自动把从Container中设置，获取相应的值
 * 如果：new Model(Object model) 他只是从model中设置获取相应的值
 * @author chaochuande
 *
 */
public class Model {
	private Object model;
	private Object  container;
	private boolean ismtc = true;
	private ARemoteConverter converter = new FastJsonConverter();
	private String postURL;
	private String getURL;
	private boolean upload = false;
	private boolean isget = true;
	
	
	private LoadErrorListener loaderror;
	private LoadFinishListener loadfinish;
	private LoadProgressListener loadprogress;
	private LoadStartListener loadstrart;
	private LoadSuccessListener loadsuccess;
	private UpLoadErrorListener uploaderror;
	private UpLoadFinishListener uploadfinish;
	private UpLoadProgressListener uploadprogress;
	private UpLoadStartListener uploadstrart;
	private UpLoadSuccessListener uploadsuccess;
	
	
	
	
	
	
	
	public LoadErrorListener getLoaderror() {
		return loaderror;
	}
	public void setLoaderror(LoadErrorListener loaderror) {
		this.loaderror = loaderror;
	}
	public LoadFinishListener getLoadfinish() {
		return loadfinish;
	}
	public void setLoadfinish(LoadFinishListener loadfinish) {
		this.loadfinish = loadfinish;
	}
	public LoadProgressListener getLoadprogress() {
		return loadprogress;
	}
	public void setLoadprogress(LoadProgressListener loadprogress) {
		this.loadprogress = loadprogress;
	}
	public LoadStartListener getLoadstrart() {
		return loadstrart;
	}
	public void setLoadstrart(LoadStartListener loadstrart) {
		this.loadstrart = loadstrart;
	}
	public LoadSuccessListener getLoadsuccess() {
		return loadsuccess;
	}
	public void setLoadsuccess(LoadSuccessListener loadsuccess) {
		this.loadsuccess = loadsuccess;
	}
	public UpLoadErrorListener getUploaderror() {
		return uploaderror;
	}
	public void setUploaderror(UpLoadErrorListener uploaderror) {
		this.uploaderror = uploaderror;
	}
	public UpLoadFinishListener getUploadfinish() {
		return uploadfinish;
	}
	public void setUploadfinish(UpLoadFinishListener uploadfinish) {
		this.uploadfinish = uploadfinish;
	}
	public UpLoadProgressListener getUploadprogress() {
		return uploadprogress;
	}
	public void setUploadprogress(UpLoadProgressListener uploadprogress) {
		this.uploadprogress = uploadprogress;
	}
	public UpLoadStartListener getUploadstrart() {
		return uploadstrart;
	}
	public void setUploadstrart(UpLoadStartListener uploadstrart) {
		this.uploadstrart = uploadstrart;
	}
	public UpLoadSuccessListener getUploadsuccess() {
		return uploadsuccess;
	}
	public void setUploadsuccess(UpLoadSuccessListener uploadsuccess) {
		this.uploadsuccess = uploadsuccess;
	}

	private InjectListener afterModelToContainer;
	private InjectListener afterContainerToModel;
	
	/**
	 * 设置数据服务器到JAVA对象的，以及JAVA对象到JSON数据的转换接口
	 * @param converter
	 */
	public void setRemoteConverter(ARemoteConverter converter){
		this.converter = converter;
	}
	/**
	 * 设置After Model To Activity事件
	 * @param afterModelToActivity
	 */
	public void setAfterModelToActivityListener(InjectListener afterModelToContainer) {
		this.afterModelToContainer = afterModelToContainer;
	}
	/**
	 * 设置After Activity To Model事件
	 * @param afterActivityToModel
	 */
	public void setAfterActivityToModelListener(InjectListener afterContainerToModel) {
		this.afterContainerToModel = afterContainerToModel;
	}

	private RequestParams multiRequestParams = new RequestParams();
	public Object getModel(){
		return model;
	}
	/**
	 * @param container  容器，包含了Object变量的类
	 * @param model 模型
	 * @param converter 服务器数据转换模型的接口，可使用FastJson,Jackson-Json等
	 */
	public Model(Object container, Object model) {
		ismtc = true;
		this.model = model;
		this.container = container;
		ModelUtils.add(container.getClass().getCanonicalName(), this);
		getUrl();
	}
	/**
	 * @param model 模型
	 */
	public Model(Object model){
		ismtc = false;
		this.model = model;
		getUrl();
	}
	/**
	 * 添加请求参数，这些参数会被记录，并与Model产生的参数结合为请求参数
	 * 请求参数有普通参数，还有MultiPart参数。AmodelField(isMultiPart=true)表示为MultiPart，如果isMultiPart被
	 * 设置为true，我们在构造RequestParams时，不会把AGS.getData()的值赋值给这个变量；如：
	 * 我们的Model如下：
	 * @AModelField(isMultiPart=true)
	 * String image;//表示的是一个图片Url地址
	 * 
	 * 我们的View如下：
	 * @AView
	 * ImageView image;
	 * 
	 * 那么，我们会构造RequestParams.put("image",ags.getData())其中的ags是ImageViewGS的实例
	 * @param paramsMap
	 */
	public void addRequestParams(HashMap<String, Object> paramsMap){
		Set<String> keys = 	paramsMap.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			multiRequestParams.put(string, paramsMap.get(string));
		}
	}
	/**
	 * 获取RequestParams
	 * @return
	 */
	public RequestParams getRequestParams(){
		if(ismtc){
			autowireContainerForModel();
			if(afterContainerToModel!=null){
				afterContainerToModel.afterInject();
			}
		}
		multiRequestParams = ParamUtils.getPrams(multiRequestParams,model);
		return multiRequestParams;
	}
	
	
	/**
	 * 根据注解获取URL地址
	 */
	private void getUrl(){
		AModel amodel = model.getClass().getAnnotation(AModel.class);
		AURLVerable aurlverable = model.getClass().getAnnotation(AURLVerable.class);
		 postURL = amodel.uploadUrl();
		 getURL = amodel.loadUrl();
		for (int i = 0; i < aurlverable.urlverable().length; i++) {
			String verable = aurlverable.urlverable()[i];
			String url = ModelUrlUtils.getUrlVerable(verable);
			if(getURL.contains(verable)){
				getURL = StringUtils.replace(getURL, verable, url);
			}
			if(postURL.contains(verable)){
				postURL = StringUtils.replace(postURL, verable, url);
			}
			if(getURL.equals("")){
				isget = false;
			}else{
				isget = true;
			}
			if(postURL.equals("")){
				upload = false;
			}else{
				upload = true;
			}
		}
	}
	public void autoLoad() {
		getData(getURL);
	}

	/**
	 * 根据不同的View有不同的AGS，我们得到View经过变化后的值，从新注入到Model层中，在得到RequestParams，开始Post
	 */
	public void autoSync() {
		if(upload){
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams p = getRequestParams();
			client.post(postURL,p, new AsyncHttpResponseHandler(){

				@Override
				public void onProgress(int bytesWritten, int totalSize) {
					// TODO Auto-generated method stub
					super.onProgress(bytesWritten, totalSize);
					if(uploadprogress!=null){
						uploadprogress.onProgress(bytesWritten, totalSize);
					}
					
				}

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					if(uploadstrart!=null){
						uploadstrart.onStart();
					}
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					if(uploadfinish!=null){
						uploadfinish.onFinish();
					}
				}

				@SuppressWarnings("deprecation")
				@Override
				public void onSuccess(String content) {
					// TODO Auto-generated method stub
					super.onSuccess(content);
					if(uploadsuccess!=null){
						uploadsuccess.onSuccess(content);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseBody, error);
					if(uploaderror!=null){
						uploaderror.onError(statusCode, responseBody);
					}
				}
				
			});
		}
		
	}

	/**
	 * 根据URL地址，开始Get数据，并把Model的数据注入到View中
	 */
	private void getData(String url) {
		if(isget){
			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, new AsyncHttpResponseHandler() {

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					if(loadfinish!=null){
						loadfinish.onFinish();
					}
				}

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					if(loadstrart!=null){
						loadstrart.onStart();
					}
				}

				
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseBody, error);
					if(loaderror!=null){
						loaderror.onError(statusCode, responseBody);
					}
				}

				@Override
				public void onProgress(int bytesWritten, int totalSize) {
					// TODO Auto-generated method stub
					super.onProgress(bytesWritten, totalSize);
					if(loadprogress!=null){
						loadprogress.onProgress(bytesWritten, totalSize);
					}
					
				}
				@SuppressWarnings("deprecation")
				@Override
				public void onSuccess(String arg0) {
					super.onSuccess(arg0);
					if(loadsuccess!=null){
						String returencontent = loadsuccess.onSuccess(arg0);
						if(returencontent!=null){
							arg0 =  returencontent;
						}	
					}
						
					arg0 = 	arg0.trim();
					model = converter.getModel(arg0, model.getClass());
					if(ismtc){
						autowireModelForContainer(container);
						if(afterModelToContainer!=null){
							afterModelToContainer.afterInject();
						}
					}
				}

			});	
		}
	}
	/**
	 * 把View的数据注入到Model中，并判断是否是ImageView，ImageButton，或者是isMultiPart=true,是的话，直接加入RequestParams,不注入到Model中
	 * 如果模型成中的变量呗注解为AData,那么，就直接把Activity中的变量值注入到Model层中
	 */
private void autowireContainerForModel(){

	if(model!=null){
		AModel am = model.getClass().getAnnotation(AModel.class);
		if( am!= null){
			for (Field field : model.getClass().getDeclaredFields()) {
				if(field.isAnnotationPresent(AData.class)){
					AData data = field.getAnnotation(AData.class);
					String modelFieldName = data.viewFieldName();
					if(modelFieldName.equals("")){
						modelFieldName = field.getName();
					}
				 try {
					 field.setAccessible(true);
					 boolean	isMultiPart = data.isMultiPart();
					 if(!isMultiPart){
						 field.set(model, ViewUtils.getData(container, modelFieldName));
						}
						else{
							if(data.upload()){
								String key = data.uploadField();
								if(key.equals("")){
									key = field.getName();
								}
								multiRequestParams.put(key, ViewUtils.getData(container, modelFieldName));
							}
							
						}
					
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				} 
				
			}
		}else{
			for (Field field : model.getClass().getDeclaredFields()) {
				if(field.isAnnotationPresent(AData.class)){
					AData data = field.getAnnotation(AData.class);
					String modelFieldName = data.viewFieldName();
					if(modelFieldName.equals("")){
						modelFieldName = field.getName();
					}
					 try {
						 field.setAccessible(true);
						 boolean	isMultiPart = data.isMultiPart();
						 if(!isMultiPart){
							 field.set(model, ViewUtils.getData(container, modelFieldName));
							}
							else{
								if(data.upload()){
									String key = data.uploadField();
									if(key.equals("")){
										key = field.getName();
									}
									multiRequestParams.put(key, ViewUtils.getData(container, modelFieldName));
								}
								
							}
						
						 
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
}
	/**
	 *把Model层中的数据注入到View中
	 */
	private void autowireModelForContainer(Object container) {
		if(model!=null){
			AModel am = model.getClass().getAnnotation(AModel.class);
			if( am!= null){
				for (Field field : model.getClass().getDeclaredFields()) {
					 if(field.isAnnotationPresent(AData.class)){
						AData data = field.getAnnotation(AData.class);
						String modelFieldName = data.viewFieldName();
						if(modelFieldName.equals("")){
							modelFieldName = field.getName();
						}
					 try {
						 field.setAccessible(true);
						ViewUtils.injectData(container,modelFieldName,field.get(model));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					}else{
						field.setAccessible(true);
						try {
							ViewUtils.injectData(container, field.getName(),field.get(model));
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
				}
			}else{
				for (Field field : model.getClass().getDeclaredFields()) {
					 if(field.isAnnotationPresent(AData.class)){
						AData data = field.getAnnotation(AData.class);
						String modelFieldName = data.viewFieldName();
						if(modelFieldName.equals("")){
							modelFieldName = field.getName();
						}
						 try {
							 field.setAccessible(true);
							ViewUtils.injectData(container,modelFieldName,field.get(model));
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		
		
		
	}
	
}
