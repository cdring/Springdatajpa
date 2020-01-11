package com.cdring.jpa.controller;

import com.cdring.jpa.jwt.JwtUserDetails;
import com.cdring.jpa.repository.User;
import com.cdring.jpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "login")
    public String login(String username, String password){
        return userService.login(username,password);
    }

    @PostMapping(value = "register")
    public User register(@RequestBody User user){
        return userService.register(user);
    }
}
