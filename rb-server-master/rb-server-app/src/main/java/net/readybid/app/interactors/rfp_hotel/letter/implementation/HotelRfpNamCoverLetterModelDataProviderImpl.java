package net.readybid.app.interactors.rfp_hotel.letter.implementation;

import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.entities.DateHelper;
import net.readybid.app.entities.PrimitivesHelper;
import net.readybid.rfp.buyer.Buyer;
import net.readybid.rfp.company.RfpCompany;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.supplier.HotelRfpSupplier;
import net.readybid.rfphotel.supplier.RfpHotel;

import java.time.LocalDate;

class HotelRfpNamCoverLetterModelDataProviderImpl implements HotelRfpNamCoverLetterModelDataProvider {

    private final Buyer buyer;
    private final RfpCompany buyerCompany;
    private final Address buyerCompanyAddress;
    private final RfpContact buyerContact;
    private final RfpCompany buyerContactCompany;
    private final Address buyerContactCompanyAddress;

    private final HotelRfpSupplier supplier;
    private final RfpHotel supplierCompany;
    private final Address supplierCompanyAddress;
    private final RfpContact supplierContact;
    private final RfpCompany supplierContactCompany;
    private final Address supplierContactCompanyAddress;

    private final Rfp rfp;

    HotelRfpNamCoverLetterModelDataProviderImpl(Rfp rfp){
        buyer = rfp.getBuyer();
        buyerCompany = buyer == null ? null : buyer.getCompany();
        buyerCompanyAddress = buyerCompany == null ? null : buyerCompany.getAddress();
        buyerContact = buyer == null ? null : buyer.getContact();
        buyerContactCompany = buyerContact == null ? null : buyerContact.getCompany();
        buyerContactCompanyAddress = buyerContactCompany == null ? null : buyerContactCompany.getAddress();

        supplier = null;
        supplierCompany = null;
        supplierCompanyAddress = null;
        supplierContact = null;
        supplierContactCompany = null;
        supplierContactCompanyAddress = null;

        this.rfp = rfp;
    }

    HotelRfpNamCoverLetterModelDataProviderImpl(HotelRfpBid bid){
        buyer = bid.getBuyer();
        buyerCompany = buyer == null ? null : buyer.getCompany();
        buyerCompanyAddress = buyerCompany == null ? null : buyerCompany.getAddress();
        buyerContact = buyer == null ? null : buyer.getContact();
        buyerContactCompany = buyerContact == null ? null : buyerContact.getCompany();
        buyerContactCompanyAddress = buyerContactCompany == null ? null : buyerContactCompany.getAddress();

        supplier = bid.getSupplier();
        supplierCompany = supplier == null ? null : supplier.getCompany();
        supplierCompanyAddress = supplierCompany == null ? null : supplierCompany.getAddress();
        supplierContact = supplier == null ? null : supplier.getContact();
        supplierContactCompany = supplierContact == null ? null : supplierContact.getCompany();
        supplierContactCompanyAddress = supplierContactCompany == null ? null : supplierContactCompany.getAddress();

        rfp = bid.getRfp();
    }

    @Override
    public String getBuyerCompanyName() {
        return buyerCompany == null ? null : buyerCompany.getName();
    }

    @Override
    public String getBuyerCompanyAddress1() {
        return buyerCompanyAddress == null ? null : buyerCompanyAddress.getAddress1();
    }

    @Override
    public String getBuyerCompanyAddress2() {
        return buyerCompanyAddress == null ? null : buyerCompanyAddress.getAddress2();
    }

    @Override
    public String getBuyerCompanyAddressCity() {
        return buyerCompanyAddress == null ? null : buyerCompanyAddress.getCity();
    }

    @Override
    public String getBuyerCompanyAddressState() {
        return buyerCompanyAddress == null ? null : buyerCompanyAddress.getStateOrRegion();
    }

    @Override
    public String getBuyerCompanyAddressZip() {
        return buyerCompanyAddress == null ? null : buyerCompanyAddress.getZip();
    }

