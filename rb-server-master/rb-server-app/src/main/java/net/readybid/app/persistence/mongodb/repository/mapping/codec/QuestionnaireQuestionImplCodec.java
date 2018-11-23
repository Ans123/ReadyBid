package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp.questionnaire.form.question.QuestionnaireQuestionImpl;
import org.bson.codecs.BsonTypeClassMap;

public class QuestionnaireQuestionImplCodec extends AbstractQuestionnaireQuestionCodecWithProvider<QuestionnaireQuestionImpl> {

    public QuestionnaireQuestionImplCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireQuestionImpl.class, bsonTypeClassMap);
    }
}
