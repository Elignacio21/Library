package com.github.Ignacio.my_Library_In_Spring.DTOs;

import com.github.Ignacio.my_Library_In_Spring.Entity.Book;

public class Mapper {

    public Book toEntityBook(BookRequest request){
        return new Book(request.getId()
                ,request.getTitle()
                ,request.getAuthor()
                ,request.getEditorial()
                ,request.getGender()
                ,true
                ,request.getYearOfPublication()
        );
    }

    public BookResponse toBookResponse(Book request){
        return new BookResponse(request.getGender()
                ,request.getAuthor()
                ,request.getTitle()
                ,request.getId()
        );
    }


}
