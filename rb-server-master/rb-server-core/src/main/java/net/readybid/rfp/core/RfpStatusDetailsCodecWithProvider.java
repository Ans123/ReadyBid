package net.readybid.rfp.core;

import net.readybid.mongodb.AbstractStatusDetailsRbMongoCodecWithProvider;
import net.readybid.rfp.core.RfpStatus;
import net.readybid.rfp.core.RfpStatusDetails;
import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 1/9/2017.
 *
 */
public class RfpStatusDetailsCodecWithProvider extends AbstractStatusDetailsRbMongoCodecWithProvider<RfpStatus, RfpStatusDetails> {

    public RfpStatusDetailsCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(bsonTypeClassMap, RfpStatusDetails.class, RfpStatus.class);
    }

    @Override
    protected RfpStatusDetails newInstance() {
        return new RfpStatusDetails();
    }
}