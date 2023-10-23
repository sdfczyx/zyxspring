package com.zyx.service;

import com.zyx.spring.Autowired;
import com.zyx.spring.BeanNameAware;
import com.zyx.spring.Compont;
import com.zyx.spring.InitillizingBean;

@Compont
public class UserService implements BeanNameAware, InitillizingBean{
    
    @Autowired
    private OrderService   orderService;
    
    private String beanName;
    private String initVar;
    
    public void test(){
        System.out.println(orderService);
    }

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    @Override
    public void afterPropertiesSet() {
        initVar = "OK";
        System.out.println("UserService.afterPropertiesSet()");
        
    }
    
    
}
