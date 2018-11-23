package net.readybid.entities.core.db;

import net.readybid.entities.core.EntityImageView;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class EntityImageViewCodecWithProvider extends RbMongoCodecWithProvider<EntityImageView> {

    public EntityImageViewCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(EntityImageView.class, bsonTypeClassMap);
    }

    @Override
    protected EntityImageView newInstance() {
        return new EntityImageView();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<EntityImageView>> decodeMap) {
        decodeMap.put("url", (c, reader, context) -> c.url = reader.readString());
        decodeMap.put("thumbnailUrl", (c, reader, context) -> c.thumbnailUrl = reader.readString());
        decodeMap.put("caption", (c, reader, context) -> c.caption = reader.readString());
        decodeMap.put("width", (c, reader, context) -> c.width = readInt(reader));
        decodeMap.put("height", (c, reader, context) -> c.height = readInt(reader));
    }

    @Override
    public void encodeDocument(Document d, EntityImageView image, BsonWriter bsonWriter, EncoderContext encoderContext) {}
}
