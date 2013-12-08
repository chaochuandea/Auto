package com.zhang.main;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.Header;

import com.zhang.annotations.AData;
import com.zhang.annotations.AModel;
import com.zhang.annotations.AModelField;
import com.zhang.annotations.AURLVerable;
import com.zhang.exception.ABindViewException;
import com.zhang.gs.AGS;
import com.zhang.gs.factory.AGSFactory;
import com.zhang.http.AsyncHttpClient;
import com.zhang.http.AsyncHttpResponseHandler;
import com.zhang.http.RequestParams;
import com.zhang.remoteconverter.ARemoteConverter;
import com.zhang.remoteconverter.FastJsonConverter;
import com.zhang.util.core.KeyDoubleValueMap;
import com.zhang.utils.AutoUtils;
import com.zhang.utils.ModelUrlUtils;
import com.zhang.utils.ParamUtils;
import com.zhang.utils.StringUtils;
import com.zhang.utils.ViewUtils;

import android.app.Activity;
import android.view.View;

public class Auto {
	private Object model;
	private View view;
	private Activity activity;
	private boolean isget = true;
	private ARemoteConverter converter = new FastJsonConverter();
	private KeyDoubleValueMap<Integer, View, AGS> kdv = new KeyDoubleValueMap<Integer, View, AGS>();
	private String postURL;
	private String getURL;
	private boolean upload = false;
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
	
	
	private InjectListener afterModelToActivity;
	private InjectListener afterLayoutToActivity;
	private InjectListener afterActivityToModel;
	
	
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
	public void setAfterModelToActivityListener(InjectListener afterModelToActivity) {
		this.afterModelToActivity = afterModelToActivity;
	}
	/**
	 * 设置After Layout To Activity事件
	 * @param afterLayoutToActivity
	 */
	public void setAfterLayoutToActivityListener(InjectListener afterLayoutToActivity) {
		this.afterLayoutToActivity = afterLayoutToActivity;
	}
	/**
	 * 设置After Activity To Model事件
	 * @param afterActivityToModel
	 */
	public void setAfterActivityToModelListener(InjectListener afterActivityToModel) {
		this.afterActivityToModel = afterActivityToModel;
	}

	private RequestParams multiRequestParams = new RequestParams();
	public Object getModel(){
		return model;
	}
	/**
	 * @param activity 
	 * @param model 模型
	 * @param converter 服务器数据转换模型的接口，可使用FastJson,Jackson-Json等
	 */
	public Auto(Activity activity, Object model) {
		this.model = model;
		this.activity = activity;
		AutoUtils.add(activity.getClass().getCanonicalName(), this);
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
	private RequestParams getRequestParams(){
		autowireViewForModel();
		multiRequestParams = ParamUtils.getPrams(multiRequestParams,model);
		if(afterActivityToModel!=null){
			afterActivityToModel.afterInject();
		}
		return multiRequestParams;
	}
	/**
	 * 返回现在的请求参数
	 * @return
	 */
	public RequestParams getCurrentRequestParams(){
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
	/**
	 * 注入View
	 */
	public void injectView(){
		kdv =  activity == null ? ViewUtils.inject(view) : ViewUtils.inject(activity);
		if(afterLayoutToActivity!=null){
			afterLayoutToActivity.afterInject();
		}
	}
	/**
	 * 注入view，开始Get数据，数据获取完成后，自动把数据填充到View里
	 */
	public void autoLoad() {
		getUrl();
		getData(getURL);
	}

	/**
	 * 根据不同的View有不同的AGS，我们得到View经过变化后的值，从新注入到Model层中，在得到RequestParams，开始Post
	 */
	public void autoSync() {
		getUrl();
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
						String returncontent = loadsuccess.onSuccess(arg0);
						if(returncontent!=null){
							arg0 = returncontent;
						}
					}
						
					arg0 = 	arg0.trim();
					System.out.println("返回值是："+arg0);
					try {
						model = converter.getModel(arg0, model.getClass());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					autowireModelForView();
					if(afterModelToActivity!=null){
						afterModelToActivity.afterInject();
					}
					
				}

			});
		}
	}
	/**
	 * 把View的数据注入到Model中，并判断是否是ImageView，ImageButton，或者是isMultiPart=true,是的话，直接加入RequestParams,不注入到Model中
	 * 如果模型成中的变量呗注解为AData,那么，就直接把Activity中的变量值注入到Model层中
	 */
