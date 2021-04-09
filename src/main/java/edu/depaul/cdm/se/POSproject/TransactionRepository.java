package edu.depaul.cdm.se.POSproject;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction,Integer> {
    
}
