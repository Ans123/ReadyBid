package net.readybid.app.interactors.rfp_hotel.bid.offer;

import net.readybid.app.entities.rfp_hotel.bid.offer.Value;
import net.readybid.rfphotel.bid.core.negotiations.values.NegotiationValue;

import java.math.BigDecimal;

public interface CreateValueAction {
    Value fromNegotiationValue(NegotiationValue negotiationValue);

    Value unavailable();

    Value fixed(BigDecimal amount);

    Value fixed(Value... values);
}
