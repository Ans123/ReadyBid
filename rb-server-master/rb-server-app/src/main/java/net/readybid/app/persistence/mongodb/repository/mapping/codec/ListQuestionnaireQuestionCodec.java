package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp.questionnaire.form.question.ListQuestionnaireQuestion;
import org.bson.codecs.BsonTypeClassMap;

public class ListQuestionnaireQuestionCodec extends AbstractQuestionnaireQuestionCodecWithProvider<ListQuestionnaireQuestion> {

    public ListQuestionnaireQuestionCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(ListQuestionnaireQuestion.class, bsonTypeClassMap);
    }
}
