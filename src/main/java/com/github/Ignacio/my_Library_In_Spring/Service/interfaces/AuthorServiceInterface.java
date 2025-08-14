package com.github.Ignacio.my_Library_In_Spring.Service.interfaces;

import com.github.Ignacio.my_Library_In_Spring.DTOs.AuthorRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.AuthorResponse;

import java.util.List;
import java.util.Map;

public interface AuthorServiceInterface {
    //query method
    List<AuthorResponse> getAllAuthors();
    AuthorResponse getAuthorById(Long id);
    AuthorResponse getAuthorByName(String name);

    //add authors
    AuthorResponse post(AuthorRequest request);

    //edit
    AuthorResponse putAuthorById(Long id,AuthorRequest request);
    AuthorResponse putAuthorByName(String name,AuthorRequest request);

    AuthorResponse patchAuthorById(Long id, Map<String,Object> updates);
    AuthorResponse patchAuthorByName(String name,Map<String,Object> updates);


    boolean deleteAuthorById(Long id);
}
