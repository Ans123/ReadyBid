package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp.questionnaire.form.question.TextQuestionnaireQuestion;
import org.bson.codecs.BsonTypeClassMap;

public class TextQuestionnaireQuestionCodec extends AbstractQuestionnaireQuestionCodecWithProvider<TextQuestionnaireQuestion> {

    public TextQuestionnaireQuestionCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(TextQuestionnaireQuestion.class, bsonTypeClassMap);
    }
}
