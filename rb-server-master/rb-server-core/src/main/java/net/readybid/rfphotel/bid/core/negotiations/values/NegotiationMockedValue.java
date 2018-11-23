package net.readybid.rfphotel.bid.core.negotiations.values;

import net.readybid.utils.RbMapUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public class NegotiationMockedValue extends NegotiationValue {

    public boolean mocked;
    public BigDecimal mockPercentage;

    public static NegotiationValue buildFromAmenityResponse(Map<String, String> response, String includedKey, String valueKey, BigDecimal mockPercentage, BigDecimal primaryRateValue) {
        final boolean isIncluded = readIncluded(response.get(includedKey));
        final BigDecimal value = readValue(response.get(valueKey));
        final NegotiationMockedValue negValue = new NegotiationMockedValue();

        negValue.included = isIncluded;

        if(value == null || BigDecimal.ZERO.equals(value)){
            negValue.mocked = true;
            negValue.mockPercentage = mockPercentage;
            negValue.value = primaryRateValue.multiply(mockPercentage);
        } else {
            negValue.mocked = false;
            negValue.value = value;
        }

        return negValue;
    }

    public static NegotiationValue buildFromNegotiationResponse(
            NegotiationRequestValue request,
            NegotiationValue previous,
            BigDecimal rateValue
    ) {
        final NegotiationMockedValue previousMock = (NegotiationMockedValue) previous;
        final NegotiationMockedValue negValue = new NegotiationMockedValue();

        negValue.included = request.included;

        if(previousMock.mocked){
            negValue.mocked = true;
            negValue.value = rateValue.multiply(previousMock.mockPercentage);
            negValue.mockPercentage = previousMock.mockPercentage;
        } else {
            negValue.mocked = false;
            negValue.value = request.value;
        }

        negValue.setChange(previous);

        return negValue;
    }

    public static NegotiationValue fromMap(Map<String, Object> map) {
        final NegotiationMockedValue value = new NegotiationMockedValue();
        value.loadMapValues(map);
        return value;
    }


    public NegotiationMockedValue(){
        type = NegotiationValueType.MOCKED;
        valid = true;
        change = NegotiationValueChangeType.INIT;
    }

    @Override
    public int compareTo(NegotiationValue previousValue) {
        int comparison = 0;
        if(previousValue.type.equals(NegotiationValueType.MOCKED)){
            comparison = value.compareTo(previousValue.value);
        }
        return comparison;
    }

    @Override
    public void writeToAnswers(Map<String, String> answers, String availabilityKey, String notAvailableValue, String valueKey, String includedKey) {
        if(mocked){
            answers.put(availabilityKey, notAvailableValue);
        }
        answers.put(valueKey, String.valueOf(value));
        answers.put(includedKey, includedToAnswer());
    }

    @Override
    public Map<String, Object> toMap() {
        final Map<String, Object> m = super.toMap();
        m.put("mocked", mocked);
        m.put("mockPercentage", mockPercentage);
        return m;
    }

    @Override
    public void loadMapValues(Map<String, Object> map) {
        super.loadMapValues(map);
        mocked = RbMapUtils.readBoolean(map.get("mocked"));
        mockPercentage = RbMapUtils.readBigDecimal(map.get("mockPercentage"));
    }
}
