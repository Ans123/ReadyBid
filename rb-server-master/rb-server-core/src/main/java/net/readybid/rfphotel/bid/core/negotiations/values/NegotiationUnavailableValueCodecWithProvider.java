package net.readybid.rfphotel.bid.core.negotiations.values;

import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public class NegotiationUnavailableValueCodecWithProvider extends NegotiationValueCodecWithProvider<NegotiationUnavailableValue> {

    public NegotiationUnavailableValueCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(NegotiationUnavailableValue.class, bsonTypeClassMap);
    }

    @Override
    protected NegotiationUnavailableValue newInstance(){ return new NegotiationUnavailableValue();}
}