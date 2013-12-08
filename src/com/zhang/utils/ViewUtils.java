/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhang.utils;

import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import com.zhang.annotations.APreference;
import com.zhang.annotations.ARes;
import com.zhang.annotations.AView;
import com.zhang.annotations.http.AnnotationsHttpListener;
import com.zhang.annotations.http.HttpEvent;
import com.zhang.annotations.inject.AnnotationInjectListener;
import com.zhang.annotations.inject.InjectEvent;
import com.zhang.gs.AGS;
import com.zhang.util.core.DoubleKeyValueMap;
import com.zhang.util.core.KeyDoubleValueMap;
import com.zhang.view.ResLoader;
import com.zhang.view.ViewCommonEventListener;
import com.zhang.view.ViewCustomEventListener;
import com.zhang.view.ViewFinder;
import com.zhang.view.annotation.event.EventBase;
import com.zhang.view.annotation.event.OnClick;

public class ViewUtils {
	
    private ViewUtils() {
    }

    private static ConcurrentHashMap<Class<? extends Annotation>, ViewCustomEventListener> annotationType_viewCustomEventListener_map;

    /**
     * register Custom Annotation
     *
     * @param annotationType The type of custom annotation must be annotated by @EventBase.
     * @param listener
     */
    public static void registerCustomAnnotation(Class<? extends Annotation> annotationType, ViewCustomEventListener listener) {
        if (annotationType_viewCustomEventListener_map == null) {
            annotationType_viewCustomEventListener_map = new ConcurrentHashMap<Class<? extends Annotation>, ViewCustomEventListener>();
        }
        annotationType_viewCustomEventListener_map.put(annotationType, listener);
    }

    public static KeyDoubleValueMap<Integer, View, AGS> inject(View view) {
      return  injectObject(view, new ViewFinder(view));
    }

    public static KeyDoubleValueMap<Integer, View, AGS> inject(Activity activity) {
       return  injectObject(activity, new ViewFinder(activity));
    }

    public static KeyDoubleValueMap<Integer, View, AGS> inject(PreferenceActivity preferenceActivity) {
      return  injectObject(preferenceActivity, new ViewFinder(preferenceActivity));
    }

    public static KeyDoubleValueMap<Integer, View, AGS> inject(Object handler, View view) {
       return injectObject(handler, new ViewFinder(view));
    }

    public static KeyDoubleValueMap<Integer, View, AGS> inject(Object handler, Activity activity) {
      return  injectObject(handler, new ViewFinder(activity));
    }

    public static KeyDoubleValueMap<Integer, View, AGS> inject(Object handler, PreferenceGroup preferenceGroup) {
       return injectObject(handler, new ViewFinder(preferenceGroup));
    }

