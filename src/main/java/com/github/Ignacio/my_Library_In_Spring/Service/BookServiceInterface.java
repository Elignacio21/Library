package com.github.Ignacio.my_Library_In_Spring.Service;

import com.github.Ignacio.my_Library_In_Spring.DTOs.BookRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.BookResponse;

import java.util.Map;
import java.util.Objects;

public interface BookServiceInterface {
    BookResponse getBookByName(String name);
    BookResponse getBookById(Long id);

    BookResponse postBook(BookRequest input);

    boolean deleteBookById(Long id);

    BookResponse putBookById(Long id,BookRequest update);
    BookResponse putBookByName(String name,BookRequest update);

    BookResponse patchBookById(Long id, Map<String, Object> input);
    BookResponse patchBookByName(String name, Map<String, Object> input);
}
