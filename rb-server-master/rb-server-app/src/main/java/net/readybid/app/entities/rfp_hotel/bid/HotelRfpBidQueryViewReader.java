package net.readybid.app.entities.rfp_hotel.bid;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.traveldestination.TravelDestinationType;
import net.readybid.entities.Id;
import net.readybid.rfp.company.RfpCompany;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.utils.RbMapUtils;
import org.bson.types.ObjectId;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.SENT;

public class HotelRfpBidQueryViewReader {

    private HotelRfpBidQueryView view;

    public HotelRfpBidQueryViewReader() {}

    public HotelRfpBidQueryViewReader(HotelRfpBidQueryView hotelRfpBidQueryView) {
        view = hotelRfpBidQueryView;
    }

    public HotelRfpBidQueryView getView() {
        return view;
    }

    public HotelRfpBidQueryViewReader init(HotelRfpBidQueryView hotelRfpBidQueryView) {
        view = hotelRfpBidQueryView;
        return this;
    }

    public boolean isStateIn(HotelRfpBidStateStatus... statuses) {
        return view != null && Arrays.asList(statuses).contains(view.$status);
    }

    public boolean containsSupplierContact(Id supplierContactId) {
        final String setSupplierContactId = view == null || view.$supplierContact == null ? null : get(view.$supplierContact, "id");
        return setSupplierContactId != null && setSupplierContactId.equals(supplierContactId.toString());
    }

    private String get(Map<String, Object> map, String path) {
        final Object o = RbMapUtils.getObject(map, path);
        return o == null ? null : String.valueOf(o);
    }

    public HotelRfpBidStateStatus getStateStatus() {
        return null == view ? null : view.$status;
    }

    private Date getStateAt() {
        return view == null ? null : view.state.at;
    }

    public boolean isBidSentStatusAfter(Date mark) {
        return (view != null && SENT.equals(view.$status)) && !mark.after(view.state.at);
    }

    public String getId() {
        return null == view ? null : view.$bidId;
    }

    public boolean isSentToType(EntityType type) {
        return view != null && type.toString().equals(get(view.$supplierContact, "company.type"));
    }

    public String getSupplierContactEmailAddress() {
        return get(view.$supplierContact, "emailAddress");
    }

    public String getRfpId() {
        return null == view ? null : view.$rfpId;
    }

    public String getRfpName() {
        return null == view ? null : get(view.rfp, "specifications.name");
    }

    public LocalDate getDueDate() {
        final String date = null == view ? null : get(view.rfp, "specifications.dueDate");
        return date == null ? null : LocalDate.parse (date);
    }

    public LocalDate getProgramEndDate() {
        final String date = null == view ? null : get(view.rfp, "specifications.programEndDate");
        return date == null ? null : LocalDate.parse (date);
    }

    public int getProgramYear() {
        final String programYear = null == view ? null : get(view.rfp, "specifications.programYear");
        return (programYear == null) ? 0 : Integer.parseInt(programYear);
    }

    private String getSupplierContactId() {
        return null == view ? null : get(view.$supplierContact, "id");
    }

    public String getSupplierContactFirstName() { return null == view ? null : get(view.$supplierContact, "firstName"); }

    private String getSupplierContactLastName() {
        return null == view ? null : get(view.$supplierContact, "lastName");
    }

    private String getSupplierContactPhone() {
        return null == view ? null : get(view.$supplierContact, "phone");
    }

    private String getSupplierContactJobTitle() {
        return null == view ? null : get(view.$supplierContact, "jobTitle");
    }

    private boolean getSupplierContactIsUser() {
        final String isUser = null == view ? null : get(view.$supplierContact, "isUser");
        return Boolean.parseBoolean(isUser);
    }

    private String getSupplierContactCompanyAccountId() {
        return null == view ? null : get(view.$supplierContact, "company.accountId");
    }

    public String getSupplierContactCompanyName() {
        return null == view ? null : get(view.$supplierContact, "company.name");
    }

    public EntityType getSupplierContactCompanyType() {
        final String type = null == view ? null : get(view.$supplierContact, "company.type");
        return type == null ? null : EntityType.valueOf(type);
    }

