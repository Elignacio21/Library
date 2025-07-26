package com.github.Ignacio.my_Library_In_Spring.Repository;

import com.github.Ignacio.my_Library_In_Spring.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryBook extends JpaRepository<Book,Long> {



}
