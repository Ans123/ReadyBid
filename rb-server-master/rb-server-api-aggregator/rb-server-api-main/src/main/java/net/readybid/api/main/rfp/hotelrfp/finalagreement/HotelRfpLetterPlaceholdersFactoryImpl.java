package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import net.readybid.app.core.entities.location.Location;
import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.entities.DateHelper;
import net.readybid.entities.rfp.RateLoadingInformationList;
import net.readybid.rfp.buyer.Buyer;
import net.readybid.rfp.company.RfpCompany;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.specifications.RfpSpecifications;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.supplier.HotelRfpSupplier;
import net.readybid.rfphotel.supplier.RfpHotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class HotelRfpLetterPlaceholdersFactoryImpl implements HotelRfpLetterPlaceholdersFactory {

    private final String CLIENT_ADDRESS;
    private final BlackoutDatesTableFactory blackoutDatesTableFactory;
    private final AmenitiesTableFactory amenitiesTableFactory;
    private final RatesTableFactory ratesTableFactory;
    private final RateLoadingInformationTableFactory rateLoadingInformationTableFactory;

    @Autowired
    public HotelRfpLetterPlaceholdersFactoryImpl(
            Environment env,
            BlackoutDatesTableFactory blackoutDatesTableFactory,
            AmenitiesTableFactory amenitiesTableFactory,
            RatesTableFactory ratesTableFactory,
            RateLoadingInformationTableFactory rateLoadingInformationTableFactory
    ) {
        CLIENT_ADDRESS = env.getRequiredProperty("client.address");
        this.blackoutDatesTableFactory = blackoutDatesTableFactory;
        this.amenitiesTableFactory = amenitiesTableFactory;
        this.ratesTableFactory = ratesTableFactory;
        this.rateLoadingInformationTableFactory = rateLoadingInformationTableFactory;
    }

    @Override
    public Map<String, String> getFromBid(HotelRfpBid bid) {
        return getFromBid(bid, true);
    }

    @Override
    public Map<String, String> getFromRfpWithDataOrPlaceholders(Rfp rfp) {
        final Map<String, String> placeholders = getPlaceholders(false);
        addRfpData(placeholders, rfp.getSpecifications());
//        addFinalAgreementData(placeholders, rfp.getAnswers(), rfp, null);

        return placeholders;
    }

    @Override
    public Map<String, String> getFromBidWithDataOrPlaceholders(HotelRfpBid bid) {
        return getFromBid(bid, false);
    }

    @Override
    public Map<String, String> getFromBidWithData(HotelRfpBid bid) {
        return getFromBid(bid, true);
    }

    private Map<String, String> getFromBid(HotelRfpBid bid, boolean asEmpty) {
        final Map<String, String> placeholders = getPlaceholders(asEmpty);
        addRfpData(placeholders, bid.getSpecifications());
        addBuyerData(placeholders, bid.getBuyer());
        addSupplierData(placeholders, bid.getSupplier());
        addTravelDestinationData(placeholders, bid.getSubject());
        addFinalAgreementData(placeholders, bid.getQuestionnaire(), bid, bid.getFinalAgreementDate());

        return placeholders;
    }

    private void addFinalAgreementData(Map<String, String> placeholders, Questionnaire questionnaire, RateLoadingInformationList rateLoadingInformationList, LocalDate finalAgreementSentDate) {
        placeholders.put("finalAgreement.rates", ratesTableFactory.create(questionnaire.getConfigState("RT"), questionnaire.getAcceptedRates(), questionnaire.getResponse()));
        placeholders.put("finalAgreement.amenities", amenitiesTableFactory.create(questionnaire.getResponse()));
        placeholders.put("finalAgreement.blackoutDates", blackoutDatesTableFactory.create(questionnaire.getResponse()));
        placeholders.put("finalAgreement.rateLoadingInformation", rateLoadingInformationTableFactory.create(rateLoadingInformationList));
        addIfExists(placeholders, "finalAgreement.date", formatDate(finalAgreementSentDate));
    }

    private void addRfpData(Map<String, String> data, RfpSpecifications rfpSpecs) {
        if(rfpSpecs == null) return;

        addIfExists(data, "rfp.name", rfpSpecs.getName());
        addIfExists(data, "rfp.sentDate", formatDate(rfpSpecs.getBidSentDate()));
        addIfExists(data, "rfp.dueDate", formatDate(rfpSpecs.getDueDate()));
        addIfExists(data, "rfp.programYear", Integer.toString(rfpSpecs.getProgramYear()));
        addIfExists(data, "rfp.programStartDate", formatDate(rfpSpecs.getProgramStartDate()));
        addIfExists(data, "rfp.programEndDate", formatDate(rfpSpecs.getProgramEndDate()));
        addBuyerData(data, rfpSpecs.getBuyer());
    }

    private void addBuyerData(Map<String, String> data, Buyer buyer) {
        if(buyer == null) return;

        addCompanyData(data, "buyer.company.", buyer.getCompany());
        addContactData(data, "buyer.contact.", buyer.getContact());
    }

    private void addSupplierData(Map<String, String> data, HotelRfpSupplier supplier) {
        if(supplier == null) return;

        addPropertyData(data, "property.", supplier.getCompany());
        addContactData(data, "supplier.contact.", supplier.getContact());

        // todo: check again after chains and HMCs are added
        if(supplier.getContact() == null || supplier.getContact().getCompany() == null){
            addCompanyData(data, "supplier.contact.company.", supplier.getCompany());
        }
    }

    private void addPropertyData(Map<String, String> data, String keyPrefix, RfpHotel rfpHotel) {
        if(rfpHotel == null) return;
        addCompanyData(data, keyPrefix, rfpHotel);
        addIfExists(data, keyPrefix + "chainBrand", rfpHotel.getBrandChainName());
        addIfExists(data, keyPrefix + "chainMaster", rfpHotel.getMasterChainName());
    }

    private void addTravelDestinationData(Map<String, String> data, TravelDestination destination) {
        if(destination == null) return;

        addIfExists(data, "travelDestination.name", destination.getName());
        addIfExists(data, "travelDestination.estimatedSpend", destination.getEstimatedSpend());
        addIfExists(data, "travelDestination.estimatedRoomNights", destination.getEstimatedRoomNights());
        final Location location = destination.getLocation();
        if(location != null){
            addAddress(data, "travelDestination.address.", location.getAddress());
            addIfExists(data, "travelDestination.address.fullAddress", location.getFullAddress());
        }
    }

    private void addContactData(Map<String, String> data, String keyPrefix, RfpContact contact) {
        if(contact == null) return;
        addIfExists(data, keyPrefix + "firstName", contact.getFirstName());
        addIfExists(data, keyPrefix + "lastName", contact.getLastName());
        addIfExists(data, keyPrefix + "fullName", contact.getFullName());
        addIfExists(data, keyPrefix + "jobTitle", contact.getJobTitle());
        addIfExists(data, keyPrefix + "email", contact.getEmailAddress());
        addIfExists(data, keyPrefix + "phone", contact.getPhone());

        addCompanyData(data, keyPrefix + "company.", contact.getCompany());
    }

    private void addCompanyData(Map<String, String> data, String keyPrefix, RfpCompany company) {
        if(company == null) return;
        addIfExists(data, keyPrefix + "name", company.getName());
        addAddress(data, keyPrefix + "address", company.getAddress());
        addIfExists(data, keyPrefix + "address.fullAddress", company.getFullAddress());
        addIfExists(data, keyPrefix + "website", company.getWebsite());
        addIfExists(data, keyPrefix + "email", company.getEmailAddress());
        addIfExists(data, keyPrefix + "phone", company.getPhone());
        wrapAndAddImage(data, keyPrefix+"logo",
                null == company.getLogo() || company.getLogo().isEmpty() ? "logo-placeholder.png" : company.getLogo());
    }

    private void addAddress(Map<String, String> data, String keyPrefix, Address address) {
        if (address == null) return;
        addIfExists(data, keyPrefix + "address1", address.getAddress1());
        addIfExists(data, keyPrefix + "address2", address.getAddress2());
        addIfExists(data, keyPrefix + "city", address.getCity());
        addIfExists(data, keyPrefix + "state", address.getStateOrRegion());
        addIfExists(data, keyPrefix + "zip", address.getZip());
        addIfExists(data, keyPrefix + "country", address.getCountry().getFullName());
    }

    private void addIfExists(Map<String, String> dataModel, String key, Object value) {
        if(value != null){
            if (value instanceof String) {
                final String v = (String) value;
                if(!v.isEmpty()) wrapAndAddText(dataModel, key, v);
            } else {
                wrapAndAddText(dataModel, key, String.valueOf(value));
            }
        }
    }

    private String formatDate(LocalDate date) {
        return DateHelper.customDateFormat(date);
    }

    private Map<String, String> getPlaceholders(boolean asEmpty) {
        final Map<String, String> m = new HashMap<>();

        addPlaceholderForText(m, asEmpty, "buyer.company.name", "BUYER_COMPANY_NAME");
        addPlaceholderForText(m, asEmpty, "buyer.company.address.address1", "BUYER_COMPANY_ADDRESS_ADDRESS1");
        addPlaceholderForText(m, asEmpty, "buyer.company.address.address2", "BUYER_COMPANY_ADDRESS_ADDRESS2");
        addPlaceholderForText(m, asEmpty, "buyer.company.address.city", "BUYER_COMPANY_ADDRESS_CITY");
        addPlaceholderForText(m, asEmpty, "buyer.company.address.state", "BUYER_COMPANY_ADDRESS_STATE");
        addPlaceholderForText(m, asEmpty, "buyer.company.address.zip", "BUYER_COMPANY_ADDRESS_ZIP");
        addPlaceholderForText(m, asEmpty, "buyer.company.address.country", "BUYER_COMPANY_ADDRESS_COUNTRY");
        addPlaceholderForText(m, asEmpty, "buyer.company.address.fullAddress", "BUYER_COMPANY_FULL_ADDRESS");
        addPlaceholderForText(m, asEmpty, "buyer.company.address.shortAddress", "BUYER_COMPANY_SHORT_ADDRESS");
        addPlaceholderForText(m, asEmpty, "buyer.company.website", "BUYER_COMPANY_WEBSITE");
        addPlaceholderForText(m, asEmpty, "buyer.company.email", "BUYER_COMPANY_EMAIL_ADDRESS");
        addPlaceholderForText(m, asEmpty, "buyer.company.phone", "BUYER_COMPANY_PHONE");
        addPlaceholderForText(m, asEmpty, "buyer.company.logo", "BUYER_COMPANY_LOGO");

        addPlaceholderForText(m, asEmpty, "buyer.contact.firstName", "BUYER_CONTACT_FIRST_NAME");
        addPlaceholderForText(m, asEmpty, "buyer.contact.lastName", "BUYER_CONTACT_LAST_NAME");
        addPlaceholderForText(m, asEmpty, "buyer.contact.fullName", "BUYER_CONTACT_NAME");
        addPlaceholderForText(m, asEmpty, "buyer.contact.jobTitle", "BUYER_CONTACT_JOB_TITLE");
        addPlaceholderForText(m, asEmpty, "buyer.contact.email", "BUYER_CONTACT_EMAIL_ADDRESS");
        addPlaceholderForText(m, asEmpty, "buyer.contact.phone", "BUYER_CONTACT_PHONE_NUMBER");

        addPlaceholderForText(m, asEmpty, "buyer.contact.company.name", "BUYER_CONTACT_COMPANY_NAME");
        addPlaceholderForText(m, asEmpty, "buyer.contact.company.address.address1", "BUYER_CONTACT_COMPANY_ADDRESS_ADDRESS1");
        addPlaceholderForText(m, asEmpty, "buyer.contact.company.address.address2", "BUYER_CONTACT_COMPANY_ADDRESS_ADDRESS2");
        addPlaceholderForText(m, asEmpty, "buyer.contact.company.address.city", "BUYER_CONTACT_COMPANY_ADDRESS_CITY");
        addPlaceholderForText(m, asEmpty, "buyer.contact.company.address.state", "BUYER_CONTACT_COMPANY_ADDRESS_STATE");
        addPlaceholderForText(m, asEmpty, "buyer.contact.company.address.zip", "BUYER_CONTACT_COMPANY_ADDRESS_ZIP");
        addPlaceholderForText(m, asEmpty, "buyer.contact.company.address.country", "BUYER_CONTACT_COMPANY_ADDRESS_COUNTRY");
        addPlaceholderForText(m, asEmpty, "buyer.contact.company.address.fullAddress", "BUYER_CONTACT_COMPANY_FULL_ADDRESS");
        addPlaceholderForText(m, asEmpty, "buyer.contact.company.address.shortAddress", "BUYER_CONTACT_COMPANY_SHORT_ADDRESS");
        addPlaceholderForText(m, asEmpty, "buyer.contact.company.website", "BUYER_CONTACT_COMPANY_WEBSITE");
        addPlaceholderForText(m, asEmpty, "buyer.contact.company.email", "BUYER_CONTACT_COMPANY_EMAIL_ADDRESS");
        addPlaceholderForText(m, asEmpty, "buyer.contact.company.phone", "BUYER_CONTACT_COMPANY_PHONE");
        addPlaceholderForImage(m, asEmpty, "buyer.contact.company.logo", "BUYER_CONTACT_COMPANY_LOGO");

        addPlaceholderForText(m, asEmpty, "property.name", "PROPERTY_NAME");
        addPlaceholderForText(m, asEmpty, "property.address.address1", "PROPERTY_ADDRESS_ADDRESS1");
        addPlaceholderForText(m, asEmpty, "property.address.address2", "PROPERTY_ADDRESS_ADDRESS2");
        addPlaceholderForText(m, asEmpty, "property.address.city", "PROPERTY_ADDRESS_CITY");
        addPlaceholderForText(m, asEmpty, "property.address.state", "PROPERTY_ADDRESS_STATE");
        addPlaceholderForText(m, asEmpty, "property.address.zip", "PROPERTY_ADDRESS_ZIP");
        addPlaceholderForText(m, asEmpty, "property.address.country", "PROPERTY_ADDRESS_COUNTRY");
        addPlaceholderForText(m, asEmpty, "property.address.fullAddress", "PROPERTY_FULL_ADDRESS");
        addPlaceholderForText(m, asEmpty, "property.address.shortAddress", "PROPERTY_SHORT_ADDRESS");
        addPlaceholderForText(m, asEmpty, "property.phone", "PROPERTY_PHONE");
        addPlaceholderForText(m, asEmpty, "property.website", "PROPERTY_WEBSITE");
        addPlaceholderForText(m, asEmpty, "property.email", "PROPERTY_EMAIL_ADDRESS");
        addPlaceholderForText(m, asEmpty, "property.chainMaster", "PROPERTY_MASTER_CHAIN");
        addPlaceholderForText(m, asEmpty, "property.chainBrand", "PROPERTY_CHAIN_BRAND");

        addPlaceholderForText(m, asEmpty, "supplier.contact.firstName", "SUPPLIER_CONTACT_FIRST_NAME");
        addPlaceholderForText(m, asEmpty, "supplier.contact.lastName", "SUPPLIER_CONTACT_LAST_NAME");
        addPlaceholderForText(m, asEmpty, "supplier.contact.fullName", "SUPPLIER_CONTACT_NAME");
        addPlaceholderForText(m, asEmpty, "supplier.contact.jobTitle", "SUPPLIER_CONTACT_JOB_TITLE");
        addPlaceholderForText(m, asEmpty, "supplier.contact.email", "SUPPLIER_CONTACT_EMAIL_ADDRESS");
        addPlaceholderForText(m, asEmpty, "supplier.contact.phone", "SUPPLIER_CONTACT_PHONE_NUMBER");

        addPlaceholderForText(m, asEmpty, "supplier.contact.company.name", "SUPPLIER_CONTACT_COMPANY_NAME");
        addPlaceholderForText(m, asEmpty, "supplier.contact.company.address.address1", "SUPPLIER_CONTACT_COMPANY_ADDRESS_ADDRESS1");
        addPlaceholderForText(m, asEmpty, "supplier.contact.company.address.address2", "SUPPLIER_CONTACT_COMPANY_ADDRESS_ADDRESS2");
        addPlaceholderForText(m, asEmpty, "supplier.contact.company.address.city", "SUPPLIER_CONTACT_COMPANY_ADDRESS_CITY");
        addPlaceholderForText(m, asEmpty, "supplier.contact.company.address.state", "SUPPLIER_CONTACT_COMPANY_ADDRESS_STATE");
        addPlaceholderForText(m, asEmpty, "supplier.contact.company.address.zip", "SUPPLIER_CONTACT_COMPANY_ADDRESS_ZIP");
        addPlaceholderForText(m, asEmpty, "supplier.contact.company.address.country", "SUPPLIER_CONTACT_COMPANY_ADDRESS_COUNTRY");
        addPlaceholderForText(m, asEmpty, "supplier.contact.company.address.fullAddress", "SUPPLIER_CONTACT_COMPANY_FULL_ADDRESS");
        addPlaceholderForText(m, asEmpty, "supplier.contact.company.address.shortAddress", "SUPPLIER_CONTACT_COMPANY_SHORT_ADDRESS");
        addPlaceholderForText(m, asEmpty, "supplier.contact.company.website", "SUPPLIER_CONTACT_COMPANY_WEBSITE");
        addPlaceholderForText(m, asEmpty, "supplier.contact.company.email", "SUPPLIER_CONTACT_COMPANY_EMAIL_ADDRESS");
        addPlaceholderForText(m, asEmpty, "supplier.contact.company.phone", "SUPPLIER_CONTACT_COMPANY_PHONE");
        addPlaceholderForImage(m, asEmpty, "supplier.contact.company.logo", "SUPPLIER_CONTACT_COMPANY_LOGO");

        addPlaceholderForText(m, asEmpty, "rfp.name", "RFP_NAME");
        addPlaceholderForText(m, asEmpty, "rfp.dueDate", "RFP_DUE_DATE");
        addPlaceholderForText(m, asEmpty, "rfp.sentDate", "RFP_SENT_DATE");
        addPlaceholderForText(m, asEmpty, "rfp.programStartDate", "RFP_PROGRAM_START_DATE");
        addPlaceholderForText(m, asEmpty, "rfp.programEndDate", "RFP_PROGRAM_END_DATE");
        addPlaceholderForText(m, asEmpty, "rfp.programYear", "RFP_PROGRAM_YEAR");

        addPlaceholderForText(m, asEmpty, "travelDestination.name", "TRAVEL_DESTINATION_NAME");
        addPlaceholderForText(m, asEmpty, "travelDestination.estimatedSpend", "TRAVEL_DESTINATION_ESTIMATED_SPEND");
        addPlaceholderForText(m, asEmpty, "travelDestination.estimatedRoomNights", "TRAVEL_DESTINATION_ESTIMATED_ROOM_NIGHTS");
        addPlaceholderForText(m, asEmpty, "travelDestination.name", "TRAVEL_DESTINATION_NAME");
        addPlaceholderForText(m, asEmpty, "travelDestination.address.address1", "TRAVEL_DESTINATION_ADDRESS_ADDRESS1");
        addPlaceholderForText(m, asEmpty, "travelDestination.address.address2", "TRAVEL_DESTINATION_ADDRESS_ADDRESS2");
        addPlaceholderForText(m, asEmpty, "travelDestination.address.city", "TRAVEL_DESTINATION_ADDRESS_CITY");
        addPlaceholderForText(m, asEmpty, "travelDestination.address.state", "TRAVEL_DESTINATION_ADDRESS_STATE");
        addPlaceholderForText(m, asEmpty, "travelDestination.address.zip", "TRAVEL_DESTINATION_ADDRESS_ZIP");
        addPlaceholderForText(m, asEmpty, "travelDestination.address.country", "TRAVEL_DESTINATION_ADDRESS_COUNTRY");
        addPlaceholderForText(m, asEmpty, "travelDestination.address.fullAddress", "TRAVEL_DESTINATION_FULL_ADDRESS");
        addPlaceholderForText(m, asEmpty, "travelDestination.address.shortAddress", "TRAVEL_DESTINATION_SHORT_ADDRESS");

        addPlaceholderForText(m, asEmpty, "finalAgreement.date", "FINAL_AGREEMENT_DATE");
        addPlaceholderForText(m, asEmpty, "finalAgreement.rates", "FINAL_AGREEMENT_RATES");
        addPlaceholderForText(m, asEmpty, "finalAgreement.amenities", "FINAL_AGREEMENT_AMENITIES");
        addPlaceholderForText(m, asEmpty, "finalAgreement.blackoutDates", "FINAL_AGREEMENT_BLACKOUT_DATES");
        addPlaceholderForText(m, asEmpty, "finalAgreement.rateLoadingInformation", "FINAL_AGREEMENT_RATE_LOADING_INFORMATION");

        return m;
    }

    private void addPlaceholderForText(Map<String, String> model, boolean asEmpty, String key, String placeholder) {
        if(asEmpty) {
            addEmpty(model, key);
        } else {
            wrapAndAddText(model, key, placeholder);
        }
    }

    private void addPlaceholderForImage(Map<String, String> model, boolean asEmpty, String key, String placeholder) {
        if(asEmpty) {
            addEmpty(model, key);
        } else {
            wrapAndAddImage(model, key, placeholder);
        }
    }

    private void addEmpty(Map<String, String> model, String key) {
        model.put(key, "");
    }

    private void wrapAndAddText(Map<String, String> model, String key, String value) {
        model.put(key, String.format("<span class=\"inserted %s\">%s</span>", key, value));
    }

    private void wrapAndAddImage(Map<String, String> model, String key, String url) {
        model.put(key, String.format("<img src=\"%s/images/logos/%s\" class=\"inserted %s insertedLogo\"/>", CLIENT_ADDRESS, url, key));
    }
}
