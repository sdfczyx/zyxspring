package com.zyx.service;

import com.zyx.spring.Autowired;
import com.zyx.spring.BeanNameAware;
import com.zyx.spring.Compont;
import com.zyx.spring.InitillizingBean;

@Compont
public class UserService implements UserInterface{
    
    @Autowired
    private OrderService   orderService;
    
    @Override
    public void test(){
        System.out.println(orderService);
    }

    
    
}
