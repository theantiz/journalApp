package com.antiz.journalApp.controller;

import com.antiz.journalApp.entity.User;
import com.antiz.journalApp.repository.UserRepository;
import com.antiz.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saved = null;
        userService.saveNewUser(user);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) // use username to find the User
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByname(userName); //work on   PUT Method which is not working
        if (userInDb != null) {
            user.setPassword(userInDb.getPassword());
            user.setUserName(userInDb.getUserName());
            userService.saveNewUser(user);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById() // use username to find the User
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
