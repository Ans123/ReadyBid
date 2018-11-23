package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp.questionnaire.QuestionnaireImpl;
import net.readybid.app.core.entities.traveldestination.TravelDestinationHotelFilter;
import net.readybid.entities.rfp.RateLoadingInformationImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.rfp.core.RfpImpl;
import net.readybid.rfp.core.RfpStatusDetails;
import net.readybid.rfp.specifications.RfpSpecificationsImpl;
import net.readybid.utils.CreationDetails;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public class RfpImplCodecWithProvider extends RbMongoCodecWithProvider<RfpImpl> {

    public RfpImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(RfpImpl.class, bsonTypeClassMap);
    }

    @Override
    protected RfpImpl newInstance() {
        return new RfpImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<RfpImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("specifications", (c, reader, context) -> c.setSpecifications(read(reader, context, RfpSpecificationsImpl.class)));
        decodeMap.put("coverLetter", (c, reader, context) -> c.setCoverLetter(reader.readString()));
        decodeMap.put("namCoverLetter", (c, reader, context) -> c.setNamCoverLetter(reader.readString()));
        decodeMap.put("questionnaire", (c, reader, context) -> c.setQuestionnaire(read(reader, context, QuestionnaireImpl.class)));
        decodeMap.put("defaultFilter", (c, reader, context) -> c.setDefaultFilter(read(reader, context, TravelDestinationHotelFilter.class)));
        decodeMap.put("finalAgreement", (c, reader, context) -> c.setFinalAgreementTemplate(reader.readString()));
        decodeMap.put("rateLoadingInformation", (c, reader, context) -> c.setRateLoadingInformation(readListOfObjects(reader, context, RateLoadingInformationImpl.class)));
        decodeMap.put("created", (c, reader, context) -> c.setCreated(read(reader, context, CreationDetails.class)));
        decodeMap.put("status", (c, reader, context) -> c.setStatus(read(reader, context, RfpStatusDetails.class)));
    }

    @Override
    public void encodeDocument(Document d, RfpImpl rfp, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "_id", rfp.getId());
        putIfNotNull(d, "specifications", rfp.getSpecifications());
        putIfNotNull(d, "coverLetter", rfp.getCoverLetter());
        putIfNotNull(d, "namCoverLetter", rfp.getNamCoverLetter());
        putIfNotNull(d, "questionnaire", rfp.getQuestionnaire());
        putIfNotNull(d, "defaultFilter", rfp.getDefaultFilter());
        putIfNotNull(d, "finalAgreement", rfp.getFinalAgreementTemplate());
        putIfNotNull(d, "created", rfp.getCreated());
        putIfNotNull(d, "status",rfp.getStatus());
    }
}
