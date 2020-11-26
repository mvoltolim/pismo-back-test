package com.pismo.apirest.mvc.model.support;

import org.springframework.data.domain.Persistable;

import static java.util.Objects.isNull;

public interface EntityPersistable<I> extends Persistable<I> {

	@Override
	I getId();

	void setId(I id);

	@Override
	default boolean isNew() {
		return isNull(getId());
	}

}
