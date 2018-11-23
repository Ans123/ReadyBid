package net.readybid.entities.rfp;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class RateLoadingInformationImplCodecWithProvider extends RbMongoCodecWithProvider<RateLoadingInformationImpl>{

    public RateLoadingInformationImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(RateLoadingInformationImpl.class, bsonTypeClassMap);
    }

    @Override
    protected RateLoadingInformationImpl newInstance() {
        return new RateLoadingInformationImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<RateLoadingInformationImpl>> decodeMap) {
        decodeMap.put("arcIatas", (c, reader, context) -> c.setArcIatas(reader.readString()));
        decodeMap.put("gdsName", (c, reader, context) -> c.setGdsName(reader.readString()));
        decodeMap.put("pseudoCityCode", (c, reader, context) -> c.setPseudoCityCode(reader.readString()));
        decodeMap.put("rateAccessCode", (c, reader, context) -> c.setRateAccessCode(reader.readString()));

    }

    @Override
    public void encodeDocument(Document d, RateLoadingInformationImpl rateLoadingInformation, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "arcIatas", rateLoadingInformation.getArcIatas());
        putIfNotNull(d, "gdsName", rateLoadingInformation.getGdsName());
        putIfNotNull(d, "pseudoCityCode", rateLoadingInformation.getPseudoCityCode());
        putIfNotNull(d, "rateAccessCode", rateLoadingInformation.getRateAccessCode());
    }
}
