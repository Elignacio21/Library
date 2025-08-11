package com.github.Ignacio.my_Library_In_Spring.DTOs;

public class BookSummary {
    private Long id;
    private String title;
    private Integer yearOfPublication;

    public BookSummary(Long id,String title,Integer yearOfPublication){
        this.id = id;
        this.title = title;
        this.yearOfPublication = yearOfPublication;
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

    public Integer getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(Integer yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }
}
