package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.mongodb.collections.fields.CreatedFields;
import net.readybid.mongodb.collections.fields.IdField;
import net.readybid.mongodb.collections.fields.StatusFields;

public class HotelRfpCollection implements IdField, StatusFields, CreatedFields {

    public static final String COLLECTION_NAME = "Rfp";

    public static final String PROGRAM_END_DATE = "specifications.programEndDate";
    public static final String CHAIN_SUPPORT = "specifications.chainSupport";

    public static final String BUYER_CONTACT = "specifications.buyer.contact";
    public static final String BUYER_CONTACT_ID = "specifications.buyer.contact.id";
    public static final String BUYER_CONTACT_FIRST_NAME = "specifications.buyer.contact.firstName";
    public static final String BUYER_CONTACT_LAST_NAME = "specifications.buyer.contact.lastName";
    public static final String BUYER_CONTACT_FULL_NAME = "specifications.buyer.contact.fullName";
    public static final String BUYER_EMAIL_ADDRESS = "specifications.buyer.contact.emailAddress";
    public static final String BUYER_CONTACT_PHONE = "specifications.buyer.contact.phone";
    public static final String BUYER_CONTACT_PROFILE_PICTURE = "specifications.buyer.contact.profilePicture";

    public static final String BUYER_CONTACT_COMPANY = "specifications.buyer.contact.company";
    public static final String BUYER_CONTACT_COMPANY_ACCOUNT_ID = "specifications.buyer.contact.company.accountId";
    public static final String BUYER_CONTACT_COMPANY_NAME = "specifications.buyer.contact.company.name";
    public static final String BUYER_CONTACT_COMPANY_INDUSTRY = "specifications.buyer.contact.company.industry";
    public static final String BUYER_CONTACT_COMPANY_LOCATION = "specifications.buyer.contact.company.location";
    public static final String BUYER_CONTACT_COMPANY_WEBSITE = "specifications.buyer.contact.company.website";
    public static final String BUYER_CONTACT_COMPANY_LOGO = "specifications.buyer.contact.company.logo";

    public static final String BUYER_COMPANY = "specifications.buyer.company";
    public static final String BUYER_COMPANY_ACCOUNT_ID = "specifications.buyer.company.accountId";
    public static final String BUYER_COMPANY_NAME= "specifications.buyer.company.name";
    public static final String BUYER_COMPANY_INDUSTRY = "specifications.buyer.company.industry";
    public static final String BUYER_COMPANY_LOCATION = "specifications.buyer.company.location";
    public static final String BUYER_COMPANY_WEBSITE = "specifications.buyer.company.website";
    public static final String BUYER_COMPANY_LOGO = "specifications.buyer.company.logo";

    public static final String FINAL_AGREEMENT = "finalAgreement";

    public static final String QUESTIONNAIRE_RESPONSE_ANSWERS_CLIENT_NAME = "questionnaire.response.answers.CLIENT_NAME";
    public static final String STATUS_VALUE = "status.value";

    public static final String NAM_COVER_LETTER = "namCoverLetter";
    public static final String COVER_LETTER = "coverLetter";

}
