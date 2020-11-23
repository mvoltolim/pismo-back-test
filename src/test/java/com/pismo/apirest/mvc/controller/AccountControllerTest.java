package com.pismo.apirest.mvc.controller;

import com.pismo.apirest.mvc.dto.AccountDto;
import com.pismo.apirest.mvc.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.pismo.apirest.mvc.TestUtils.getAccountDto;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(AccountController.class)
class AccountControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AccountService service;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void create() throws Exception {
		given(service.create(any(AccountDto.class))).willAnswer(invocation -> {
			var argument = (AccountDto) invocation.getArgument(0);
			argument.setId(1L);
			return argument;
		});

		mvc.perform(post("/accounts").contentType(APPLICATION_JSON).content("{\"document_number\":\"12345678900\"}"))
		   .andExpect(status().isCreated())
		   .andExpect(jsonPath("$", aMapWithSize(1)))
		   .andExpect(jsonPath("$.id", is(1)));

		verify(service).create(any(AccountDto.class));
		verifyNoMoreInteractions(service);
	}

	@Test
	void create_WithId() throws Exception {
		mvc.perform(post("/accounts").contentType(APPLICATION_JSON).content("{\"id\":1,\"document_number\":\"12345678900\"}"))
		   .andExpect(status().isBadRequest())
		   .andExpect(jsonPath("$.errorMsg", is("accountDto -> [id: deve ser nulo]")));

		verifyNoInteractions(service);
	}

	@Test
	void create_WithoutRequiredFields() throws Exception {
		mvc.perform(post("/accounts").contentType(APPLICATION_JSON).content("{}"))
		   .andExpect(status().isBadRequest())
		   .andExpect(jsonPath("$.errorMsg", is("accountDto -> [documentNumber: não deve estar vazio]")));

		verifyNoInteractions(service);
	}

	@Test
	void update() throws Exception {
		given(service.update(any(AccountDto.class))).willAnswer(invocation -> invocation.getArgument(0));

		mvc.perform(put("/accounts").contentType(APPLICATION_JSON).content("{\"id\":1,\"document_number\":\"12345678900\"}"))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$", aMapWithSize(2)))
		   .andExpect(jsonPath("$.id", is(1)))
		   .andExpect(jsonPath("$.document_number", is("12345678900")));

		verify(service).update(any(AccountDto.class));
		verifyNoMoreInteractions(service);
	}

	@Test
	void update_WithoutRequiredFields() throws Exception {
		mvc.perform(put("/accounts").contentType(APPLICATION_JSON).content("{}"))
		   .andExpect(status().isBadRequest())
		   .andExpect(jsonPath("$.errorMsg", is("accountDto -> [documentNumber: não deve estar vazio, id: não deve ser nulo]")));

		verifyNoInteractions(service);
	}

	@Test
	void getById() throws Exception {
		given(service.findById(anyLong())).willReturn(Optional.of(getAccountDto(1L, "12345678900")));

		mvc.perform(get("/accounts/1"))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$", aMapWithSize(2)))
		   .andExpect(jsonPath("$.id", is(1)))
		   .andExpect(jsonPath("$.document_number", is("12345678900")));

		verify(service).findById(anyLong());
		verifyNoMoreInteractions(service);
	}

	@Test
	void getById_NoExists() throws Exception {
		mvc.perform(get("/accounts/1"))
		   .andExpect(status().isNotFound());

		verify(service).findById(anyLong());
		verifyNoMoreInteractions(service);
	}


	@Test
	void deleteById() throws Exception {
		given(service.deleteById(anyLong())).willReturn(true);

		mvc.perform(delete("/accounts/1"))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$", is(true)));

		verify(service).deleteById(anyLong());
		verifyNoMoreInteractions(service);
	}

}