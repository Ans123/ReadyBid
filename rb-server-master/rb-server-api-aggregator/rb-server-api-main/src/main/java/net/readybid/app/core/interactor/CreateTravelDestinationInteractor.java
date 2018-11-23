package net.readybid.app.core.interactor;

import net.readybid.app.interactors.core.gate.IdFactory;
import net.readybid.app.core.action.traveldestination.CreateTravelDestinationAction;
import net.readybid.app.core.action.traveldestination.CreateTravelDestinationRequest;
import net.readybid.app.core.entities.location.distance.DistanceImpl;
import net.readybid.app.core.entities.location.distance.DistanceUnit;
import net.readybid.app.core.entities.traveldestination.*;
import net.readybid.core.rfp.hotel.traveldestination.UpdateTravelDestinationFilterRequest;
import net.readybid.core.rfp.hotel.traveldestination.UpdateTravelDestinationRequest;
import net.readybid.user.BasicUserDetails;
import net.readybid.utils.CreationDetails;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by DejanK on 4/4/2017.
 *
 */

public class CreateTravelDestinationInteractor implements CreateTravelDestinationAction {

    final private IdFactory idFactory;

    public CreateTravelDestinationInteractor(IdFactory idFactory){
        this.idFactory = idFactory;
    }

    @Override
    public TravelDestinationImpl create(CreateTravelDestinationRequest requestModel, TravelDestinationHotelFilter rfpDefaultFilter)  {
        final TravelDestinationImpl destination = new TravelDestinationImpl();

        destination.setId(idFactory.create());
        destination.setRfpId(requestModel.rfpId);
        destination.setType(requestModel.type);
        destination.setName(requestModel.name);
        destination.setEstimatedSpend(requestModel.estimatedSpend);
        destination.setEstimatedRoomNights(requestModel.estimatedRoomNights);
        destination.setLocation(requestModel.location);
        destination.setFilter(rfpDefaultFilter == null ? createFilter(requestModel.type) : rfpDefaultFilter);

        destination.setCreationDetails(new CreationDetails(requestModel.creator));
        destination.setStatusDetails(new TravelDestinationStatusDetails(destination.getCreationDetails()));

        return destination;
    }

    @Override
    public List<TravelDestinationImpl> createAll(List<CreateTravelDestinationRequest> createTravelDestinationRequests, TravelDestinationHotelFilter rfpDefaultFilter) {
        return createTravelDestinationRequests.stream().map(r -> create(r, rfpDefaultFilter)).collect(Collectors.toList());
    }

    @Override
    public void update(TravelDestination destination, UpdateTravelDestinationRequest model) {
        final TravelDestinationImpl d = (TravelDestinationImpl) destination;

        d.setType(model.type);
        d.setName(model.name);
        d.setEstimatedSpend(model.estimatedSpend);
        d.setEstimatedRoomNights(model.estimatedRoomNights);
        d.setLocation(model.location);
        d.setStatusDetails(new TravelDestinationStatusDetails(model.creator, TravelDestinationStatus.UPDATED));
    }

    @Override
    public TravelDestinationStatusDetails createStatusDetails(TravelDestinationStatus status, BasicUserDetails currentUser) {
        return new TravelDestinationStatusDetails(status, currentUser);
    }

    @Override
    public TravelDestinationHotelFilter createFilter(UpdateTravelDestinationFilterRequest model) {
        final TravelDestinationHotelFilter filter = new TravelDestinationHotelFilter();

        filter.maxDistance = new DistanceImpl(model.maxDistance.value, model.maxDistance.unit);
        filter.amenities = model.amenities;
        filter.chains = model.chains;

        return filter;
    }

    private TravelDestinationHotelFilter createFilter(TravelDestinationType type) {
        final TravelDestinationHotelFilter filter = new TravelDestinationHotelFilter();
        switch (type){
            case CITY:
                filter.maxDistance = new DistanceImpl(10, DistanceUnit.MI);
                break;
            case OFFICE:
            default:
        }
        return filter;
    }
}
