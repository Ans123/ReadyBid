package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp.questionnaire.form.question.NumberQuestionnaireQuestion;
import org.bson.codecs.BsonTypeClassMap;

public class NumberQuestionnaireQuestionCodec extends AbstractQuestionnaireQuestionCodecWithProvider<NumberQuestionnaireQuestion> {

    public NumberQuestionnaireQuestionCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(NumberQuestionnaireQuestion.class, bsonTypeClassMap);
    }
}
