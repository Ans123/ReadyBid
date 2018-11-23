package net.readybid.core.rfp.hotel.traveldestination;

import net.readybid.app.core.action.traveldestination.CreateTravelDestinationAction;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.core.entities.traveldestination.TravelDestinationStatus;
import net.readybid.app.core.entities.traveldestination.TravelDestinationStatusDetails;
import net.readybid.app.core.service.traveldestination.LoadTravelDestinationRepository;
import net.readybid.app.core.service.traveldestination.SaveTravelDestinationRepository;
import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.app.entities.rfp_hotel.dirty.DeleteBidRequest;
import net.readybid.app.entities.rfp_hotel.dirty.DeleteBidsResult;
import net.readybid.core.rfp.hotel.bid.DeleteBidsTransaction;

public class DeleteTravelDestinationTransactionImpl implements DeleteTravelDestinationTransaction {

    private final DeleteBidsTransaction deleteBidsTransaction;
    private final LoadTravelDestinationRepository loadDestinationRepository;
    private final SaveTravelDestinationRepository saveDestinationRepository;
    private final CreateTravelDestinationAction destinationFactory;

    public DeleteTravelDestinationTransactionImpl(
            DeleteBidsTransaction deleteBidsTransaction,
            LoadTravelDestinationRepository loadDestinationRepository,
            SaveTravelDestinationRepository saveDestinationRepository, CreateTravelDestinationAction destinationFactory) {
        this.deleteBidsTransaction = deleteBidsTransaction;
        this.loadDestinationRepository = loadDestinationRepository;
        this.saveDestinationRepository = saveDestinationRepository;
        this.destinationFactory = destinationFactory;
    }

    @Override
    public DeleteTravelDestinationResult delete(DeleteTravelDestinationRequest model) {
        final TravelDestination destination = loadDestinationRepository.getById(model.destinationId);
        if(destination == null) throw new NotFoundException();

        final DeleteBidsResult deleteBidsResult = deleteBidsTransaction
                .delete(new DeleteBidRequest(destination.getRfpId(), model.destinationId, model.currentUser));

        if(deleteBidsResult.failCount == 0){
            final TravelDestinationStatusDetails deletedStatus =
                    destinationFactory.createStatusDetails(TravelDestinationStatus.DELETED, model.currentUser);
            saveDestinationRepository.markAsDeleted(destination.getId(), deletedStatus);
        }
        return new DeleteTravelDestinationResult(deleteBidsResult.successCount, deleteBidsResult.failCount);
    }
}