private void autowireViewForModel(){

	if(model!=null){
		AModel am = model.getClass().getAnnotation(AModel.class);
		boolean isDefaultToView ;
		if( am!= null){
			isDefaultToView = am.isDefaultToView();
			for (Field field : model.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(AModelField.class)) {
					AModelField androidView = field.getAnnotation(AModelField.class);
					if(isDefaultToView||androidView.viewid()!=-1||androidView.bindView()){
							int resId = androidView.viewid();
							if (resId == -1) {
								String viewId = field.getName();

								resId = activity.getResources().getIdentifier(viewId, "id",
										activity.getPackageName());
							}
							try {
								field.setAccessible(true);
								Object view = kdv.getView(resId);
								AGS ags = kdv.getAgs(resId);
								if(ags!=null&&view!=null){
									ags.setView(view);
									boolean	isMultiPart = androidView.isMultiPart();
									if(view.getClass().getSimpleName().equals("ImageView")||view.getClass().getSimpleName().equals("ImageButton")){
										isMultiPart = true;
									}
									if(!isMultiPart){
										field.set(model, ags.getData());
									}
									else{
										if(androidView.upload()){
											String key = androidView.uploadField();
											if(key.equals("")){
												key = field.getName();
											}
											multiRequestParams.put(key, ags.getData());
										}
										
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
						
					
				} else if(field.isAnnotationPresent(AData.class)){
					AData data = field.getAnnotation(AData.class);
					String modelFieldName = data.viewFieldName();
					if(modelFieldName.equals("")){
						modelFieldName = field.getName();
					}
				 try {
					 field.setAccessible(true);
					 boolean	isMultiPart = data.isMultiPart();
					 if(!isMultiPart){
						 field.set(model, ViewUtils.getData(activity, modelFieldName));
						}
						else{
							if(data.upload()){
								String key = data.uploadField();
								if(key.equals("")){
									key = field.getName();
								}
								multiRequestParams.put(key, ViewUtils.getData(activity, modelFieldName));
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
				else{
					if(isDefaultToView){
						String viewId = field.getName();
						int resId = activity.getResources().getIdentifier(viewId, "id",
								activity.getPackageName());
						try {
							if(resId!=0){
								try {
									field.setAccessible(true);
									Object view = kdv.getView(resId);
									AGS ags = kdv.getAgs(resId);
									if(ags!=null&&view!=null){
										ags.setView(view);
										field.set(model, ags.getData());
									}
									
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				
			}
		}else{
			for (Field field : model.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(AModelField.class)) {
					AModelField androidView = field.getAnnotation(AModelField.class);
					int resId = androidView.viewid();
					if(androidView.bindView()||androidView.viewid()!=-1){
						if (resId == -1) {
							String viewId = field.getName();

							resId = activity.getResources().getIdentifier(viewId, "id",
									activity.getPackageName());
						}
						try {
							field.setAccessible(true);
							Object view = kdv.getView(resId);
							AGS ags = kdv.getAgs(resId);
							if(ags!=null&&view!=null){
								ags.setView(view);
								boolean	isMultiPart = androidView.isMultiPart();
								if(!view.getClass().getSimpleName().equals("ImageView")||!view.getClass().getSimpleName().equals("ImageButton")){
									isMultiPart = true;
								}
								if(!isMultiPart){
									field.set(model, ags.getData());
								}
								else{
									if(androidView.upload()){
										String key = androidView.uploadField();
										if(key.equals("")){
											key = field.getName();
										}
										multiRequestParams.put(key, ags.getData());
									}
									
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
					
				}else if(field.isAnnotationPresent(AData.class)){
					AData data = field.getAnnotation(AData.class);
					String modelFieldName = data.viewFieldName();
					if(modelFieldName.equals("")){
						modelFieldName = field.getName();
					}
					 try {
						 field.setAccessible(true);
						 boolean	isMultiPart = data.isMultiPart();
						 if(!isMultiPart){
							 field.set(model, ViewUtils.getData(activity, modelFieldName));
							}
							else{
								if(data.upload()){
									String key = data.uploadField();
									if(key.equals("")){
										key = field.getName();
									}
									multiRequestParams.put(key, ViewUtils.getData(activity, modelFieldName));
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
	private void autowireModelForView() {
		if(model!=null){
			AModel am = model.getClass().getAnnotation(AModel.class);
			boolean isDefaultToView ;
			if( am!= null){
				isDefaultToView = am.isDefaultToView();
				for (Field field : model.getClass().getDeclaredFields()) {
					if (field.isAnnotationPresent(AModelField.class)) {
						AModelField androidView = field.getAnnotation(AModelField.class);
						if(isDefaultToView||androidView.viewid()!=-1||androidView.bindView()){
								int resId = androidView.viewid();
								if (resId == -1) {
									String viewId = field.getName();

									resId = activity.getResources().getIdentifier(viewId, "id",
											activity.getPackageName());
								}
								try {
									field.setAccessible(true);
									Object obj = field.get(model);
									Object view = kdv.getView(resId);
									if(kdv.getAgs(resId)==null&&view!=null){
										AGS ags = AGSFactory.create(view.getClass().getSimpleName());
										if(ags==null){
											throw new ABindViewException("没有找到对应的默认AGS,请继承AGS，并指定"+field.getName()+"的@AGS");
										}else{
											kdv.put(resId, (View)view, ags);
										}
									}
									if(kdv.getAgs(resId)!=null&&view!=null){
										kdv.getAgs(resId).setView(view);
										kdv.getAgs(resId).setData(obj);
									}
									
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
						
					} else if(field.isAnnotationPresent(AData.class)){
						AData data = field.getAnnotation(AData.class);
						String modelFieldName = data.viewFieldName();
						if(modelFieldName.equals("")){
							modelFieldName = field.getName();
						}
					 try {
						 field.setAccessible(true);
						ViewUtils.injectData(activity,modelFieldName,field.get(model));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					} 
					else{
						if(isDefaultToView){
							String viewId = field.getName();
							int resId = activity.getResources().getIdentifier(viewId, "id",
									activity.getPackageName());
							try {
								if(resId!=0){
									field.setAccessible(true);
									Object obj = field.get(model);
									Object view = kdv.getView(resId);
									try {
										if(kdv.getAgs(resId)==null&&view!=null){
											AGS ags = AGSFactory.create(view.getClass().getSimpleName());
											if(ags==null){
												throw new ABindViewException("没有找到对应的默认AGS,请继承AGS，并指定"+field.getName()+"的@AGS");
											}else{
												kdv.put(resId, (View)view, ags);
											}
										}
										kdv.getAgs(resId).setView(view);
										kdv.getAgs(resId).setData(obj);
										
									} catch (Exception e) {
										e.printStackTrace();
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
			}else{
				for (Field field : model.getClass().getDeclaredFields()) {
					if (field.isAnnotationPresent(AModelField.class)) {
						AModelField androidView = field.getAnnotation(AModelField.class);
						int resId = androidView.viewid();
						if(androidView.bindView()||androidView.viewid()!=-1){
							if (resId == -1) {
								String viewId = field.getName();

								resId = activity.getResources().getIdentifier(viewId, "id",
										activity.getPackageName());
							}
							try {
								field.setAccessible(true);
								Object obj = field.get(model);
								Object view = kdv.getView(resId);
								if(kdv.getAgs(resId)==null&&view!=null){
									AGS ags = AGSFactory.create(view.getClass().getSimpleName());
									if(ags==null){
										throw new ABindViewException("没有找到对应的默认AGS,请继承AGS，并指定"+field.getName()+"的@AGS");
									}else{
										kdv.put(resId, (View)view, ags);
									}
								}
								kdv.getAgs(resId).setView(view);
								kdv.getAgs(resId).setData(obj);
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}else if(field.isAnnotationPresent(AData.class)){
						AData data = field.getAnnotation(AData.class);
						String modelFieldName = data.viewFieldName();
						if(modelFieldName.equals("")){
							modelFieldName = field.getName();
						}
						 try {
							 field.setAccessible(true);
							ViewUtils.injectData(activity,modelFieldName,field.get(model));
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
	 * 下载开始
	 * @author chaochuande
	 *
	 */
	public interface LoadStartListener{
		void onStart();
	}
	public interface LoadSuccessListener{
		String onSuccess(String content);
	}
	public interface LoadErrorListener{
		void onError(int statusCode,byte[] responseBody);
	}
	public interface LoadProgressListener{
		void onProgress(int bytesWritten, int totalSize);
	}
	public interface LoadFinishListener{
		void onFinish();
	}
	public interface UpLoadStartListener{
		void onStart();
	}
	public interface UpLoadSuccessListener{
		String onSuccess(String content);
	}
	public interface UpLoadErrorListener{
		void onError(int statusCode,byte[] responseBody);
	}
	public interface UpLoadProgressListener{
		void onProgress(int bytesWritten, int totalSize);
	}
	public interface UpLoadFinishListener{
		void onFinish();
	}
	/**
	 * interface InjectListener
	 */
	public interface InjectListener{
		void afterInject();
	}
}
