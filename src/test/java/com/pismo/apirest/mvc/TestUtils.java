package com.pismo.apirest.mvc;

import com.pismo.apirest.mvc.dto.AccountDto;
import com.pismo.apirest.mvc.dto.TransactionDto;
import com.pismo.apirest.mvc.enums.OperationType;
import com.pismo.apirest.mvc.model.Transaction;

import java.math.BigDecimal;

public abstract class TestUtils {

	public static AccountDto getAccountDto(Long id, String documentNumber) {
		var dto = new AccountDto();
		dto.setId(id);
		dto.setDocumentNumber(documentNumber);
		return dto;
	}

	public static TransactionDto getTransactionDto(Long id) {
		return getTransactionDto(id, null, null, null);
	}

	public static TransactionDto getTransactionDto(Long id, Long accountId, OperationType operationType, BigDecimal amount) {
		var dto = new TransactionDto();
		dto.setId(id);
		dto.setAccountId(accountId);
		dto.setOperationType(operationType);
		dto.setAmount(amount);
		return dto;
	}

	public static Transaction getTransaction(OperationType value) {
		var entity = new Transaction();
		entity.setOperationType(value);
		entity.setAmount(new BigDecimal("123.45"));
		return entity;
	}

}