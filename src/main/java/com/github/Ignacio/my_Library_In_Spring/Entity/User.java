package com.github.Ignacio.my_Library_In_Spring.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name not valid")
    private String name;

    @NotBlank(message = "password not valid")
    @Size(min = 5,message = "password size not valid")
    private String password;

    @NotBlank(message = "email not valid")
    @Email(message = "email structure not valid")
    private String email;

    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roles;

    public User(){}

    public User(Long id, String name, String password, String email, List<String> roles) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public User(String name, String password, String email, List<String> roles) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
