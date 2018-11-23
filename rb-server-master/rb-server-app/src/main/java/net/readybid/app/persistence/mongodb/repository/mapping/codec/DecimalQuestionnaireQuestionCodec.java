package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp.questionnaire.form.question.DecimalQuestionnaireQuestion;
import org.bson.codecs.BsonTypeClassMap;

public class DecimalQuestionnaireQuestionCodec extends AbstractQuestionnaireQuestionCodecWithProvider<DecimalQuestionnaireQuestion> {

    public DecimalQuestionnaireQuestionCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(DecimalQuestionnaireQuestion.class, bsonTypeClassMap);
    }
}
