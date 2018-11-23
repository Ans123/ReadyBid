package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireTable;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireTableRow;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class QuestionnaireTableCodec extends RbMongoCodecForBuilder<QuestionnaireTable, QuestionnaireTable.Builder> {

    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ORD = "ord";
    public static final String CLASSES = "classes";
    public static final String FILTERS = "filters";
    public static final String DEFAULT_FILTERS = "defaultFilters";
    public static final String MANAGE_ROWS = "manageRows";
    public static final String ACTIONS = "actions";
    public static final String CELLS = "cells";

    public QuestionnaireTableCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireTable.class, bsonTypeClassMap);
    }

    @Override
    protected void builderDecoderMapSetup(Map<String, RbMongoDecoder<QuestionnaireTable.Builder>> decoderMap) {
        decoderMap.put(TYPE, (builder, reader, context) -> builder.setType(read(reader, context, QuestionnaireSectionType.class)));
        decoderMap.put(ID, (builder, reader, context) -> builder.setId(reader.readString()));
        decoderMap.put(NAME, (builder, reader, context) -> builder.setName(reader.readString()));
        decoderMap.put(ORD, (builder, reader, context) -> builder.setOrd(readInt(reader)));
        decoderMap.put(CLASSES, (builder, reader, context) -> builder.setClasses(reader.readString()));
        decoderMap.put(FILTERS, (builder, reader, context) -> builder.setFilters(readMap(reader)));
        decoderMap.put(DEFAULT_FILTERS, (builder, reader, context) -> builder.setDefaultFilters(readMap(reader)));
        decoderMap.put(MANAGE_ROWS, (builder, reader, context) -> builder.setManageRows(readList(reader, Object.class)));
        decoderMap.put(ACTIONS, (builder, reader, context) -> builder.setActions(readMap(reader)));
        decoderMap.put(CELLS, (builder, reader, context) -> builder.setRows(readListOfObjects(reader, context, QuestionnaireTableRow.class)));
    }

    @Override
    protected QuestionnaireTable.Builder getBuilderInstance() {
        return new QuestionnaireTable.Builder();
    }

    @Override
    public void encodeDocument(Document d, QuestionnaireTable tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, TYPE, tObject.getType());
        putIfNotNull(d, ID, tObject.getId());
        putIfNotNull(d, NAME, tObject.getName());
        putIfNotNull(d, ORD, tObject.getOrd());
        putIfNotNull(d, CLASSES, tObject.getClasses());
        putIfNotNull(d, FILTERS, tObject.getFilters());
        putIfNotNull(d, DEFAULT_FILTERS, tObject.getDefaultFilters());
        putIfNotNull(d, MANAGE_ROWS, tObject.getManageRows());
        putIfNotNull(d, ACTIONS, tObject.getActions());
        putIfNotNull(d, CELLS, tObject.getRows());
    }
}
