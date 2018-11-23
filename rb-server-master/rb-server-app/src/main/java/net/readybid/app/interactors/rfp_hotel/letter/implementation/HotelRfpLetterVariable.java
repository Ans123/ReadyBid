package net.readybid.app.interactors.rfp_hotel.letter.implementation;


import static net.readybid.app.interactors.rfp_hotel.letter.implementation.HotelRfpLetterVariableFormat.LOGO;
import static net.readybid.app.interactors.rfp_hotel.letter.implementation.HotelRfpLetterVariableFormat.TEXT;

enum HotelRfpLetterVariable {

    BUYER_COMPANY_NAME("buyer.company.name", "BUYER_COMPANY_NAME", TEXT),
    BUYER_COMPANY_ADDRESS_ADDRESS1("buyer.company.address.address1", "BUYER_COMPANY_ADDRESS_ADDRESS1", TEXT),
    BUYER_COMPANY_ADDRESS_ADDRESS2("buyer.company.address.address2", "BUYER_COMPANY_ADDRESS_ADDRESS2", TEXT),
    BUYER_COMPANY_ADDRESS_CITY("buyer.company.address.city", "BUYER_COMPANY_ADDRESS_CITY", TEXT),
    BUYER_COMPANY_ADDRESS_STATE("buyer.company.address.state", "BUYER_COMPANY_ADDRESS_STATE", TEXT),
    BUYER_COMPANY_ADDRESS_ZIP("buyer.company.address.zip", "BUYER_COMPANY_ADDRESS_ZIP", TEXT),
    BUYER_COMPANY_ADDRESS_COUNTRY("buyer.company.address.country", "BUYER_COMPANY_ADDRESS_COUNTRY", TEXT),
    BUYER_COMPANY_FULL_ADDRESS("buyer.company.address.fullAddress", "BUYER_COMPANY_FULL_ADDRESS", TEXT),
    BUYER_COMPANY_SHORT_ADDRESS("buyer.company.address.shortAddress", "BUYER_COMPANY_SHORT_ADDRESS", TEXT),
    BUYER_COMPANY_WEBSITE("buyer.company.website", "BUYER_COMPANY_WEBSITE", TEXT),
    BUYER_COMPANY_EMAIL_ADDRESS("buyer.company.email", "BUYER_COMPANY_EMAIL_ADDRESS", TEXT),
    BUYER_COMPANY_PHONE("buyer.company.phone", "BUYER_COMPANY_PHONE", TEXT),
    BUYER_COMPANY_LOGO("buyer.company.logo", "logo-placeholder.png", LOGO),

    BUYER_CONTACT_FIRST_NAME("buyer.contact.firstName", "BUYER_CONTACT_FIRST_NAME", TEXT),
    BUYER_CONTACT_LAST_NAME("buyer.contact.lastName", "BUYER_CONTACT_LAST_NAME", TEXT),
    BUYER_CONTACT_NAME("buyer.contact.fullName", "BUYER_CONTACT_NAME", TEXT),
    BUYER_CONTACT_JOB_TITLE("buyer.contact.jobTitle", "BUYER_CONTACT_JOB_TITLE", TEXT),
    BUYER_CONTACT_EMAIL_ADDRESS("buyer.contact.email", "BUYER_CONTACT_EMAIL_ADDRESS", TEXT),
    BUYER_CONTACT_PHONE_NUMBER("buyer.contact.phone", "BUYER_CONTACT_PHONE_NUMBER", TEXT),

    BUYER_CONTACT_COMPANY_NAME("buyer.contact.company.name", "BUYER_CONTACT_COMPANY_NAME", TEXT),
    BUYER_CONTACT_COMPANY_ADDRESS_ADDRESS1("buyer.contact.company.address.address1", "BUYER_CONTACT_COMPANY_ADDRESS_ADDRESS1", TEXT),
    BUYER_CONTACT_COMPANY_ADDRESS_ADDRESS2("buyer.contact.company.address.address2", "BUYER_CONTACT_COMPANY_ADDRESS_ADDRESS2", TEXT),
    BUYER_CONTACT_COMPANY_ADDRESS_CITY("buyer.contact.company.address.city", "BUYER_CONTACT_COMPANY_ADDRESS_CITY", TEXT),
    BUYER_CONTACT_COMPANY_ADDRESS_STATE("buyer.contact.company.address.state", "BUYER_CONTACT_COMPANY_ADDRESS_STATE", TEXT),
    BUYER_CONTACT_COMPANY_ADDRESS_ZIP("buyer.contact.company.address.zip", "BUYER_CONTACT_COMPANY_ADDRESS_ZIP", TEXT),
    BUYER_CONTACT_COMPANY_ADDRESS_COUNTRY("buyer.contact.company.address.country", "BUYER_CONTACT_COMPANY_ADDRESS_COUNTRY", TEXT),
    BUYER_CONTACT_COMPANY_FULL_ADDRESS("buyer.contact.company.address.fullAddress", "BUYER_CONTACT_COMPANY_FULL_ADDRESS", TEXT),
    BUYER_CONTACT_COMPANY_SHORT_ADDRESS("buyer.contact.company.address.shortAddress", "BUYER_CONTACT_COMPANY_SHORT_ADDRESS", TEXT),
    BUYER_CONTACT_COMPANY_WEBSITE("buyer.contact.company.website", "BUYER_CONTACT_COMPANY_WEBSITE", TEXT),
    BUYER_CONTACT_COMPANY_EMAIL_ADDRESS("buyer.contact.company.email", "BUYER_CONTACT_COMPANY_EMAIL_ADDRESS", TEXT),
    BUYER_CONTACT_COMPANY_PHONE("buyer.contact.company.phone", "BUYER_CONTACT_COMPANY_PHONE", TEXT),
    BUYER_CONTACT_COMPANY_LOGO( "buyer.contact.company.logo", "logo-placeholder.png", LOGO),

