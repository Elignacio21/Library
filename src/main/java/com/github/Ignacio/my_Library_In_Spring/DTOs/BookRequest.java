package com.github.Ignacio.my_Library_In_Spring.DTOs;

import com.github.Ignacio.my_Library_In_Spring.Entity.Author;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class BookRequest {
    @Positive
    private Long id ;

    @NotBlank
    private String title;

    @Valid
    private Author author;

    @NotBlank
    private String gender;

    @Positive
    private Integer yearOfPublication;

    @NotBlank
    private String editorial;

    public BookRequest(){};

    public BookRequest(Long id, String title, Author author, String gender, Integer yearOfPublication, String editorial) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.gender = gender;
        this.yearOfPublication = yearOfPublication;
        this.editorial = editorial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(Integer yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
}
