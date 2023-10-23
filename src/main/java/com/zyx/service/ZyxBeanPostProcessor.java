package com.zyx.service;

import com.zyx.spring.BeanPostProcessor;
import com.zyx.spring.Compont;

@Compont
public class ZyxBeanPostProcessor implements BeanPostProcessor{

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (beanName.equals("userService")) {
            System.out.println("userService bean postProcessBeforeInitialization");
        }
        return null;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (beanName.equals("userService")) {
            System.out.println("userService bean postProcessAfterInitialization");
        }
        return null;
    }

}
