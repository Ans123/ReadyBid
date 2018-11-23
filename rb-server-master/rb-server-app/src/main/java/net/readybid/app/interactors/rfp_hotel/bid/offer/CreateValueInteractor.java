package net.readybid.app.interactors.rfp_hotel.bid.offer;

import net.readybid.app.entities.rfp_hotel.bid.offer.Value;
import net.readybid.app.entities.rfp_hotel.bid.offer.ValueType;
import net.readybid.rfphotel.bid.core.negotiations.values.NegotiationMockedValue;
import net.readybid.rfphotel.bid.core.negotiations.values.NegotiationValue;
import net.readybid.rfphotel.bid.core.negotiations.values.NegotiationValueType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreateValueInteractor implements CreateValueAction {

    @Override
    public Value unavailable() {
        final Value v = new Value();
        v.type = ValueType.UNAVAILABLE;
        v.amount = BigDecimal.ZERO;
        v.auxiliaryAmount = BigDecimal.ZERO;
        v.isDerived = false;
        v.isIncluded= false;
        return v;
    }

    @Override
    public Value fixed(BigDecimal amount){
        final Value v = new Value();
        v.type = ValueType.FIXED;
        v.amount = amount;
        v.auxiliaryAmount = BigDecimal.ZERO;
        v.isDerived= false;
        v.isIncluded= false;
        return v;
    }

    @Override
    public Value fixed(Value... values) {
        return fixed(total(values));
    }

    @Override
    public Value fromNegotiationValue(NegotiationValue negVal) {
        if(negVal == null || !negVal.valid || NegotiationValueType.UNAVAILABLE.equals(negVal.type)) return unavailable();

        final Value v = new Value();
        v.type = fromNegotiationType(negVal.type);
        v.isDerived = negVal.type.equals(NegotiationValueType.MOCKED);
        v.amount = negVal.value;
        v.auxiliaryAmount = BigDecimal.ZERO;
        v.isIncluded = negVal.included;

        if(negVal instanceof NegotiationMockedValue){
            final NegotiationMockedValue mockNegVal = (NegotiationMockedValue) negVal;
            v.isDerived = mockNegVal.mocked;
            if(v.isDerived){
                v.amount = BigDecimal.ZERO;
                v.auxiliaryAmount = mockNegVal.mockPercentage;
            }
        }

        return v;
    }

    private BigDecimal total(Value... values) {
        BigDecimal result = BigDecimal.ZERO;

        for(Value v : values){
            if(v != null && v.isAvailable() && !v.isIncluded){
                result = result.add(v.getFixedAmount());
            }
        }

        return result;
    }

    private ValueType fromNegotiationType(NegotiationValueType negotiationValueType) {
        try {
            return NegotiationValueType.MOCKED.equals(negotiationValueType)
                    ? ValueType.FIXED
                    : ValueType.valueOf(String.valueOf(negotiationValueType));
        } catch (IllegalArgumentException iae){
            return ValueType.UNAVAILABLE;
        }
    }
}
