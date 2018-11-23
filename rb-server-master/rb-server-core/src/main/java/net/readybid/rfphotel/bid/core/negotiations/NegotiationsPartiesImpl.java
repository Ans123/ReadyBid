package net.readybid.rfphotel.bid.core.negotiations;

/**
 * Created by DejanK on 7/25/2017.
 *
 */
public class NegotiationsPartiesImpl implements NegotiationsParties {

    private Negotiator buyer;
    private Negotiator supplier;

    @Override
    public Negotiator getBuyer() {
        return buyer;
    }

    @Override
    public Negotiator getSupplier() {
        return supplier;
    }

    public void setBuyer(NegotiatorImpl buyer) {
        this.buyer = buyer;
    }

    public void setSupplier(NegotiatorImpl supplier) {
        this.supplier = supplier;
    }
}
