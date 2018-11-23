package net.readybid.app.persistence.mongodb.repository;

import net.readybid.app.core.entities.rfp.QuestionnaireForm;
import org.bson.conversions.Bson;

public interface QuestionnaireTemplateRepository {
    QuestionnaireForm findOne(Bson filter);
}
