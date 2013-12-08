package com.zhang.annotations.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当Model取到Activity中声明为@AData,@AView变量的值后，触发的事件
 * @author chaochuande
 *
 */
@InjectEvent
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AfterActivityToModel {
	
}
