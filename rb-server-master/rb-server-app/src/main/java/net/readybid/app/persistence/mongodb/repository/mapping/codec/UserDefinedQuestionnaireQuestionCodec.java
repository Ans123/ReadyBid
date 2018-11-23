package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp.questionnaire.form.question.UserDefinedQuestionnaireQuestion;
import org.bson.codecs.BsonTypeClassMap;

public class UserDefinedQuestionnaireQuestionCodec extends AbstractQuestionnaireQuestionCodecWithProvider<UserDefinedQuestionnaireQuestion> {

    public UserDefinedQuestionnaireQuestionCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(UserDefinedQuestionnaireQuestion.class, bsonTypeClassMap);
    }
}
