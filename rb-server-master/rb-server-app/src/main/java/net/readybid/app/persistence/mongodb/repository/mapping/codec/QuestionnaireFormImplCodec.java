package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireModule;
import net.readybid.app.entities.rfp.questionnaire.form.QuestionnaireFormImpl;
import net.readybid.app.entities.rfp.questionnaire.form.QuestionnaireModuleImpl;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;


public class QuestionnaireFormImplCodec extends RbMongoCodecForBuilder<QuestionnaireFormImpl, QuestionnaireFormImpl.Builder> {

    public static final String ID = "id";
    public static final String CELLS = "cells";

    public QuestionnaireFormImplCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireFormImpl.class, bsonTypeClassMap);
    }

    @Override
    protected void builderDecoderMapSetup(Map<String, RbMongoDecoder<QuestionnaireFormImpl.Builder>> decoderMap) {
        decoderMap.put(ID, (builder, reader, context) -> builder.setId(reader.readString()));
        decoderMap.put(CELLS, (builder, reader, context) ->
                        builder.setModules(readListOfObjects(reader, context, QuestionnaireModuleImpl.class, QuestionnaireModule.class)));
    }

    @Override
    protected QuestionnaireFormImpl.Builder getBuilderInstance() {
        return new QuestionnaireFormImpl.Builder();
    }

    @Override
    public void encodeDocument(Document d, QuestionnaireFormImpl tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, ID, tObject.getId());
        putIfNotNull(d, CELLS, tObject.getModules());
    }
}
