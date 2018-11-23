package net.readybid.app.persistence.mongodb.app.auth.account;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.interactors.authentication.account.gate.AccountStorageManager;
import net.readybid.app.persistence.mongodb.repository.AccountRepository;
import net.readybid.auth.account.core.Account;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;
import static net.readybid.app.persistence.mongodb.repository.mapping.AccountCollection.*;
import static net.readybid.mongodb.RbMongoFilters.oid;

@Service
public class AccountStorageManagerImpl implements AccountStorageManager {

    private final AccountRepository repository;

    @Autowired
    public AccountStorageManagerImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account updateBasicInformationFromEntity(Entity entity) {
        final List<Bson> updates = new ArrayList<>();
        updates.add(set(NAME, entity.getName()));
        updates.add(set(INDUSTRY, entity.getIndustry()));
        updates.add(set(LOCATION, entity.getLocation()));

        final String website = entity.getWebsite();
        updates.add(website == null || website.isEmpty() ? unset(WEBSITE) : set(WEBSITE, website));

        return repository.findOneAndUpdate(
                eq(ENTITY_ID, oid(String.valueOf(entity.getId()))),
                combine(updates),
                createReturnNew());
    }

    @Override
    public Account updateLogoFromEntity(Entity entity) {
        return repository.findOneAndUpdate(
                eq(ENTITY_ID, oid(String.valueOf(entity.getId()))),
                set(LOGO, entity.getLogo()),
                createReturnNew());
    }

    private FindOneAndUpdateOptions createReturnNew() {
        return new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
    }
}
