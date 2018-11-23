package net.readybid.rfphotel.bid.core.negotiations.values;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public class NegotiationInvalidValue extends NegotiationMixedValue {

    public static NegotiationInvalidValue buildInvalid(NegotiationValue previousValue) {
        final NegotiationInvalidValue value = new NegotiationInvalidValue();
        value.setChange(previousValue);
        return value;
    }

    public static NegotiationValue fromMap(Map<String, Object> map) {
        final NegotiationInvalidValue value = new NegotiationInvalidValue();
        value.loadMapValues(map);
        return value;
    }

    @Override
    public void writeToAnswers(Map<String, String> answers, String availabilityKey, String notAvailableValue, String valueKey, String includedKey) {
        answers.put(availabilityKey, notAvailableValue);
        answers.put(valueKey, String.valueOf(BigDecimal.ZERO));
        answers.put(includedKey, "N");
    }
}
