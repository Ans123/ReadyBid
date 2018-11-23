package net.readybid.rfphotel.bid.core.negotiations.values;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public abstract class NegotiationMixedValue extends NegotiationValue {

    public static NegotiationValue buildFromBidResponseWithZeroValueAsUnavailable(
            Map<String, String> response,
            String valueKey, String typeKey, String includeKey,
            BigDecimal primaryRateValue
    ){
        final BigDecimal value = readValue(response.get(valueKey));
        final NegotiationValueType type = NegotiationValueType.readType(response.get(typeKey));
        final boolean included = readIncluded(response.get(includeKey));

        NegotiationValue negotiationValue = null;

        switch(type){
            case FIXED:
                negotiationValue = NegotiationFixedValue.buildFixed(value, included);
                break;
            case UNAVAILABLE:
                negotiationValue = NegotiationUnavailableValue.buildUnavailable();
                break;
            case PERCENTAGE:
                negotiationValue = NegotiationPercentageValue.buildPercentageFromBidResponse(value, included, primaryRateValue);
                break;
        }

        return negotiationValue;
    }

    public static NegotiationValue buildFromNegotiationResponse(NegotiationRequestValue request, NegotiationValue previous, BigDecimal rateValue) {
        NegotiationValue negotiationValue = null;

        switch(request.type){
            case FIXED:
            case UNAVAILABLE:
                negotiationValue = NegotiationFixedValue.buildFromNegotiationResponse(request, previous);
                break;
            case PERCENTAGE:
                negotiationValue = NegotiationPercentageValue.buildFromNegotiationResponse(request, previous, rateValue);
                break;
        }

        return negotiationValue;
    }

    @Override
    public abstract void writeToAnswers(Map<String, String> answers, String availabilityKey, String notAvailableValue, String valueKey, String includedKey);
}
