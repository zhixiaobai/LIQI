package com.xb.controller;

import com.xb.entity.User;
import com.xb.mapper.UserMapper;
import com.xb.service.MainService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description
 */
@RestController
public class MainController {
    @Resource
    private UserMapper userMapper;

    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }


    @GetMapping("/a")
    public User getL(User user) {
        user = new User();
        user.setId(1);
        user.setDmName("xiaobai");
        user.setPassword("222");
        user.setUsername("qifei");
        return mainService.getObj(user);
    }

    @GetMapping("/list")
    public Object getA(@RequestParam("id") int id) {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setUsername("111");
        user.setPassword("222");
        users.add(user);
        user = new User();
        user.setUsername("zxbUser");
        user.setPassword("1122");
        users.add(user);
        user = new User();
        user.setUsername("777");
        user.setPassword("222999");
        users.add(user);
//        System.out.println(users);
        return userMapper.selectList(users, id);
//        userMapper.selectList("new User()");
//        userMapper.selectList(new User());
    }

    @GetMapping("/c")
    public void getC() {
        User user = new User();
        user.setUsername("456");
        user.setPassword("222");
        user.setDmName("111aaa");
        int a = userMapper.insertData(user);
        System.out.println(a);
//        userMapper.selectList("new User()");
//        userMapper.selectList(new User());
    }

    @GetMapping("/cc")
    public Object getCc() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setUsername("412356");
        user.setPassword("222123");
        user.setDmName("111aa22a");
        users.add(user);
        user = new User();
        user.setUsername("33311");
        user.setPassword("221111");
        user.setDmName("787878111");
        users.add(user);
        user = new User();
        user.setUsername("asdas111");
        user.setPassword("aaxxx11");
        user.setDmName("vvvvv111");
        users.add(user);
        user = new User();
        user.setUsername("asdas111");
        user.setPassword("aaxxx11");
        user.setDmName("vvvvv111");
        users.add(user);
        return userMapper.insertData2(users);
//        userMapper.selectList("new User()");
//        userMapper.selectList(new User());
    }

    @GetMapping("/d")
    public void getD() {
        User user = new User();
        user.setUsername("zxbUser");
        user.setPassword("password");
        user.setDmName("123123");
        user.setId(3);
        int i = userMapper.updateData(user);
        System.out.println(i);
//        userMapper.selectList("new User()");
//        userMapper.selectList(new User());
    }

    @GetMapping("/e")
    public void getE() {
        int i = userMapper.deleteData(9);
        System.out.println(i);
//        userMapper.selectList("new User()");
//        userMapper.selectList(new User());
    }
}
