//package com.context.action;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.context.SpringContextHolder;
//import com.context.ThreadContextHolder;
//import com.shiroDemo.service.UserServiceImpl;
//
//@Controller
//@RequestMapping("contextHolderTest")
//public class ContextHolderTest {
//    @Autowired
//    private UserServiceImpl userService;
//
//    @RequestMapping("getBean")
//    @ResponseBody
//    public String getBean(){
//        UserServiceImpl userService2=SpringContextHolder.getBean(UserServiceImpl.class);
//        System.out.println(userService.getString());
//        System.out.println(userService2.getString());
//
//
//        System.out.println( ThreadContextHolder.getSession().getAttribute("111"));
//        return "success";
//    }
//}
