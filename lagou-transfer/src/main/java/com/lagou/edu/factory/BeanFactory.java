package com.lagou.edu.factory;

import com.ispring.context.annotation.AnnotationApplicationContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 应癫
 *
 * 工厂类，生产对象（使用反射技术）
 */
public class BeanFactory {

    /**
     * 任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
     * 任务二：对外提供获取实例对象的接口（根据id获取）
     */

    private static AnnotationApplicationContext applicationContext;  // 存储对象

    static {
        try {
            applicationContext = new AnnotationApplicationContext("com.lagou.edu");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

//    static {
//        //读取注解配置
//        //扫描注解类
//        String packNameList = "com.lagou.edu";
//        FilterBuilder filterBuilder = new FilterBuilder();
//
//        for (String packName : packNameList.split(",")) {
//            filterBuilder = filterBuilder.includePackage(packName);//定义要扫描的包
//        }
//
//
//        Predicate<String> filter = filterBuilder;//过滤器
//
//        //里面放的是这些包所在的资源路径
//        Collection<URL> urlTotals = new ArrayList<>();
//        for (String packName : packNameList.split(",")) {
//            Collection<URL> urls =  ClasspathHelper.forPackage(packName);
//            urlTotals.addAll(urls);
//        }
//
//        /**
//         * 定义Reflections对象，指明"包过滤器"，以及扫描器的类型，主要把是扫描器的类型
//         * 细分之后，得到对应的数据
//         */
//        Reflections reflections = new Reflections(new ConfigurationBuilder()
//                .filterInputsBy(filter)
//                .setScanners(
//                        new SubTypesScanner().filterResultsBy(filter),
//                        new TypeAnnotationsScanner().filterResultsBy(filter),
//                        new FieldAnnotationsScanner().filterResultsBy(filter),
//                        new MethodAnnotationsScanner().filterResultsBy(filter),
//                        new MethodParameterScanner().filterResultsBy(filter)
//
//                ).setUrls(urlTotals));
//
//        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
//        for (Class<?> service : services) {
//                Service annotation = service.getAnnotation(Service.class);
//                String beanName;
//                if ((beanName = annotation.value()).isEmpty()) {
//                    beanName = service.getName().split("\\.")[service.getName().split("\\.").length - 1];
//                    beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
//                }
//                beanDefinitionMap.put(beanName, new BeanDefinition<>(beanName, service, service.getName()));
//                // System.out.println(value.invoke(service) + "," + o);
//        }
//
//        services = reflections.getTypesAnnotatedWith(Controller.class);
//        for (Class<?> service : services) {
//                Controller annotation = service.getAnnotation(Controller.class);
//                String beanName;
//                if ((beanName = annotation.value()).isEmpty()) {
//                    beanName = service.getName().split("\\.")[service.getName().split("\\.").length - 1];
//                    beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
//                }
//                beanDefinitionMap.put(beanName, new BeanDefinition<>(beanName, service, service.getName()));
//                // System.out.println(value.invoke(service) + "," + o);
//
//        }
//
//        services = reflections.getTypesAnnotatedWith(Transactional.class);
//        for (Class<?> service : services) {
//                Transactional annotation = service.getAnnotation(Transactional.class);
//
//                String beanName;
//                if ((beanName = annotation.value()).isEmpty()) {
//                    beanName = service.getName().split("\\.")[service.getName().split("\\.").length - 1];
//                    beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
//                }
//                beanDefinitionMap.put(beanName, new BeanDefinition<>(beanName, service, service.getName()));
//        }
//
//        services = reflections.getTypesAnnotatedWith(Repository.class);
//        for (Class<?> service : services) {
//
//
//                Repository annotation = service.getAnnotation(Repository.class);
//
//                String beanName;
//                if ((beanName = annotation.value()).isEmpty()) {
//                    beanName = service.getName().split("\\.")[service.getName().split("\\.").length - 1];
//                    beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
//                }
//                beanDefinitionMap.put(beanName, new BeanDefinition<>(beanName, service, service.getName()));
//        }
//
//
//        //实例化
//        beanDefinitionMap.forEach((k,v)->{
//            try {
//                //v.getClazz().newInstance()
//                v.setInstance(v.getClazz().newInstance());
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        });
//
//        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(Autowired.class);
//        Set<Field> fields = reflections.getFieldsAnnotatedWith(Autowired.class);
//        for (Field field : fields) {
//            field.setAccessible(true);
//            String name = field.getName();
//            BeanDefinition beanDefinition = beanDefinitionMap.get(name);
//            Class<?> o = beanDefinition.getClazz();
//
//            try {
//                Class<?> declaringClass = field.getDeclaringClass();
//                field.set(declaringClass.newInstance(), o.newInstance());
////                beanDefinition.setInstance(declaringClass.newInstance());
////                beanDefinitionMap.put(name, beanDefinition);
//            } catch (IllegalAccessException | InstantiationException e) {
//                e.printStackTrace();
//            }
//        }
//        // reflections.getMethodParamNames(Autowired.class)
//
//
//
//    }


//    static {
//        // 任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
//        // 加载xml
//        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
//        // 解析xml
//        SAXReader saxReader = new SAXReader();
//        try {
//            Document document = saxReader.read(resourceAsStream);
//            Element rootElement = document.getRootElement();
//            List<Element> beanList = rootElement.selectNodes("//bean");
//            for (int i = 0; i < beanList.size(); i++) {
//                Element element =  beanList.get(i);
//                // 处理每个bean元素，获取到该元素的id 和 class 属性
//                String id = element.attributeValue("id");        // accountDao
//                String clazz = element.attributeValue("class");  // com.lagou.edu.dao.impl.JdbcAccountDaoImpl
//                // 通过反射技术实例化对象
//                Class<?> aClass = Class.forName(clazz);
//                Object o = aClass.newInstance();  // 实例化之后的对象
//
//                // 存储到map中待用
//                map.put(id,o);
//
//            }
//
//            // 实例化完成之后维护对象的依赖关系，检查哪些对象需要传值进入，根据它的配置，我们传入相应的值
//            // 有property子元素的bean就有传值需求
//            List<Element> propertyList = rootElement.selectNodes("//property");
//            // 解析property，获取父元素
//            for (int i = 0; i < propertyList.size(); i++) {
//                Element element =  propertyList.get(i);   //<property name="AccountDao" ref="accountDao"></property>
//                String name = element.attributeValue("name");
//                String ref = element.attributeValue("ref");
//
//                // 找到当前需要被处理依赖关系的bean
//                Element parent = element.getParent();
//
//                // 调用父元素对象的反射功能
//                String parentId = parent.attributeValue("id");
//                Object parentObject = map.get(parentId);
//                // 遍历父对象中的所有方法，找到"set" + name
//                Method[] methods = parentObject.getClass().getMethods();
//                for (int j = 0; j < methods.length; j++) {
//                    Method method = methods[j];
//                    if(method.getName().equalsIgnoreCase("set" + name)) {  // 该方法就是 setAccountDao(AccountDao accountDao)
//                        method.invoke(parentObject,map.get(ref));
//                    }
//                }
//
//                // 把处理之后的parentObject重新放到map中
//                map.put(parentId,parentObject);
//
//            }
//
//
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//
//    }


    // 任务二：对外提供获取实例对象的接口（根据id获取）
    public static  Object getBean(String id) {
        return applicationContext.getBean(id);
    }

}
