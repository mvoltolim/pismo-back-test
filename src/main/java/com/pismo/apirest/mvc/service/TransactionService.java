package com.pismo.apirest.mvc.service;

import com.pismo.apirest.mvc.dto.TransactionDto;
import com.pismo.apirest.mvc.model.Transaction;
import com.pismo.apirest.mvc.repository.TransactionRepository;
import com.pismo.apirest.mvc.service.support.CrudServiceImpl;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionService extends CrudServiceImpl<TransactionRepository, Transaction, Long, TransactionDto> {

	public TransactionService(TransactionRepository repository, ModelMapper modelMapper) {
		super(repository, Transaction.class, TransactionDto.class, modelMapper);
	}

}