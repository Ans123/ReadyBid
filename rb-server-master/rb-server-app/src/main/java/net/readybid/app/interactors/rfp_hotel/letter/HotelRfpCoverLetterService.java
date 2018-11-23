package net.readybid.app.interactors.rfp_hotel.letter;

import net.readybid.rfp.core.Rfp;
import net.readybid.rfphotel.bid.core.HotelRfpBid;

public interface HotelRfpCoverLetterService  {

    void parseLetters(Rfp rfp);

    void parseLetters(HotelRfpBid bid, boolean showPlaceholders);
}
