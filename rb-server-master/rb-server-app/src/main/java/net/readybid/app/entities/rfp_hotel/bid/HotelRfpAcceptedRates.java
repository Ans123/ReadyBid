package net.readybid.app.entities.rfp_hotel.bid;

import net.readybid.app.entities.IBuilder;
import net.readybid.entities.Id;

import java.util.Date;
import java.util.List;

public class HotelRfpAcceptedRates {

    public final List<String> acceptedRates;
    public final Date at;
    public final Id by;

    private HotelRfpAcceptedRates(Builder b){
        acceptedRates = b.acceptedRates;
        at = b.at == null ? new Date() : b.at;
        by = b.by;
    }

    public boolean containsRates() {
        return !(acceptedRates == null || acceptedRates.isEmpty());
    }

    public static class Builder implements IBuilder<HotelRfpAcceptedRates> {

        private List<String> acceptedRates;
        private Id by;
        private Date at;

        public Builder setAcceptedRates(List<String> acceptedRates) {
            this.acceptedRates = acceptedRates;
            return this;
        }

        public Builder setBy(Id by) {
            this.by = by;
            return this;
        }

        public Builder setAt(Date at) {
            this.at = at;
            return this;
        }

        @Override
        public HotelRfpAcceptedRates build() {
            return new HotelRfpAcceptedRates(this);
        }
    }
}
