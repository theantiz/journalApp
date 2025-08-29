package com.antiz.journalApp.controller;

import com.antiz.journalApp.entity.JournalEntry;
import com.antiz.journalApp.entity.User;
import com.antiz.journalApp.service.JournalEntryService;
import com.antiz.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    public Map<ObjectId, JournalEntry> journalEntries = new HashMap<>();


    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    // the method inside a controller class must be in public so that they can be accessed and invoked by the spring framerwork or extranal http request.
    @GetMapping("{userName}")//path will "/journal/abc"
    public ResponseEntity<?> getAll(@PathVariable String userName) {

        User byUserName = userService.findByname(userName);

        List<JournalEntry> all = journalEntryService.getAll();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("{userName}")
    //to get the data from JSON (raw) to use the "@requestbody", data from the request and turn it into a java object that i can use code.
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName) {  // new object/instance will get created
        try {
            myEntry.setEntryDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("id/{myId}") //two ways to send the data 1 by using path variable 2 request parameter
    public ResponseEntity<JournalEntry> getByID(@PathVariable ObjectId myId) { // "journal/id/1" can be retrive using Path variable
        Optional<JournalEntry> journalEntry = journalEntryService.findByID(myId);
        if (journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @DeleteMapping("id/{userName}/{myId}")
    public ResponseEntity<?> deleteByID(@PathVariable ObjectId myId, @PathVariable String userName) { // "journal/id/1" can be deleted using Path variable
        journalEntryService.deleteByID(myId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateById(@RequestBody ObjectId id, @PathVariable JournalEntry newEntry, @PathVariable String userName) {
        JournalEntry old = journalEntryService.findByID(id).orElse(null);
        if (old != null) {
            old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
            old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
