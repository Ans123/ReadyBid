package net.readybid.entities.core.db;

import net.readybid.entities.core.EntityImageImpl;
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
public class EntityImageImplCodecWithProvider extends RbMongoCodecWithProvider<EntityImageImpl> {

    public EntityImageImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(EntityImageImpl.class, bsonTypeClassMap);
    }

    @Override
    protected EntityImageImpl newInstance() {
        return new EntityImageImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<EntityImageImpl>> decodeMap) {
        decodeMap.put("url", (c, reader, context) -> c.setUrl(reader.readString()));
        decodeMap.put("thumbnailUrl", (c, reader, context) -> c.setThumbnailUrl(reader.readString()));
        decodeMap.put("caption", (c, reader, context) -> c.setCaption(reader.readString()));
        decodeMap.put("width", (c, reader, context) -> c.setWidth(readInt(reader)));
        decodeMap.put("height", (c, reader, context) -> c.setHeight(readInt(reader)));
    }

    @Override
    public void encodeDocument(Document d, EntityImageImpl image, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "url", image.getUrl());
        putIfNotNull(d, "thumbnailUrl", image.getThumbnailUrl());
        putIfNotNull(d, "caption", image.getCaption());
        putIfNotNull(d, "width", image.getWidth());
        putIfNotNull(d, "height", image.getHeight());
    }
}
