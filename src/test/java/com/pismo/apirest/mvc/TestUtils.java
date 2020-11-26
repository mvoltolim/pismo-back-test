package com.pismo.apirest.mvc;

import com.pismo.apirest.mvc.dto.AccountDto;
import com.pismo.apirest.mvc.dto.TransactionDto;
import com.pismo.apirest.mvc.enums.OperationType;
import com.pismo.apirest.mvc.model.Account;
import com.pismo.apirest.mvc.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.NonNull;
import lombok.SneakyThrows;

import static java.util.Objects.nonNull;

public abstract class TestUtils {

	public static Account getAccount(String documentNumber, BigDecimal limit) {
		var entity = new Account();
		entity.setDocumentNumber(documentNumber);
		entity.setAvailableCreditLimit(limit);
		return entity;
	}

	public static AccountDto getAccountDto(Long id, String documentNumber) {
		var dto = new AccountDto();
		dto.setId(id);
		dto.setDocumentNumber(documentNumber);
		return dto;
	}

	public static TransactionDto getTransactionDto(Long id, @NonNull Long accountId) {
		return getTransactionDto(id, accountId, null, null);
	}

	public static TransactionDto getTransactionDto(Long id, @NonNull Long accountId, OperationType operationType, String amount) {
		var dto = new TransactionDto();
		dto.setId(id);
		dto.setAccountId(accountId);
		dto.setOperationType(operationType);
		dto.setAmount(nonNull(amount) ? new BigDecimal(amount) : BigDecimal.ZERO);
		return dto;
	}

	@SneakyThrows
	public static Transaction getTransaction(OperationType value, String amount) {
		var entity = new Transaction();
		entity.setOperationType(value);
		entity.setAmount(new BigDecimal(amount));
		Thread.sleep(1);
		entity.setEventDate(LocalDateTime.now());
		entity.prePersist();
		return entity;
	}

}