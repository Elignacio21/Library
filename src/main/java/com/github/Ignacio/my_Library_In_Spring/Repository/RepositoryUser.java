package com.github.Ignacio.my_Library_In_Spring.Repository;

import com.github.Ignacio.my_Library_In_Spring.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryUser extends JpaRepository<User,Long> {
}
