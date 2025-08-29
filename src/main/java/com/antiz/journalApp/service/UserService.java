package com.antiz.journalApp.service;


import com.antiz.journalApp.entity.User;
import com.antiz.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    // business logic here
    //create e entry in MongoDB

    @Autowired
    private UserRepository userRepository;


    public User saveEntry(User user) {
        userRepository.save(user);

        return user;
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

    public User findByname(String username) {
        return userRepository.findByUserName(username);
    }
}
