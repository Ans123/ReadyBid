package net.readybid.app.persistence.mongodb.app.rfp_hotel.bid;

import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidImpl;
import net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier.HotelRfpBidSetSupplierContactCommand;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidSupplierContactStorageManager;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidRepository;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.*;
import static net.readybid.mongodb.RbMongoFilters.byId;
import static net.readybid.mongodb.RbMongoFilters.byState;

@Service
public class HotelRfpBidSupplierContactStorageManagerImpl implements HotelRfpBidSupplierContactStorageManager {

    private final HotelRfpBidRepository repository;

    @Autowired
    public HotelRfpBidSupplierContactStorageManagerImpl(HotelRfpBidRepository repository) {
        this.repository = repository;
    }

    @Override
    public void setContactAndSendBid(List<HotelRfpBidSetSupplierContactCommand> commands) {
        final List<WriteModel<HotelRfpBidImpl>> writes = new ArrayList<>();

        for(HotelRfpBidSetSupplierContactCommand c : commands){
            final Bson filter = and( byId(c.bidId), byState(c.currentBidStatus) );
            final List<Bson> updates = new ArrayList<>();
            addContactUpdate(updates, c);
            addBidTypeUpdate(updates, c);
            addSendBidUpdate(updates, c);
            addResponseDraftUpdate(updates, c);
            writes.add(new UpdateOneModel<>(filter, combine(updates)));
        }

        repository.bulkWrite(writes, new BulkWriteOptions().ordered(false));
    }

    private void addResponseDraftUpdate(List<Bson> updates, HotelRfpBidSetSupplierContactCommand command) {
        if(command.shouldResetResponse) updates.add(set(QUESTIONNAIRE_RESPONSE_DRAFT, command.response));
    }

    private void addSendBidUpdate(List<Bson> updates, HotelRfpBidSetSupplierContactCommand command) {
        if(command.shouldSendBid) {
            updates.add(set(STATE, command.bidState));
            updates.add(set(SENT_DATE, command.sentDate));
        }
    }

    private void addBidTypeUpdate(List<Bson> updates, HotelRfpBidSetSupplierContactCommand command) {
        if(command.shouldChangeBidType) {
            updates.add(set(BID_HOTEL_RFP_TYPE, command.bidType));
            updates.add(set(COVER_LETTER, command.coverLetter));
        }
    }

    private void addContactUpdate(List<Bson> updates, HotelRfpBidSetSupplierContactCommand command) {
        if(command.shouldUpdateContact)
            updates.add(set(SUPPLIER_CONTACT, command.supplierContact));
    }
}
