package com.github.Ignacio.my_Library_In_Spring.Entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    private Integer stock;

    @Valid
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Valid
    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    private LocalDateTime returnDate;

    private LocalDateTime loanDate;

    private boolean returned;

    public Loan(){}

    public Loan(Long id,Integer stock, Book book, User user, LocalDateTime returnDate, LocalDateTime loanDate, boolean returned) {
        this.id = id;
        this.stock = stock;
        this.book = book;
        this.user = user;
        this.returnDate = returnDate;
        this.loanDate = loanDate;
        this.returned = returned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public Boolean getReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
