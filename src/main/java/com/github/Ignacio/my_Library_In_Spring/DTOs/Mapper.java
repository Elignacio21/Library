package com.github.Ignacio.my_Library_In_Spring.DTOs;

import com.github.Ignacio.my_Library_In_Spring.Entity.Author;
import com.github.Ignacio.my_Library_In_Spring.Entity.Book;
import com.github.Ignacio.my_Library_In_Spring.Entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mapper {

    public Book toEntityBook(BookRequest request){
        return new Book(request.getTitle()
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

    public Author toEntityAuthor(AuthorRequest request){
        return new Author(request.getName(),request.getNationality(), request.getBiography());
    }

    public AuthorResponse toAuthorResponse(Author request){
        return  new AuthorResponse(request.getId(), request.getName(), request.getNationality(), request.getBiography());
    }

    public User toUserEntity(UserRequest request){
        return new User(
                request.getUsername(),
                request.getPassword(),
                null, // email no viene en el request
                new ArrayList<>() // roles vacíos o con un rol por defecto
        );
    }

    public UserResponse toUserResponse(User user, String token){
        return new UserResponse(
                user.getName(),
                token,
                user.getRoles()
        );
    }

}
