package net.readybid.mongodb;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by DejanK on 1/19/2017.
 *
 */
public class LinkedHashMapCodec extends RbMongoCodecWithProvider<LinkedHashMap>{

    public LinkedHashMapCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(LinkedHashMap.class, bsonTypeClassMap);
    }

    @Override
    public LinkedHashMap decode(BsonReader bsonReader, DecoderContext decoderContext) {
        throw new RuntimeException("LinkedHashMap decoder1 is not implemented!");
    }

    @Override
    public void encode(BsonWriter bsonWriter, LinkedHashMap linkedHashMap, EncoderContext encoderContext) {
        final Document doc = new Document(linkedHashMap);
        documentCodec().encode(bsonWriter, doc, encoderContext);
    }

    @Override
    public Class<LinkedHashMap> getEncoderClass() {
        return LinkedHashMap.class;
    }

    @Override
    protected LinkedHashMap newInstance() {
        return new LinkedHashMap();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<LinkedHashMap>> decodeMap) {
    }

    @Override
    public void encodeDocument(Document document, LinkedHashMap tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        throw new RuntimeException("LinkedHashMap encoder is overriden!");
    }
}
