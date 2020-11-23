package com.pismo.apirest.mvc.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

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


	@Column(nullable = false)
	private String documentNumber;

	@OneToMany(mappedBy = "account")
	private List<Transaction> transactions = new ArrayList<>();

}