    @Override
    public String getBuyerCompanyAddressCountry() {
        return buyerCompanyAddress == null ? null : buyerCompanyAddress.getCountryName();
    }

    @Override
    public String getBuyerCompanyFullAddress() {
        return buyerCompany == null ? null : buyerCompany.getFullAddress();
    }

    @Override
    public String getBuyerCompanyShortAddress() {
        return buyerCompanyAddress == null ? null : buyerCompanyAddress.getShortAddress();
    }

    @Override
    public String getBuyerCompanyWebsite() {
        return buyerCompany == null ? null : buyerCompany.getWebsite();
    }

    @Override
    public String getBuyerCompanyEmailAddress() {
        return buyerCompany == null ? null : buyerCompany.getEmailAddress();
    }

    @Override
    public String getBuyerCompanyPhone() {
        return buyerCompany == null ? null : buyerCompany.getPhone();
    }

    @Override
    public String getBuyerCompanyLogo() {
        return buyerCompany == null ? null : buyerCompany.getLogo();
    }

    @Override
    public String getBuyerContactFirstName() {
        return buyerContact == null ? null : buyerContact.getFirstName();
    }

    @Override
    public String getBuyerContactLastName() {
        return buyerContact == null ? null : buyerContact.getLastName();
    }

    @Override
    public String getBuyerContactName() {
        return buyerContact == null ? null : buyerContact.getFullName();
    }

    @Override
    public String getBuyerContactJobTitle() {
        return buyerContact == null ? null : buyerContact.getJobTitle();
    }

    @Override
    public String getBuyerContactEmailAddress() {
        return buyerContact == null ? null : buyerContact.getEmailAddress();
    }

    @Override
    public String getBuyerContactPhone() {
        return buyerContact == null ? null : buyerContact.getPhone();
    }

    @Override
    public String getBuyerContactCompanyName() {
        return buyerContactCompany == null ? null : buyerContactCompany.getName();
    }

    @Override
    public String getBuyerContactCompanyAddress1() {
        return buyerContactCompanyAddress == null ? null : buyerContactCompanyAddress.getAddress1();
    }

    @Override
    public String getBuyerContactCompanyAddress2() {
        return buyerContactCompanyAddress == null ? null : buyerContactCompanyAddress.getAddress2();
    }

    @Override
    public String getBuyerContactCompanyAddressCity() {
        return buyerContactCompanyAddress == null ? null : buyerContactCompanyAddress.getCity();
    }

    @Override
    public String getBuyerContactCompanyAddressState() {
        return buyerContactCompanyAddress == null ? null : buyerContactCompanyAddress.getStateOrRegion();
    }

    @Override
    public String getBuyerContactCompanyAddressZip() {
        return buyerContactCompanyAddress == null ? null : buyerContactCompanyAddress.getZip();
    }

    @Override
    public String getBuyerContactCompanyAddressCountry() {
        return buyerContactCompanyAddress == null ? null : buyerContactCompanyAddress.getCountryName();
    }

    @Override
    public String getBuyerContactCompanyFullAddress() {
        return buyerContactCompany == null ? null : buyerContactCompany.getFullAddress();
    }

    @Override
    public String getBuyerContactCompanyShortAddress() {
        return buyerContactCompanyAddress == null ? null : buyerContactCompanyAddress.getShortAddress();
    }

    @Override
    public String getBuyerContactCompanyWebsite() {
        return buyerContactCompany == null ? null : buyerContactCompany.getWebsite();
    }

    @Override
    public String getBuyerContactCompanyEmailAddress() {
        return buyerContactCompany == null ? null : buyerContactCompany.getEmailAddress();
    }

    @Override
    public String getBuyerContactCompanyPhone() {
        return buyerContactCompany == null ? null : buyerContactCompany.getPhone();
    }

    @Override
    public String getBuyerContactCompanyLogo() {
        return buyerContactCompany == null ? null : buyerContactCompany.getLogo();
    }

