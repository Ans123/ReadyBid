package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.mongodb.collections.fields.CreatedFields;
import net.readybid.mongodb.collections.fields.IdField;
import net.readybid.mongodb.collections.fields.StatusFields;

public class HotelRfpBidCollection implements IdField, StatusFields, CreatedFields {

    public static final String COLLECTION_NAME = "Bid";

    public static final String BUYER = "buyer";
    public static final String BUYER_COMPANY_ENTITY_ID = "buyer.company.entityId";

    public static final String BUYER_CONTACT = "buyer.contact";
    public static final String BUYER_CONTACT_ID = "buyer.contact.id";
    public static final String BUYER_CONTACT_FIRST_NAME = "buyer.contact.firstName";
    public static final String BUYER_CONTACT_LAST_NAME = "buyer.contact.lastName";
    public static final String BUYER_CONTACT_FULL_NAME = "buyer.contact.fullName";
    public static final String BUYER_CONTACT_EMAIL_ADDRESS = "buyer.contact.emailAddress";
    public static final String BUYER_CONTACT_PHONE = "buyer.contact.phone";
    public static final String BUYER_CONTACT_PROFILE_PICTURE = "buyer.contact.profilePicture";

    public static final String BUYER_CONTACT_COMPANY_ACCOUNT_ID = "buyer.contact.company.accountId";
    public static final String BUYER_CONTACT_COMPANY_NAME = "buyer.contact.company.name";
    public static final String BUYER_CONTACT_COMPANY_INDUSTRY = "buyer.contact.company.industry";
    public static final String BUYER_CONTACT_COMPANY_LOCATION = "buyer.contact.company.location";
    public static final String BUYER_CONTACT_COMPANY_WEBSITE = "buyer.contact.company.website";
    public static final String BUYER_CONTACT_COMPANY_LOGO = "buyer.contact.company.logo";

    public static final String BUYER_COMPANY_ACCOUNT_ID = "buyer.company.accountId";
    public static final String BUYER_COMPANY_NAME = "buyer.company.name";
    public static final String BUYER_COMPANY_INDUSTRY = "buyer.company.industry";
    public static final String BUYER_COMPANY_LOCATION = "buyer.company.location";
    public static final String BUYER_COMPANY_WEBSITE = "buyer.company.website";
    public static final String BUYER_COMPANY_LOGO = "buyer.company.logo";

    public static final String DISTANCE_KM = "analytics.distanceKm";
    public static final String DISTANCE_MI = "analytics.distanceMi";
    public static final String FINAL_AGREEMENT = "rfp.finalAgreement";
    public static final String FINAL_AGREEMENT_DATE = "finalAgreementDate";
    public static final String NEGOTIATIONS = "negotiations";
    public static final String NO_SYNC = "_noSync";
    public static final String RATE_LOADING_INFORMATION = "rateLoadingInformation";
    public static final String RFP_ID = "rfp._id";
    public static final String RFP_SPECIFICATIONS = "rfp.specifications";
    public static final String RFP_NAME = "rfp.specifications.name";
    public static final String PROGRAM_START_DATE = "rfp.specifications.programStartDate";
    public static final String PROGRAM_END_DATE = "rfp.specifications.programEndDate";
    public static final String PROGRAM_YEAR = "rfp.specifications.programYear";
    public static final String SENT_DATE = "rfp.specifications.sentDate";
    public static final String DUE_DATE = "rfp.specifications.dueDate";
    public static final String RFP_CHAIN_SUPPORT = "rfp.specifications.chainSupport";

    public static final String SUPPLIER = "supplier";
    public static final String SUPPLIER_COMPANY_ACCOUNT_ID = "supplier.company.accountId";
    public static final String SUPPLIER_COMPANY_ENTITY_ID = "supplier.company.entityId";
    public static final String SUPPLIER_COMPANY_FULL_ADDRESS = "supplier.company.location.fullAddress";
    public static final String SUPPLIER_COMPANY_NAME = "supplier.company.name";
    public static final String SUPPLIER_COMPANY_INDUSTRY = "supplier.company.industry";
    public static final String SUPPLIER_COMPANY_LOCATION = "supplier.company.location";
    public static final String SUPPLIER_COMPANY_WEBSITE = "supplier.company.website";

