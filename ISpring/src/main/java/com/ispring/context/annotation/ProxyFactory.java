package com.ispring.context.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * @author 应癫
 *
 *
 * 代理对象工厂：生成代理对象的
 */
public class ProxyFactory {

    private ProxyFactory() {

    }

    private static class Holder {
        private static ProxyFactory instance = new ProxyFactory();
    }

    public static ProxyFactory getInstance() {
        return Holder.instance;
    }

    /**
     * Jdk动态代理
     * @param obj  委托对象
     * @return   代理对象
     */
    public Object getJdkProxy(Object obj, Object tranObj) {
        // 获取代理对象
        return  Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    Object result;
                    Method[] declaredMethods = tranObj.getClass().getDeclaredMethods();
                    try{
                        // 开启事务(关闭事务的自动提交)

                        for (Method declaredMethod : declaredMethods) {
                            if (declaredMethod.isAnnotationPresent(Before.class)) {
                                declaredMethod.invoke(tranObj);
                            }
                        }
                        result = method.invoke(obj, args);

                        // 提交事务
                        for (Method declaredMethod : declaredMethods) {
                            if (declaredMethod.isAnnotationPresent(After.class)) {
                                declaredMethod.invoke(tranObj);
                            }
                        }
                        //transactionManager.commit();
                    }catch (Exception e) {
                        e.printStackTrace();
                        // 回滚事务
                        for (Method declaredMethod : declaredMethods) {
                            if (declaredMethod.isAnnotationPresent(AfterThrowing.class)) {
                                declaredMethod.invoke(tranObj);
                            }
                        }
                        //transactionManager.rollback();

                        // 抛出异常便于上层servlet捕获
                        throw e;

                    }

                    return result;
                });

    }


    /**
     * 使用cglib动态代理生成代理对象
     * @param obj 委托对象
     * @return
     */
//    public Object getCglibProxy(Object obj) {
//        return  Enhancer.create(obj.getClass(), new MethodInterceptor() {
//            @Override
//            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//                Object result = null;
//                try{
//                    // 开启事务(关闭事务的自动提交)
//                    transactionManager.beginTransaction();
//
//                    result = method.invoke(obj,objects);
//
//                    // 提交事务
//
//                    transactionManager.commit();
//                }catch (Exception e) {
//                    e.printStackTrace();
//                    // 回滚事务
//                    transactionManager.rollback();
//
//                    // 抛出异常便于上层servlet捕获
//                    throw e;
//
//                }
//                return result;
//            }
//        });
//    }
}
