package com.github.Ignacio.my_Library_In_Spring.Repository;

import com.github.Ignacio.my_Library_In_Spring.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RepositoryBook extends JpaRepository<Book,Long> {
    Optional<Book> findByTitle(String name);
}
