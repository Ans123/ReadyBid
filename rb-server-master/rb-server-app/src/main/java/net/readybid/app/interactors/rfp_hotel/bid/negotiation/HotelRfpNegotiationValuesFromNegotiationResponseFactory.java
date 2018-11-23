package net.readybid.app.interactors.rfp_hotel.bid.negotiation;

import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationValues;
import net.readybid.rfphotel.bid.core.negotiations.NegotiationRequest;
import net.readybid.rfphotel.bid.core.negotiations.values.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 7/31/2017.
 *
 */
@Service
public class HotelRfpNegotiationValuesFromNegotiationResponseFactory extends HotelRfpNegotiationValuesFactory {

    public HotelRfpNegotiationValues readValues(NegotiationRequest negotiationRequest, HotelRfpNegotiationValues lastCommunication) {
        final HotelRfpNegotiationValues values = new HotelRfpNegotiationValues();

        values.rates = readRatesValues(negotiationRequest.rates, lastCommunication.rates);
        final String primaryRatePrefix = getPrimaryRatePrefix(values.rates);
        final BigDecimal primaryRateValue = getPrimaryRateValue(values.rates, primaryRatePrefix);

        values.amenities = readAmenities(negotiationRequest.amenities, lastCommunication.amenities, primaryRateValue);
        values.amenitiesTotal = NegotiationFixedValue.buildFixed(
                calculateTotal(values.amenities, primaryRateValue), lastCommunication.amenitiesTotal);

        values.taxes = readTaxes(lastCommunication.taxes, primaryRateValue);
        values.taxesTotal = NegotiationFixedValue.buildFixed(
                calculateTotal(values.taxes, primaryRateValue), lastCommunication.taxesTotal);

        values.totalCosts = readTotalCosts(values, primaryRatePrefix, lastCommunication.totalCosts);

        return values;
    }

    private Map<String, NegotiationValue> readRatesValues(Map<String, NegotiationRequestValue> rates, Map<String, NegotiationValue> previousRates) {
        final Map<String, NegotiationValue> rateValues = new HashMap<>();

        for(String rateKey : previousRates.keySet()){
            if("dyn".equalsIgnoreCase(rateKey)) {
                rateValues.put(rateKey, previousRates.get(rateKey));
            } else {
                rateValues.put(rateKey,
                        NegotiationFixedValue.buildFromNegotiationResponse(
                                rates.get(rateKey), previousRates.get(rateKey)));
            }
        }
        return rateValues;
    }
    private Map<String, NegotiationValue> readAmenities(Map<String, NegotiationRequestValue> response, Map<String, NegotiationValue> previous, BigDecimal primaryRateValue) {
        final Map<String, NegotiationValue> amenities = new HashMap<>();

        readAndAddMixed("ec", amenities, response, previous, primaryRateValue);
        readAndAddFixed("prk", amenities, response, previous);
        readAndAddMocked("bf", amenities, response, previous, primaryRateValue);
        readAndAddFixed("ft", amenities, response, previous);
        readAndAddFixed("ia", amenities, response, previous);
        readAndAddFixed("wf", amenities, response, previous);
        readAndAddFixed("as", amenities, response, previous);

        return amenities;
    }

    private void readAndAddMixed(String key, Map<String, NegotiationValue> amenities, Map<String, NegotiationRequestValue> response, Map<String, NegotiationValue> previous, BigDecimal rateValue) {
        amenities.put(key, NegotiationMixedValue.buildFromNegotiationResponse(
                response.get(key), previous.get(key), rateValue
        ));
    }

    private void readAndAddFixed(String key, Map<String, NegotiationValue> amenities, Map<String, NegotiationRequestValue> response, Map<String, NegotiationValue> previous) {
        amenities.put(key, NegotiationFixedValue.buildFromNegotiationResponse(
                response.get(key), previous.get(key)
        ));
    }

    private void readAndAddMocked(String key, Map<String, NegotiationValue> amenities, Map<String, NegotiationRequestValue> response, Map<String, NegotiationValue> previous, BigDecimal rateValue) {
        amenities.put(key, NegotiationMockedValue.buildFromNegotiationResponse(
                response.get(key), previous.get(key), rateValue
        ));
    }

    private Map<String, NegotiationValue> readTaxes(Map<String, NegotiationValue> previousTaxes, BigDecimal primaryRateValue) {
        final Map<String, NegotiationValue> taxes = new HashMap<>();

        for(String key : previousTaxes.keySet()){
            final NegotiationValue previousTax = previousTaxes.get(key);
            NegotiationValue tax = previousTax;
            if(NegotiationValueType.PERCENTAGE.equals(previousTax.type)){
                tax = NegotiationPercentageValue.buildPercentage(previousTax.value, previousTax.included, previousTax, primaryRateValue);
            }
            taxes.put(key, tax);
        }
        return taxes;
    }
}
