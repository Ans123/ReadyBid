package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 10/23/2017.
 *
 */
public class QuestionnaireConfigurationItemCodecWithProvider extends RbMongoCodecWithProvider<QuestionnaireConfigurationItem> {

    private static final String FIELD_ID = "id";
    private static final String FIELD_DATA = "data";

    public QuestionnaireConfigurationItemCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireConfigurationItem.class, bsonTypeClassMap);
    }

    @Override
    protected QuestionnaireConfigurationItem newInstance() {
        return new QuestionnaireConfigurationItem();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<QuestionnaireConfigurationItem>> decodeMap) {
        decodeMap.put(FIELD_ID, (c, reader, context) -> c.id = reader.readString());
        decodeMap.put(FIELD_DATA, (c, reader, context) -> c.data = readMap(reader));
    }

    @Override
    public void encodeDocument(Document d, QuestionnaireConfigurationItem tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, FIELD_ID, tObject.id);
        putIfNotNull(d, FIELD_DATA, tObject.data);
    }
}
