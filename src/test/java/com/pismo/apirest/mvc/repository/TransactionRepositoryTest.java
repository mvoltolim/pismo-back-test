package com.pismo.apirest.mvc.repository;

import com.pismo.apirest.mvc.enums.OperationType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static com.pismo.apirest.mvc.TestUtils.getTransaction;
import static com.pismo.apirest.mvc.enums.OperationType.SAQUE;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransactionRepositoryTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private TransactionRepository repository;

	@Test
	void operationNegative_ammountNegative() {
		Arrays.stream(OperationType.values()).filter(OperationType::isNegativo).forEach(value -> {
			var entity = getTransaction(value);
			repository.save(entity);
			assertThat(entity.getAmount()).isEqualTo("-123.45");
		});
	}

	@Test
	void operationTypePositive_ammountPositive() {
		Arrays.stream(OperationType.values()).filter(operationType -> !operationType.isNegativo()).forEach(value -> {
			var entity = getTransaction(value);
			repository.save(entity);
			assertThat(entity.getAmount()).isEqualTo("123.45");
		});
	}

	@Test
	void usingEnumConverterGeneric() {
		var id = testEntityManager.persistAndGetId(getTransaction(SAQUE), Long.class);
		var typeBd = testEntityManager.getEntityManager()
									  .createNativeQuery("SELECT operation_type FROM Transaction WHERE id = :id")
									  .setParameter("id", id)
									  .getSingleResult();

		assertThat(typeBd)
			.isEqualTo(SAQUE.getId())
			.isNotEqualTo(SAQUE.ordinal())
			.isNotEqualTo(SAQUE.name());
	}

}