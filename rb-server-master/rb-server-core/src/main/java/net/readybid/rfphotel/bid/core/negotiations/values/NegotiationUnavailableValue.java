package net.readybid.rfphotel.bid.core.negotiations.values;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public class NegotiationUnavailableValue extends NegotiationMixedValue {

    public static NegotiationValue buildUnavailable() {
        return buildUnavailable(null);
    }

    public static NegotiationUnavailableValue buildUnavailable(NegotiationValue previousValue) {
        final NegotiationUnavailableValue value = new NegotiationUnavailableValue();
        value.setChange(previousValue);
        return value;
    }

    public static NegotiationValue fromMap(Map<String, Object> map) {
        final NegotiationUnavailableValue value = new NegotiationUnavailableValue();
        value.loadMapValues(map);
        return value;
    }

    public NegotiationUnavailableValue(){
        this.type = NegotiationValueType.UNAVAILABLE;
        this.valid = true;
    }

    @Override
    public BigDecimal getCost() {
        return BigDecimal.ZERO;
    }

    @Override
    public int compareTo(NegotiationValue previousValue) {
        int comparison = 0;

        switch (previousValue.type){
            case FIXED:
            case PERCENTAGE:
                comparison = -1;
                break;
            case UNAVAILABLE:
                comparison = 0;
                break;
        }
        return comparison;
    }

    @Override
    public void writeToAnswers(Map<String, String> answers, String availabilityKey, String notAvailableValue, String valueKey, String includedKey) {
        answers.put(availabilityKey, notAvailableValue);
        answers.put(valueKey, String.valueOf(BigDecimal.ZERO));
        answers.put(includedKey, "N");
    }
}
