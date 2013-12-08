package com.zhang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ����ģ�͹�����View
 * @author chaochuande
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AData {
	String viewFieldName() default "";
	boolean isMultiPart() default false;
	boolean upload() default true;
	String uploadField() default "";
}
