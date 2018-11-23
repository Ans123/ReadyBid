package net.readybid.app.persistence.mongodb.repository;

import net.readybid.app.entities.rfp.LetterTemplateImpl;
import org.bson.conversions.Bson;

public interface LetterTemplateRepository {
    LetterTemplateImpl findOne(Bson filter, Bson projection);
}