    PROPERTY_NAME("property.name", "PROPERTY_NAME", TEXT),
    PROPERTY_ADDRESS_ADDRESS1("property.address.address1", "PROPERTY_ADDRESS_ADDRESS1", TEXT),
    PROPERTY_ADDRESS_ADDRESS2("property.address.address2", "PROPERTY_ADDRESS_ADDRESS2", TEXT),
    PROPERTY_ADDRESS_CITY("property.address.city", "PROPERTY_ADDRESS_CITY", TEXT),
    PROPERTY_ADDRESS_STATE("property.address.state", "PROPERTY_ADDRESS_STATE", TEXT),
    PROPERTY_ADDRESS_ZIP("property.address.zip", "PROPERTY_ADDRESS_ZIP", TEXT),
    PROPERTY_ADDRESS_COUNTRY("property.address.country", "PROPERTY_ADDRESS_COUNTRY", TEXT),
    PROPERTY_FULL_ADDRESS("property.address.fullAddress", "PROPERTY_FULL_ADDRESS", TEXT),
    PROPERTY_SHORT_ADDRESS("property.address.shortAddress", "PROPERTY_SHORT_ADDRESS", TEXT),
    PROPERTY_PHONE("property.phone", "PROPERTY_PHONE", TEXT),
    PROPERTY_WEBSITE("property.website", "PROPERTY_WEBSITE", TEXT),
    PROPERTY_EMAIL_ADDRESS("property.email", "PROPERTY_EMAIL_ADDRESS", TEXT),
    PROPERTY_MASTER_CHAIN("property.chainMaster", "PROPERTY_MASTER_CHAIN", TEXT),
    PROPERTY_CHAIN_BRAND("property.chainBrand", "PROPERTY_CHAIN_BRAND", TEXT),

    SUPPLIER_CONTACT_FIRST_NAME("supplier.contact.firstName", "SUPPLIER_CONTACT_FIRST_NAME", TEXT),
    SUPPLIER_CONTACT_LAST_NAME("supplier.contact.lastName", "SUPPLIER_CONTACT_LAST_NAME", TEXT),
    SUPPLIER_CONTACT_NAME("supplier.contact.fullName", "SUPPLIER_CONTACT_NAME", TEXT),
    SUPPLIER_CONTACT_JOB_TITLE("supplier.contact.jobTitle", "SUPPLIER_CONTACT_JOB_TITLE", TEXT),
    SUPPLIER_CONTACT_EMAIL_ADDRESS("supplier.contact.email", "SUPPLIER_CONTACT_EMAIL_ADDRESS", TEXT),
    SUPPLIER_CONTACT_PHONE_NUMBER("supplier.contact.phone", "SUPPLIER_CONTACT_PHONE_NUMBER", TEXT),

