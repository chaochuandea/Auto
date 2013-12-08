package com.zhang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ���������Դ
 * @author chaochuande
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface AModel {
	boolean isDefaultToView() default true;
	String uploadUrl() default "";
	String loadUrl() default "";
}
