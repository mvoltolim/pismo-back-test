package com.pismo.apirest.mvc.model;

import com.pismo.apirest.mvc.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@EntityListeners(AuditingEntityListener.class)
@Entity
//@Table
public class Transaction extends AbstractPersistable<Long> {

	@ManyToOne
	private Account account;

	@Column(nullable = false, updatable = false)
	private OperationType operationType;

	@Column(nullable = false, updatable = false)
	private BigDecimal amount;

	@Column(nullable = false)
	private BigDecimal balance;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime eventDate;

	@CreatedBy
	@Column(updatable = false)
	private String createdBy;

	@PrePersist
	public void prePersist() {
		if (Objects.nonNull(amount) && operationType.isNegative()) {
			amount = amount.negate();
		}
		if (Objects.isNull(balance)) {
			balance = amount;
		}
	}

}