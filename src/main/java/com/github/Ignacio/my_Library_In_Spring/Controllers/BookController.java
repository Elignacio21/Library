package com.github.Ignacio.my_Library_In_Spring.Controllers;

import com.github.Ignacio.my_Library_In_Spring.DTOs.BookRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.BookResponse;
import com.github.Ignacio.my_Library_In_Spring.Service.BookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService service;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllBooks(){
        logger.info("Requesting all books");
        List<BookResponse> list = service.getAllBooks();
        if(list.isEmpty()){
            logger.warn("No books found in database");
            return ResponseEntity.noContent().build();
        }
        logger.info("Found {} books", list.size());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getBookById(@PathVariable int id){
        logger.info("Searching for book with ID: {}", id);
        BookResponse response = service.getBookById((long) id);
        logger.debug("Book found: {}", response.getTitle());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/post")
    public ResponseEntity<?> postBook(@RequestBody @Valid BookRequest input){
        logger.info("Creating new book: {}", input.getTitle());
        BookResponse response = service.postBook(input);
        logger.info("Book created successfully - ID: {}, Title: {}", response.getId(), response.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/patchById/{id}")
    public ResponseEntity<?> patchBook(@PathVariable int id, Map<String,Object> update){
        logger.info("Partially updating book ID: {} with fields: {}", id, update.keySet());
        BookResponse response = service.patchBookById((long)id,update);
        logger.info("Book ID: {} updated successfully", id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/patchByName/{name}")
    public ResponseEntity<?> patchBookName(@PathVariable String name, Map<String,Object> update){
        logger.info("Partially updating book by name: {} with fields: {}", name, update.keySet());
        BookResponse response = service.patchBookByName(name,update);
        logger.info("Book '{}' updated successfully", name);
        return ResponseEntity.ok(response);
    }

    @PutMapping("putById/{id}")
    public ResponseEntity<?> putBookId(@PathVariable int id, BookRequest request){
        logger.info("Fully updating book ID: {}", id);
        BookResponse update = service.putBookById((long)id,request);
        logger.info("Book ID: {} fully updated", id);
        return ResponseEntity.ok(update);
    }

    @PutMapping("putByName/{name}")
    public ResponseEntity<?> putBookName(@PathVariable String name, BookRequest request){
        logger.info("Fully updating book by name: {}", name);
        BookResponse update = service.putBookByName(name,request);
        logger.info("Book '{}' fully updated", name);
        return ResponseEntity.ok(update);
    }
}