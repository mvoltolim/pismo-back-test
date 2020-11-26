package com.pismo.apirest.mvc.controller;

import com.pismo.apirest.mvc.dto.TransactionDto;
import com.pismo.apirest.mvc.enums.OperationType;
import com.pismo.apirest.mvc.service.TransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.pismo.apirest.mvc.TestUtils.getTransactionDto;
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
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TransactionService service;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void create() throws Exception {
		given(service.create(any(TransactionDto.class))).willAnswer(invocation -> {
			var argument = (TransactionDto) invocation.getArgument(0);
			argument.setId(1L);
			return argument;
		});

		mvc.perform(post("/transactions").contentType(APPLICATION_JSON).content("{\"account_id\":1,\"operation_type_id\":4,\"amount\":123.45}"))
		   .andExpect(status().isCreated())
		   .andExpect(jsonPath("$", aMapWithSize(1)))
		   .andExpect(jsonPath("$.id", is(1)));

		verify(service).create(any(TransactionDto.class));
		verifyNoMoreInteractions(service);
	}

	@Test
	void create_WithId() throws Exception {
		mvc.perform(post("/transactions").contentType(APPLICATION_JSON).content("{\"id\":1,\"account_id\":1,\"operation_type_id\":4,\"amount\":123.45}"))
		   .andExpect(status().isBadRequest())
		   .andExpect(jsonPath("$.errorMsg", is("transactionDto -> [id: deve ser nulo]")));

		verifyNoInteractions(service);
	}

	@Test
	void create_WithoutRequiredFields() throws Exception {
		mvc.perform(post("/transactions").contentType(APPLICATION_JSON).content("{}"))
		   .andExpect(status().isBadRequest())
		   .andExpect(jsonPath("$.errorMsg",
							   is("transactionDto -> [account: accountId não deve ser nulo, amount: não deve ser nulo, operationType: operationTypeId não deve ser " +
									  "nulo]")));

		verifyNoInteractions(service);
	}

	@Test
	void update() throws Exception {
		given(service.update(any(TransactionDto.class))).willAnswer(invocation -> invocation.getArgument(0));

		mvc.perform(put("/transactions").contentType(APPLICATION_JSON).content("{\"id\":1,\"account_id\":1,\"operation_type_id\":4,\"amount\":123.45}"))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$", aMapWithSize(5)))
		   .andExpect(jsonPath("$.id", is(1)))
		   .andExpect(jsonPath("$.account.id", is(1)))
		   .andExpect(jsonPath("$.operation_type", is("PAGAMENTO")))
		   .andExpect(jsonPath("$.amount", is(123.45)));

		verify(service).update(any(TransactionDto.class));
		verifyNoMoreInteractions(service);
	}

	@Test
	void update_WithoutRequiredFields() throws Exception {
		mvc.perform(put("/transactions").contentType(APPLICATION_JSON).content("{}"))
		   .andExpect(status().isBadRequest())
		   .andExpect(jsonPath("$.errorMsg",
							   is("transactionDto -> [account: accountId não deve ser nulo, amount: não deve ser nulo, id: não deve ser nulo, " +
									  "operationType: operationTypeId não deve ser nulo]")));

		verifyNoInteractions(service);
	}

	@Test
	void getById() throws Exception {
		given(service.findById(anyLong())).willReturn(Optional.of(getTransactionDto(1L, 1L, OperationType.COMPRA_A_VISTA, "123.45")));

		mvc.perform(get("/transactions/1"))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$", aMapWithSize(5)))
		   .andExpect(jsonPath("$.id", is(1)))
		   .andExpect(jsonPath("$.account.id", is(1)))
		   .andExpect(jsonPath("$.operation_type", is("COMPRA_A_VISTA")))
		   .andExpect(jsonPath("$.amount", is(123.45)));

		verify(service).findById(anyLong());
		verifyNoMoreInteractions(service);
	}

	@Test
	void getById_NoExists() throws Exception {
		mvc.perform(get("/transactions/1"))
		   .andExpect(status().isNotFound());

		verify(service).findById(anyLong());
		verifyNoMoreInteractions(service);
	}


	@Test
	void deleteById() throws Exception {
		given(service.deleteById(anyLong())).willReturn(true);

		mvc.perform(delete("/transactions/1"))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$", is(true)));

		verify(service).deleteById(anyLong());
		verifyNoMoreInteractions(service);
	}

}