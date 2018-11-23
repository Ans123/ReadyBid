package net.readybid.app.core.transaction;

import net.readybid.api.main.access.RfpAccessControlService;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.core.service.ListBidsRepository;
import net.readybid.app.core.service.traveldestination.GetTravelDestinationService;
import net.readybid.app.core.service.traveldestination.LoadTravelDestinationRepository;
import net.readybid.app.core.service.traveldestination.ListTravelDestinationsResult;

import java.util.List;
import java.util.Map;

public class GetTravelDestinationTransaction implements GetTravelDestinationService {

    private final RfpAccessControlService rfpAccessControlService;
    private final LoadTravelDestinationRepository loadDestinationRepository;
    private final ListBidsRepository listBidsRepository;

    public GetTravelDestinationTransaction(
            RfpAccessControlService rfpAccessControlService,
            LoadTravelDestinationRepository loadDestinationRepository,
            ListBidsRepository listBidsRepository) {
        this.rfpAccessControlService = rfpAccessControlService;
        this.loadDestinationRepository = loadDestinationRepository;
        this.listBidsRepository = listBidsRepository;
    }

    @Override
    public TravelDestination get(String rfpId, String destinationId) {
        rfpAccessControlService.read(rfpId);
        return loadDestinationRepository.getById(destinationId);
    }

    @Override
    public ListTravelDestinationsResult listRfpDestinations(String rfpId) {
        rfpAccessControlService.read(rfpId);
        final List<? extends TravelDestination> destinations = loadDestinationRepository.listByRfpId(rfpId);
        final Map<String, Long> bidsPerTravelDestination =
                listBidsRepository.getBidsCountPerDestinationForRfp(rfpId);

        return new ListTravelDestinationsResult(destinations, bidsPerTravelDestination);
    }
}
