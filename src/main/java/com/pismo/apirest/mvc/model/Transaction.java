package com.pismo.apirest.mvc.model;

import com.pismo.apirest.mvc.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

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

	@Column(nullable = false)
	private OperationType operationType;

	@Column(nullable = false)
	private BigDecimal amount;

	@CreatedDate
	@Column
	private LocalDateTime eventDate;

	@CreatedBy
	@Column
	private String createdBy;

	@PrePersist
	@PreUpdate
	void prePersistPreUpdate() {
		if (amount != null && operationType.isNegativo()) {
			amount = amount.negate();
		}
	}

}