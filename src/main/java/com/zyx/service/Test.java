package com.zyx.service;

import com.zyx.spring.ZyxApplicationContext;

public class Test {
    public static void main(String[] args) {
       var applicationContext = new ZyxApplicationContext(AppConfig.class);
       
       UserInterface userService = (UserInterface)applicationContext.getBean("userService");
       userService.test();
        
    }
}
