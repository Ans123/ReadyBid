package net.readybid.api.main.bid;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.result.UpdateResult;
import net.readybid.api.hotelrfp.traveldestination.HotelRfpBidSupplierCompanyEntityAndSubjectRepository;
import net.readybid.api.main.entity.rateloadinginformation.RateLoadingInformationRepository;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.core.entities.traveldestination.TravelDestinationImpl;
import net.readybid.app.core.service.ListBidsRepository;
import net.readybid.app.entities.authorization.dirty.InvolvedAccounts;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidImpl;
import net.readybid.app.entities.rfp_hotel.dirty.DeleteBidRequest;
import net.readybid.app.entities.rfp_hotel.dirty.DeleteBidsResult;
import net.readybid.app.interactors.rfp_hotel.gateway_dirty.BidRepository;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.mongodb.MongoRetry;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfp.specifications.HotelRfpType;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.rfphotel.bid.core.HotelRfpBidSupplierCompanyEntityAndSubject;
import net.readybid.rfphotel.supplier.HotelRfpSupplier;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.*;
import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 4/3/2017.
 *
 */
@Service
public class BidRepositoryImpl implements BidRepository, ListBidsRepository, HotelRfpBidSupplierCompanyEntityAndSubjectRepository {

    private final MongoCollection<HotelRfpBidImpl> bidCollection;
    private final MongoDatabase mongoDatabase;