    @Override
    public String getSupplierContactFirstName() {
        return supplierContact == null ? null : supplierContact.getFirstName();
    }

    @Override
    public String getSupplierContactLastName() {
        return supplierContact == null ? null : supplierContact.getLastName();
    }

    @Override
    public String getSupplierContactName() {
        return supplierContact == null ? null : supplierContact.getFullName();
    }

    @Override
    public String getSupplierContactJobTitle() {
        return supplierContact == null ? null : supplierContact.getJobTitle();
    }

    @Override
    public String getSupplierContactEmailAddress() {
        return supplierContact == null ? null : supplierContact.getEmailAddress();
    }

    @Override
    public String getSupplierContactPhoneNumber() {
        return supplierContact == null ? null : supplierContact.getPhone();
    }

    @Override
    public String getSupplierContactCompanyName() {
        return supplierContactCompany == null ? null : supplierContactCompany.getName();
    }

    @Override
    public String getSupplierContactCompanyAddress1() {
        return supplierContactCompanyAddress == null ? null : supplierContactCompanyAddress.getAddress1();
    }

    @Override
    public String getSupplierContactCompanyAddress2() {
        return supplierContactCompanyAddress == null ? null : supplierContactCompanyAddress.getAddress2();
    }

    @Override
    public String getSupplierContactCompanyAddressCity() {
        return supplierContactCompanyAddress == null ? null : supplierContactCompanyAddress.getCity();
    }

    @Override
    public String getSupplierContactCompanyAddressState() {
        return supplierContactCompanyAddress == null ? null : supplierContactCompanyAddress.getStateOrRegion();
    }

    @Override
    public String getSupplierContactCompanyAddressZip() {
        return supplierContactCompanyAddress == null ? null : supplierContactCompanyAddress.getZip();
    }

    @Override
    public String getSupplierContactCompanyAddressCountry() {
        return supplierContactCompanyAddress == null ? null : supplierContactCompanyAddress.getCountryName();
    }

    @Override
    public String getSupplierContactCompanyFullAddress() {
        return supplierContactCompany == null ? null : supplierContactCompany.getFullAddress();
    }

    @Override
    public String getSupplierContactCompanyShortAddress() {
        return supplierContactCompanyAddress == null ? null : supplierContactCompanyAddress.getShortAddress();
    }

    @Override
    public String getSupplierContactCompanyWebsite() {
        return supplierContactCompany == null ? null : supplierContactCompany.getWebsite();
    }

    @Override
    public String getSupplierContactCompanyEmailAddress() {
        return supplierContactCompany == null ? null : supplierContactCompany.getEmailAddress();
    }

    @Override
    public String getSupplierContactCompanyPhone() {
        return supplierContactCompany == null ? null : supplierContactCompany.getPhone();
    }

    @Override
    public String getSupplierContactCompanyLogo() {
        return supplierContactCompany == null ? null : supplierContactCompany.getLogo();
    }

    @Override
    public String getRfpName() {
        return rfp == null ? null : rfp.getName();
    }

    @Override
    public String getRfpDueDate() {
        return rfp == null ? null : DateHelper.customDateFormat(rfp.getDueDate());
    }

    @Override
    public String getRfpSentDate(boolean createMock) {
        final String sentDate = rfp == null ? null : DateHelper.customDateFormat(rfp.getBidSentDate());
        return sentDate == null && createMock ? DateHelper.customDateFormat(LocalDate.now()) : sentDate;
    }

    @Override
    public String getRfpProgramStartDate() {
        return rfp == null ? null : DateHelper.customDateFormat(rfp.getProgramStartDate());
    }

    @Override
    public String getRfpProgramEndDate() {
        return rfp == null ? null : DateHelper.customDateFormat(rfp.getProgramEndDate());
    }

    @Override
    public String getRfpProgramYear() {
        return rfp == null ? null : PrimitivesHelper.formatInteger(rfp.getProgramYear());
    }
}
