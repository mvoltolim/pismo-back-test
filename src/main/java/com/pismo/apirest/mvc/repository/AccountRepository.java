package com.pismo.apirest.mvc.repository;

import com.pismo.apirest.mvc.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {}