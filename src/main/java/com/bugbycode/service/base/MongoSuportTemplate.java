package com.bugbycode.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoSuportTemplate {
	
	@Autowired
	protected MongoTemplate mongoTemplate;
}
