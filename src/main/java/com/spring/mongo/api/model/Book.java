package com.spring.mongo.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Books")
public class Book {

	@Transient public static final String SEQUENCE_NAME = "users_sequence";
	
	@Id
	private int id;
	
	@Indexed(unique = true)
	private String bookName;
	
	@Indexed(unique = true)
	private String authorName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Book(int id, String bookName, String authorName) {
		super();
		this.id = id;
		this.bookName = bookName;
		this.authorName = authorName;
	}
	
	public Book() {	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", bookName=" + bookName + ", authorName=" + authorName + "]";
	}
}
