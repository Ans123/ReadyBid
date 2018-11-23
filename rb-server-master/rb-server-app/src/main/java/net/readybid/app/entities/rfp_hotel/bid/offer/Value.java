package net.readybid.app.entities.rfp_hotel.bid.offer;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Value {

    public ValueType type = ValueType.UNAVAILABLE;
    public BigDecimal amount = BigDecimal.ZERO;
    public BigDecimal auxiliaryAmount = BigDecimal.ZERO;
    public boolean isDerived = false;
    public boolean isIncluded = false;

    public boolean isAvailable() {
        return !ValueType.UNAVAILABLE.equals(type);
    }

    private boolean isPercentage() {
        return ValueType.PERCENTAGE.equals(type);
    }

    public void calculateAuxiliaryAmount(Value value) {
        if(isAvailable() && isPercentage() && value != null && value.isAvailable()){
            auxiliaryAmount = amount.multiply(value.amount).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void calculateDerivedAmount(Value value) {
        if(isDerived && value != null && value.isAvailable()){
            type = ValueType.FIXED;
            amount = auxiliaryAmount.multiply(value.amount).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public BigDecimal getFixedAmount() {
        return isPercentage() ? auxiliaryAmount : amount;
    }
}
