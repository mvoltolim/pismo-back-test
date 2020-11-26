package com.pismo.apirest.mvc.service;

import com.pismo.apirest.mvc.dto.TransactionDto;
import com.pismo.apirest.mvc.enums.OperationType;
import com.pismo.apirest.mvc.model.Transaction;
import com.pismo.apirest.mvc.repository.TransactionRepository;
import com.pismo.apirest.mvc.service.support.CrudServiceImpl;
import org.modelmapper.ModelMapper;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static java.math.BigDecimal.ZERO;

@Slf4j
@Service
public class TransactionService extends CrudServiceImpl<TransactionRepository, Transaction, Long, TransactionDto> {

	private final AccountService accountService;

	public TransactionService(TransactionRepository repository, @NonNull AccountService accountService, ModelMapper modelMapper) {
		super(repository, Transaction.class, TransactionDto.class, modelMapper);
		this.accountService = accountService;
	}

	@Override
	public TransactionDto create(@NonNull TransactionDto dto) {
		accountService.updateAvailableCreditLimit(dto.getAccount().getId(), dto.getOperationType(), dto.getAmount());
		makePayments(dto);

		return super.create(dto);
	}

	void makePayments(@NonNull TransactionDto payment) {
		if (payment.getOperationType() != OperationType.PAGAMENTO || payment.getAmount().compareTo(ZERO) <= 0) {
			return;
		}

		var amount = new AtomicReference<>(payment.getAmount());
		var transactions = repository.findAllByAccount_IdAndBalanceIsLessThan(payment.getAccount().getId(), ZERO);

		transactions.stream().filter(t -> t.getOperationType().isNegative())
					.sorted(Comparator.comparingInt((Transaction o) -> o.getOperationType().getChargeOrder()).reversed().thenComparing(Transaction::getEventDate))
					.forEach(transaction -> {
						var balancePositive = transaction.getBalance().abs();

						if (amount.get().compareTo(balancePositive) >= 0) {
							payment.setBalance(amount.updateAndGet(v -> v.subtract(balancePositive)));
							transaction.setBalance(ZERO);
						} else {
							payment.setBalance(ZERO);
							transaction.setBalance(amount.updateAndGet(v -> v.subtract(balancePositive)));
						}
					});

		repository.saveAll(transactions);
	}

	@Override
	public TransactionDto update(@NonNull TransactionDto dto) {
		throw new UnsupportedOperationException("Não é possível atualizar uma transação");
	}

}