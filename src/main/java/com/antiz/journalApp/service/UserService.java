package com.antiz.journalApp.service;


import com.antiz.journalApp.entity.User;
import com.antiz.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Component
public class UserService {
    // business logic here
    //create e entry in MongoDB

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public void saveUser(User user) {

        userRepository.save(user);

    }

    public boolean saveNewUser(User user) {
        try {

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("ADMIN", "USER"));
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByID(ObjectId myId) {
        return userRepository.findById(myId);
    }

    public void deleteByID(ObjectId myId) {
        userRepository.deleteById(myId);
    }

    public void deleteByUserName(String userName) {
        userRepository.deleteByUserName(userName);
    }

    public User findByname(String username) {
        return userRepository.findByUserName(username);
    }
}


