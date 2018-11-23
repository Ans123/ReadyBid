package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp.questionnaire.form.question.DateQuestionnaireQuestion;
import org.bson.codecs.BsonTypeClassMap;

public class DateQuestionnaireQuestionCodec extends AbstractQuestionnaireQuestionCodecWithProvider<DateQuestionnaireQuestion> {

    public DateQuestionnaireQuestionCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(DateQuestionnaireQuestion.class, bsonTypeClassMap);
    }
}
