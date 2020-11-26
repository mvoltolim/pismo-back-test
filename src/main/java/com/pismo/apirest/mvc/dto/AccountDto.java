package com.pismo.apirest.mvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pismo.apirest.mvc.dto.support.PersistableDto;

import java.math.BigDecimal;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountDto extends PersistableDto<Long> {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String documentNumber;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private BigDecimal availableCreditLimit;

}