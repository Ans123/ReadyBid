package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import net.readybid.rfp.core.Rfp;
import net.readybid.rfphotel.bid.core.HotelRfpBid;

import java.util.Map;

public interface HotelRfpLetterPlaceholdersFactory {
    Map<String,String> getFromBid(HotelRfpBid bid);

    Map<String,String> getFromRfpWithDataOrPlaceholders(Rfp rfp);

    Map<String,String> getFromBidWithDataOrPlaceholders(HotelRfpBid bid);

    Map<String,String> getFromBidWithData(HotelRfpBid bid);
}
