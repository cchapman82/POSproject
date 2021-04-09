package edu.depaul.cdm.se.POSproject;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmployeeRepository extends MongoRepository<Employee,Integer> {
	
    
}
