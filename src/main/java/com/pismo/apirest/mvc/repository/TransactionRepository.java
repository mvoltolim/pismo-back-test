package com.pismo.apirest.mvc.repository;

import com.pismo.apirest.mvc.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findAllByAccount_IdAndBalanceIsLessThan(Long accountId, BigDecimal amount);

}