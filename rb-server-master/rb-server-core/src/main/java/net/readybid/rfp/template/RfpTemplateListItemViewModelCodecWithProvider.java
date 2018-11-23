package net.readybid.rfp.template;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/2/2017.
 *
 */
public class RfpTemplateListItemViewModelCodecWithProvider extends RbMongoCodecWithProvider<RfpTemplateListItemViewModel> {

    public RfpTemplateListItemViewModelCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(RfpTemplateListItemViewModel.class, bsonTypeClassMap);
    }

    @Override
    protected RfpTemplateListItemViewModel newInstance() {
        return new RfpTemplateListItemViewModel();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<RfpTemplateListItemViewModel>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.id = reader.readObjectId().toString());
        decodeMap.put("name", (c, reader, context) -> c.name = reader.readString());
        decodeMap.put("type", (c, reader, context) -> c.type = reader.readString());
        decodeMap.put("description", (c, reader, context) -> c.description = reader.readString());
    }

    @Override
    public void encodeDocument(Document document, RfpTemplateListItemViewModel tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {}
}
