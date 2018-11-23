package net.readybid.rfphotel.bid.core.negotiations;

import net.readybid.rfphotel.bid.core.negotiations.values.NegotiationValue;

import java.util.Map;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public class HotelRfpNegotiationValues implements NegotiationValues {
    public Map<String, NegotiationValue> rates;
    public Map<String, NegotiationValue> amenities;
    public NegotiationValue amenitiesTotal;
    public Map<String, NegotiationValue> taxes;
    public NegotiationValue taxesTotal;
    public Map<String, NegotiationValue> totalCosts;
}
