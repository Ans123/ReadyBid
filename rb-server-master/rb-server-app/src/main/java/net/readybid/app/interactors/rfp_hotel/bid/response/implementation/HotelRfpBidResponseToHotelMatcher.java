package net.readybid.app.interactors.rfp_hotel.bid.response.implementation;

import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.app.interactors.core.entity.gate.HotelLoader;
import net.readybid.entities.Id;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
class HotelRfpBidResponseToHotelMatcher {

    final static List<String> CODES_TO_CHECK = Arrays.asList("SABRE_PROPCODE", "AMADEUS_PROPCODE", "APOLLO_PROPCODE", "WRLDSPAN_PROPCODE", "PROPCODE", "INTERNALHOTELCODE");

    private final HotelLoader hotelLoader;

    @Autowired
    HotelRfpBidResponseToHotelMatcher(
            HotelLoader hotelLoader
    ) {
        this.hotelLoader = hotelLoader;
    }

    Map<HotelRfpBid, Map<String, String>> matchBidsToResponses(List<HotelRfpBid> bids, List<Map<String, String>> validatedResponses) {
        if(bids == null || bids.isEmpty() || validatedResponses == null || validatedResponses.isEmpty()) return new HashMap<>();
        final Map<String, HotelRfpBid> bidsMap = mapBids(bids);
        final Map<Id, Map<String, String>> hotelsCodes = loadPropertyCodes(bidsMap);
        return matchBidsWithResponses(bidsMap, hotelsCodes, validatedResponses);
    }

    private Map<Id, Map<String,String>> loadPropertyCodes(Map<String, HotelRfpBid> bids) { 
    	final List<Id> hotelsIds = bids.values().stream().map(b -> Id.valueOf(b.getSupplierCompanyEntityId()))
                .collect(Collectors.toList());

        return hotelLoader.getPropertyCodes(hotelsIds)
                .stream().collect(Collectors.toMap(h -> Id.valueOf(h.getId()), Hotel::getAnswers));
    }

    private static Map<HotelRfpBid, Map<String, String>> matchBidsWithResponses(Map<String, HotelRfpBid> bidsMap, Map<Id, Map<String, String>> hotelsCodes, List<Map<String, String>> validatedResponses) {
        final Map<HotelRfpBid, Map<String, String>> matchedResponses = new HashMap<>();
        for(String bidId : bidsMap.keySet()) {
            final HotelRfpBid bid = bidsMap.get(bidId);
            final Map<String, String> hotelCodes = hotelsCodes.get(Id.valueOf(bid.getSupplierCompanyEntityId()));
            final Map<String, String> response = matchResponseHotelCodesToSavedHotelCodes(validatedResponses, hotelCodes);
            if(response != null) {
                response.put("PROPCODE", hotelCodes.get("PROPCODE"));
                matchedResponses.put(bid, response);
            }
        }
        return matchedResponses;
    }

    private static Map<String, HotelRfpBid> mapBids(List<HotelRfpBid> bids) {
        return bids.stream().collect(Collectors.toMap(b -> String.valueOf(b.getId()), b -> b ));
    }

    private static Map<String, String> matchResponseHotelCodesToSavedHotelCodes(List<Map<String, String>> responses, Map<String, String> hotelCodes) {
        if(hotelCodes == null || hotelCodes.isEmpty()) return null;
        for(Map<String, String> response : responses){
            for(String codeId : CODES_TO_CHECK) {
                final String hotelCode = hotelCodes.get(codeId);
                final String responseCode = response.get(codeId);
                if(areCodesEqual(hotelCode, responseCode)) return response;
            }
        }

        return null;
    }

    private static boolean areCodesEqual(String hotelCode, String responseCode) {
        if(hotelCode == null || hotelCode.isEmpty()) return false;
        if(responseCode == null || responseCode.isEmpty()) return false;
        return areCodesEqualAsStrings(hotelCode, responseCode) || areCodesEqualAsNumbers(hotelCode, responseCode);
    }

    private static boolean areCodesEqualAsStrings(String hotelCode, String responseCode) {
        return hotelCode.equalsIgnoreCase(responseCode);
    }

    private static boolean areCodesEqualAsNumbers(String hotelCode, String responseCode) {
        try {
            final long hotelCodeAsLong = Long.parseLong(hotelCode);
            final long responseCodeAsLong = Long.parseLong(responseCode);
            return hotelCodeAsLong == responseCodeAsLong;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
}
