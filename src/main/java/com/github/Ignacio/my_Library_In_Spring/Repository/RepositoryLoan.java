package com.github.Ignacio.my_Library_In_Spring.Repository;

import com.github.Ignacio.my_Library_In_Spring.Entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryLoan extends JpaRepository<Loan,Long> {
}
