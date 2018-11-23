package net.readybid.rfphotel.bid.core.negotiations.values;

import net.readybid.utils.RbMapUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public abstract class NegotiationValue {

    public BigDecimal value = BigDecimal.ZERO;
    public boolean valid = false;
    public NegotiationValueType type = NegotiationValueType.FIXED;
    public boolean included = false;
    public NegotiationValueChangeType change = NegotiationValueChangeType.INIT;

    protected static BigDecimal readValue(Object val){
        return RbMapUtils.readBigDecimal(val, null);
    }

    protected static boolean readIncluded(Object inc){
        return "Y".equalsIgnoreCase(String.valueOf(inc));
    }

    protected static boolean readAvailability(Object avail){
        final String availabilityString = String.valueOf(avail);
        return !(avail == null
                || "N".equalsIgnoreCase(availabilityString)
                || "NA".equalsIgnoreCase(availabilityString)
                || "NONE AVAILABLE".equalsIgnoreCase(availabilityString));
    }

    public BigDecimal getCost(){
        return included || !valid ? BigDecimal.ZERO : value;
    }

    public BigDecimal getCalculatedCost(BigDecimal rateValue) {
        return included || !valid ? BigDecimal.ZERO : value;
    }

    public void setChange(NegotiationValue previousValue) {
        change = previousValue == null
                ? NegotiationValueChangeType.INIT
                : fromComparison(previousValue);
    }

    private NegotiationValueChangeType fromComparison(NegotiationValue previousValue) {
        int comparisonResult = compareTo(previousValue);
        return comparisonResult == 0 && (included != previousValue.included || !type.equals(previousValue.type))
                ? NegotiationValueChangeType.INIT
                : NegotiationValueChangeType.getFromComparison(comparisonResult);
    }

    public int compareTo(NegotiationValue previousValue) {
        return 0;
    }

    public void writeToAnswers(Map<String, String> answers, String typeKey, String valueKey, String includedKey) {
        answers.put(typeKey, "N");
        answers.put(valueKey, String.valueOf(BigDecimal.ZERO));
        answers.put(includedKey, "N");
    }

    public abstract void writeToAnswers(Map<String, String> answers, String availabilityKey, String notAvailableValue, String valueKey, String includedKey);

    public String includedToAnswer() {
        return included ? "Y" : "N";
    }

    @Deprecated
    public Map<String, Object> toMap() {

        final Map<String, Object> m = new HashMap<>();
        m.put("value", value);
        m.put("valid", valid);
        m.put("type", type);
        m.put("included", included);
        m.put("change", change);

        return m;
    }

    public static NegotiationValue fromMap(Map<String, Object> map) {
        final NegotiationValueType type = NegotiationValueType.valueOf(String.valueOf(map.get("type")));

        if(RbMapUtils.readBoolean(map.get("valid"))){
            switch(type){
                case FIXED:
                    return NegotiationFixedValue.fromMap(map);
                case UNAVAILABLE:
                    return NegotiationUnavailableValue.fromMap(map);
                case PERCENTAGE:
                    return NegotiationPercentageValue.fromMap(map);
                case MOCKED:
                    return NegotiationMockedValue.fromMap(map);
            }
        }
        return NegotiationInvalidValue.fromMap(map);
    }

    protected void loadMapValues(Map<String, Object> map){
        type = NegotiationValueType.valueOf(String.valueOf(map.get("type")));
        valid = RbMapUtils.readBoolean(map.get("valid"));
        value = RbMapUtils.readBigDecimal(map.get("value"));
        included = RbMapUtils.readBoolean(map.get("included"));
        change = NegotiationValueChangeType.valueOf(String.valueOf(map.get("change")));
    }
}
