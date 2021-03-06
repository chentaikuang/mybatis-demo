package com.example.demo.controller;

import com.example.demo.entity.UserInfo;
import com.example.demo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    UserInfoService userInfoService;

    //http://localhost:8888/test/getById?id=1
    @RequestMapping("/getById")
    public UserInfo getById(int id) {
        UserInfo user = userInfoService.getById(id);
        if (user == null) {
            user = new UserInfo();
            user.setName("xiao chen");
            user.setId(id);
            user.setAge(8);
            user.setRemark("no found by id["+id+"]");
        }
        return user;
    }

    @RequestMapping("/changeAgeById")
    public UserInfo changeAgeById(int id,int age) throws Exception {
        UserInfo user = userInfoService.getById(id);
        if (user == null){
            throw new Exception("no found user by id,id : " + id);
        }
        user.setAge(age);
        return userInfoService.changeAgeById(user);
    }
}