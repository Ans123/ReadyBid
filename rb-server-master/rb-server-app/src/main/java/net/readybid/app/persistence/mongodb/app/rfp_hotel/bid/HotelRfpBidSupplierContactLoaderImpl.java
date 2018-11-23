package net.readybid.app.persistence.mongodb.app.rfp_hotel.bid;

import com.mongodb.client.model.Aggregates;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier.HotelRfpBidSetSupplierContactAndSendBidCommandProducer;
import net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier.HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidSupplierContactLoader;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidRepository;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidSetBidContactDataRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpCollection;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.readybid.mongodb.RbMongoFilters.byIds;
import static net.readybid.mongodb.RbMongoFilters.include;

@Service
public class HotelRfpBidSupplierContactLoaderImpl implements HotelRfpBidSupplierContactLoader {

    private final HotelRfpBidRepository repository;
    private final HotelRfpBidSetBidContactDataRepository setBidContactDataRepository;

    @Autowired
    public HotelRfpBidSupplierContactLoaderImpl(
            HotelRfpBidRepository repository,
            HotelRfpBidSetBidContactDataRepository setBidContactDataRepository
    ) {
        this.repository = repository;
        this.setBidContactDataRepository = setBidContactDataRepository;
    }

    @Override
    public String getEntityId(EntityType type, List<String> bidsIds) {
        final List<? extends HotelRfpBid> bids = repository.find( byIds(bidsIds),
                include(
                        HotelRfpBidCollection.SUPPLIER_COMPANY_ENTITY_ID,
                        HotelRfpBidCollection.SUPPLIER_COMPANY_CHAIN_MASTER_ID
                ));
        final Function<HotelRfpBid, String> getEntityIdFunction = getEntityIdSupplierFunction(type);
        final Set<String> entityIds = bids.stream().map(getEntityIdFunction).collect(Collectors.toSet());
        return entityIds.size() == 1 ? entityIds.stream().findFirst().get() : null;
    }

    @Override
    public List<HotelRfpBidSetSupplierContactAndSendBidCommandProducer> loadSetBidContactData(List<String> bidsIds) {
        final List<HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl> list = setBidContactDataRepository.aggregate(
                Arrays.asList(
                        Aggregates.match(byIds(bidsIds)),
                        Aggregates.project(
                                new Document("bidType", "$hotelRfpType")
                                        .append("rfpId", "$rfp._id")
                                        .append("supplierContact", "$supplier.contact")
                                        .append("defaultResponse", "$questionnaire.response")
                                        .append("bidStatus", "$state.status")),
                        Aggregates.lookup(HotelRfpCollection.COLLECTION_NAME, "rfpId", "_id", "rfp"),
                        Aggregates.unwind("$rfp"),
                        Aggregates.project(
                                new Document("bidType", 1)
                                        .append("rfpId", 1)
                                        .append("supplierContact", 1)
                                        .append("defaultResponse", 1)
                                        .append("bidStatus", 1)
                                        .append("rfpChainSupport", "$rfp.specifications.chainSupport")
                                        .append("rfpName", "$rfp.specifications.name")
                                        .append("coverLetterTemplate", "$rfp.coverLetter")
                                        .append("namCoverLetterTemplate", "$rfp.namCoverLetter"))
                ));

        return new ArrayList<>(list);
    }

    private Function<HotelRfpBid, String> getEntityIdSupplierFunction(EntityType type) {
        if(type.equals(EntityType.CHAIN)){
            return HotelRfpBid::getSupplierCompanyMasterChainId;
        } else {
            return this::getSupplierCompanyEntityId;
        }
    }

    private String getSupplierCompanyEntityId(HotelRfpBid bid) {
        final ObjectId supplierCompanyEntityId = bid.getSupplierCompanyEntityId();
        return supplierCompanyEntityId == null ? null :  String.valueOf(supplierCompanyEntityId);
    }
}
