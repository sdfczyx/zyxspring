package com.zyx.service;

import com.zyx.spring.BeanPostProcessor;
import com.zyx.spring.Compont;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


@Compont
public class ZyxBeanPostProcessor implements BeanPostProcessor{

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (beanName.equals("userService")) {
            System.out.println("userService bean postProcessBeforeInitialization");
            Object proxyObject = Proxy.newProxyInstance(ZyxBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(),
            new InvocationHandler(){
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    // 增强方法
                    System.out.println("切面逻辑....");
                    return method.invoke(bean, args);
                }
            });
            return proxyObject;
            
        }
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (beanName.equals("userService")) {
            System.out.println("userService bean postProcessAfterInitialization");
        }
        return bean;
    }

}
