package com.pismo.apirest.mvc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.pismo.apirest.mvc.converter.EnumsConverters;
import com.pismo.apirest.mvc.dto.support.PersistableDto;
import com.pismo.apirest.mvc.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionDto extends PersistableDto<Long> {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "accountId {javax.validation.constraints.NotNull.message}")
	private AccountDto account;

	@NotNull(message = "operationTypeId {javax.validation.constraints.NotNull.message}")
	private OperationType operationType;

	@NotNull
	private BigDecimal amount;

	@JsonIgnore
	private BigDecimal balance;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime eventDate;

	@SuppressWarnings("unused")
	@JsonSetter
	public void setOperationTypeId(Integer operationTypeId) {
		operationType = new EnumsConverters.OperationTypeConverter().convertToEntityAttribute(operationTypeId);
	}

	@SuppressWarnings("unused")
	@JsonSetter
	public void setAccountId(Long accountId) {
		if (Objects.nonNull(accountId)) {
			account = new AccountDto();
			account.setId(accountId);
		}
	}

}