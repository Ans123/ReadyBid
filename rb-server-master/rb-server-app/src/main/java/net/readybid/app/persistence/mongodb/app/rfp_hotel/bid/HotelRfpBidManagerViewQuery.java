package net.readybid.app.persistence.mongodb.app.rfp_hotel.bid;

import org.bson.conversions.Bson;

class HotelRfpBidManagerViewQuery {

    public final Bson filter;
    public final Bson projection;


    HotelRfpBidManagerViewQuery(Bson filter, Bson projection) {
        this.filter = filter;
        this.projection = projection;
    }
}
