package net.readybid.app.interactors.rfp_hotel.bid.negotiation;

import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationValues;
import net.readybid.rfphotel.bid.core.negotiations.values.NegotiationFixedValue;
import net.readybid.rfphotel.bid.core.negotiations.values.NegotiationValue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 7/31/2017.
 *
 */
public abstract class HotelRfpNegotiationValuesFactory {

    public static final List<String> RATES_LIST = Arrays.asList("lraS", "lraD", "nlraS", "nlraD");
    public static final String FORMAT_RATE = "%s_s%s_rt%s";

    protected String getPrimaryRatePrefix(Map<String, NegotiationValue> rates) {
        final List<String> prefixes = RATES_LIST;
        for(String prefix: prefixes){
            final Object primaryRate = rates.get(String.format(FORMAT_RATE, prefix, 1, 1));
            if (null != primaryRate) return prefix;
        }
        return RATES_LIST.get(0);
    }

    protected BigDecimal calculateTotal(Map<String, NegotiationValue> values, BigDecimal rateValue) {
        BigDecimal total = BigDecimal.ZERO;
        for(String key : values.keySet()){
            total = total.add(values.get(key).getCalculatedCost(rateValue));
        }
        return total;
    }

    protected BigDecimal getPrimaryRateValue(Map<String, NegotiationValue> rates, String primaryRatePrefix) {
        final NegotiationValue rate = rates.get(String.format(FORMAT_RATE, primaryRatePrefix, 1, 1));
        return rate == null ? BigDecimal.ZERO : rate.value;
    }

    protected Map<String, NegotiationValue> readTotalCosts(HotelRfpNegotiationValues values, String primaryRatePrefix) {
        return readTotalCosts(values, primaryRatePrefix, null);
    }

    protected Map<String, NegotiationValue> readTotalCosts(HotelRfpNegotiationValues values, String primaryRatePrefix, Map<String, NegotiationValue> previousTotalCosts) {
        final Map<String, NegotiationValue> totalCostValues = new HashMap<>();

        values.rates.keySet().stream()
                .filter(rateKey -> rateKey.startsWith(primaryRatePrefix))
                .forEach(rateKey -> {
                    final String tcosId = String.format("tcos_%s", rateKey.substring(primaryRatePrefix.length() + 1));
                    final BigDecimal rateValue = values.rates.get(rateKey).value;
                    final BigDecimal totalCost = rateValue
                            .add(calculateTotal(values.amenities, rateValue))
                            .add(calculateTotal(values.taxes, rateValue));

                    totalCostValues.put(tcosId,
                            NegotiationFixedValue.buildFixed(totalCost, previousTotalCosts == null ? null : previousTotalCosts.get(tcosId)));
                });
        return totalCostValues;
    }
}
