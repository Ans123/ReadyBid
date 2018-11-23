package net.readybid.app.interactors.rfp_hotel.bid.negotiation;

import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationValues;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationsConfig;
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
public class HotelRfpNegotiationValuesFromBidResponseFactory extends HotelRfpNegotiationValuesFactory {

    public HotelRfpNegotiationValues readValues(HotelRfpNegotiationsConfig config, Map<String, String> response) {
        final HotelRfpNegotiationValues values = new HotelRfpNegotiationValues();
        values.rates = readRatesValues(config, response);
        readDynamicRate(values.rates, response);
        final String primaryRatePrefix = getPrimaryRatePrefix(values.rates);
        final BigDecimal primaryRateValue = getPrimaryRateValue(values.rates, primaryRatePrefix);

        values.amenities = readAmenities(response, primaryRateValue);
        values.amenitiesTotal = NegotiationFixedValue.buildFixed(
                calculateTotal(values.amenities, primaryRateValue));

        values.taxes = readTaxes(response, primaryRateValue);
        values.taxesTotal = NegotiationFixedValue.buildFixed(
                calculateTotal(values.taxes, primaryRateValue));

        values.totalCosts = readTotalCosts(values, primaryRatePrefix);

        return values;
    }

    private Map<String, NegotiationValue> readRatesValues(HotelRfpNegotiationsConfig config, Map<String, String> response) {
        final Map<String, NegotiationValue> rateValues = new HashMap<>();

        for(String ratePrefix : config.rates){
            final String rateId = ratePrefix.substring(0, ratePrefix.length() - 1).toUpperCase();
            final String occupancy = ratePrefix.substring(ratePrefix.length()-1).equalsIgnoreCase("S") ? "SGL" : "DBL";

            for(Map<String, Object> season: config.seasons){
                final String seasonId = String.valueOf(season.get("id"));

                for(Map<String, Object> roomType: config.roomTypes) {
                    final String roomTypeId = String.valueOf(roomType.get("id"));
                    final NegotiationValue value = NegotiationFixedValue.buildFromRateResponse(response,
                            String.format("%s_S%s_RT%s_%s", rateId, seasonId, roomTypeId, occupancy));
                    rateValues.put(String.format(FORMAT_RATE, ratePrefix, seasonId, roomTypeId), value);
                }
            }
        }

        return rateValues;
    }

    private void readDynamicRate(Map<String, NegotiationValue> rates, Map<String, String> response) {
        final NegotiationValue dynamicRate = NegotiationPercentageValue.buildPercentageRateFromBidResponse(response, "DYNAMIC_PRICING", "DYNAMIC_PCT_Discount");
        if(null != dynamicRate) rates.put("dyn", dynamicRate);
    }

    private Map<String, NegotiationValue> readAmenities(Map<String, String> response, BigDecimal primaryRateValue) {
        final Map<String, NegotiationValue> amenities = new HashMap<>();

        amenities.put("ec", NegotiationMixedValue.buildFromBidResponseWithZeroValueAsUnavailable(
                response, "EARLYCK_FEE", "EARLYCK_UOM", "EARLYCK_INCLUDE", primaryRateValue
        ));

        amenities.put("prk", NegotiationFixedValue.buildFromAmenityResponse(
                response, "PARKATTEND", "PARK_FEE", "PARK_INCLUDE"
        ));

        amenities.put("bf", NegotiationMockedValue.buildFromAmenityResponse(
                response, "BREAK_INCLUDE", "BREAK_FEE", BigDecimal.valueOf(6D / 100), primaryRateValue
        ));

        amenities.put("ft", NegotiationFixedValue.buildFromAmenityResponse(
                response, "FITON_CENT", "FITNESS_FEE_ON", "FITNESS_INCLUDE_ON"
        ));

        amenities.put("ia", NegotiationFixedValue.buildFromAmenityResponse(
                response, "HIGHSPEED_INROOM", "HSIA_FEE", "HSIA_INCLUDE"
        ));

        amenities.put("wf", NegotiationFixedValue.buildFromAmenityResponse(
                response, "WIRELESS", "WIRELESS_FEE", "WIRELESS_INCLUDE"
        ));

        amenities.put("as", NegotiationFixedValue.buildFromAmenityResponse(
                response, "AIRTRANS_DESCRIBE", "AIRTRANS_FEE", "AIRTRANS_INCLUDE"
        ));

        return amenities;
    }

    private Map<String, NegotiationValue> readTaxes(Map<String, String> response, BigDecimal primaryRateValue) {
        final Map<String, NegotiationValue> taxes = new HashMap<>();

        taxes.put("lodging", NegotiationMixedValue.buildFromBidResponseWithZeroValueAsUnavailable(
                response, "LODGTX_FEE", "LODGTX_UOM", "LODGTX_INCLUDE", primaryRateValue
        ));

        taxes.put("state", NegotiationMixedValue.buildFromBidResponseWithZeroValueAsUnavailable(
                response, "STATETX_FEE", "STATETX_UOM", "STATETX_INCLUDE", primaryRateValue
        ));

        taxes.put("city", NegotiationMixedValue.buildFromBidResponseWithZeroValueAsUnavailable(
                response, "CITYTX_FEE", "CITYTX_UOM", "CITYTX_INCLUDE", primaryRateValue
        ));

        taxes.put("vatGstRm", NegotiationMixedValue.buildFromBidResponseWithZeroValueAsUnavailable(
                response, "VATGSTRM_FEE", "VATGSTRM_UOM", "VATGSTRM_INCLUDE", primaryRateValue
        ));

        taxes.put("vatGstFb", NegotiationMixedValue.buildFromBidResponseWithZeroValueAsUnavailable(
                response, "VATGSTFB_FEE", "VATGSTFB_UOM", "VATGSTFB_INCLUDE", primaryRateValue
        ));

        taxes.put("service", NegotiationMixedValue.buildFromBidResponseWithZeroValueAsUnavailable(
                response, "SERVICE_FEE", "SERVICE_UOM", "SERVICE_INCLUDE", primaryRateValue
        ));

        taxes.put("occupancy", NegotiationMixedValue.buildFromBidResponseWithZeroValueAsUnavailable(
                response, "OCC_FEE", "OCC_UOM", "OCC_INCLUDE", primaryRateValue
        ));

        taxes.put("other", NegotiationMixedValue.buildFromBidResponseWithZeroValueAsUnavailable(
                response, "OTHERTX_FEE", "OTHERTX_FEE_UOM", "OTHERTX_FEE_INCL", primaryRateValue
        ));

        return taxes;
    }
}
