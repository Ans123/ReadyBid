package net.readybid.app.persistence.mongodb.app.rfp_hotel;

import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.Sorts;
import net.readybid.app.entities.rfp_hotel.HotelRfpRepresentative;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpRepresentativesLoader;
import net.readybid.app.persistence.mongodb.repository.HotelRfpRepresentativesRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.AccountCollection;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection;
import net.readybid.app.persistence.mongodb.repository.mapping.UserAccountCollection;
import net.readybid.app.persistence.mongodb.repository.mapping.UserCollection;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Projections.include;
import static net.readybid.mongodb.RbMongoFilters.*;

@Service
public class HotelRfpRepresentativesLoaderImpl implements HotelRfpRepresentativesLoader {

    private final HotelRfpRepresentativesRepository repository;

    @Autowired
    public HotelRfpRepresentativesLoaderImpl(HotelRfpRepresentativesRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<String> getHotelAndMasterChainAccountIdsForHotel(String hotelEntityId) {
        final List<Document> ids = repository.aggregateHotel(Arrays.asList(
                match(byId(hotelEntityId)),
                project(new Document("ids", Arrays.asList("$_id", "$chain.master._id"))),
                unwind("$ids"),
                lookup(AccountCollection.COLLECTION_NAME, "ids", AccountCollection.ENTITY_ID, "account"),
                unwind("$account"),
                project(include("account._id")),
                replaceRoot("$account")
        ));

        return ids.stream().map(d -> String.valueOf(d.get("_id"))).collect(Collectors.toList());
    }

    @Override
    public String getAccountIdForEntity(String chainEntityId) {
        final Document d = repository
                .findInAccount(eq(AccountCollection.ENTITY_ID, oid(chainEntityId)), include(AccountCollection.ID));
        final Object id = d == null ? null : d.getOrDefault(AccountCollection.ID, null);

        return id == null ? null : String.valueOf(id);
    }

    @Override
    public List<HotelRfpRepresentative> getRepresentativesFromAccounts(List<String> accountsIds) {
        if(accountsIds == null) return new ArrayList<>();
        return repository.aggregateAccount(Arrays.asList(
                match(in(AccountCollection.ID, oid(accountsIds))),
                project(include(AccountCollection.TYPE, AccountCollection.NAME, AccountCollection.ENTITY_ID)),
                project(fields(computed("account", "$$ROOT"))),
                lookup(UserAccountCollection.COLLECTION_NAME, "_id", UserAccountCollection.ACCOUNT_ID, "userAccount"),
                unwind("$userAccount"),
                project(include("account", "userAccount._id", "userAccount.userId", "userAccount.jobTitle")),
                lookup(UserCollection.COLLECTION_NAME, "userAccount.userId", UserCollection.ID, "user"),
                unwind( "$user"),
                match(eq("user.status.value", "ACTIVE")),
                project(fields(
                        computed("representative._id", "$userAccount._id"),
                        computed("representative.userId", "$user._id"),
                        computed("representative.entityId", "$account.entityId"),
                        computed("representative.accountId", "$account._id"),
                        computed("representative.firstName", "$user.firstName"),
                        computed("representative.lastName", "$user.lastName"),
                        computed("representative.emailAddress", "$user.emailAddress"),
                        computed("representative.phone", "$user.phone"),
                        computed("representative.profilePicture", "$user.profilePicture"),
                        computed("representative.jobTitle", "$userAccount.jobTitle"),
                        computed("representative.accountName", "$account.name"),
                        computed("representative.accountType", "$account.type"),
                        new Document("representative.isUser", new Document("$literal",  true))
                )),
                replaceRoot("$representative")
        ));
    }

    @Override
    public List<HotelRfpRepresentative> getSupplierContactsFromBidsWithAccounts(List<String> accountsIds) {
        if(accountsIds == null) return new ArrayList<>();
        return repository.aggregateBid(Arrays.asList(
                match(and(
                        in(HotelRfpBidCollection.SUPPLIER_CONTACT_COMPANY_ACCOUNT_ID, oid(accountsIds)),
                        byState(HotelRfpBidStateStatus.SENT),
                        eq(HotelRfpBidCollection.SUPPLIER_CONTACT_IS_USER, false)
                )),
                project(fields(
                        computed("representative._id", "$supplier.contact.id"),
                        computed("representative.entityId", "$supplier.contact.company.entityId"),
                        computed("representative.accountId", "$supplier.contact.company.accountId"),
                        computed("representative.firstName", "$supplier.contact.firstName"),
                        computed("representative.lastName", "$supplier.contact.lastName"),
                        computed("representative.emailAddress", "$supplier.contact.emailAddress"),
                        computed("representative.phone", "$supplier.contact.phone"),
                        computed("representative.profilePicture", "$supplier.contact.profilePicture"),
                        computed("representative.jobTitle", "$supplier.contact.jobTitle"),
                        computed("representative.accountName", "$supplier.contact.company.name"),
                        computed("representative.accountType", "$supplier.contact.company.type"),
                        computed("representative.isUser", "$supplier.contact.isUser")
                )),
                sort(Sorts.descending("representative._id")),
                group(new Document("emailAddress", "$representative.emailAddress")
                                .append("accountId", "$representative.accountId"),

                        new BsonField("representative", new Document("$first", "$representative"))),
                replaceRoot("$representative")
        ));
    }
}
