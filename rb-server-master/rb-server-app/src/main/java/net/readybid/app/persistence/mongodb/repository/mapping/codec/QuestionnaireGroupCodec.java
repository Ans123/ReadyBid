package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireGroup;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;


public class QuestionnaireGroupCodec extends RbMongoCodecForBuilder<QuestionnaireGroup, QuestionnaireGroup.Builder> {

    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ORD = "ord";
    public static final String CELLS = "cells";
    public static final String CLASSES = "classes";

    public QuestionnaireGroupCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireGroup.class, bsonTypeClassMap);
    }

    @Override
    protected void builderDecoderMapSetup(Map<String, RbMongoDecoder<QuestionnaireGroup.Builder>> decoderMap) {
        decoderMap.put(TYPE, (builder, reader, context) -> builder.setType(read(reader, context, QuestionnaireSectionType.class)));
        decoderMap.put(ID, (builder, reader, context) -> builder.setId(reader.readString()));
        decoderMap.put(NAME, (builder, reader, context) -> builder.setName(reader.readString()));
        decoderMap.put(ORD, (builder, reader, context) -> builder.setOrd(readInt(reader)));
        decoderMap.put(CLASSES, (builder, reader, context) -> builder.setClasses(reader.readString()));
        decoderMap.put(CELLS, (builder, reader, context) -> builder.setQuestions(readListOfObjects(reader, context, QuestionnaireQuestion.class)));
    }

    @Override
    protected QuestionnaireGroup.Builder getBuilderInstance() {
        return new QuestionnaireGroup.Builder();
    }

    @Override
    public void encodeDocument(Document d, QuestionnaireGroup tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, TYPE, tObject.getType());
        putIfNotNull(d, ID, tObject.getId());
        putIfNotNull(d, NAME, tObject.getName());
        putIfNotNull(d, ORD, tObject.getOrd());
        putIfNotNull(d, CLASSES, tObject.getClasses());
        putIfNotNull(d, CELLS, tObject.getQuestions());
    }
}
