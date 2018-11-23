package net.readybid.app.gate.api.rfps.traveldestinations.get;

import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.core.entities.traveldestination.TravelDestinationType;
import net.readybid.location.LocationViewModel;
import net.readybid.web.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 1/11/2017.
 *
 */
@SuppressWarnings("WeakerAccess")
public class TravelDestinationListItemView implements ViewModel {

    public String id;
    public String rfpId;
    public TravelDestinationType type;
    public String name;
    public Integer estimatedRoomNights;
    public Long estimatedSpend;
    public LocationViewModel location;
    public long properties;

    public static List<TravelDestinationListItemView> fromList(
            List<? extends TravelDestination> destinations,
            Map<String, Long> bidsPerTravelDestination
    ) {
        final List<TravelDestinationListItemView> viewList = new ArrayList<>();
        for(TravelDestination destination : destinations){
            final TravelDestinationListItemView view = new TravelDestinationListItemView(destination,
                    bidsPerTravelDestination.getOrDefault(destination.getId(), 0L));
            viewList.add(view);
        }
        return viewList;
    }

    private TravelDestinationListItemView(TravelDestination destination, Long propertiesCount) {
        id = destination.getId();
        rfpId = destination.getRfpId();
        type = destination.getType();
        name = destination.getName();
        estimatedRoomNights = destination.getEstimatedRoomNights();
        estimatedSpend = destination.getEstimatedSpend();
        location = LocationViewModel.FACTORY.createAsPartial(destination.getLocation());
        properties = propertiesCount;
    }
}
