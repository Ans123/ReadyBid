package net.readybid.app.persistence.mongodb.repository;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import net.readybid.auth.user.UserImpl;
import org.bson.conversions.Bson;

public interface UserRepository {
    UserImpl findAndUpdate(Bson query, Bson update, FindOneAndUpdateOptions options);

    void update(Bson query, Bson update);
}
