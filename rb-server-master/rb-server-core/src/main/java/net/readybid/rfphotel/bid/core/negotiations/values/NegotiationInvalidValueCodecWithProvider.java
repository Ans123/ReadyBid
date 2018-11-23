package net.readybid.rfphotel.bid.core.negotiations.values;

import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public class NegotiationInvalidValueCodecWithProvider extends NegotiationValueCodecWithProvider<NegotiationInvalidValue> {

    public NegotiationInvalidValueCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(NegotiationInvalidValue.class, bsonTypeClassMap);
    }

    @Override
    protected NegotiationInvalidValue newInstance(){ return new NegotiationInvalidValue();}
}