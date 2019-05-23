package io.pismo.payments.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.pismo.payments.domain.OperationsTypes;
import io.pismo.payments.exceptions.InvalidOperationTypeException;

import java.io.IOException;
import java.util.stream.Stream;

public class OperationsTypesDeserializer extends JsonDeserializer {

    @Override
    public OperationsTypes deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Integer operationTypeId = p.getIntValue();

        return Stream.of(OperationsTypes.values())
                .filter(operationType -> operationType.getOperationTypeId().equals(operationTypeId))
                .findFirst().orElseThrow(InvalidOperationTypeException::new);

    }
}
