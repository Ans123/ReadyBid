package net.readybid.api.main.bid.negotiations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.auth.useraccount.core.UserAccountRepository;
import net.readybid.mongodb.MongoDbSafeNonIndempotentWriteService;
import net.readybid.mongodb.MongoRetry;
import net.readybid.app.persistence.mongodb.repository.mapping.AccountCollection;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection;
import net.readybid.app.persistence.mongodb.repository.mapping.UserCollection;
import net.readybid.rfphotel.bid.HotelRfpBidOffer;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiation;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiations;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationsImpl;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;
import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 7/25/2017.
 *
 */
@Service
public class HotelRfpBidNegotiationsRepositoryImpl implements HotelRfpBidNegotiationsRepository {

    private final MongoCollection<HotelRfpNegotiationsImpl> negotiationsCollection;
    private final MongoDbSafeNonIndempotentWriteService safeNonIndempotentWriteService;

    @Autowired
    public HotelRfpBidNegotiationsRepositoryImpl(
            MongoDatabase mongoDatabase,
            MongoDbSafeNonIndempotentWriteService safeNonIndempotentWriteService
    ) {
        negotiationsCollection = mongoDatabase.getCollection(HotelRfpBidCollection.COLLECTION_NAME, HotelRfpNegotiationsImpl.class);
        this.safeNonIndempotentWriteService = safeNonIndempotentWriteService;
    }

    @Override
    @MongoRetry
    public HotelRfpNegotiations getNegotiations(String bidId) {
        return negotiationsCollection.aggregate(
                Arrays.asList(
                        match(new Document(HotelRfpBidCollection.ID, oid(bidId))),
                        project(new Document("communication", "$negotiations.communication")),
                        unwind("$communication", false),
                        lookup(UserAccountRepository.COLLECTION, "communication.from.userAccountId", "_id", "userAccount"),
                        unwind("$userAccount", false),
                        lookup(UserCollection.COLLECTION_NAME, "userAccount.userId", "_id", "user"),
                        unwind("$user", false),
                        lookup(AccountCollection.COLLECTION_NAME, "userAccount.accountId", "_id", "account"),
                        unwind("$account", false ),
                        project(new Document("communication._id", 1)
                                .append("communication.message", 1)
                                .append("communication.at", 1)
                                .append("communication.values", 1)
                                .append("communication.from.userId", "$user._id")
                                .append("communication.from.userAccountId", "$userAccount._id")
                                .append("communication.from.accountId", "$account._id")
                                .append("communication.from.accountType", "$account.type")
                                .append("communication.from.type", "$communication.from.type")
                                .append("communication.from.logo", "$account.logo")
                                .append("communication.from.companyName", "$account.name")
                                .append("communication.from.firstName", "$user.firstName")
                                .append("communication.from.lastName", "$user.lastName")
                                .append("communication.from.emailAddress", "$user.emailAddress")
                                .append("communication.from.jobTitle", "$userAccount.jobTitle")
                                .append("communication.from.profilePicture", "$user.picture")
                        ),
                        new Document("$group", new Document("_id", "$_id").append("communication", new Document("$push", "$communication"))),
                        lookup(HotelRfpBidCollection.COLLECTION_NAME, "_id", "_id", "bid"),
                        unwind("$bid", false),
                        project(new Document("parties.buyer.userAccountId", "$bid.buyer.contact.id")
                                .append("parties.buyer.accountId", "$bid.buyer.contact.company.accountId")
                                .append("parties.buyer.accountType", "$bid.buyer.contact.company.accountType")
                                .append("parties.buyer.type", "BUYER")
                                .append("parties.buyer.logo", "$bid.buyer.contact.company.logo")
                                .append("parties.buyer.companyName", "$bid.buyer.contact.company.name")
                                .append("parties.buyer.firstName", "$bid.buyer.contact.firstName")
                                .append("parties.buyer.lastName", "$bid.buyer.contact.lastName")
                                .append("parties.buyer.emailAddress", "$bid.buyer.contact.emailAddress")
                                .append("parties.buyer.jobTitle", "$bid.buyer.contact.jobTitle")
                                .append("parties.buyer.profilePicture", "$bid.buyer.contact.picture")

                                .append("parties.supplier.userAccountId", "$bid.supplier.contact.id")
                                .append("parties.supplier.accountId", "$bid.supplier.contact.company.accountId")
                                .append("parties.supplier.accountType", "$bid.supplier.contact.company.accountType")
                                .append("parties.supplier.type", "SUPPLIER")
                                .append("parties.supplier.logo", "$bid.supplier.contact.company.logo")
                                .append("parties.supplier.companyName", "$bid.supplier.contact.company.name")
                                .append("parties.supplier.firstName", "$bid.supplier.contact.firstName")
                                .append("parties.supplier.lastName", "$bid.supplier.contact.lastName")
                                .append("parties.supplier.emailAddress", "$bid.supplier.contact.emailAddress")
                                .append("parties.supplier.jobTitle", "$bid.supplier.contact.jobTitle")
                                .append("parties.supplier.profilePicture", "$bid.supplier.contact.picture")
                                .append("config", "$bid.negotiations.config")
                                .append("communication", 1))
                )).first();
    }

