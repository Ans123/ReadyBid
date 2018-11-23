package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfphotel.bid.core.HotelRfpBid;

public interface HotelRfpFinalAgreementService {

    String getFromBid(String bidId);

    void parseFinalAgreementWithDataOrPlaceholders(Rfp rfp);

    void parseFinalAgreementWithDataOrPlaceholders(HotelRfpBid bid);

    HotelRfpBid sendFinalAgreement(HotelRfpBid bid, AuthenticatedUser user);

    String getTemplateFromRfp(String rfpId);

    void updateTemplateInRfp(String rfpId, String sanitizedTemplate);

    String getTemplateFromBid(String bidId);

    void updateTemplateInBid(String bidId, String sanitizedTemplate);
}
