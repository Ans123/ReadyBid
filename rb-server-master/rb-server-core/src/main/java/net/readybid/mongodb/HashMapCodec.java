package net.readybid.mongodb;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by DejanK on 1/19/2017.
 *
 */
public class HashMapCodec extends RbMongoCodecWithProvider<HashMap>{

    public HashMapCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(HashMap.class, bsonTypeClassMap);
    }

    @Override
    public HashMap decode(BsonReader bsonReader, DecoderContext decoderContext) {
        throw new RuntimeException("HashMap decoder1 is not implemented!");
    }

    @Override
    public void encode(BsonWriter bsonWriter, HashMap hashMap, EncoderContext encoderContext) {
        final Document doc = new Document(hashMap);
        documentCodec().encode(bsonWriter, doc, encoderContext);
    }

    @Override
    public Class<HashMap> getEncoderClass() {
        return HashMap.class;
    }

    @Override
    protected LinkedHashMap newInstance() {
        return new LinkedHashMap();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HashMap>> decodeMap) {
    }

    @Override
    public void encodeDocument(Document document, HashMap tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        throw new RuntimeException("HashMap encoder is overriden!");
    }
}
