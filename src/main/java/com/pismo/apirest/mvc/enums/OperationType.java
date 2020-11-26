package com.pismo.apirest.mvc.enums;

import com.pismo.apirest.mvc.enums.support.PersistableEnum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OperationType implements PersistableEnum<Integer> {

	COMPRA_A_VISTA(1, 2, ValueType.NEGATIVO),
	COMPRA_PARCELADA(2, 1, ValueType.NEGATIVO),
	SAQUE(3, 0, ValueType.NEGATIVO),
	PAGAMENTO(4, 0, ValueType.POSITIVO),
	;

	private final Integer id;

	private final Integer chargeOrder;

	private final ValueType valueType;

	public boolean isNegative() {
		return valueType == ValueType.NEGATIVO;
	}

	private enum ValueType {
		NEGATIVO,
		POSITIVO,
		;
	}

}