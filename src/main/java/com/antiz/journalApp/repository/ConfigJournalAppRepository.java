package com.antiz.journalApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository<ConfigJournalAppEntity> extends MongoRepository<ConfigJournalAppEntity, ObjectId> {

}