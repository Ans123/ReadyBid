package net.readybid.app.interactors.rfp_hotel.bid.implementation;

import net.readybid.app.core.entities.traveldestination.TravelDestinationType;
import net.readybid.app.entities.email.EmailTemplate;
import net.readybid.app.interactors.email.EmailTemplateTools;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryViewReader;

import javax.mail.internet.InternetAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class HotelRfpBidReceivedForHotelEmailTemplate implements EmailTemplate {

    private static final String HTML_TEMPLATE = "/email-templates/hotel-rfp-bid-received-for-hotel.html";

    private String invitationToken;
    private LocalDate rfpDueDate;
    private String programYear;

    private String receiverFirstName;
    private String supplierCompanyName;

    private String buyerContactName;
    private String buyerJobTitle;
    private String buyerContactCompanyName;
    private String buyerContactCompanyOrUserEmailAddress;
    private String buyerCompanyName;

    private String travelDestinationAddress;
    private String estimatedRoomNights;
    private final boolean estimatedRoomNightsDisplayed;
    private boolean generalAreaDisplayed;
    private boolean distanceDisplayed;
    private String distance;

    private final InternetAddress receiver;
    private final String emailSubject;

    HotelRfpBidReceivedForHotelEmailTemplate(HotelRfpBidQueryViewReader bidReader, String invitationToken) {
        this.receiver = EmailTemplateTools.createInternetAddress(
                bidReader.getSupplierContactEmailAddress(),
                bidReader.getSupplierContactFullName()
                );
        this.emailSubject = String.format("New RFP Received from \"%s\"", bidReader.getBuyerContactCompanyName());

        this.invitationToken = invitationToken;
        rfpDueDate = bidReader.getDueDate();
        programYear = String.valueOf(bidReader.getProgramYear());

        receiverFirstName = bidReader.getSupplierContactFirstName();
        supplierCompanyName = bidReader.getSupplierCompanyName();

        buyerContactName = bidReader.getBuyerContactFullName();
        buyerJobTitle = bidReader.getBuyerContactJobTitle();
        buyerContactCompanyName = bidReader.getBuyerContactCompanyName();
        buyerContactCompanyOrUserEmailAddress = bidReader.getBuyerContactCompanyOrUserEmailAddress();
        buyerCompanyName = bidReader.getBuyerCompanyName();

        travelDestinationAddress = bidReader.getTravelDestinationFullAddress();
        estimatedRoomNights = bidReader.getTravelDestinationEstimatedRoomNights();
        estimatedRoomNightsDisplayed = !( estimatedRoomNights == null || estimatedRoomNights.isEmpty() );

        final TravelDestinationType travelDestinationType = bidReader.getTravelDestinationType();
        generalAreaDisplayed = TravelDestinationType.CITY.equals(travelDestinationType);
        distanceDisplayed = TravelDestinationType.OFFICE.equals(travelDestinationType);
        distance = String.format("%.2f Miles", bidReader.getDistanceInMiles());
    }

    @Override
    public String getId() {
        return invitationToken;
    }

    @Override
    public InternetAddress getReceiver() {
        return receiver;
    }

    @Override
    public InternetAddress[] getCC() {
        return null;
    }

    @Override
    public String getSubject() {
        return emailSubject;
    }

    @Override
    public String getHtmlTemplateName() {
        return HTML_TEMPLATE;
    }

    @Override
    public Map<String, String> getModel() {
        final Map<String, String> model = new HashMap<>();

        model.put("INVITATION_TOKEN", invitationToken);
        model.put("RFP_DUE_DATE_MEDIUM", DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(rfpDueDate));
        model.put("RFP_DUE_DATE_SHORT", DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(rfpDueDate));
        model.put("PROGRAM_YEAR", programYear);

        model.put("RECEIVER_FIRST_NAME", receiverFirstName);
        model.put("SUPPLIER_COMPANY_NAME", supplierCompanyName);

        model.put("BUYER_FULL_NAME", buyerContactName);
        model.put("BUYER_JOB_TITLE", buyerJobTitle);
        model.put("BUYER_CONTACT_COMPANY_NAME", buyerContactCompanyName);
        model.put("BUYER_COMPANY_NAME", buyerCompanyName);
        model.put("BUYER_EMAIL_ADDRESS", buyerContactCompanyOrUserEmailAddress);

        model.put("TRAVEL_DESTINATION_ADDRESS", travelDestinationAddress);
        model.put("ESTIMATED_ROOM_NIGHTS", estimatedRoomNights);
        model.put("ESTIMATED_ROOM_NIGHTS_DISPLAY_AS", estimatedRoomNightsDisplayed ? "table-row":"none");
        model.put("GENERAL_AREA_DISPLAY_AS", generalAreaDisplayed ? "table-row":"none");
        model.put("DISTANCE_DISPLAY_AS", distanceDisplayed ? "table-row":"none");
        model.put("DISTANCE", distance);

        return Collections.unmodifiableMap(model);
    }
}