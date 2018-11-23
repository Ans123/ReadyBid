package net.readybid.rfphotel.destinations.db;

import net.readybid.mongodb.AbstractStatusDetailsRbMongoCodecWithProvider;
import net.readybid.app.core.entities.traveldestination.TravelDestinationStatus;
import net.readybid.app.core.entities.traveldestination.TravelDestinationStatusDetails;
import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 1/12/2017.
 *
 */
public class TravelDestinationStatusDetailsCodecWithProvider extends AbstractStatusDetailsRbMongoCodecWithProvider<TravelDestinationStatus, TravelDestinationStatusDetails> {

    public TravelDestinationStatusDetailsCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(bsonTypeClassMap, TravelDestinationStatusDetails.class, TravelDestinationStatus.class);
    }

    @Override
    protected TravelDestinationStatusDetails newInstance() {
        return new TravelDestinationStatusDetails();
    }
}
