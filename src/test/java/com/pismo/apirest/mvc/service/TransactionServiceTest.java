package com.pismo.apirest.mvc.service;

import com.pismo.apirest.mvc.config.ModelMapperConfig;
import com.pismo.apirest.mvc.dto.TransactionDto;
import com.pismo.apirest.mvc.enums.OperationType;
import com.pismo.apirest.mvc.exception.CustomRuntimeException;
import com.pismo.apirest.mvc.model.Transaction;
import com.pismo.apirest.mvc.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static com.pismo.apirest.mvc.TestUtils.getTransactionDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@Mock
	private TransactionRepository repository;

	@Spy
	private final ModelMapper modelMapper = ModelMapperConfig.getModelMapper();

	@InjectMocks
	private TransactionService service;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void create() {
		given(repository.saveAndFlush(any(Transaction.class))).willAnswer(invocation -> invocation.getArgument(0));

		var expected = service.create(new TransactionDto());

		assertThat(expected).isInstanceOf(TransactionDto.class);
		verify(repository).saveAndFlush(any(Transaction.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	void create_WithId() {
		var expected = assertThrows(CustomRuntimeException.class, () -> service.create(getTransactionDto(1L)));
		assertThat(expected).hasMessage("entity_with_id: [Transaction]");

		verifyNoInteractions(repository);
	}

	@Test
	void update() {
		given(repository.saveAndFlush(any(Transaction.class))).willAnswer(invocation -> invocation.getArgument(0));
		given(repository.existsById(any(Long.class))).willReturn(true);

		var expected = service.update(getTransactionDto(1L, 1L, OperationType.COMPRA_A_VISTA, new BigDecimal("123.45")));

		assertThat(expected).isInstanceOf(TransactionDto.class);
		verify(repository).saveAndFlush(any(Transaction.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	void update_WithoutId() {
		var expected = assertThrows(CustomRuntimeException.class, () -> service.update(new TransactionDto()));
		assertThat(expected).hasMessage("entity_without_id: [Transaction]");

		verifyNoInteractions(repository);
	}

	@Test
	void update_EntityNoExists() {
		given(repository.existsById(any(Long.class))).willReturn(false);

		var expected = assertThrows(CustomRuntimeException.class, () -> service.update(getTransactionDto(1L)));
		assertThat(expected).hasMessage("entity_no_exists: [Transaction]");

		verifyNoMoreInteractions(repository);
	}

	@Test
	void findById() {
		given(repository.findById(any(Long.class))).willReturn(Optional.of(new Transaction()));

		var expected = service.findById(1L).orElse(null);
		assertThat(expected).isInstanceOf(TransactionDto.class);

		verifyNoMoreInteractions(repository);
	}

	@Test
	void findAll() {
		given(repository.findAll(any(Pageable.class))).willReturn(PageableExecutionUtils.getPage(List.of(new Transaction()), Pageable.unpaged(), () -> 0));

		var expected = service.findAll(Pageable.unpaged());
		assertThat(expected)
			.isNotEmpty()
			.hasSize(1)
			.hasOnlyElementsOfType(TransactionDto.class);

		verifyNoMoreInteractions(repository);
	}

	@Test
	void deleteById() {
		var expected = service.deleteById(1L);
		assertThat(expected).isTrue();

		verify(repository).deleteById(any(Long.class));
		verifyNoMoreInteractions(repository);
	}

}