    @Override
    @MongoRetry
    public void updateNegotiation(String bidId, HotelRfpNegotiation negotiation, HotelRfpBidState state) {
        // todo: should check for status changes
        negotiationsCollection.updateOne(
                and(byId(bidId), eq("negotiations.communication._id", negotiation.getId())),
                combine(
                        set(HotelRfpBidCollection.STATE, state),
                        set("negotiations.communication.$", negotiation)
                ));
    }

    @Override
    @MongoRetry
    public void updateNegotiation(String bidId, HotelRfpNegotiation negotiation, HotelRfpBidOffer offer, HotelRfpBidState state) {
        // todo: should check for status changes
        negotiationsCollection.updateOne(
                and(byId(bidId), eq("negotiations.communication._id", negotiation.getId())),
                combine(
                        set(HotelRfpBidCollection.STATE, state),
                        set("offer", offer),
                        set("negotiations.communication.$", negotiation)
                ));
    }

    @Override
    @MongoRetry
    public void updateAndFinalize(String bidId, QuestionnaireResponse response, HotelRfpNegotiation negotiation, HotelRfpBidOffer offer, HotelRfpBidState state) {
        // todo: should check for status changes
        negotiationsCollection.updateOne(
                and(byId(bidId), eq("negotiations.communication._id", negotiation.getId())),
                combine(
                        set(HotelRfpBidCollection.STATE, state),
                        set("questionnaire.response", response),
                        set("offer", offer),
                        set("negotiations.communication.$", negotiation)
                ));
    }

    @Override
    public void addNegotiation(String bidId, HotelRfpNegotiation negotiation, HotelRfpBidState state) {
        safeNonIndempotentWriteService.updateOne(negotiationsCollection, byId(bidId), new ArrayList<>(Arrays.asList(
                set(HotelRfpBidCollection.STATE, state),
                push("negotiations.communication", negotiation)
        )));
    }

    @Override
    public void addNegotiation(String bidId, HotelRfpNegotiation negotiation, HotelRfpBidOffer offer, HotelRfpBidState state) {
        // todo: should check for status changes
        safeNonIndempotentWriteService.updateOne(negotiationsCollection, byId(bidId), new ArrayList<>(Arrays.asList(
                set(HotelRfpBidCollection.STATE, state),
                set("offer", offer),
                push("negotiations.communication", negotiation)
        )));
    }

    @Override
    public void addAndFinalize(String bidId, QuestionnaireResponse response, HotelRfpNegotiation negotiation, HotelRfpBidOffer offer, HotelRfpBidState state) {
        // todo: should check for status changes
        safeNonIndempotentWriteService.updateOne(negotiationsCollection, byId(bidId), new ArrayList<>(Arrays.asList(
                set(HotelRfpBidCollection.STATE, state),
                set("questionnaire.response", response),
                set("offer", offer),
                push("negotiations.communication", negotiation))));
    }
}
