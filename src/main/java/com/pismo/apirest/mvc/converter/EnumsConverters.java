package com.pismo.apirest.mvc.converter;

import com.pismo.apirest.mvc.enums.OperationType;
import com.pismo.apirest.mvc.enums.support.PersistableEnum;

import java.util.Objects;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("unused")
public interface EnumsConverters {

	@RequiredArgsConstructor
	abstract class AbstractPersistableEnumConverter<E extends Enum<E> & PersistableEnum<I>, I> implements AttributeConverter<E, I> {

		private final E[] enumConstants;

		public AbstractPersistableEnumConverter(@NonNull Class<E> enumType) {
			enumConstants = enumType.getEnumConstants();
		}


		@Override
		public I convertToDatabaseColumn(E attribute) {
			return Objects.isNull(attribute) ? null : attribute.getId();
		}

		@Override
		public E convertToEntityAttribute(I dbData) {
			return fromId(dbData, enumConstants);
		}

		public static <E extends Enum<E> & PersistableEnum<I>, I> E fromId(I idValue, E[] enumConstants) {
			return Objects.isNull(idValue) ? null : Stream.of(enumConstants)
														  .filter(e -> e.getId().equals(idValue))
														  .findAny()
														  .orElseThrow(() -> new IllegalArgumentException(
															  String.format("Does not exist %s with ID: %s", enumConstants[0].getClass().getSimpleName(), idValue)));
		}

	}

	@Converter(autoApply = true)
	class OperationTypeConverter extends AbstractPersistableEnumConverter<OperationType, Integer> {

		public OperationTypeConverter() {
			super(OperationType.class);
		}

	}

}