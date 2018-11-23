package net.readybid.entities.core;

import net.readybid.app.core.entities.entity.EntityImage;
import net.readybid.mongodb.RbDocument;
import org.bson.Document;

public class EntityImageMapper {

    private static EntityImageMapper instance;

    public static EntityImageMapper getInstance() {
        if(instance == null) instance = new EntityImageMapper();
        return instance;
    }

    public Document toDocument(EntityImage entityImage) {
        final RbDocument d = new RbDocument();

        d.put("url", entityImage.getUrl());

        return d;
    }
}
