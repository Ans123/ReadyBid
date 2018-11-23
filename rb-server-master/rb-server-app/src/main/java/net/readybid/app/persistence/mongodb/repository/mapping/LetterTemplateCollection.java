package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.app.entities.rfp.LetterTemplateImpl;
import net.readybid.entities.Id;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.codecs.BsonTypeClassMap;

import java.util.Map;

public class LetterTemplateCollection {

    public static final Id HOTEL_RFP_CHAIN_COVER_LETTER_ID = Id.valueOf("5b6962b6412e026d4dfbdd14");

    private LetterTemplateCollection(){}

    public static final String NAME = "LetterTemplate";

    public static final String ID = "_id";
    public static final String TEMPLATE = "template";


    public static RbMongoCodecWithProvider<LetterTemplateImpl> codec(BsonTypeClassMap bsonTypeClassMap) {
        return new LetterTemplateImplCodec(bsonTypeClassMap);
    }

    private static class LetterTemplateImplCodec extends RbMongoCodecWithProvider<LetterTemplateImpl> {

        LetterTemplateImplCodec(BsonTypeClassMap bsonTypeClassMap) {
            super(LetterTemplateImpl.class, bsonTypeClassMap);
        }

        @Override
        protected LetterTemplateImpl newInstance() {
            return new LetterTemplateImpl();
        }

        @Override
        public void decodeMapSetup(Map<String, RbMongoDecoder<LetterTemplateImpl>> decodeMap) {
            decodeMap.put(ID, (c, reader, context) -> c.setId(read(reader,context, Id.class)));
            decodeMap.put(TEMPLATE, (c, reader, context) -> c.setTemplate(reader.readString()));
        }
    }
}