    public String getSupplierContactFullName() {
        return null == view ? null : get(view.$supplierContact, "fullName");
    }

    private String getContactCompanyOrUserEmailAddress(Map<String, Object> side) {
        return getContactCompanyOrUserProperty(side, "emailAddress");
    }

    private String getContactCompanyOrUserPhone(Map<String, Object> side) {
        return getContactCompanyOrUserProperty(side, "phone");
    }

    private String getContactCompanyOrUserProperty(Map<String, Object> side, String property) {
        final String contactCompanyProperty = get(side, "contact.company." + property);
        final String contactUserProperty = get(side, "contact." + property);
        return contactCompanyProperty == null ? contactUserProperty : contactCompanyProperty;
    }

    public String getSupplierCompanyName() {
        return null == view ? null : get(view.supplier, "company.name");
    }

    public String getBuyerCompanyName() {
        return null == view ? null : get(view.buyer, "company.name");
    }

    public String getBuyerContactFullName() {
        return null == view ? null : get(view.buyer, "contact.fullName");
    }

    public String getBuyerContactJobTitle() {
        return null == view ? null : get(view.buyer, "contact.jobTitle");
    }

    public String getBuyerContactCompanyName() {
        return null == view ? null : get(view.buyer, "contact.company.name");
    }

    public String getBuyerContactCompanyLogo() {
        return null == view ? null : get(view.buyer, "contact.company.logo");
    }

    public String getBuyerContactCompanyOrUserEmailAddress() {
        return null == view ? null : getContactCompanyOrUserEmailAddress(view.buyer);
    }

    public String getBuyerContactCompanyOrUserPhone() {
        return null == view ? null : getContactCompanyOrUserPhone(view.buyer);
    }

    public String getTravelDestinationFullAddress() {
        return null == view ? null : get(view.subject, "location.fullAddress");
    }

    public String getTravelDestinationEstimatedRoomNights() {
        return null == view ? null : get(view.subject, "estRoomNights");
    }

    public TravelDestinationType getTravelDestinationType() {
        final String type = null == view ? null : get(view.subject, "type");
        return type == null ? null : TravelDestinationType.valueOf(type);
    }

    public double getDistanceInMiles() {
        final String distance = null == view ? null : get(view.analytics, "distanceMi");
        return distance == null ? Double.MAX_VALUE : Double.parseDouble(distance);
    }

    public boolean isSupplierContactEmpty() {
        final String supplierContactId = getSupplierContactId();
        return supplierContactId == null || supplierContactId.isEmpty();
    }

    public RfpContact getSupplierContact() {
        return new RfpContact() {
            @Override
            public InternetAddress getInternetAddress() throws UnsupportedEncodingException {
                return new InternetAddress(getEmailAddress(), getFullName());
            }

            @Override
            public RfpCompany getCompany() {
                return null;
            }

            @Override
            public String getFirstName() {
                return getSupplierContactFirstName();
            }

            @Override
            public String getLastName() {
                return getSupplierContactLastName();
            }

            @Override
            public String getFullName() {
                return getSupplierContactFullName();
            }

            @Override
            public String getEmailAddress() {
                return getSupplierContactEmailAddress();
            }

            @Override
            public String getPhone() {
                return getSupplierContactPhone();
            }

            @Override
            public ObjectId getId()
            {
                final String supplierContactId = getSupplierContactId();
                return supplierContactId == null ? null : new ObjectId(supplierContactId);
            }

            @Override
            public String getJobTitle() {
                return getSupplierContactJobTitle();
            }

            @Override
            public String getCompanyName() {
                return getSupplierContactCompanyName();
            }

            @Override
            public boolean isUser() {
                return getSupplierContactIsUser();
            }

            @Override
            public ObjectId getCompanyAccountId() {
                final String supplierContactCompanyAccountId = getSupplierContactCompanyAccountId();
                return supplierContactCompanyAccountId == null ? null : new ObjectId(supplierContactCompanyAccountId);
            }

            @Override
            public EntityType getCompanyType() {
                return getSupplierContactCompanyType();
            }
        };
    }

    boolean containsMatchingIdAndState(Id id, HotelRfpBidState state) {
        return id.equals(getId()) && state.matches(getStateStatus(), getStateAt());
    }
}