    SUPPLIER_CONTACT_COMPANY_NAME("supplier.contact.company.name", "SUPPLIER_CONTACT_COMPANY_NAME", TEXT),
    SUPPLIER_CONTACT_COMPANY_ADDRESS_ADDRESS1("supplier.contact.company.address.address1", "SUPPLIER_CONTACT_COMPANY_ADDRESS_ADDRESS1", TEXT),
    SUPPLIER_CONTACT_COMPANY_ADDRESS_ADDRESS2("supplier.contact.company.address.address2", "SUPPLIER_CONTACT_COMPANY_ADDRESS_ADDRESS2", TEXT),
    SUPPLIER_CONTACT_COMPANY_ADDRESS_CITY("supplier.contact.company.address.city", "SUPPLIER_CONTACT_COMPANY_ADDRESS_CITY", TEXT),
    SUPPLIER_CONTACT_COMPANY_ADDRESS_STATE("supplier.contact.company.address.state", "SUPPLIER_CONTACT_COMPANY_ADDRESS_STATE", TEXT),
    SUPPLIER_CONTACT_COMPANY_ADDRESS_ZIP("supplier.contact.company.address.zip", "SUPPLIER_CONTACT_COMPANY_ADDRESS_ZIP", TEXT),
    SUPPLIER_CONTACT_COMPANY_ADDRESS_COUNTRY("supplier.contact.company.address.country", "SUPPLIER_CONTACT_COMPANY_ADDRESS_COUNTRY", TEXT),
    SUPPLIER_CONTACT_COMPANY_FULL_ADDRESS("supplier.contact.company.address.fullAddress", "SUPPLIER_CONTACT_COMPANY_FULL_ADDRESS", TEXT),
    SUPPLIER_CONTACT_COMPANY_SHORT_ADDRESS("supplier.contact.company.address.shortAddress", "SUPPLIER_CONTACT_COMPANY_SHORT_ADDRESS", TEXT),
    SUPPLIER_CONTACT_COMPANY_WEBSITE("supplier.contact.company.website", "SUPPLIER_CONTACT_COMPANY_WEBSITE", TEXT),
    SUPPLIER_CONTACT_COMPANY_EMAIL_ADDRESS("supplier.contact.company.email", "SUPPLIER_CONTACT_COMPANY_EMAIL_ADDRESS", TEXT),
    SUPPLIER_CONTACT_COMPANY_PHONE("supplier.contact.company.phone", "SUPPLIER_CONTACT_COMPANY_PHONE", TEXT),
    SUPPLIER_CONTACT_COMPANY_LOGO( "supplier.contact.company.logo", "logo-placeholder.png", LOGO),

    RFP_NAME("rfp.name", "RFP_NAME", TEXT),
    RFP_DUE_DATE("rfp.dueDate", "RFP_DUE_DATE", TEXT),
    RFP_SENT_DATE("rfp.sentDate", "RFP_SENT_DATE", TEXT),
    RFP_PROGRAM_START_DATE("rfp.programStartDate", "RFP_PROGRAM_START_DATE", TEXT),
    RFP_PROGRAM_END_DATE("rfp.programEndDate", "RFP_PROGRAM_END_DATE", TEXT),
    RFP_PROGRAM_YEAR("rfp.programYear", "RFP_PROGRAM_YEAR", TEXT),

    TRAVEL_DESTINATION_NAME("travelDestination.name", "TRAVEL_DESTINATION_NAME", TEXT),
    TRAVEL_DESTINATION_ESTIMATED_SPEND("travelDestination.estimatedSpend", "TRAVEL_DESTINATION_ESTIMATED_SPEND", TEXT),
    TRAVEL_DESTINATION_ESTIMATED_ROOM_NIGHTS("travelDestination.estimatedRoomNights", "TRAVEL_DESTINATION_ESTIMATED_ROOM_NIGHTS", TEXT),
    TRAVEL_DESTINATION_ADDRESS_ADDRESS1("travelDestination.address.address1", "TRAVEL_DESTINATION_ADDRESS_ADDRESS1", TEXT),
    TRAVEL_DESTINATION_ADDRESS_ADDRESS2("travelDestination.address.address2", "TRAVEL_DESTINATION_ADDRESS_ADDRESS2", TEXT),
    TRAVEL_DESTINATION_ADDRESS_CITY("travelDestination.address.city", "TRAVEL_DESTINATION_ADDRESS_CITY", TEXT),
    TRAVEL_DESTINATION_ADDRESS_STATE("travelDestination.address.state", "TRAVEL_DESTINATION_ADDRESS_STATE", TEXT),
    TRAVEL_DESTINATION_ADDRESS_ZIP("travelDestination.address.zip", "TRAVEL_DESTINATION_ADDRESS_ZIP", TEXT),
    TRAVEL_DESTINATION_ADDRESS_COUNTRY("travelDestination.address.country", "TRAVEL_DESTINATION_ADDRESS_COUNTRY", TEXT),
    TRAVEL_DESTINATION_FULL_ADDRESS("travelDestination.address.fullAddress", "TRAVEL_DESTINATION_FULL_ADDRESS", TEXT),
    TRAVEL_DESTINATION_SHORT_ADDRESS("travelDestination.address.shortAddress", "TRAVEL_DESTINATION_SHORT_ADDRESS", TEXT);

    final String key;
    final String placeholder;
    final HotelRfpLetterVariableFormat format;

    HotelRfpLetterVariable(String key, String placeholder, HotelRfpLetterVariableFormat format) {
        this.key = key;
        this.placeholder = placeholder;
        this.format = format;
    }

    void addTo(HotelRfpLetterModel model, String value) {
        if(model.usePlaceholders()){
            final String viewValue = value == null || value.isEmpty() ? placeholder : value;
            model.getModel().put(key, format.apply(key, viewValue));
        } else if (value == null || value.isEmpty()){
            model.getModel().put(key, "");
        } else {
            model.getModel().put(key, format.apply(key, value));
        }
    }
}