package com.github.Ignacio.my_Library_In_Spring.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class BookRequest {

    @NotBlank
    private String title;

    @Valid
    private Long authorId;

    @NotBlank
    private String gender;

    @Positive
    private Integer yearOfPublication;

    @NotBlank
    private String editorial;

    public BookRequest(){};

    public BookRequest(String title,Long authorId, String gender, Integer yearOfPublication, String editorial) {
        this.title = title;
        this.authorId = authorId;
        this.gender = gender;
        this.yearOfPublication = yearOfPublication;
        this.editorial = editorial;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
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
