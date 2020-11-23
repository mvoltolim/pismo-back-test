package com.pismo.apirest.mvc.service;

import com.pismo.apirest.mvc.config.ModelMapperConfig;
import com.pismo.apirest.mvc.dto.AccountDto;
import com.pismo.apirest.mvc.exception.CustomRuntimeException;
import com.pismo.apirest.mvc.model.Account;
import com.pismo.apirest.mvc.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static com.pismo.apirest.mvc.TestUtils.getAccountDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	@Mock
	private AccountRepository repository;

	@Spy
	private final ModelMapper modelMapper = ModelMapperConfig.getModelMapper();

	@InjectMocks
	private AccountService service;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void create() {
		given(repository.saveAndFlush(any(Account.class))).willAnswer(invocation -> invocation.getArgument(0));

		var expected = service.create(new AccountDto());

		assertThat(expected).isInstanceOf(AccountDto.class);
		verify(repository).saveAndFlush(any(Account.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	void create_WithId() {
		var expected = assertThrows(CustomRuntimeException.class, () -> service.create(getAccountDto(1L, "12345678900")));
		assertThat(expected).hasMessage("entity_with_id: [Account]");

		verifyNoInteractions(repository);
	}

	@Test
	void update() {
		given(repository.saveAndFlush(any(Account.class))).willAnswer(invocation -> invocation.getArgument(0));
		given(repository.existsById(any(Long.class))).willReturn(true);

		var expected = service.update(getAccountDto(1L, "12345678900"));

		assertThat(expected).isInstanceOf(AccountDto.class);
		verify(repository).saveAndFlush(any(Account.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	void update_WithoutId() {
		var expected = assertThrows(CustomRuntimeException.class, () -> service.update(new AccountDto()));
		assertThat(expected).hasMessage("entity_without_id: [Account]");

		verifyNoInteractions(repository);
	}

	@Test
	void update_EntityNoExists() {
		given(repository.existsById(any(Long.class))).willReturn(false);

		var expected = assertThrows(CustomRuntimeException.class, () -> service.update(getAccountDto(1L, "12345678900")));
		assertThat(expected).hasMessage("entity_no_exists: [Account]");

		verifyNoMoreInteractions(repository);
	}

	@Test
	void findById() {
		given(repository.findById(any(Long.class))).willReturn(Optional.of(new Account()));

		var expected = service.findById(1L).orElse(null);
		assertThat(expected).isInstanceOf(AccountDto.class);

		verifyNoMoreInteractions(repository);
	}

	@Test
	void findAll() {
		given(repository.findAll(any(Pageable.class))).willReturn(PageableExecutionUtils.getPage(List.of(new Account(), new Account()), Pageable.unpaged(), () -> 0));

		var expected = service.findAll(Pageable.unpaged());
		assertThat(expected)
			.isNotEmpty()
			.hasSize(2)
			.hasOnlyElementsOfType(AccountDto.class);

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