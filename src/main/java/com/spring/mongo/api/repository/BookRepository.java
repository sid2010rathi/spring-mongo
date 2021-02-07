package com.spring.mongo.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.spring.mongo.api.model.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, Integer>{

}
