package com.zyx.service;

import com.zyx.spring.Autowired;
import com.zyx.spring.BeanNameAware;
import com.zyx.spring.Compont;

@Compont
public class UserService implements BeanNameAware{
    
    @Autowired
    private OrderService   orderService;
    
    private String beanName;
    
    public void test(){
        System.out.println(orderService);
    }

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }
    
    
}
