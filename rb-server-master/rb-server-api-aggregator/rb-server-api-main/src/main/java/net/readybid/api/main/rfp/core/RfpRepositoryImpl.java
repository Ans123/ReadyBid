package net.readybid.api.main.rfp.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.api.hotelrfp.traveldestination.HotelRfpBuyerCompanyAndProgramYearRepository;
import net.readybid.api.main.entity.rateloadinginformation.RateLoadingInformationRepository;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.traveldestination.TravelDestinationHotelFilter;
import net.readybid.app.entities.authorization.dirty.InvolvedAccounts;
import net.readybid.app.interactors.rfp_hotel.gateway_dirty.RfpRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpCollection;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.mongodb.MongoRetry;
import net.readybid.rfp.core.HotelRfpBuyerCompanyAndProgramYear;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.core.RfpImpl;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
@Service
public class RfpRepositoryImpl implements RfpRepository, HotelRfpBuyerCompanyAndProgramYearRepository {

    private final MongoCollection<RfpImpl> rfpCollection;

    @Autowired
    public RfpRepositoryImpl(MongoDatabase mongoDatabase) {
        this.rfpCollection = mongoDatabase.getCollection(HotelRfpCollection.COLLECTION_NAME, RfpImpl.class);
    }

    @Override
    @MongoRetry
    public void createRfp(Rfp rfp) {
        rfpCollection.insertOne((RfpImpl) rfp);
    }

    @Override
    @MongoRetry
    public Rfp getRfpById(String rfpId) {
        return rfpCollection.aggregate(joinCreatedAndStatus(byId(rfpId))).first();
    }

    @Override
    @MongoRetry
    public void updateRfpName(String rfpId, String name, AuthenticatedUser user) {
        rfpCollection
                .updateOne(
                        and(byId(rfpId)),
                        combine(set("specifications.name", name))
                );
    }

    @Override
    @MongoRetry
    public void updateRfpDueDate(String rfpId, LocalDate dueDate, AuthenticatedUser user) {
        rfpCollection
                .updateOne(
                        and(byId(rfpId)),
                        combine(set("specifications.dueDate", dueDate))
                );
    }

    @Override
    @MongoRetry
    public void updateRfpProgramPeriod(String rfpId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user) {
        rfpCollection
                .updateOne(
                        and(byId(rfpId)),
                        combine(
                                set("specifications.programStartDate", programStartDate),
                                set("specifications.programEndDate", programEndDate),
                                set("specifications.programYear", programStartDate.getYear()),
                                set("response.answers.SEASON1START", programStartDate),
                                set("response.answers.SEASON1END", programEndDate)
                        )
                );
    }

    @Override
    @MongoRetry
    public Rfp getRfpWithCoverLetterTemplate(String rfpId) {
        return rfpCollection
                .find(and(byId(rfpId)))
                .projection(include(
                        "coverLetter"
                )).first();
    }

    @Override
    @MongoRetry
    public void updateCoverLetterTemplate(String rfpId, String sanitizedTemplate) {
        rfpCollection
                .updateOne(
                        and(byId(rfpId)),
                        combine(
                                set("coverLetter", sanitizedTemplate)
                        )
                );
    }

    @Override
    @MongoRetry
    public Questionnaire getQuestionnaireModel(String rfpId) {
        final Rfp rfp = rfpCollection
                .find(and(byId(rfpId)))
                .projection(include(
                        "questionnaire.type",
                        "questionnaire.model",
                        "questionnaire.config"
                )).first();

        return rfp == null ? null : rfp.getQuestionnaire();
    }

    @Override
    @MongoRetry
    public void updateQuestionnaireModel(String rfpId, Map<String, Object> model, List<QuestionnaireConfigurationItem> config) {
        rfpCollection
                .updateOne(
                        and(byId(rfpId)),
                        combine(
                                set("questionnaire.model", model),
                                set("questionnaire.config", config)
                        )
                );
    }

    @Override
    @MongoRetry
    public InvolvedAccounts getInvolvedCompanies(String id) {
        return rfpCollection.find(byId(id))
                .projection(include("specifications.buyer.company.accountId"))
                .map(InvolvedAccounts::create)
                .first();
    }

    @Override
    public List<InvolvedAccounts> getInvolvedCompanies(List<String> ids) {
        return rfpCollection.find(byIds(ids))
                .projection(include("specifications.buyer.company.accountId"))
                .map(InvolvedAccounts::create).into(new ArrayList<>());
    }

    @Override
    @MongoRetry
    public Rfp getRfpWithRateLoadingInformation(String rfpId) {
        final List<Bson> aggregates = joinCreatedAndStatus(byId(rfpId));
        aggregates.add(lookup(
                RateLoadingInformationRepository.COLLECTION,
                "specifications.buyer.company.entityId",
                "entityId",
                "rateLoadingInformation"));

        aggregates.add(unwind("$rateLoadingInformation", true));
        aggregates.add(addFields(new Document("rateLoadingInformation", "$rateLoadingInformation.information")));

        return rfpCollection.aggregate(aggregates).first();
    }

    @Override
    @MongoRetry
    public HotelRfpBuyerCompanyAndProgramYear findHotelRfpBuyerCompanyAndProgramYear(String rfpId) {
        return rfpCollection.find(byId(rfpId))
                .projection(include("specifications.buyer.company", "specifications.programYear"))
                .first();
    }

    @Override
    @MongoRetry
    public void setDefaultFilter(String rfpId, TravelDestinationHotelFilter filter){
        rfpCollection.updateOne(
                and(byId(rfpId)),
                set("defaultFilter", filter)
        );
    }

    @Override
    @MongoRetry
    public TravelDestinationHotelFilter getRfpDefaultFilter(String rfpId) {
        final RfpImpl rfp = rfpCollection
                .find(byId(rfpId))
                .projection(include("defaultFilter"))
                .first();
        return rfp == null ? null : rfp.getDefaultFilter();
    }
}
