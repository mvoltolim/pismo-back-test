package com.pismo.apirest.mvc.service;

import com.pismo.apirest.mvc.dto.AccountDto;
import com.pismo.apirest.mvc.enums.OperationType;
import com.pismo.apirest.mvc.exception.CustomRuntimeException;
import com.pismo.apirest.mvc.exception.ValidationMsg;
import com.pismo.apirest.mvc.model.Account;
import com.pismo.apirest.mvc.repository.AccountRepository;
import com.pismo.apirest.mvc.service.support.CrudServiceImpl;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService extends CrudServiceImpl<AccountRepository, Account, Long, AccountDto> {

	public AccountService(AccountRepository repository, ModelMapper modelMapper) {
		super(repository, Account.class, AccountDto.class, modelMapper);
	}

	public Account updateAvailableCreditLimit(@NonNull Long idAccount, @NonNull OperationType operationType, @NonNull BigDecimal amount) {
		var atomic = new AtomicReference<Account>();

		repository.findById(idAccount).ifPresent(account -> {
			if (operationType.isNegative() && account.getAvailableCreditLimit().subtract(amount).compareTo(BigDecimal.ZERO) <= 0) {
				throw new CustomRuntimeException(ValidationMsg.ACCOUNT_WITHOUT_LIMIT);
			}

			var newLimit = operationType.isNegative() ? account.getAvailableCreditLimit().subtract(amount) :
				account.getAvailableCreditLimit().add(amount);

			account.setAvailableCreditLimit(newLimit);
			atomic.set(repository.saveAndFlush(account));
		});

		return atomic.get();
	}

}