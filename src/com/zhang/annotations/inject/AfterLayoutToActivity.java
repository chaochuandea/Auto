package com.zhang.annotations.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 把Activity中声明为@AView的变量赋值后触发的事件
 * @author chaochuande
 *
 */
@InjectEvent
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AfterLayoutToActivity {
	
}
