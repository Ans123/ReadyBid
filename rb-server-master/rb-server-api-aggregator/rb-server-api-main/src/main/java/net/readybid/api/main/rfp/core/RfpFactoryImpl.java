package net.readybid.api.main.rfp.core;

import net.readybid.api.main.rfp.buyer.BuyerFactory;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.entities.rfp.questionnaire.QuestionnaireImpl;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.core.entities.rfp.QuestionnaireResponseImpl;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.buyer.Buyer;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.core.RfpImpl;
import net.readybid.rfp.core.RfpStatus;
import net.readybid.rfp.core.RfpStatusDetails;
import net.readybid.rfp.specifications.RfpSpecifications;
import net.readybid.rfp.specifications.RfpSpecificationsFactory;
import net.readybid.rfp.template.RfpTemplate;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
@Service
public class RfpFactoryImpl implements RfpFactory {

    private final BuyerFactory buyerFactory;
    private final RfpSpecificationsFactory specificationsFactory;

    @Autowired
    public RfpFactoryImpl(BuyerFactory buyerFactory, RfpSpecificationsFactory specificationsFactory) {
        this.buyerFactory = buyerFactory;
        this.specificationsFactory = specificationsFactory;
    }

    @Override
    public Rfp createRfp(RfpTemplate template, AuthenticatedUser user, Entity entity) {
        final RfpImpl rfp = new RfpImpl();
        final Buyer buyer = buyerFactory.createBuyer(user, entity);
        rfp.setId(new ObjectId());

        rfp.setSpecifications(specificationsFactory.createSpecifications(template.getType(), buyer));

        rfp.setCoverLetter(template.getCoverLetter());
        rfp.setQuestionnaire(createQuestionnaire(template.getQuestionnaire(), rfp));
        rfp.setFinalAgreementTemplate(template.getFinalAgreementTemplate());

        rfp.setCreated(new CreationDetails(user));
        rfp.setStatus(new RfpStatusDetails(rfp.getCreated(), RfpStatus.CREATED));

        return rfp;
    }

    private static Questionnaire createQuestionnaire(Questionnaire questionnaire, RfpImpl rfp) {
        final QuestionnaireImpl impl = new QuestionnaireImpl();
        impl.setType(rfp.getType());
        impl.setModel(questionnaire.getModel());
        impl.setConfig(questionnaire.getConfig());
        impl.setGlobals(createQuestionnaireGlobals(rfp.getSpecifications()));
        impl.setResponse(createResponse(questionnaire.getResponse(), rfp.getSpecifications(), rfp.getBuyerCompanyName()));

        return impl;
    }

    private static Map<String, String> createQuestionnaireGlobals(RfpSpecifications specifications) {
        final Map<String, String> questionnaireGlobals = new HashMap<>();
        questionnaireGlobals.put("programStartDate", String.valueOf(specifications.getProgramStartDate()));
        questionnaireGlobals.put("programEndDate", String.valueOf(specifications.getProgramEndDate()));
        return questionnaireGlobals;
    }

    private static QuestionnaireResponse createResponse(QuestionnaireResponse responseModel, RfpSpecifications rfpSpecifications, String companyName) {
        final QuestionnaireResponse response = new QuestionnaireResponseImpl(responseModel);

        response.setAnswer("CLIENT_NAME", trim(companyName, 50));
        response.setAnswer("RATE_CURR", "USD");
        response.setAnswer("SEASON1START", String.valueOf(rfpSpecifications.getProgramStartDate()));
        response.setAnswer("SEASON1END", String.valueOf(rfpSpecifications.getProgramEndDate()));

        return response;
    }

    private static String trim(String s, int toLength) {
        final int endIndex = s.length() >= toLength ? toLength-1 : s.length();
        return s.substring(0, endIndex);
    }
}
