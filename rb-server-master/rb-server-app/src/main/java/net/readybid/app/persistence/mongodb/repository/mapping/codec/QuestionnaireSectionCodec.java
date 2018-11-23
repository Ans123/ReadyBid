package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireSection;
import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireGroup;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireHotelRfpRateGrid;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireTable;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class QuestionnaireSectionCodec extends RbMongoCodecWithProvider<QuestionnaireSection> {

    public QuestionnaireSectionCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireSection.class, bsonTypeClassMap);
    }

    @Override
    public QuestionnaireSection decode(BsonReader reader, DecoderContext context) {
        QuestionnaireSectionType type = readField(reader, context, "type", QuestionnaireSectionType.class);
        type = type == null ? QuestionnaireSectionType.GROUP : type;
        switch (type){
            case TABLE:
                return registry.get(QuestionnaireTable.class).decode(reader, context);
            case HOTEL_RFP_RATE_GRID:
                return registry.get(QuestionnaireHotelRfpRateGrid.class).decode(reader, context);
            case GROUP:
            default:
                return registry.get(QuestionnaireGroup.class).decode(reader, context);
        }
    }

    @Override
    public void encode(BsonWriter bsonWriter, QuestionnaireSection tObject, EncoderContext encoderContext) {
        QuestionnaireSectionType type = tObject.getType();
        type = type == null ? QuestionnaireSectionType.GROUP : type;
        switch (type){
            case TABLE:
                registry.get(QuestionnaireTable.class).encode(bsonWriter, (QuestionnaireTable) tObject, encoderContext);
                break;
            case HOTEL_RFP_RATE_GRID:
                registry.get(QuestionnaireHotelRfpRateGrid.class).encode(bsonWriter, (QuestionnaireHotelRfpRateGrid) tObject, encoderContext);
            case GROUP:
            default:
                registry.get(QuestionnaireGroup.class).encode(bsonWriter, (QuestionnaireGroup) tObject, encoderContext);
        }
    }
}
