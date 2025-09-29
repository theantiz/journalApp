package com.antiz.journalApp.service;

import com.antiz.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTests {
    //writing multiple test cases for UserService

    @Autowired
    private UserRepository userRepository;

    @Test //sample test case
    public void testAdd() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testFindByUserName() {
        assertNotNull(userRepository.findByUserName("ram")); //make sure the username from the repo is not null or making assumption that userName repo is not null
    }

    @ParameterizedTest
    @CsvSource({
            "jay",
            "ram",
            "shyam"
    })
// Tests addition operation with multiple input combinations from CSV data
    public void testParameterized(String name) {
        assertNotNull(userRepository.findByUserName(name));
    }
}
