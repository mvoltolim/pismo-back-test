package com.pismo.apirest.mvc.controller;

import com.pismo.apirest.mvc.controller.support.CrudController;
import com.pismo.apirest.mvc.dto.TransactionDto;
import com.pismo.apirest.mvc.service.TransactionService;
import com.pismo.apirest.mvc.util.Constants;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(Constants.API_V_1 + "/transactions")
@RestController
public class TransactionController extends CrudController<TransactionService, Long, TransactionDto> {

	public TransactionController(TransactionService service) {
		super(service);
	}

}