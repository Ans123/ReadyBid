package net.readybid.app.core.transaction;

import net.readybid.api.main.access.RfpAccessControlService;
import net.readybid.app.interactors.rfp_hotel.gateway_dirty.RfpRepository;
import net.readybid.app.core.action.traveldestination.CreateTravelDestinationAction;
import net.readybid.app.core.action.traveldestination.CreateTravelDestinationRequest;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.core.entities.traveldestination.TravelDestinationHotelFilter;
import net.readybid.app.core.entities.traveldestination.TravelDestinationImpl;
import net.readybid.app.core.service.traveldestination.CreateTravelDestinationService;
import net.readybid.app.core.service.traveldestination.SaveTravelDestinationRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CreateTravelDestinationTransaction implements CreateTravelDestinationService {

    private final RfpAccessControlService rfpAccessControlService;
    private final CreateTravelDestinationAction createTravelDestinationAction;
    private final SaveTravelDestinationRepository destinationRepository;
    private final RfpRepository rfpRepository;

    public CreateTravelDestinationTransaction(
            RfpAccessControlService rfpAccessControlService,
            CreateTravelDestinationAction createTravelDestinationAction,
            SaveTravelDestinationRepository destinationRepository,
            RfpRepository rfpRepository) {
        this.rfpAccessControlService = rfpAccessControlService;
        this.createTravelDestinationAction = createTravelDestinationAction;
        this.destinationRepository = destinationRepository;
        this.rfpRepository = rfpRepository;
    }

    @Override
    public TravelDestination create(CreateTravelDestinationRequest requestModel) {
        rfpAccessControlService.update(requestModel.rfpId);
        // todo: RFP AUDIT "TRAVEL DESTINATION ADDED"
        final TravelDestinationHotelFilter rfpDefaultFilter = rfpRepository.getRfpDefaultFilter(requestModel.rfpId);
        final TravelDestinationImpl destination = createTravelDestinationAction.create(requestModel, rfpDefaultFilter);
        destinationRepository.create(destination);
        return destination;
    }

    @Override
    public List<String> createAll(String rfpId, List<CreateTravelDestinationRequest> createModel) {
        rfpAccessControlService.update(rfpId);
        final TravelDestinationHotelFilter rfpDefaultFilter = rfpRepository.getRfpDefaultFilter(rfpId);
        final List<TravelDestinationImpl> destinations = createTravelDestinationAction.createAll(createModel, rfpDefaultFilter);
        destinationRepository.createAll(destinations);

        return destinations.stream().map(TravelDestination::getId).collect(Collectors.toList());
    }
}
