package com.pismo.apirest.mvc.dto.support;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PersistableDto<I> implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "ID entity")
	@JsonView(JsonViews.Create.class)
	@Null(groups = ValidationGroups.Create.class)
	@NotNull(groups = ValidationGroups.Update.class)
	private I id;

}