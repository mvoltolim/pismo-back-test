package com.pismo.apirest.mvc.enums;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperationTypeTest {

	@Test
	void idsUniques() {
		var values = OperationType.values();
		assertEquals(values.length, Arrays.stream(values).map(OperationType::getId).distinct().count());
	}

}