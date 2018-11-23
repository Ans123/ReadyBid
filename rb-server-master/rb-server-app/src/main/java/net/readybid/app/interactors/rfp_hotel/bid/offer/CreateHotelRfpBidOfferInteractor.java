package net.readybid.app.interactors.rfp_hotel.bid.offer;

import net.readybid.app.entities.rfp_hotel.bid.offer.HotelRfpBidOfferDetails;
import net.readybid.app.entities.rfp_hotel.bid.offer.HotelRfpBidOfferImpl;
import net.readybid.app.entities.rfp_hotel.bid.offer.Value;
import net.readybid.rfphotel.bid.HotelRfpBidOffer;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationValues;
import net.readybid.rfphotel.bid.core.negotiations.NegotiationsConfig;
import net.readybid.rfphotel.bid.core.negotiations.values.NegotiationValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CreateHotelRfpBidOfferInteractor implements CreateHotelRfpBidOfferAction {

    private final CreateValueAction createValue;

    @Autowired
    public CreateHotelRfpBidOfferInteractor(CreateValueAction createValueAction) {
        this.createValue = createValueAction;
    }

    @Override
    public HotelRfpBidOffer createFromNegotiation(NegotiationsConfig config, HotelRfpNegotiationValues negotiationValues) {
        final HotelRfpBidOfferImpl offer = new HotelRfpBidOfferImpl();

        offer.main = createMain(config, negotiationValues);
        offer.averages = createAverages(config, negotiationValues);
        return offer;
    }

    private HotelRfpBidOfferDetails createMain(NegotiationsConfig config, HotelRfpNegotiationValues negotiationValues) {
        final HotelRfpBidOfferDetails main = new HotelRfpBidOfferDetails();

        loadMainRates(main, config.getPrimaryRatePrefix(), negotiationValues.rates);
        final Value primaryRate = getPrimaryRate(main.lraRate, main.nlraRate);
        loadAmenities(main, negotiationValues.amenities, primaryRate);
        loadTaxes(main, negotiationValues.taxes, primaryRate);
        calculateTotalCost(main, primaryRate);

        return main;
    }

    private HotelRfpBidOfferDetails createAverages(NegotiationsConfig config, HotelRfpNegotiationValues negotiationValues) {
        final HotelRfpBidOfferDetails averages = new HotelRfpBidOfferDetails();

        loadAverageRates(averages, config.getPrimaryRatePrefix(), config.getSeasons(), negotiationValues.rates);
        final Value primaryRate = getPrimaryRate(averages.lraRate, averages.nlraRate);
        loadAmenities(averages, negotiationValues.amenities, primaryRate);
        loadTaxes(averages, negotiationValues.taxes, primaryRate);
        calculateTotalCost(averages, primaryRate);

        return averages;
    }

    private void loadMainRates(
            HotelRfpBidOfferDetails offer, String primaryRatePrefix,
            Map<String, NegotiationValue> rates
    ) {
        final String occupancy = primaryRatePrefix.endsWith("S") ? "S" : "D";
        offer.lraRate = getRate("lra", occupancy, rates);
        offer.nlraRate = getRate("nlra", occupancy, rates);
        offer.dynamicRate = rates.containsKey("dyn") ? createValue.fromNegotiationValue(rates.get("dyn")) : createValue.unavailable();
    }

    private void loadAverageRates(
            HotelRfpBidOfferDetails offer, String primaryRatePrefix,
            List<Map<String, Object>> seasons,
            Map<String, NegotiationValue> rates
    ) {
        final String occupancy = primaryRatePrefix.endsWith("S") ? "S" : "D";
        final Map<Integer, Integer> seasonsDurations = calculateSeasonsDurations(seasons);
        offer.lraRate = calculateRateAverage("lra", occupancy, seasonsDurations, rates);
        offer.nlraRate = calculateRateAverage("nlra", occupancy, seasonsDurations, rates);
        offer.dynamicRate = rates.containsKey("dyn") ? createValue.fromNegotiationValue(rates.get("dyn")) : createValue.unavailable();
    }

    private Map<Integer, Integer> calculateSeasonsDurations(List<Map<String, Object>> seasons) {
        final Map<Integer, Integer> s = new HashMap<>();

        for(Map<String, Object> season : seasons){
            try {
                final LocalDate seasonStartDate = LocalDate.parse(String.valueOf(season.get("start")));
                final LocalDate seasonEndDate = LocalDate.parse(String.valueOf(season.get("end")));
                final long seasonDays = ChronoUnit.DAYS.between(seasonStartDate, seasonEndDate) + 1L;

                s.put(Integer.parseInt(String.valueOf(season.get("id"))), (int) seasonDays);
            } catch(DateTimeParseException ex){
                s.put(Integer.parseInt(String.valueOf(season.get("id"))), 0);
            }
        }

        return s;
    }

    private void loadAmenities(HotelRfpBidOfferDetails offer, Map<String, NegotiationValue> amenities, Value primaryRate) {
        offer.earlyCheckout = calculateEarlyCheckout(amenities.get("ec"), primaryRate);
        offer.parking = createValue.fromNegotiationValue(amenities.get("prk"));
        offer.breakfast = calculateBreakfast(amenities.get("bf"), primaryRate);
        offer.fitness = createValue.fromNegotiationValue(amenities.get("ft"));
        offer.internetAccess = createValue.fromNegotiationValue(amenities.get("ia"));
        offer.wiFi = createValue.fromNegotiationValue(amenities.get("wf"));
        offer.airportShuttle = createValue.fromNegotiationValue(amenities.get("as"));

        offer.amenitiesTotal = primaryRate != null && primaryRate.isAvailable()
                ? createValue.fixed(offer.earlyCheckout, offer.parking, offer.breakfast, offer.fitness, offer.internetAccess, offer.wiFi, offer.airportShuttle)
                : createValue.unavailable();
    }

    private void loadTaxes(HotelRfpBidOfferDetails offer, Map<String, NegotiationValue> taxes, Value primaryRate) {
        offer.lodgingTax = calculateTax(taxes.get("lodging"), primaryRate);
        offer.stateTax = calculateTax(taxes.get("state"), primaryRate);
        offer.cityTax = calculateTax(taxes.get("city"), primaryRate);
        offer.vatGstRm = calculateTax(taxes.get("vatGstRm"), primaryRate);
        offer.vatGstFb = calculateTax(taxes.get("vatGstFb"), primaryRate);
        offer.serviceTax = calculateTax(taxes.get("service"), primaryRate);
        offer.occupancyTax = calculateTax(taxes.get("occupancy"), primaryRate);
        offer.otherTax = calculateTax(taxes.get("other"), primaryRate);

        offer.taxesTotal = primaryRate != null && primaryRate.isAvailable()
                ? createValue.fixed(offer.lodgingTax, offer.stateTax, offer.cityTax, offer.vatGstRm, offer.vatGstFb, offer.serviceTax, offer.occupancyTax, offer.otherTax)
                : createValue.unavailable();
    }

    private void calculateTotalCost(HotelRfpBidOfferDetails offer, Value primaryRate) {
        offer.totalCost = primaryRate != null && primaryRate.isAvailable()
                ? createValue.fixed(primaryRate, offer.amenitiesTotal, offer.taxesTotal)
                : createValue.unavailable();
    }

    private Value calculateTax(NegotiationValue val, Value primaryRate){
        final Value tax = createValue.fromNegotiationValue(val);
        tax.calculateAuxiliaryAmount(primaryRate);
        return tax;
    }

    private Value getPrimaryRate(Value lraRate, Value nlraRate) {
        if(lraRate != null && lraRate.isAvailable()){
            return lraRate;
        } else if(nlraRate != null && nlraRate.isAvailable()) {
            return nlraRate;
        } else {
            return null;
        }
    }

    private Value getRate(
            String rate, String occupancy,
            Map<String, NegotiationValue> rates
    ) {
        final String RATE_PATTERN = "%s%s_s%s_rt%s";
        final NegotiationValue rateValue = rates.get(String.format(RATE_PATTERN, rate, occupancy, 1, 1));
        return rateValue != null && rateValue.valid ? createValue.fixed(rateValue.value) : createValue.unavailable();
    }

    private Value calculateRateAverage(
            String rate, String occupancy,
            Map<Integer, Integer> seasons,
            Map<String, NegotiationValue> rates
    ) {
        final String RATE_PATTERN = "%s%s_s%s_rt%s";
        BigDecimal sum = BigDecimal.ZERO;
        int days = 0;

        for(Integer seasonOrd : seasons.keySet()){
            final NegotiationValue rateValue = rates.get(String.format(RATE_PATTERN, rate, occupancy, seasonOrd, 1));
            if(rateValue != null && rateValue.valid){
                final Integer seasonDays = seasons.get(seasonOrd);
                sum = sum.add(rateValue.value.multiply(BigDecimal.valueOf(seasonDays)));
                days += seasonDays;
            } else {
                return createValue.unavailable();
            }
        }

        final BigDecimal amount = days == 0 ? BigDecimal.ZERO : sum.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);
        return createValue.fixed(amount);
    }

    private Value calculateBreakfast(NegotiationValue negBf, Value primaryRate) {
        final Value bf = createValue.fromNegotiationValue(negBf);
        if(bf.isDerived){
            bf.calculateDerivedAmount(primaryRate);
        }
        return bf;
    }

    private Value calculateEarlyCheckout(NegotiationValue negEc, Value primaryRate) {
        final Value ec = createValue.fromNegotiationValue(negEc);
        ec.calculateAuxiliaryAmount(primaryRate);
        return ec;
    }


    private BigDecimal calculateYearDifference(Map<String, Object> offer) {
        int randomNum = ThreadLocalRandom.current().nextInt(-5, 20);
        return new BigDecimal(randomNum);
    }

}
