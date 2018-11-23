package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireSection;
import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireHotelRfpRateGrid;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class QuestionnaireHotelRfpRateGridCodec extends RbMongoCodecForBuilder<QuestionnaireHotelRfpRateGrid, QuestionnaireHotelRfpRateGrid.Builder> {

    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String ORD = "ord";
    public static final String LOCKED = "locked";
    public static final String FILTERS = "filters";
    public static final String DEFAULT_FILTERS = "defaultFilters";
    public static final String ACTIONS = "actions";
    public static final String CELLS = "cells";

    public QuestionnaireHotelRfpRateGridCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireHotelRfpRateGrid.class, bsonTypeClassMap);
    }

    @Override
    protected void builderDecoderMapSetup(Map<String, RbMongoDecoder<QuestionnaireHotelRfpRateGrid.Builder>> decoderMap) {
        decoderMap.put(TYPE, (builder, reader, context) -> builder.setType(read(reader, context, QuestionnaireSectionType.class)));
        decoderMap.put(ID, (builder, reader, context) -> builder.setId(reader.readString()));
        decoderMap.put(ORD, (builder, reader, context) -> builder.setOrd(readInt(reader)));
        decoderMap.put(LOCKED, (builder, reader, context) -> builder.setLocked(reader.readBoolean()));
        decoderMap.put(FILTERS, (builder, reader, context) -> builder.setFilters(readMap(reader)));
        decoderMap.put(DEFAULT_FILTERS, (builder, reader, context) -> builder.setDefaultFilters(readMap(reader)));
        decoderMap.put(ACTIONS, (builder, reader, context) -> builder.setActions(readMap(reader)));
        decoderMap.put(CELLS, (builder, reader, context) -> builder.setSections(readListOfObjects(reader, context, QuestionnaireSection.class)));
    }

    @Override
    protected QuestionnaireHotelRfpRateGrid.Builder getBuilderInstance() {
        return new QuestionnaireHotelRfpRateGrid.Builder();
    }

    @Override
    public void encodeDocument(Document d, QuestionnaireHotelRfpRateGrid tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, ID, tObject.getId());
        putIfNotNull(d, ORD, tObject.getOrd());
        putIfNotNull(d, LOCKED, tObject.getLocked());
        putIfNotNull(d, FILTERS, tObject.getFilters());
        putIfNotNull(d, DEFAULT_FILTERS, tObject.getDefaultFilters());
        putIfNotNull(d, ACTIONS, tObject.getActions());
        putIfNotNull(d, CELLS, tObject.getSections());
    }
}