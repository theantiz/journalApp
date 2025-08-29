package com.antiz.journalApp.controller;

import com.antiz.journalApp.entity.User;
import com.antiz.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saved = userService.saveEntry(user);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }


    @PutMapping("/{userName}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String userName) // use username to find the User
    {
        User userInDb = userService.findByname(userName); //work on PUT Method which is not working
        if (userInDb != null) {
            user.setPassword(userInDb.getPassword());
            user.setUserName(userInDb.getUserName());
            userService.saveEntry(user);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
