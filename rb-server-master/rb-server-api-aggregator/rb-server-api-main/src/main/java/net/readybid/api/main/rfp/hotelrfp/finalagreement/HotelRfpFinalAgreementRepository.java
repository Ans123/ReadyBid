package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import net.readybid.entities.rfp.RateLoadingInformation;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.core.RfpStatus;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;

import java.util.List;

public interface HotelRfpFinalAgreementRepository {
    HotelRfpBid getFinalAgreementWithModelData(String bidId);

    HotelRfpBid send(HotelRfpBid bid, List<? extends RateLoadingInformation> information);

    String getTemplateFromRfp(String rfpId);

    Rfp updateTemplateInRfpIfNotInStates(String rfpId, String sanitizedTemplate, List<RfpStatus> states);

    void updateTemplateInSyncedRfpBids(String rfpId, String sanitizedTemplate);

    String getTemplateFromBid(String bidId);

    void updateTemplateInBidIfNotInStates(String bidId, String sanitizedTemplate, List<HotelRfpBidStateStatus> hotelRfpBidStatuses);
}
