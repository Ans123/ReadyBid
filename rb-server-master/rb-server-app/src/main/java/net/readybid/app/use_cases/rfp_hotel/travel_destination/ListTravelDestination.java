package net.readybid.app.use_cases.rfp_hotel.travel_destination;

import net.readybid.web.RbViewModel;

public interface ListTravelDestination {

    RbViewModel listProperties(String rfpId, String destinationId);
}
