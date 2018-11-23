package net.readybid.app.interactors.rfp_hotel.letter.implementation;

import net.readybid.rfp.core.Rfp;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static net.readybid.app.interactors.rfp_hotel.letter.implementation.HotelRfpLetterVariable.*;

@Service
class HotelRfpNamCoverLetterModelFactory {

    Map<String, String> getWithPlaceholders(Rfp rfp) {
        return toMap(true, new HotelRfpNamCoverLetterModelDataProviderImpl(rfp));
    }

    Map<String, String> getWithPlaceholders(HotelRfpBid bid) {
        return toMap(true, new HotelRfpNamCoverLetterModelDataProviderImpl(bid));
    }

    Map<String, String> get(HotelRfpBid bid) {
        return toMap(false, new HotelRfpNamCoverLetterModelDataProviderImpl(bid));
    }

    private Map<String, String> toMap(boolean withPlaceholders, HotelRfpNamCoverLetterModelDataProvider data) {
        final Map<String, String> m = new HashMap<>();
        final HotelRfpLetterModel model = new HotelRfpLetterModelImpl(m, withPlaceholders);

        BUYER_COMPANY_NAME.addTo(model, data.getBuyerCompanyName());
        BUYER_COMPANY_ADDRESS_ADDRESS1.addTo(model, data.getBuyerCompanyAddress1());
        BUYER_COMPANY_ADDRESS_ADDRESS2.addTo(model, data.getBuyerCompanyAddress2());
        BUYER_COMPANY_ADDRESS_CITY.addTo(model, data.getBuyerCompanyAddressCity());
        BUYER_COMPANY_ADDRESS_STATE.addTo(model, data.getBuyerCompanyAddressState());
        BUYER_COMPANY_ADDRESS_ZIP.addTo(model, data.getBuyerCompanyAddressZip());
        BUYER_COMPANY_ADDRESS_COUNTRY.addTo(model, data.getBuyerCompanyAddressCountry());
        BUYER_COMPANY_FULL_ADDRESS.addTo(model, data.getBuyerCompanyFullAddress());
        BUYER_COMPANY_SHORT_ADDRESS.addTo(model, data.getBuyerCompanyShortAddress());
        BUYER_COMPANY_WEBSITE.addTo(model, data.getBuyerCompanyWebsite());
        BUYER_COMPANY_EMAIL_ADDRESS.addTo(model, data.getBuyerCompanyEmailAddress());
        BUYER_COMPANY_PHONE.addTo(model, data.getBuyerCompanyPhone());
        BUYER_COMPANY_LOGO.addTo(model, data.getBuyerCompanyLogo());

        BUYER_CONTACT_FIRST_NAME.addTo(model, data.getBuyerContactFirstName());
        BUYER_CONTACT_LAST_NAME.addTo(model, data.getBuyerContactLastName());
        BUYER_CONTACT_NAME.addTo(model, data.getBuyerContactName());
        BUYER_CONTACT_JOB_TITLE.addTo(model, data.getBuyerContactJobTitle());
        BUYER_CONTACT_EMAIL_ADDRESS.addTo(model, data.getBuyerContactEmailAddress());
        BUYER_CONTACT_PHONE_NUMBER.addTo(model, data.getBuyerContactPhone());

        BUYER_CONTACT_COMPANY_NAME.addTo(model, data.getBuyerContactCompanyName());
        BUYER_CONTACT_COMPANY_ADDRESS_ADDRESS1.addTo(model, data.getBuyerContactCompanyAddress1());
        BUYER_CONTACT_COMPANY_ADDRESS_ADDRESS2.addTo(model, data.getBuyerContactCompanyAddress2());
        BUYER_CONTACT_COMPANY_ADDRESS_CITY.addTo(model, data.getBuyerContactCompanyAddressCity());
        BUYER_CONTACT_COMPANY_ADDRESS_STATE.addTo(model, data.getBuyerContactCompanyAddressState());
        BUYER_CONTACT_COMPANY_ADDRESS_ZIP.addTo(model, data.getBuyerContactCompanyAddressZip());
        BUYER_CONTACT_COMPANY_ADDRESS_COUNTRY.addTo(model, data.getBuyerContactCompanyAddressCountry());
        BUYER_CONTACT_COMPANY_FULL_ADDRESS.addTo(model, data.getBuyerContactCompanyFullAddress());
        BUYER_CONTACT_COMPANY_SHORT_ADDRESS.addTo(model, data.getBuyerContactCompanyShortAddress());
        BUYER_CONTACT_COMPANY_WEBSITE.addTo(model, data.getBuyerContactCompanyWebsite());
        BUYER_CONTACT_COMPANY_EMAIL_ADDRESS.addTo(model, data.getBuyerContactCompanyEmailAddress());
        BUYER_CONTACT_COMPANY_PHONE.addTo(model, data.getBuyerContactCompanyPhone());
        BUYER_CONTACT_COMPANY_LOGO.addTo(model, data.getBuyerContactCompanyLogo());

        SUPPLIER_CONTACT_FIRST_NAME.addTo(model, data.getSupplierContactFirstName());
        SUPPLIER_CONTACT_LAST_NAME.addTo(model, data.getSupplierContactLastName());
        SUPPLIER_CONTACT_NAME.addTo(model, data.getSupplierContactName());
        SUPPLIER_CONTACT_JOB_TITLE.addTo(model, data.getSupplierContactJobTitle());
        SUPPLIER_CONTACT_EMAIL_ADDRESS.addTo(model, data.getSupplierContactEmailAddress());
        SUPPLIER_CONTACT_PHONE_NUMBER.addTo(model, data.getSupplierContactPhoneNumber());

        SUPPLIER_CONTACT_COMPANY_NAME.addTo(model, data.getSupplierContactCompanyName());
        SUPPLIER_CONTACT_COMPANY_ADDRESS_ADDRESS1.addTo(model, data.getSupplierContactCompanyAddress1());
        SUPPLIER_CONTACT_COMPANY_ADDRESS_ADDRESS2.addTo(model, data.getSupplierContactCompanyAddress2());
        SUPPLIER_CONTACT_COMPANY_ADDRESS_CITY.addTo(model, data.getSupplierContactCompanyAddressCity());
        SUPPLIER_CONTACT_COMPANY_ADDRESS_STATE.addTo(model, data.getSupplierContactCompanyAddressState());
        SUPPLIER_CONTACT_COMPANY_ADDRESS_ZIP.addTo(model, data.getSupplierContactCompanyAddressZip());
        SUPPLIER_CONTACT_COMPANY_ADDRESS_COUNTRY.addTo(model, data.getSupplierContactCompanyAddressCountry());
        SUPPLIER_CONTACT_COMPANY_FULL_ADDRESS.addTo(model, data.getSupplierContactCompanyFullAddress());
        SUPPLIER_CONTACT_COMPANY_SHORT_ADDRESS.addTo(model, data.getSupplierContactCompanyShortAddress());
        SUPPLIER_CONTACT_COMPANY_WEBSITE.addTo(model, data.getSupplierContactCompanyWebsite());
        SUPPLIER_CONTACT_COMPANY_EMAIL_ADDRESS.addTo(model, data.getSupplierContactCompanyEmailAddress());
        SUPPLIER_CONTACT_COMPANY_PHONE.addTo(model, data.getSupplierContactCompanyPhone());
        SUPPLIER_CONTACT_COMPANY_LOGO.addTo(model, data.getSupplierContactCompanyLogo());

        RFP_NAME.addTo(model, data.getRfpName());
        RFP_DUE_DATE.addTo(model, data.getRfpDueDate());
        RFP_SENT_DATE.addTo(model, data.getRfpSentDate(!withPlaceholders));
        RFP_PROGRAM_START_DATE.addTo(model, data.getRfpProgramStartDate());
        RFP_PROGRAM_END_DATE.addTo(model, data.getRfpProgramEndDate());
        RFP_PROGRAM_YEAR.addTo(model, data.getRfpProgramYear());

        return m;
    }

    private class HotelRfpLetterModelImpl implements HotelRfpLetterModel {
        private final Map<String, String> m;
        private final boolean withPlaceholders;

        private HotelRfpLetterModelImpl(Map<String, String> m, boolean withPlaceholders) {
            this.m = m;
            this.withPlaceholders = withPlaceholders;
        }

        @Override
        public Map<String, String> getModel() {
            return m;
        }

        @Override
        public boolean usePlaceholders() {
            return withPlaceholders;
        }
    }
}
