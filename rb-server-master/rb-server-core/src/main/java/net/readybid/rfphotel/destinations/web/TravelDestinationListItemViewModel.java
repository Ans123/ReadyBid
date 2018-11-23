package net.readybid.rfphotel.destinations.web;


import net.readybid.app.core.entities.traveldestination.TravelDestinationType;
import net.readybid.web.ViewModel;

/**
 * Created by DejanK on 1/11/2017.
 *
 */
public class TravelDestinationListItemViewModel implements ViewModel {
    public String id;
    public String rfpId;
    public String rfpName;
    public TravelDestinationType type;
    public String name;
    public Integer estimatedRoomNights;
    public Long estimatedSpend;
    public String fullAddress;
    public long properties;
}
