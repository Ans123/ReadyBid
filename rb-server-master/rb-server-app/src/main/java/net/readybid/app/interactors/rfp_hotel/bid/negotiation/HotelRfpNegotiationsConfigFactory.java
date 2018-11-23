package net.readybid.app.interactors.rfp_hotel.bid.negotiation;

import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationsConfig;

import java.util.Map;

/**
 * Created by DejanK on 7/26/2017.
 *
 */
public interface HotelRfpNegotiationsConfigFactory {
    HotelRfpNegotiationsConfig create(Map<String, String> response);
}
