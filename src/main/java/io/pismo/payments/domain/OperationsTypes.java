package io.pismo.payments.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OperationsTypes {

    COMPRA_A_VISTA(1, "Compra a vista", 2),
    COMPRA_PARCELADA(2, "Compra parcelada", 1),
    SAQUE(3, "Saque", 0),
    PAGAMENTO(4, "Pagamento", 0);

    private final Integer operationTypeId;
    private final String description;
    private final Integer chargeOrder;

    OperationsTypes(Integer operationTypeId, String description, Integer chargeOrder) {
        this.operationTypeId = operationTypeId;
        this.description = description;
        this.chargeOrder = chargeOrder;
    }

    @JsonValue
    public Integer getOperationTypeId() {
        return operationTypeId;
    }

    public String getDescription() {
        return description;
    }

    public Integer getChargeOrder() {
        return chargeOrder;
    }
}
