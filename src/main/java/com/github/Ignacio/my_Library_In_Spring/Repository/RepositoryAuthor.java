package com.github.Ignacio.my_Library_In_Spring.Repository;

import com.github.Ignacio.my_Library_In_Spring.Entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryAuthor extends JpaRepository<Author,Long>{
    Optional<Author> findByName(String name);
}
