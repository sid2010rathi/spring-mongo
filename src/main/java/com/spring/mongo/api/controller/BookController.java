package com.spring.mongo.api.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.spring.mongo.api.model.Book;
import com.spring.mongo.api.repository.BookRepository;
import com.spring.mongo.api.service.SequenceGenerator;

@RestController
@RequestMapping(value = "/api")
public class BookController {

	@Autowired private BookRepository bookRepository;
	@Autowired private SequenceGenerator sequenceGenerator;
	@Autowired private GridFsTemplate gridFsTemplate;
    @Autowired private GridFsOperations gridFsOperations;
    @Autowired private GridFSBucket gridFSBucket;
    
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
	
	@GetMapping("/file/upload")
	public String uploadFile() {
		DBObject dbObject = new BasicDBObject();
		try {
			File file = new File("C:/Users/Krutagn/Desktop/5d26069178823_thumb900.jpg");
			FileInputStream fileInputStream = new FileInputStream(file);
			String fileName = file.getName();
			gridFsTemplate.store(fileInputStream, fileName, "image", dbObject);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "File Uploaded";
	}
	
	@GetMapping(value = "/file/download/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] downLoadFile(@PathVariable(value = "filename") String filename) {
		if (filename == null) {
            return null;
        }
		//Download file
		GridFSFindIterable result = gridFsOperations.find(Query.query(GridFsCriteria.whereFilename().is(filename)));
        GridFSFile gridFSFile = result.first();
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        
        //Stream object
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        
		try {
			return IOUtils.toByteArray(gridFsResource.getInputStream());
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
