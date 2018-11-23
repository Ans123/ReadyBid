package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireTableRow;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireTableRowCell;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class QuestionnaireTableRowCodec extends RbMongoCodecForBuilder<QuestionnaireTableRow, QuestionnaireTableRow.Builder> {

    public static final String TYPE = "type";
    public static final String CLASSES = "classes";
    public static final String FOR = "for";
    public static final String CELLS = "cells";

    public QuestionnaireTableRowCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireTableRow.class, bsonTypeClassMap);
    }

    @Override
    protected void builderDecoderMapSetup(Map<String, RbMongoDecoder<QuestionnaireTableRow.Builder>> decoderMap) {
        decoderMap.put(TYPE, (builder, reader, context) -> builder.setType(reader.readString()));
        decoderMap.put(CLASSES, (builder, reader, context) -> builder.setClasses(reader.readString()));
        decoderMap.put(FOR, (builder, reader, context) -> builder.setForFilters(readMap(reader)));
        decoderMap.put(CELLS, (builder, reader, context) -> builder.setCells(readListOfObjects(reader, context, QuestionnaireTableRowCell.class)));
    }

    @Override
    protected QuestionnaireTableRow.Builder getBuilderInstance() {
        return new QuestionnaireTableRow.Builder();
    }

    @Override
    public void encodeDocument(Document d, QuestionnaireTableRow tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, TYPE, tObject.type);
        putIfNotNull(d, CLASSES, tObject.classes);
        putIfNotNull(d, FOR, tObject.forFilters);
        putIfNotNull(d, CELLS, tObject.cells);
    }
}
