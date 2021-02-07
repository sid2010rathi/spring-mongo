package com.spring.mongo.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.mongo.api.model.Book;
import com.spring.mongo.api.repository.BookRepository;
import com.spring.mongo.api.service.SequenceGenerator;

@RestController
@RequestMapping(value = "/api")
public class BookController {

	@Autowired private BookRepository bookRepository;
	@Autowired private SequenceGenerator sequenceGenerator;
	
	@PostMapping("/book")
	public Book addBook(@RequestBody Book book) {
		book.setId(sequenceGenerator.generateSequence(Book.SEQUENCE_NAME));
		return bookRepository.save(book);
	}
	
	@GetMapping("/book")
	public List<Book> getBooks() {
		return bookRepository.findAll();
	}
	
	@GetMapping("/book/{id}")
	public Optional<Book> getBook(@PathVariable(value = "id") Integer id) {
		return bookRepository.findById(id);
	}
	
	@DeleteMapping("/book/{id}")
	public String deleteBook(@PathVariable(value = "id") Integer id) {
		bookRepository.deleteById(id);
		return "Book is deleted";
	}
	
	@PutMapping("/book/{id}")
	public ResponseEntity<Book> updateBook(@PathVariable(value = "id") Integer id, @RequestBody Book book) {
		Optional<Book> books = bookRepository.findById(id);
		Book newBook = books.get();
		newBook.setBookName(book.getBookName());
		newBook.setAuthorName(book.getAuthorName());
		Book updatedBook = bookRepository.save(newBook);
		return ResponseEntity.ok(updatedBook);
	}
}
