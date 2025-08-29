package com.antiz.journalApp.service;

import com.antiz.journalApp.entity.JournalEntry;
import com.antiz.journalApp.entity.User;
import com.antiz.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    // business logic here
    //create e entry in MongoDB

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    @Transactional // meaning all the loc inside this method treats this as single operation  will make container & if any error data will get rollback
    public void saveEntry(JournalEntry journalEntry, String userName) { //created a transactional method
      try {
          JournalEntry saved = journalEntryRepository.save(journalEntry);
          User user = userService.findByname(userName);
          user.getJournalEntries().add(saved);
          userService.saveEntry(user);
      }
      catch (Exception e) {
          System.out.println(e);
          throw new RuntimeException("error while saving journal entry");
      }
    } // here we have achieved the atomicity meaning if one failed then all failed.
    // if two user call this api then a box will be created for both user corresponding. Both operation are isolated
    // now to handle this type of transaction we make manager to make, find this method, maange the method
    //manager is platformTransactionManger (Interface)  -> MongoTransactionManager (this instance will get return &&  behind the scene session && helps to establish the connection with database)
    // if everything good manager will "commit" else "rollback"


    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findByID(ObjectId myId) {
        return journalEntryRepository.findById(myId);
    }

    public void deleteByID(ObjectId myId, String userName) {
        User user = userService.findByname(userName);
        user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
        userService.saveEntry(user);
        journalEntryRepository.deleteById(myId);
    }

}