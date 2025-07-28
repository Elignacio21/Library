package com.github.Ignacio.my_Library_In_Spring.DTOs;

import com.github.Ignacio.my_Library_In_Spring.Entity.Book;

public class Mapper {

    public Book toEntity(BookRequest request){
        return new Book(request.getId(),request.getAuthor());
    }
}
