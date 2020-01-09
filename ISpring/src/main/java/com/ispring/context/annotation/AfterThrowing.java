package com.ispring.context.annotation;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AfterThrowing {

	String value();

	String argNames() default "";

}
