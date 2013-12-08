package com.zhang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ���������Դ
 * 如果指定了AModel 不指定AModelField (变量全部（viewid=R.id.变量名,upload = true）为默认)
 * 如果指定了Amodel 指定了AmodelField 按照AmodelField的说明
 * @author chaochuande
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AModelField {
	int viewid() default -1;
	boolean upload() default true;
	String uploadField() default "";
	boolean bindView() default true;
	String getURL() default "";
	String postURL() default "";
	String dataField() default "";
	boolean isMultiPart() default false;
}
