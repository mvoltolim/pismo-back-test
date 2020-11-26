package com.pismo.apirest.mvc.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
//@Table
public class Account extends AbstractPersistable<Long> {

	private static final BigDecimal CREDIT_LIMIT_DEFAULT = new BigDecimal("500");

	@Column(nullable = false, unique = true)
	private String documentNumber;

	@NotNull
	@Column
	private BigDecimal availableCreditLimit;

	@OneToMany(mappedBy = "account")
	private List<Transaction> transactions = new ArrayList<>();

	@PrePersist
	void prePersist() {
		if (Objects.isNull(availableCreditLimit) || availableCreditLimit.equals(BigDecimal.ZERO)) {
			availableCreditLimit = CREDIT_LIMIT_DEFAULT;
		}
	}

}