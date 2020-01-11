package com.ispring.context.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
	Class<? extends Annotation> value() default AspectAll.class;
}
