package com.github.Ignacio.my_Library_In_Spring.Controllers;

import com.github.Ignacio.my_Library_In_Spring.DTOs.AuthorRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.AuthorResponse;
import com.github.Ignacio.my_Library_In_Spring.Service.AuthorService;
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
@RequestMapping("/author")
public class AuthorController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    private AuthorService service;

    @GetMapping("/getAll")
    public ResponseEntity<List<AuthorResponse>> getAllAuthors() {
        logger.info("Request to get all authors");
        List<AuthorResponse> authors = service.getAllAuthors();

        if (authors.isEmpty()) {
            logger.info("No authors found, returning no content");
            return ResponseEntity.noContent().build();
        }

        logger.info("Returning {} authors", authors.size());
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable Long id) {
        logger.info("Request to get author by id: {}", id);
        AuthorResponse response = service.getAuthorById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<AuthorResponse> getAuthorByName(@PathVariable String name) {
        logger.info("Request to get author by name: {}", name);
        AuthorResponse response = service.getAuthorByName(name);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/post")
    public ResponseEntity<AuthorResponse> postAuthor(@RequestBody @Valid AuthorRequest input) {
        logger.info("Request to create new author");
        AuthorResponse response = service.post(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/putById/{id}")
    public ResponseEntity<AuthorResponse> putAuthorById(@PathVariable Long id,
                                                        @RequestBody @Valid AuthorRequest request) {
        logger.info("Request to update author by id: {}", id);
        AuthorResponse response = service.putAuthorById(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/putByName/{name}")
    public ResponseEntity<AuthorResponse> putAuthorByName(@PathVariable String name,
                                                          @RequestBody @Valid AuthorRequest request) {
        logger.info("Request to update author by name: {}", name);
        AuthorResponse response = service.putAuthorByName(name, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/patchById/{id}")
    public ResponseEntity<AuthorResponse> patchAuthorById(@PathVariable Long id,
                                                          @RequestBody Map<String, Object> updates) {
        logger.info("Request to patch author by id: {}", id);
        AuthorResponse response = service.patchAuthorById(id, updates);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/patchByName/{name}")
    public ResponseEntity<AuthorResponse> patchAuthorByName(@PathVariable String name,
                                                            @RequestBody Map<String, Object> updates) {
        logger.info("Request to patch author by name: {}", name);
        AuthorResponse response = service.patchAuthorByName(name, updates);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable Long id) {
        logger.info("Request to delete author by id: {}", id);
        service.deleteAuthorById(id);
        return ResponseEntity.noContent().build();
    }
}
