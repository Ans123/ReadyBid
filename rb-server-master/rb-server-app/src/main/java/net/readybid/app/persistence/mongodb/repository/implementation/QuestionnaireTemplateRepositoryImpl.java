package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.core.entities.rfp.QuestionnaireForm;
import net.readybid.app.entities.rfp.questionnaire.form.QuestionnaireFormImpl;
import net.readybid.app.persistence.mongodb.repository.QuestionnaireTemplateRepository;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionnaireTemplateRepositoryImpl implements QuestionnaireTemplateRepository {

    private final MongoCollection<QuestionnaireFormImpl> collection;

    @Autowired
    public QuestionnaireTemplateRepositoryImpl(MongoDatabase database) {
        collection = database.getCollection("QuestionnaireTemplate", QuestionnaireFormImpl.class);
    }

    @Override
    public QuestionnaireForm findOne(Bson filter) {
        return collection.find(filter).first();
    }
}
