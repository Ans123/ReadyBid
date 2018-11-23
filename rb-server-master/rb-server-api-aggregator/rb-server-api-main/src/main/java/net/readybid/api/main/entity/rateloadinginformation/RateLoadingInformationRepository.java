package net.readybid.api.main.entity.rateloadinginformation;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpRateLoadingInformationCollection;
import net.readybid.entities.rfp.EntityRateLoadingInformation;
import net.readybid.entities.rfp.RateLoadingInformation;
import org.bson.types.ObjectId;

import java.util.List;

public interface RateLoadingInformationRepository {
    String COLLECTION = HotelRfpRateLoadingInformationCollection.COLLECTION_NAME;

    EntityRateLoadingInformation getByEntityId(ObjectId entityId);

    EntityRateLoadingInformation getByEntityId(String entityId);

    void update(String entityId, EntityType type, List<RateLoadingInformation> information);


}
