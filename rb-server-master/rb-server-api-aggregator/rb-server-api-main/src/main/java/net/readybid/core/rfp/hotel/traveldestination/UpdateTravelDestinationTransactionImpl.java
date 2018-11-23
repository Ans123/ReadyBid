package net.readybid.core.rfp.hotel.traveldestination;

import net.readybid.app.core.action.traveldestination.CreateTravelDestinationAction;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.core.entities.traveldestination.TravelDestinationHotelFilter;
import net.readybid.app.core.entities.traveldestination.TravelDestinationImpl;
import net.readybid.app.core.service.traveldestination.LoadTravelDestinationRepository;
import net.readybid.app.core.service.traveldestination.SaveTravelDestinationRepository;
import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.core.rfp.hotel.bid.UpdateBidTravelDestinationRequest;
import net.readybid.core.rfp.hotel.bid.UpdateBidsTransaction;

public class UpdateTravelDestinationTransactionImpl implements UpdateTravelDestinationTransaction {

    private final CreateTravelDestinationAction travelDestinationFactory;
    private final LoadTravelDestinationRepository loadDestinationsRepository;
    private final SaveTravelDestinationRepository saveDestinationsRepository;
    private final UpdateBidsTransaction updateBidsTransaction;

    public UpdateTravelDestinationTransactionImpl(
            CreateTravelDestinationAction travelDestinationFactory,
            LoadTravelDestinationRepository loadDestinationsRepository, SaveTravelDestinationRepository saveDestinationsRepository,
            UpdateBidsTransaction updateBidsTransaction
    ) {
        this.travelDestinationFactory = travelDestinationFactory;
        this.loadDestinationsRepository = loadDestinationsRepository;
        this.saveDestinationsRepository = saveDestinationsRepository;
        this.updateBidsTransaction = updateBidsTransaction;
    }

    @Override
    public TravelDestination update(UpdateTravelDestinationRequest model) {
        final TravelDestinationImpl destination = loadDestinationsRepository.getById(model.destinationId);
        if(destination == null) throw new NotFoundException();

        final boolean coordinatesChange = !destination.hasCoordinates(model.location.getCoordinates());
        travelDestinationFactory.update(destination, model);
        saveDestinationsRepository.save(destination);
        updateBidsTransaction.updateTravelDestination(new UpdateBidTravelDestinationRequest(destination, coordinatesChange));
        return destination;
    }

    @Override
    public void updateFilter(UpdateTravelDestinationFilterRequest model) {
        final TravelDestinationHotelFilter filter = travelDestinationFactory.createFilter(model);
        saveDestinationsRepository.setFilter(model.destinationId, filter);
    }
}
