package com.spring.mongo.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

@Configuration
public class MongoConfig{

	@Value("${spring.data.mongodb.database}")
	private String databaseName;
	
	@Bean
	public GridFSBucket getGridFSBucket(MongoClient mongoClient) {
		MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
		GridFSBucket bucket = GridFSBuckets.create(mongoDatabase);
		return bucket;
	}

}
