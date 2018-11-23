package net.readybid.auth.invitation;


import net.readybid.mongodb.AbstractStatusDetailsRbMongoCodecWithProvider;
import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public class InvitationStatusDetailsCodecWithProvider extends AbstractStatusDetailsRbMongoCodecWithProvider<InvitationStatus, InvitationStatusDetails> {

    public InvitationStatusDetailsCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(bsonTypeClassMap, InvitationStatusDetails.class, InvitationStatus.class);
    }

    @Override
    protected InvitationStatusDetails newInstance() {
        return new InvitationStatusDetails();
    }
}