package com.pismo.apirest.mvc.dto;

import com.pismo.apirest.mvc.dto.support.PersistableDto;

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

}