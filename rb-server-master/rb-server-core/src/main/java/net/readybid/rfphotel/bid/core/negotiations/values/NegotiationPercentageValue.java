package net.readybid.rfphotel.bid.core.negotiations.values;

import net.readybid.utils.RbMapUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public class NegotiationPercentageValue extends NegotiationValue {

    public BigDecimal amount;

    public static NegotiationValue buildPercentageFromBidResponse(BigDecimal value, boolean included, BigDecimal primaryRateValue) {
        final BigDecimal val = preparePercentageValue(value);
        return buildPercentage(val, included, null, primaryRateValue);
    }

    public static NegotiationValue buildFromNegotiationResponse(NegotiationRequestValue request, NegotiationValue previous, BigDecimal rateValue) {
        NegotiationValue value;
        if(NegotiationValueType.UNAVAILABLE.equals(request.type)){
            value = NegotiationUnavailableValue.buildUnavailable(previous);
        } else if(!request.valid){
            value = NegotiationInvalidValue.buildInvalid(previous);
        } else {
            value = buildPercentage(request.value, request.included, previous, rateValue);
        }
        return value;
    }

    public static NegotiationValue buildPercentage(BigDecimal value, boolean included, NegotiationValue previousValue, BigDecimal primaryRateValue) {
        return value == null
                ? NegotiationInvalidValue.buildInvalid(previousValue)
                : build(value, included, previousValue, primaryRateValue);
    }

    public static NegotiationValue fromMap(Map<String, Object> map) {
        final NegotiationPercentageValue value = new NegotiationPercentageValue();
        value.loadMapValues(map);
        return value;
    }


    private static NegotiationValue build(BigDecimal value, boolean included, NegotiationValue previousValue, BigDecimal primaryRateValue) {
        final NegotiationPercentageValue val = new NegotiationPercentageValue();

        val.value = value;
        val.amount = value.multiply(primaryRateValue);
        val.valid = true;
        val.included = included;
        val.setChange(previousValue);
        return val;
    }

    public NegotiationPercentageValue(){
        this.type = NegotiationValueType.PERCENTAGE;
    }

    @Override
    public BigDecimal getCost() {
        return included ? BigDecimal.ZERO : amount;
    }

    @Override
    public BigDecimal getCalculatedCost(BigDecimal rateValue) {
        return included ? BigDecimal.ZERO : value.multiply(rateValue);
    }

    @Override
    public int compareTo(NegotiationValue previousValue) {
        int comparison = 0;

        switch (previousValue.type){
            case FIXED:
                comparison = amount.compareTo(previousValue.value);
                break;
            case UNAVAILABLE:
                comparison = 1;
                break;
            case PERCENTAGE:
                comparison = value.compareTo(previousValue.value);
                break;
        }
        return comparison;
    }

    @Override
    public void writeToAnswers(Map<String, String> answers, String typeKey, String valueKey, String includedKey) {
        answers.put(typeKey, "P");
        answers.put(valueKey, String.valueOf(value.multiply(BigDecimal.valueOf(100D))));
        answers.put(includedKey, includedToAnswer());
    }

    @Override
    public void writeToAnswers(Map<String, String> answers, String availabilityKey, String notAvailableValue, String valueKey, String includedKey) {
        answers.put(valueKey, String.valueOf(value.multiply(BigDecimal.valueOf(100D))));
        answers.put(includedKey, includedToAnswer());
    }

    @Override
    public Map<String, Object> toMap() {
        final Map<String, Object> m = super.toMap();
        m.put("amount", amount);
        return m;
    }

    @Override
    public void loadMapValues(Map<String, Object> map) {
        super.loadMapValues(map);
        amount = RbMapUtils.readBigDecimal(map.get("amount"));
    }

    public static NegotiationValue buildPercentageRateFromBidResponse(Map<String, String> response, String isAvailableKey, String valueKey) {
        final boolean isAvailable = readAvailability(response.get(isAvailableKey));
        final BigDecimal value = RbMapUtils.readBigDecimal(response.get(valueKey), null);

        NegotiationValue nv = null;
        if(isAvailable){
            nv = new NegotiationPercentageValue();
            nv.valid = null != value;
            nv.type = NegotiationValueType.PERCENTAGE;
            nv.value = preparePercentageValue(value);
            nv.included = false;
            nv.change = NegotiationValueChangeType.INIT;
        }
        return nv;
    }

    private static BigDecimal preparePercentageValue(BigDecimal value) {
         return value == null ? null :
                value.setScale(2, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP);
    }
}