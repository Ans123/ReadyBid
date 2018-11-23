package net.readybid.api.main.bid.hotelrfp;

import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.interactors.rfp_hotel.bid.offer.CreateHotelRfpBidOfferAction;
import net.readybid.rfphotel.bid.HotelRfpBidOffer;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiation;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationValues;
import net.readybid.rfphotel.bid.core.negotiations.NegotiationsConfig;
import net.readybid.rfphotel.bid.core.negotiations.values.NegotiationMockedValue;
import net.readybid.rfphotel.bid.core.negotiations.values.NegotiationValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by DejanK on 5/30/2017.
 *
 */
@Service
public class BidResponseServiceImpl implements BidResponseService {

    private final CreateHotelRfpBidOfferAction createHotelRfpBidOfferAction;

    @Autowired
    public BidResponseServiceImpl(
            CreateHotelRfpBidOfferAction createHotelRfpBidOfferAction
    ) {
        this.createHotelRfpBidOfferAction = createHotelRfpBidOfferAction;
    }

    @Override
    public HotelRfpBidOffer createOffer(NegotiationsConfig config, HotelRfpNegotiation negotiation) {
        return createHotelRfpBidOfferAction.createFromNegotiation(config, negotiation.getValues());
    }

    @Override
    public void updateResponseWithOffer(QuestionnaireResponse questionnaireResponse, HotelRfpNegotiationValues negotiatedValues) {
        updateRates(questionnaireResponse.getAnswers(), negotiatedValues.rates);
        updateTaxes(questionnaireResponse.getAnswers(), negotiatedValues.taxes);
        updateAmenities(questionnaireResponse.getAnswers(), negotiatedValues.amenities);
    }

    private void updateTaxes(Map<String, String> answers, Map<String, NegotiationValue> taxes) {
        updateMixed(answers, "LODGTX_FEE", "LODGTX_UOM", "LODGTX_INCLUDE", taxes, "lodging");
        updateMixed(answers, "STATETX_FEE", "STATETX_UOM", "STATETX_INCLUDE", taxes, "state");
        updateMixed(answers, "CITYTX_FEE", "CITYTX_UOM", "CITYTX_INCLUDE", taxes, "city");
        updateMixed(answers, "VATGSTRM_FEE", "VATGSTRM_UOM", "VATGSTRM_INCLUDE", taxes, "vatGstRm");
        updateMixed(answers, "VATGSTFB_FEE", "VATGSTFB_UOM", "VATGSTFB_INCLUDE", taxes, "vatGstFb");
        updateMixed(answers, "SERVICE_FEE", "SERVICE_UOM", "SERVICE_INCLUDE", taxes, "service");
        updateMixed(answers, "OCC_FEE", "OCC_UOM", "OCC_INCLUDE", taxes, "occupancy");
        updateMixed(answers, "OTHERTX_FEE", "OTHERTX_FEE_UOM", "OTHERTX_FEE_INCL", taxes, "other");
    }

    private void updateAmenities(Map<String, String> answers, Map<String, NegotiationValue> amenities) {
        updateMixed(answers, "EARLYCK_FEE", "EARLYCK_UOM", "EARLYCK_INCLUDE", amenities, "ec");
        updateFixed(answers, "PARKATTEND", "None Available", "PARK_FEE", "PARK_INCLUDE", amenities, "prk");
        updateMocked(answers, "BREAK_TYPE", "BREAK_FEE", "BREAK_INCLUDE", amenities, "bf");
        updateFixed(answers, "FITON_CENT", "FITNESS_FEE_ON", "FITNESS_INCLUDE_ON", amenities, "ft");
        updateFixed(answers, "HIGHSPEED_INROOM", "HSIA_FEE", "HSIA_INCLUDE", amenities, "ia");
        updateFixed(answers, "WIRELESS", "WIRELESS_FEE", "WIRELESS_INCLUDE", amenities, "wf");
        updateFixed(answers, "AIRTRANS_DESCRIBE", "NA", "AIRTRANS_FEE", "AIRTRANS_INCLUDE", amenities, "as");
    }

    private void updateMocked(Map<String, String> answers, String isAvailableKey, String valueKey, String includedKey, Map<String, NegotiationValue> negotiated, String negotiatedKey) {
        final NegotiationMockedValue negotiatedValue = (NegotiationMockedValue) negotiated.get(negotiatedKey);

        answers.put(valueKey, String.valueOf(negotiatedValue.mocked ? BigDecimal.ZERO : negotiatedValue.value));
        if(!negotiatedValue.included) answers.put(isAvailableKey, "N");
        answers.put(includedKey, negotiatedValue.includedToAnswer());
    }

    private void updateFixed(Map<String, String> answers, String availabilityKey, String valueKey, String includedKey, Map<String, NegotiationValue> negotiated, String negotiatedKey) {
        updateFixed(answers, availabilityKey, "N", valueKey, includedKey, negotiated, negotiatedKey);
    }


    private void updateFixed(Map<String, String> answers, String availabilityKey, String notAvailableValue, String valueKey, String includedKey, Map<String, NegotiationValue> negotiated, String negotiatedKey) {
        final NegotiationValue negotiatedValues = negotiated.get(negotiatedKey);
        negotiatedValues.writeToAnswers(answers, availabilityKey, notAvailableValue, valueKey, includedKey);
    }

    private void updateMixed(Map<String, String> answers, String valueKey, String typeKey, String includedKey, Map<String, NegotiationValue> negotiated, String negotiatedKey) {
        final NegotiationValue negotiatedValues = negotiated.get(negotiatedKey);
        negotiatedValues.writeToAnswers(answers, typeKey, valueKey, includedKey);
    }

    private void updateRates(Map<String, String> answers, Map<String, NegotiationValue> rates) {
        for(String rateKey : rates.keySet()){
            if("dyn".equalsIgnoreCase(rateKey)){

            } else {
                final String[] rateKeyParts = rateKey.split("_");
                final String answerKey = String.format("%s_%s_%s_%s", readRateName(rateKeyParts[0]), rateKeyParts[1].toUpperCase(), rateKeyParts[2].toUpperCase(), readOccupancy(rateKeyParts[0]));
                answers.put(answerKey, String.valueOf(rates.get(rateKey).value));
            }
        }
    }

    private String readOccupancy(String ratePart) {
        return ratePart.endsWith("S") ? "SGL" : "DBL";
    }

    private String readRateName(String ratePart) {
        return ratePart.substring(0, ratePart.length() - 1).toUpperCase();
    }
}
