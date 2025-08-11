package com.github.Ignacio.my_Library_In_Spring.DTOs;

import jakarta.validation.constraints.NotBlank;

public class AuthorRequest {

    @NotBlank(message = "id not valid")
    private Long id;

    @NotBlank(message = "name not valid")
    private String name;

    @NotBlank(message = "nationality not valid")
    private String nationality;

    @NotBlank(message = "biography not valid")
    private String biography;

    public AuthorRequest(String name, String nationality, String biography) {
        this.name = name;
        this.nationality = nationality;
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
