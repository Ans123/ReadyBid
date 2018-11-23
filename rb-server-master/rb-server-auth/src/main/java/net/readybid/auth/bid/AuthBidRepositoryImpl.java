package net.readybid.auth.bid;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.mongodb.MongoRetry;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfp.contact.RfpContactImpl;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.set;

/**
 * Created by DejanK on 5/21/2017.
 *
 */
@Service
public class AuthBidRepositoryImpl implements AuthBidRepository {

    private final MongoCollection bidCollection;

    @Autowired
    public AuthBidRepositoryImpl(MongoDatabase mongoDatabase) {
        this.bidCollection = mongoDatabase.getCollection("Bid");
    }

    @Override
    @MongoRetry
    public void setNewSupplierContactToAllAccountBidsWithoutSupplierContact(RfpContact contact, ObjectId accountId) {
        bidCollection.updateMany(
                or ( emptySupplierContactForAccount(accountId), contactSetAsSupplier(contact.getEmailAddress(), accountId)),
                set("supplier.contact", (RfpContactImpl) contact)
        );
    }

    private Bson contactSetAsSupplier(String emailAddress, ObjectId accountId) {
        return and(
                eq("supplier.contact.company.accountId", accountId),
                eq("supplier.contact.emailAddress", emailAddress)
        );
    }

    private Bson emptySupplierContactForAccount(ObjectId accountId) {
        return and(
                eq("supplier.company.accountId", accountId),
                exists("supplier.contact", false)
        );
    }
}
