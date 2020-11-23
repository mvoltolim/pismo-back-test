package com.pismo.apirest.mvc.converter;

import com.pismo.apirest.mvc.enums.OperationType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumsConvertersTest {

	@Test
	void nullValue() {
		assertNull(EnumsConverters.AbstractPersistableEnumConverter.fromId(null, null));
	}

	@Test
	void operationTypeConverter() {
		var converter = new EnumsConverters.OperationTypeConverter();
		Arrays.stream(OperationType.values()).forEach(value -> {
			assertEquals(value.getId(), converter.convertToDatabaseColumn(value));
			assertEquals(value, converter.convertToEntityAttribute(value.getId()));
		});

		var exception = assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute(-999));
		Assertions.assertThat(exception).hasMessage("Does not exist OperationType with ID: -999");
	}

}