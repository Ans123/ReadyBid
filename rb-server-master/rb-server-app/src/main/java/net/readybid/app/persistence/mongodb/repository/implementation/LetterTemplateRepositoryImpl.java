package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.entities.rfp.LetterTemplateImpl;
import net.readybid.app.persistence.mongodb.repository.LetterTemplateRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.LetterTemplateCollection;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

@Service
public class LetterTemplateRepositoryImpl implements LetterTemplateRepository {

    private final MongoCollection<LetterTemplateImpl> collection;

    public LetterTemplateRepositoryImpl(MongoDatabase mongoDatabase) {
        this.collection = mongoDatabase.getCollection(LetterTemplateCollection.NAME, LetterTemplateImpl.class);
    }

    @Override
    public LetterTemplateImpl findOne(Bson filter, Bson projection) {
        return collection.find(filter).projection(projection).first();
    }
}
