package com.antiz.journalApp.controller;

import com.antiz.journalApp.entity.User;
import com.antiz.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    public PublicController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping("/create-user")
    public void  createUser(@RequestBody User user) {
        userService.saveNewUser(user);
    }


}
