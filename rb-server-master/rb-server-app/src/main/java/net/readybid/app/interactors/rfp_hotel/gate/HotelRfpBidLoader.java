package net.readybid.app.interactors.rfp_hotel.gate;

import net.readybid.rfphotel.bid.core.HotelRfpBid;

import java.time.LocalDate;
import java.util.List;

public interface HotelRfpBidLoader {
    LocalDate getExpiredBidDueDate(List<String> bidsIds);

    List<HotelRfpBid> listTravelDestinationProperties(String rfpId, String destinationId);

    List<HotelRfpBid> getResponseAnswers(List<String> bidsIds);

    List<HotelRfpBid> getFinalAgreements(List<String> bidsIds, boolean includeUnsent);
}
