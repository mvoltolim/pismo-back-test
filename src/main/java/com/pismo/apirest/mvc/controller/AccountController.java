package com.pismo.apirest.mvc.controller;

import com.pismo.apirest.mvc.controller.support.CrudController;
import com.pismo.apirest.mvc.dto.AccountDto;
import com.pismo.apirest.mvc.service.AccountService;
import com.pismo.apirest.mvc.util.Constants;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(Constants.API_V_1 + "/accounts")
@RestController
public class AccountController extends CrudController<AccountService, Long, AccountDto> {

	public AccountController(AccountService service) {
		super(service);
	}

}