    @Autowired
    public BidRepositoryImpl(MongoDatabase mongoDatabase) {
        bidCollection = mongoDatabase.getCollection(COLLECTION_NAME, HotelRfpBidImpl.class);
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    @MongoRetry
    public void updateBidsRfpName(String rfpId, String name, AuthenticatedUser user) {
        bidCollection.updateMany(
                and(eq(RFP_ID, oid(rfpId)), exists(NO_SYNC, false)),
                set(RFP_NAME, name));
    }

    @Override
    @MongoRetry
    public void updateBidsDueDate(String rfpId, LocalDate dueDate, AuthenticatedUser user) {
        bidCollection.updateMany(
                and(eq(RFP_ID, oid(rfpId)), exists(NO_SYNC, false)),
                set(DUE_DATE, dueDate));
    }

    @Override
    @MongoRetry
    // todo: ONLY IF NOT RESPONDED
    // todo: what about DRAFT Response
    public void updateBidsProgramPeriod(String rfpId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user) {
        bidCollection.updateMany(
                and(eq(RFP_ID, oid(rfpId)), exists(NO_SYNC, false)),
                combine(
                        set(PROGRAM_START_DATE, programStartDate),
                        set(PROGRAM_END_DATE, programEndDate),
                        set(PROGRAM_YEAR, programStartDate.getYear()),
                        set("response.answers.SEASON1START", programStartDate),
                        set("response.answers.SEASON1END", programEndDate)
                ));
    }

    @Override
    @MongoRetry
    public void updateBidsCoverLetterTemplate(String rfpId, String sanitizedTemplate) {
        bidCollection.updateMany(
                and(eq(RFP_ID, oid(rfpId)), exists(NO_SYNC, false), eq(BID_HOTEL_RFP_TYPE, HotelRfpType.DIRECT)),
                set(COVER_LETTER, sanitizedTemplate)
        );
    }

    @Override
    @MongoRetry
    public void updateBidsQuestionnaireModel(String rfpId, Map<String, Object> model, List<QuestionnaireConfigurationItem> config) {
        bidCollection.updateMany(
                and(eq(RFP_ID, oid(rfpId)), exists(NO_SYNC, false)),
                combine(
                        set("questionnaire.model", model),
                        set("questionnaire.config", config)
                ));
    }

    @Override
    @MongoRetry
    public Map<String, Long> getBidsCountPerDestinationForRfp(String rfpId) {
        final Map<String, Long> result = new HashMap<>();

        AggregateIterable<Document> output = mongoDatabase.getCollection(COLLECTION_NAME)
                .aggregate(Arrays.asList(
                        match(doc(RFP_ID, oid(rfpId)).append(STATE_STATUS, doc("$ne", "DELETED"))),
                        doc("$group", doc("_id", "$subject._id").append("properties", doc("$sum", 1)))
                ));

        for(Document d : output){
            result.put(
                    d.getObjectId("_id").toString(), Long.parseLong(String.valueOf(d.get("properties"))));
        }

        return result;
    }

    @Override
    @MongoRetry
    public HotelRfpBid markCreatedBidAsDeleted(String rfpId, String bidId, HotelRfpBidState state) {
        final HotelRfpBidImpl bid = bidCollection.findOneAndUpdate(
                and(byId(bidId), eq(RFP_ID, oid(rfpId)), byState(HotelRfpBidStateStatus.CREATED)),
                combine(set(STATE, state)),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        );
        if(bid != null) bid.setState(state); // status by is not joined so it is set with this
        return bid;
    }

    @Override
    public HotelRfpBid getBidById(String bidId) {
        return getBidById(oid(bidId));
    }

    @Override
    @MongoRetry
    public void updateBidDueDate(String bidId, LocalDate dueDate, AuthenticatedUser user) {
        bidCollection.updateOne(
                and(byId(bidId)),
                combine(set(DUE_DATE, dueDate), set(NO_SYNC, true)));
    }

    @Override
    @MongoRetry
    // todo: what about draft response?
    public void updateBidProgramPeriod(String bidId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user) {
        bidCollection.updateOne(
                and(byId(bidId)),
                combine(
                        set(PROGRAM_START_DATE, programStartDate),
                        set(PROGRAM_END_DATE, programEndDate),
                        set(PROGRAM_YEAR, programStartDate.getYear()),
                        set("response.answers.SEASON1START", programStartDate),
                        set("response.answers.SEASON1END", programEndDate),
                        set(NO_SYNC, true)
                ));
    }

    @Override
    @MongoRetry
    public HotelRfpBid getBidWithCoverLetterTemplate(String bidId, AuthenticatedUser user) {
        return bidCollection
                .find(and(byId(bidId)))
                .projection(exclude("rfp.questionnaire", "response", "negotiations"))
                .first();
    }

    @Override
    @MongoRetry
    public void updateBidCoverLetterTemplate(String bidId, String sanitizedTemplate, AuthenticatedUser user) {
        bidCollection.updateOne(
                and(byId(bidId), eq(BID_HOTEL_RFP_TYPE, HotelRfpType.DIRECT)),
                combine(
                        set(COVER_LETTER, sanitizedTemplate),
                        set(NO_SYNC, true)
                ));
    }

    @Override
    @MongoRetry
    public void updateBidQuestionnaireModel(String bidId, Map<String, Object> questionnaireModel, List<QuestionnaireConfigurationItem> questionnaireConfig, AuthenticatedUser user) {
        bidCollection.updateOne(
                and(byId(bidId)),
                combine(
                        set("questionnaire.model", questionnaireModel),
                        set("questionnaire.config", questionnaireConfig),
                        set(NO_SYNC, true)
                ));
    }

    @Override
    @MongoRetry
    public Questionnaire getQuestionnaireModel(String bidId, AuthenticatedUser user) {
        final HotelRfpBidImpl bid =  bidCollection
                .find(and(byId(bidId)))
                .projection(include("questionnaire.config", "questionnaire.model", "questionnaire.type"))
                .first();
        return bid == null ? null : bid.getQuestionnaire();
    }

    @Override
    @MongoRetry
    public InvolvedAccounts getInvolvedAccounts(String id) {
        final HotelRfpBid bid = bidCollection.find(byId(id))
                .projection(include(SUPPLIER_CONTACT_COMPANY_ACCOUNT_ID, BUYER_COMPANY_ACCOUNT_ID))
                .first();
        return InvolvedAccounts.create(bid);
    }

    @Override
    public List<InvolvedAccounts> getInvolvedAccounts(List<String> ids) {
        final List<HotelRfpBid> bids = bidCollection.find(byIds(ids))
                .projection(include(SUPPLIER_CONTACT_COMPANY_ACCOUNT_ID, BUYER_COMPANY_ACCOUNT_ID))
                .into(new ArrayList<>());

        return bids.stream().map(InvolvedAccounts::create).collect(Collectors.toList());
    }

    @Override
    @MongoRetry
    public void setSupplierContact(HotelRfpBid bid) {
        bidCollection.updateOne(
                byId(bid.getId()),
                set(SUPPLIER_CONTACT, bid.getSupplierContact())
        );
    }

    @Override
    @MongoRetry
    public RfpContact getBidSupplierContact(String bidId) {
        final HotelRfpBid bid = bidCollection.find(byId(bidId)).projection(include(SUPPLIER_CONTACT)).first();
        return null == bid ? null : bid.getSupplierContact();
    }

    @Override
    @MongoRetry
    public HotelRfpSupplier getBidSupplier(String bidId) {
        final HotelRfpBid bid = bidCollection.find(byId(bidId)).projection(include(SUPPLIER)).first();
        return null == bid ? null : bid.getSupplier();
    }

    @Override
    @MongoRetry
    public HotelRfpBid getBidById(ObjectId bidId) {
        return bidCollection.find(byId(bidId)).projection(exclude("negotiations")).first();
    }

    @Override
    @MongoRetry
    public QuestionnaireResponse getResponse(String bidId) {
        final HotelRfpBid bid = bidCollection.find(byId(bidId))
                .projection(include("questionnaire.response"))
                .first();
        return bid == null ? null : bid.getResponse();
    }

    @Override
    @MongoRetry
    public HotelRfpBid getBidWithRateLoadingInformation(String bidId) {
        return bidCollection.aggregate(Arrays.asList(
                match(new Document("_id", oid(bidId))),
                lookup(
                        RateLoadingInformationRepository.COLLECTION,
                        "buyer.company.entityId",
                        "entityId",
                        "rateLoadingInformation"),
                unwind("$rateLoadingInformation", true),
                addFields(new Document("rateLoadingInformation", "$rateLoadingInformation.information"))
        )).first();
    }

    @Override
    @MongoRetry
    public DeleteBidsResult markAsDeleted(DeleteBidRequest request, HotelRfpBidState state) {
        final Bson byRfpId = eq(RFP_ID, oid(request.rfpId));
        final Bson bySubjectId = eq(SUBJECT_ID, oid(request.destinationId));

        final UpdateResult updateResult = bidCollection.updateMany(
                and(byRfpId, bySubjectId, byState(HotelRfpBidStateStatus.CREATED, HotelRfpBidStateStatus.DELETED )),
                combine(set(STATE, state)));

        final long remainingDestinationBids = bidCollection
                .count(and(byRfpId, bySubjectId, skipState(HotelRfpBidStateStatus.DELETED, HotelRfpBidStateStatus.WITHDRAWN)));

        return new DeleteBidsResult(updateResult.getModifiedCount(), remainingDestinationBids);
    }

    @Override
    @MongoRetry
    public HotelRfpBid findDeletedBid(ObjectId rfpId, String travelDestinationId, String hotelId) {
        return bidCollection.find(
                and(
                        eq(RFP_ID, rfpId),
                        eq(SUBJECT_ID, oid(travelDestinationId)),
                        eq(SUPPLIER_COMPANY_ENTITY_ID, oid(hotelId)),
                        byState(HotelRfpBidStateStatus.DELETED))
        )
                .projection(include("created"))
                .first();
    }

    @Override
    @MongoRetry
    public void create(HotelRfpBid bid) {
        bidCollection.insertOne((HotelRfpBidImpl) bid);
    }

    @Override
    @MongoRetry
    public void replace(HotelRfpBid bid) {
        bidCollection.replaceOne(byId(bid.getId()), (HotelRfpBidImpl) bid);
    }

    @Override
    @MongoRetry
    public void updateTravelDestination(TravelDestination destination) {
        bidCollection.updateMany(
                eq(SUBJECT_ID, oid(destination.getId())),
                combine(set(SUBJECT, (TravelDestinationImpl) destination))
        );
    }

    @Override
    @MongoRetry
    public void updateDistancesForDestination(String destinationId) {
        final BasicDBObject command = new BasicDBObject();
        final String updateDistancesCommand = "function distance(lat1, lon1, lat2, lon2) { "+
                "var R = 6371; "+
                "var radiansMultiplier = 0.01745329251994329576923690768489;"+
                "var dLatHalf = (lat2-lat1) * radiansMultiplier/2;"+
                "var dLonHalf = (lon2-lon1) *  radiansMultiplier/2;"+
                "var a = Math.pow(Math.sin(dLatHalf),2) +"+
                "Math.cos(lat1 * radiansMultiplier) * Math.cos(lat2 * radiansMultiplier) *"+
                "Math.pow(Math.sin(dLonHalf),2);"+
                "return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));"+
                "};"+
                // todo: project only coordinates
                "db.getCollection('Bid').find({\"subject._id\": ObjectId(\"%s\")}).forEach(function(bid){"+

                "var hotel = bid.supplier.company.location.coordinates;"+
                "var destination = bid.subject.location.coordinates;"+
                "var kmDistance = distance(destination.lat, destination.lng, hotel.lat, hotel.lng);"+

                "db.getCollection('Bid').updateOne({\"_id\": bid._id}, {$set: {\"analytics.distanceKm\": kmDistance, \"analytics.distanceMi\": kmDistance * 0.62137119223733396961743418436332}});"+
                "});";
        command.put("eval", String.format("function() { %s return; }", String.format(updateDistancesCommand, destinationId)));
        Document result = mongoDatabase.runCommand(command);
    }

    @Override
    @MongoRetry
    public HotelRfpBid getBidForResponseDraft(String bidId) {
        return bidCollection.find( byId(bidId) )
                .projection(include("questionnaire", "rfp.specifications"))
                .first();
    }

    @Override
    @MongoRetry
    public List<HotelRfpBidSupplierCompanyEntityAndSubject> getBuyerBidsFromLastYearOrInDestination(ObjectId buyerCompanyAccountId, int lastProgramYear, String destinationId) {
        final Bson buyer = eq(BUYER_COMPANY_ACCOUNT_ID, buyerCompanyAccountId);
        final Bson lastYearBid = and(eq(PROGRAM_YEAR, lastProgramYear), byState(HotelRfpBidStateStatus.CREATED));
        final Bson destinationBid = and(eq(SUBJECT_ID, oid(destinationId)), ne(STATE_STATUS, HotelRfpBidStateStatus.DELETED));

        return bidCollection.find(and(buyer, or(lastYearBid,destinationBid)))
                .projection(include(SUPPLIER_COMPANY_ENTITY_ID, STATE, SUBJECT_ID))
                .into(new ArrayList<>());
    }
}