    public static KeyDoubleValueMap<Integer, View, AGS> inject(Object handler, PreferenceActivity preferenceActivity) {
       return injectObject(handler, new ViewFinder(preferenceActivity));
    }
    public static Object getData(Object handler,String modelFieldName){
    	Object obj = null;
    	try {
			Field field = handler.getClass().getDeclaredField(modelFieldName);
			field.setAccessible(true);
			obj = field.get(handler);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
    }
    public static void injectData(Object handler,String modelFieldName,Object obj){
    	try {
			Field field = handler.getClass().getDeclaredField(modelFieldName);
			field.setAccessible(true);
			field.set(handler, obj);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    private static KeyDoubleValueMap<Integer, View, AGS> injectObject(Object handler, ViewFinder finder) {
    	KeyDoubleValueMap<Integer, View, AGS> kdv = new KeyDoubleValueMap<Integer, View, AGS>();
        // inject view
        Field[] fields = handler.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                AView viewInject = field.getAnnotation(AView.class);
                if (viewInject != null) {
                    try {
                    	int resId = viewInject.id();
                    	if(resId==-1){
                    		 String viewId  = field.getName();
                             
                             resId = finder.getContext().getResources().getIdentifier(viewId, "id", finder.getContext().getPackageName());                        
                    	}
                        View view = finder.findViewById(resId);
                        kdv.put(resId, view,null);
                        if (view != null) {
                            field.setAccessible(true);
                            field.set(handler, view);
                        }
                    } catch (Throwable e) {
                        LogUtils.e(e.getMessage(), e);
                    }
                } else {
                    ARes resInject = field.getAnnotation(ARes.class);
                    if (resInject != null) {
                        try {
                        	int resId = resInject.id();
                        	if(resId==-1){
                        		String viewId  = field.getName();
                                
                                resId = finder.getContext().getResources().getIdentifier(viewId, "id", finder.getContext().getPackageName());                        
                        	}
                            Object res = ResLoader.loadRes(
                                    resInject.type(), finder.getContext(), resId);
                            if (res != null) {
                                field.setAccessible(true);
                                field.set(handler, res);
                            }
                        } catch (Throwable e) {
                            LogUtils.e(e.getMessage(), e);
                        }
                    } else if (AGS.class.isAssignableFrom(field.getType())) {// ��AGS����
				com.zhang.annotations.AGS ags = field
						.getAnnotation(com.zhang.annotations.AGS.class);
				int[] id = ags.id();
				for (int i = 0; i < id.length; i++) {
					try {
						field.setAccessible(true);
						kdv.put(id[i], null,(AGS) field.get(handler));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
                    }  
                    
                    else {
                        APreference preferenceInject = field.getAnnotation(APreference.class);
                        if (preferenceInject != null) {
                            try {
                                Preference preference = finder.findPreference(preferenceInject.id());
                                if (preference != null) {
                                    field.setAccessible(true);
                                    field.set(handler, preference);
                                }
                            } catch (Throwable e) {
                                LogUtils.e(e.getMessage(), e);
                            }
                        }
                    }
                }
            }
        }

        // inject event
        Method[] methods = handler.getClass().getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            String eventName = OnClick.class.getCanonicalName();
            String prefix = eventName.substring(0, eventName.lastIndexOf('.'));
            DoubleKeyValueMap<Object, Annotation, Method> value_annotation_method_map = new DoubleKeyValueMap<Object, Annotation, Method>();
            for (Method method : methods) {
                Annotation[] annotations = method.getDeclaredAnnotations();
                method.setAccessible(true);
                if (annotations != null && annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType().getAnnotation(EventBase.class) != null) {
                            if (annotation.annotationType().getCanonicalName().startsWith(prefix)) {
                                try {
                                    Method valueMethod = annotation.annotationType().getDeclaredMethod("value");
                                    Object value = valueMethod.invoke(annotation);
                                    if (value.getClass().isArray()) {
                                        int len = Array.getLength(value);
                                        for (int i = 0; i < len; i++) {
                                            value_annotation_method_map.put(Array.get(value, i), annotation, method);
                                        }
                                    } else {
                                        value_annotation_method_map.put(value, annotation, method);
                                    }
                                } catch (Throwable e) {
                                    LogUtils.e(e.getMessage(), e);
                                }
                            } else {
                                Class<? extends Annotation> annType = annotation.annotationType();
                                ViewCustomEventListener listener = annotationType_viewCustomEventListener_map.get(annType);
                                if (listener != null) {
                                    listener.setEventListener(handler, finder, annotation, method);
                                }
                            }
                        }else if(annotation.annotationType().getAnnotation(HttpEvent.class) != null){
                        	AnnotationsHttpListener.setListener(handler, annotation, method);
                        }else if(annotation.annotationType().getAnnotation(InjectEvent.class) != null){
                        	AnnotationInjectListener.setListener(handler, annotation, method);
                        }
                    }
                }
            }
            ViewCommonEventListener.setEventListener(handler, finder, value_annotation_method_map);
        }
        return kdv;
    }

}
