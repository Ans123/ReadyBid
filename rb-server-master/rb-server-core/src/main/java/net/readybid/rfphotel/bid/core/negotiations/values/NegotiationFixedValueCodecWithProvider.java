package net.readybid.rfphotel.bid.core.negotiations.values;

import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public class NegotiationFixedValueCodecWithProvider extends NegotiationValueCodecWithProvider<NegotiationFixedValue> {

    public NegotiationFixedValueCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(NegotiationFixedValue.class, bsonTypeClassMap);
    }

    @Override
    protected NegotiationFixedValue newInstance(){ return new NegotiationFixedValue();}
}