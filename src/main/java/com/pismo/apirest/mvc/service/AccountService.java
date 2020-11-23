package com.pismo.apirest.mvc.service;

import com.pismo.apirest.mvc.dto.AccountDto;
import com.pismo.apirest.mvc.model.Account;
import com.pismo.apirest.mvc.repository.AccountRepository;
import com.pismo.apirest.mvc.service.support.CrudServiceImpl;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService extends CrudServiceImpl<AccountRepository, Account, Long, AccountDto> {

	public AccountService(AccountRepository repository, ModelMapper modelMapper) {
		super(repository, Account.class, AccountDto.class, modelMapper);
	}

}