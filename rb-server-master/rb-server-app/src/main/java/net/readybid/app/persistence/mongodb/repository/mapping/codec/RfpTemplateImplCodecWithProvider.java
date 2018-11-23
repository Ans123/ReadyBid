package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp.questionnaire.QuestionnaireImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.rfp.template.RfpTemplateImpl;
import net.readybid.rfp.type.RfpType;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 12/25/2016.
 *
 */
public class RfpTemplateImplCodecWithProvider extends RbMongoCodecWithProvider<RfpTemplateImpl> {

    public RfpTemplateImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(RfpTemplateImpl.class, bsonTypeClassMap);
    }

    @Override
    protected RfpTemplateImpl newInstance() {
        return new RfpTemplateImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<RfpTemplateImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("name", (c, reader, context) -> c.setName(reader.readString()));
        decodeMap.put("type", (c, reader, context) -> c.setType(read(reader, context, RfpType.class)));
        decodeMap.put("description", (c, reader, context) -> c.setDescription(reader.readString()));
        decodeMap.put("coverLetter", (c, reader, context) -> c.setCoverLetter(reader.readString()));
        decodeMap.put("questionnaire", (c, reader, context) -> c.setQuestionnaire(read(reader, context, QuestionnaireImpl.class)));
        decodeMap.put("finalAgreement", (c, reader, context) -> c.setFinalAgreementTemplate(reader.readString()));
    }

    @Override
    public void encodeDocument(Document document, RfpTemplateImpl template, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(document, "_id", template.getId());
        putIfNotNull(document, "name", template.getName());
        putIfNotNull(document, "type", template.getType());
        putIfNotNull(document, "description", template.getDescription());
        putIfNotNull(document, "coverLetter", template.getCoverLetter());
        putIfNotNull(document, "questionnaire", template.getQuestionnaire());
        putIfNotNull(document, "finalAgreement", template.getFinalAgreementTemplate());
    }
}
