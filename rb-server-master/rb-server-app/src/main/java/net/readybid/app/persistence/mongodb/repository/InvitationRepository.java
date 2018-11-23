package net.readybid.app.persistence.mongodb.repository;

import org.bson.conversions.Bson;

public interface InvitationRepository {
    void update(Bson filter, Bson update);
}
