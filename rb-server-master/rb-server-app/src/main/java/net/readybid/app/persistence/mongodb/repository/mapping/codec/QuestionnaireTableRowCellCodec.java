package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireTableRowCell;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class QuestionnaireTableRowCellCodec extends RbMongoCodecForBuilder<QuestionnaireTableRowCell, QuestionnaireTableRowCell.Builder> {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String VARIABLE = "variable";
    public static final String COLSPAN = "colspan";
    public static final String COLSPAN_ID = "colspanId";
    public static final String ROWSPAN = "rowspan";
    public static final String ROWSPAN_ID = "rowspanId";
    public static final String CLASSES = "classes";
    public static final String FOR = "for";
    public static final String CELL = "cell";

    public QuestionnaireTableRowCellCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireTableRowCell.class, bsonTypeClassMap);
    }

    @Override
    protected void builderDecoderMapSetup(Map<String, RbMongoDecoder<QuestionnaireTableRowCell.Builder>> decoderMap) {
        decoderMap.put(ID, (builder, reader, context) -> builder.setId(readToString(reader)));
        decoderMap.put(NAME, (builder, reader, context) -> builder.setName(readToString(reader)));
        decoderMap.put(DESCRIPTION, (builder, reader, context) -> builder.setDescription(reader.readString()));
        decoderMap.put(VARIABLE, (builder, reader, context) -> builder.setVariable(reader.readBoolean()));
        decoderMap.put(COLSPAN, (builder, reader, context) -> builder.setColSpan(readIntObject(reader)));
        decoderMap.put(COLSPAN_ID, (builder, reader, context) -> builder.setColSpanId(reader.readString()));
        decoderMap.put(ROWSPAN, (builder, reader, context) -> builder.setRowSpan(readIntObject(reader)));
        decoderMap.put(ROWSPAN_ID, (builder, reader, context) -> builder.setRowSpanId(reader.readString()));
        decoderMap.put(CLASSES, (builder, reader, context) -> builder.setClasses(reader.readString()));
        decoderMap.put(FOR, (builder, reader, context) -> builder.setForFilters(readMap(reader)));
        decoderMap.put(CELL, (builder, reader, context) -> builder.setQuestion(read(reader, context, QuestionnaireQuestion.class)));
    }

    @Override
    protected QuestionnaireTableRowCell.Builder getBuilderInstance() {
        return new QuestionnaireTableRowCell.Builder();
    }

    @Override
    public void encodeDocument(Document d, QuestionnaireTableRowCell tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, ID, tObject.id);
        putIfNotNull(d, NAME, tObject.name);
        putIfNotNull(d, DESCRIPTION, tObject.description);
        putIfNotNull(d, VARIABLE, tObject.variable);
        putIfNotNull(d, COLSPAN, tObject.colSpan);
        putIfNotNull(d, COLSPAN_ID, tObject.colSpanId);
        putIfNotNull(d, ROWSPAN, tObject.rowSpan);
        putIfNotNull(d, ROWSPAN_ID, tObject.rowSpanId);
        putIfNotNull(d, CLASSES, tObject.classes);
        putIfNotNull(d, FOR, tObject.forFilters);
        putIfNotNull(d, CELL, tObject.question);
    }
}