    public static final String SUPPLIER_COMPANY_CHAIN_ID = "supplier.company.chain._id";
    public static final String SUPPLIER_COMPANY_CHAIN_NAME = "supplier.company.chain.name";
    public static final String SUPPLIER_COMPANY_CHAIN_INDUSTRY = "supplier.company.chain.industry";
    public static final String SUPPLIER_COMPANY_CHAIN_LOCATION = "supplier.company.chain.location";
    public static final String SUPPLIER_COMPANY_CHAIN_WEBSITE = "supplier.company.chain.website";

    public static final String SUPPLIER_COMPANY_CHAIN_MASTER_ID = "supplier.company.chain.master._id";
    public static final String SUPPLIER_COMPANY_CHAIN_MASTER_NAME = "supplier.company.chain.master.name";
    public static final String SUPPLIER_COMPANY_CHAIN_MASTER_INDUSTRY = "supplier.company.chain.master.industry";;
    public static final String SUPPLIER_COMPANY_CHAIN_MASTER_LOCATION = "supplier.company.chain.master.location";
    public static final String SUPPLIER_COMPANY_CHAIN_MASTER_WEBSITE = "supplier.company.chain.master.website";


    public static final String SUPPLIER_CONTACT = "supplier.contact";
    public static final String SUPPLIER_CONTACT_ID = "supplier.contact.id";
    public static final String SUPPLIER_CONTACT_FIRST_NAME = "supplier.contact.firstName";
    public static final String SUPPLIER_CONTACT_LAST_NAME = "supplier.contact.lastName";
    public static final String SUPPLIER_CONTACT_FULL_NAME = "supplier.contact.fullName";
    public static final String SUPPLIER_CONTACT_EMAIL_ADDRESS = "supplier.contact.emailAddress";
    public static final String SUPPLIER_CONTACT_PHONE = "supplier.contact.phone";
    public static final String SUPPLIER_CONTACT_PROFILE_PICTURE = "supplier.contact.profilePicture";
    public static final String SUPPLIER_CONTACT_IS_USER = "supplier.contact.isUser";
    public static final String SUPPLIER_CONTACT_JOB_TITLE = "supplier.contact.jobTitle";

    public static final String SUPPLIER_CONTACT_COMPANY_ACCOUNT_ID = "supplier.contact.company.accountId";
    public static final String SUPPLIER_CONTACT_COMPANY_LOCATION = "supplier.contact.company.location";
    public static final String SUPPLIER_CONTACT_COMPANY_INDUSTRY = "supplier.contact.company.industry";
    public static final String SUPPLIER_CONTACT_COMPANY_NAME = "supplier.contact.company.name";
    public static final String SUPPLIER_CONTACT_COMPANY_WEBSITE = "supplier.contact.company.website";
    public static final String SUPPLIER_CONTACT_COMPANY_TYPE = "supplier.contact.company.type";

    public static final String QUESTIONNAIRE = "questionnaire";
    public static final String ACCEPTED_RATES = "questionnaire.accepted";
    public static final String QUESTIONNAIRE_RESPONSE = "questionnaire.response";
    public static final String QUESTIONNAIRE_RESPONSE_ANSWERS = "questionnaire.response.answers";
    public static final String QUESTIONNAIRE_RESPONSE_ANSWERS_CLIENT_NAME = "questionnaire.response.answers.CLIENT_NAME";

    public static final String QUESTIONNAIRE_RESPONSE_DRAFT = "questionnaire.responseDraft";
    public static final String QUESTIONNAIRE_RESPONSE_DRAFT_ANSWERS = "questionnaire.responseDraft.answers";
    public static final String QUESTIONNAIRE_RESPONSE_DRAFT_ANSWERS_CLIENT_NAME = "questionnaire.responseDraft.answers.CLIENT_NAME";

    public static final String SUBJECT = "subject";
    public static final String SUBJECT_ID = "subject._id";
    public static final String SUBJECT_LOCATION_COORDINATES = "subject.location.coordinates";

    public static final String COVER_LETTER = "rfp.coverLetter";
    public static final String ANALYTICS = "analytics";
    public static final String OFFER = "offer";

    public static final String STATE = "state";
    public static final String STATE_STATUS = "state.status";

    public static final String BID_HOTEL_RFP_TYPE = "hotelRfpType";
}
