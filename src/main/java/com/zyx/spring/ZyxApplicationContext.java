package com.zyx.spring;

import java.beans.Introspector;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZyxApplicationContext {
    private Class<?> configClass;
    
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    
    
    public ZyxApplicationContext(Class<?> configClass){
        this.configClass = configClass;
        
        //扫描
        if (this.configClass.isAnnotationPresent(CompontScan.class)) {
            CompontScan compontScanAnnotation = (CompontScan)configClass.getAnnotation(CompontScan.class);
            String path = compontScanAnnotation.value();  //扫描路径 com.zyx.service
            path = path.replace(".", "/");
            
            ClassLoader classLoader = ZyxApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());
            
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                
                for (var f : listFiles) {
                    String fileName = f.getAbsolutePath();
                    if (fileName.endsWith(".class")) {
                        String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class")).replace("\\", ".");
                        // 判断是否Bean
                        try {
                            Class<?> clazz = classLoader.loadClass(className);
                            if (clazz.isAnnotationPresent(Compont.class)) {
                                
                                String beanName = clazz.getAnnotation(Compont.class).value();
                                if("".equals(beanName)){
                                    beanName =  Introspector.decapitalize(clazz.getSimpleName());
                                }
                                // 是一个Bean, 生成 BeanDefinition 对象
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    Scope scopyAnnotation = (Scope)clazz.getAnnotation(Scope.class);
                                    beanDefinition.setScope(scopyAnnotation.value());
                                }else{
                                    beanDefinition.setScope("singleton");
                                }
                                
                                beanDefinitionMap.put(beanName, beanDefinition);
                                
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            
            // 创建单例Bean
            for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
                if ("singleton".equals(entry.getValue().getScope())) {
                    singletonObjects.put(entry.getKey(), createBean(entry.getKey(), entry.getValue()));
                }
            }

            
            
        }
    }
    
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        // 1. 获取Bean的类型
        Class<?> clazz = beanDefinition.getType();
        
        // 2. 创建Bean对象
        Object instance = null;
        try {
            instance = clazz.getConstructor().newInstance();
            
            // 依赖注入
            for (var f : clazz.getDeclaredFields()) {
                if(f.isAnnotationPresent(Autowired.class)){
                    
                    // 设置Bean对象的值
                    f.setAccessible(true);
                    f.set(instance, getBean(f.getName()));
                }
            }
            
            // Aware回调
            if (instance instanceof BeanNameAware){
                ((BeanNameAware) instance).setBeanName(beanName);
            }
            
            // 初始化
            if (instance instanceof InitillizingBean){
                ((InitillizingBean) instance).afterPropertiesSet();
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return instance;
    }
    
    public Object getBean(String beanName){

        Object bean = null;
        
        // 1. 获取配置类
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        
        if (beanDefinition == null) {
            throw new RuntimeException("beanName = " + beanName + " 没有找到");
        }else{
            String scope = beanDefinition.getScope();
            if ("singleton".equals(scope)) {
                //单例
                bean = singletonObjects.get(beanName);
                // 一个Bean里面使用 Autowired 注入另外一个Bean的情况下, 执行下面的代码
                if (bean == null) {
                    bean = createBean(beanName, beanDefinition);
                    singletonObjects.put(beanName, bean);
                }
            }else{
                //多例
                bean = createBean(beanName, beanDefinition);
            }
            
        }
        
        return bean;
    }
    
}