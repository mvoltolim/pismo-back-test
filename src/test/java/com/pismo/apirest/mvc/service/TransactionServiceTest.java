package com.pismo.apirest.mvc.service;

import com.pismo.apirest.mvc.config.ModelMapperConfig;
import com.pismo.apirest.mvc.dto.TransactionDto;
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

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static com.pismo.apirest.mvc.TestUtils.getTransaction;
import static com.pismo.apirest.mvc.TestUtils.getTransactionDto;
import static com.pismo.apirest.mvc.enums.OperationType.COMPRA_A_VISTA;
import static com.pismo.apirest.mvc.enums.OperationType.COMPRA_PARCELADA;
import static com.pismo.apirest.mvc.enums.OperationType.PAGAMENTO;
import static com.pismo.apirest.mvc.enums.OperationType.SAQUE;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@Mock
	private AccountService accountService;

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
		given(repository.saveAndFlush(any(Transaction.class))).willReturn(getTransaction(COMPRA_A_VISTA, "123.45"));

		var expected = service.create(getTransactionDto(null, 1L));

		assertThat(expected).isInstanceOf(TransactionDto.class);
		assertThat(expected.getOperationType()).isEqualTo(COMPRA_A_VISTA);
		assertThat(expected.getAmount()).isEqualTo("123.45");

		verify(repository).saveAndFlush(any(Transaction.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	void create_WithId() {
		var expected = assertThrows(CustomRuntimeException.class, () -> service.create(getTransactionDto(1L, 1L)));
		assertThat(expected).hasMessage("entity_with_id: [Transaction]");

		verifyNoInteractions(repository);
	}

	@Test
	void update_throwUnsupportedOperationException() {
		var expected = assertThrows(UnsupportedOperationException.class, () -> service.update(new TransactionDto()));
		assertThat(expected).hasMessage("Não é possível atualizar uma transação");

		verifyNoInteractions(repository);
	}

	@Test
	void findById() {
		given(repository.findById(anyLong())).willReturn(Optional.of(new Transaction()));

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

		verify(repository).deleteById(anyLong());
		verifyNoMoreInteractions(repository);
	}

	@Test
	void makePayments_withNoPaymentOperation() {
		service.makePayments(getTransactionDto(1L, 1L, COMPRA_A_VISTA, "500"));

		verifyNoInteractions(repository);
	}

	@Test
	void makePayments_withPaymentOperationAndAmountZero() {
		service.makePayments(getTransactionDto(1L, 1L, COMPRA_A_VISTA, "0"));

		verifyNoInteractions(repository);
	}

	@Test
	void makePayments_withPaymentOperationAndAmountGreaterThanZero() {
		var saque = getTransaction(SAQUE, "200");
		var compraParcelada = getTransaction(COMPRA_PARCELADA, "150");
		var compraVista1 = getTransaction(COMPRA_A_VISTA, "50");
		var compraVista2 = getTransaction(COMPRA_A_VISTA, "100");
		var pagamento = getTransactionDto(1L, 1L, PAGAMENTO, "450");

		given(repository.findAllByAccount_IdAndBalanceIsLessThan(1L, ZERO)).willReturn(List.of(saque, compraVista2, compraParcelada, compraVista1));

		service.makePayments(pagamento);

		assertThat(pagamento.getAmount()).isEqualTo("450");
		assertThat(pagamento.getBalance()).isEqualTo("0");

		//ordem de pagamento segundo OperationType.chargeOrder
		assertThat(compraVista1.getAmount()).isEqualTo("-50");
		assertThat(compraVista1.getBalance()).isEqualTo("0");

		assertThat(compraVista2.getAmount()).isEqualTo("-100");
		assertThat(compraVista2.getBalance()).isEqualTo("0");

		assertThat(compraParcelada.getAmount()).isEqualTo("-150");
		assertThat(compraParcelada.getBalance()).isEqualTo("0");

		assertThat(saque.getAmount()).isEqualTo("-200");
		assertThat(saque.getBalance()).isEqualTo("-50");

		verify(repository).findAllByAccount_IdAndBalanceIsLessThan(1L, ZERO);
		verify(repository).saveAll(anyList());
		verifyNoMoreInteractions(repository);
	}

}