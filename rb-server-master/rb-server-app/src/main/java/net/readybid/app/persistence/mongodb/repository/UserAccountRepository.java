package net.readybid.app.persistence.mongodb.repository;

import net.readybid.auth.useraccount.UserAccount;
import org.bson.conversions.Bson;

import java.util.List;

public interface UserAccountRepository {
    List<? extends UserAccount> find(Bson filter, Bson projection);
}
