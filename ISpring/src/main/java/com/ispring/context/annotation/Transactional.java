package com.ispring.context.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transactional {


	//@AliasFor("transactionManager")
	String value() default "";

	//@AliasFor("value")
	String transactionManager() default "";


	//Propagation propagation() default Propagation.REQUIRED;


	//Isolation isolation() default Isolation.DEFAULT;


	//int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;


	//boolean readOnly() default false;

	Class<? extends Throwable>[] rollbackFor() default {};

	String[] rollbackForClassName() default {};

	Class<? extends Throwable>[] noRollbackFor() default {};

	String[] noRollbackForClassName() default {};

}
