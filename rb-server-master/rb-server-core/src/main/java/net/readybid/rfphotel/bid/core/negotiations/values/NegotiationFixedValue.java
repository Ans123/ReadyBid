package net.readybid.rfphotel.bid.core.negotiations.values;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public class NegotiationFixedValue extends NegotiationValue {

    public static NegotiationValue buildFromRateResponse(Map<String, String> response, String key){
        return NegotiationFixedValue.buildFixed(readValue(response.get(key)), false);
    }

    public static NegotiationValue buildFixed(BigDecimal value, boolean included) {
        return NegotiationFixedValue.buildFixed(value, included, null);
    }

    public static NegotiationValue buildFromNegotiationResponse(NegotiationRequestValue request, NegotiationValue previousRate) {
        NegotiationValue value;
        if(NegotiationValueType.UNAVAILABLE.equals(request.type)){
            value = NegotiationUnavailableValue.buildUnavailable(previousRate);
        } else if(!request.valid){
            value = NegotiationInvalidValue.buildInvalid(previousRate);
        } else {
            value = buildFixed(request.value, request.included, previousRate);
        }
        return value;
    }

    public static NegotiationValue buildFixed(BigDecimal value, NegotiationValue previousValue) {
        return buildFixed(value, false, previousValue);
    }

    public static NegotiationValue buildFixed(BigDecimal value, boolean included, NegotiationValue previousValue) {
        return value == null
                ? NegotiationInvalidValue.buildInvalid(previousValue)
                : build(value, included, previousValue);
    }

    public static NegotiationValue buildFixed(BigDecimal value) {
        return buildFixed(value, false, null);
    }

    public static NegotiationValue buildFromAmenityResponse(Map<String, String> response, String availabilityKey, String valueKey, String includedKey) {
        final boolean isAvailable = readAvailability(response.get(availabilityKey));
        final BigDecimal value = readValue(response.get(valueKey));
        final boolean included = readIncluded(response.get(includedKey));

        if(isAvailable){
            return buildFixed(value, included);
        } else {
            return NegotiationUnavailableValue.buildUnavailable();
        }
    }

    public static NegotiationValue fromMap(Map<String, Object> map) {
        final NegotiationFixedValue value = new NegotiationFixedValue();
        value.loadMapValues(map);
        return value;
    }

    private static NegotiationValue build(BigDecimal value, boolean included, NegotiationValue previousValue) {
        final NegotiationValue val = new NegotiationFixedValue();
        val.value = value;
        val.valid = true;
        val.included = included;
        val.setChange(previousValue);
        return val;
    }

    @Override
    public int compareTo(NegotiationValue previousValue) {
        int comparison = 0;

        switch (previousValue.type){
            case FIXED:
                comparison = value.compareTo(previousValue.value);
                break;
            case UNAVAILABLE:
                comparison = 1;
                break;
            case PERCENTAGE:
                comparison = value.compareTo(((NegotiationPercentageValue)previousValue).amount);
                break;
        }
        return comparison;
    }

    @Override
    public void writeToAnswers(Map<String, String> answers, String typeKey, String valueKey, String includedKey) {
        answers.put(typeKey, "F");
        answers.put(valueKey, String.valueOf(value));
        answers.put(includedKey, includedToAnswer());
    }

    @Override
    public void writeToAnswers(Map<String, String> answers, String availabilityKey, String notAvailableValue, String valueKey, String includedKey) {
        answers.put(valueKey, String.valueOf(value));
        answers.put(includedKey, includedToAnswer());
    }
}