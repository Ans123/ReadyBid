package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireSection;
import net.readybid.app.entities.rfp.questionnaire.form.QuestionnaireModuleImpl;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;


public class QuestionnaireModuleCodec extends RbMongoCodecForBuilder<QuestionnaireModuleImpl, QuestionnaireModuleImpl.Builder> {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ORD = "ord";
    public static final String CELLS = "cells";

    public QuestionnaireModuleCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireModuleImpl.class, bsonTypeClassMap);
    }

    @Override
    protected void builderDecoderMapSetup(Map<String, RbMongoDecoder<QuestionnaireModuleImpl.Builder>> decoderMap) {
        decoderMap.put(ID, (builder, reader, context) -> builder.setId(reader.readString()));
        decoderMap.put(NAME, (builder, reader, context) -> builder.setName(reader.readString()));
        decoderMap.put(ORD, (builder, reader, context) -> builder.setOrd(readInt(reader)));
        decoderMap.put(CELLS, (builder, reader, context) -> builder.setSections(readListOfObjects(reader, context, QuestionnaireSection.class)));
    }

    @Override
    protected QuestionnaireModuleImpl.Builder getBuilderInstance() {
        return new QuestionnaireModuleImpl.Builder();
    }

    @Override
    public void encodeDocument(Document d, QuestionnaireModuleImpl tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, ID, tObject.getId());
        putIfNotNull(d, NAME, tObject.getName());
        putIfNotNull(d, ORD, tObject.getOrd());
        putIfNotNull(d, CELLS, tObject.getSections());
    }
}