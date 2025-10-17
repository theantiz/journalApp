package com.antiz.journalApp.scheduler;

import com.antiz.journalApp.cache.AppCache;
import com.antiz.journalApp.entity.JournalEntry;
import com.antiz.journalApp.entity.User;
import com.antiz.journalApp.repository.UserRepositoryImpl;
import com.antiz.journalApp.service.EmailService;
import com.antiz.journalApp.service.JournalEntryService;
import com.antiz.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.awt.AppContext;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;
    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;
    @Autowired
    private AppCache appCache;

    // every day at 9:00 AM email will be sent to all users with journal entries for the last 7 days.
    @Scheduled(cron = "0 0 9 * * Mon")
    public void fetchUserandSendEmail() {
        List<User> users = userRepository.getUserForSA();
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<JournalEntry> filteredEntries = journalEntries.stream().filter(x -> x.getEntryDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).collect(Collectors.toList());
            String entry = String.join((CharSequence) " ", (CharSequence) filteredEntries);
            String sentiment = sentimentAnalysisService.getSentiment(entry);
            emailService.sendEmail(user.getEmail(), "Sentiment Analysis", "Your journal entries for the last 7 days are: " + entry + " and the sentiment is: " + sentiment + "");
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void clearCache() {
        appCache.init();
    }
}
