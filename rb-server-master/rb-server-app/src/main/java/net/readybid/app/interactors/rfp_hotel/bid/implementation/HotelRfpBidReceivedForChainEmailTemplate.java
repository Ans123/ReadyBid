package net.readybid.app.interactors.rfp_hotel.bid.implementation;

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

class HotelRfpBidReceivedForChainEmailTemplate implements EmailTemplate {

    private static final String HTML_TEMPLATE = "/email-templates/hotel-rfp-bid-received-for-chain.html";

    private String invitationToken;

    private String rfpName;
    private LocalDate rfpDueDate;
    private String programYear;

    private String receiverFirstName;
    private String supplierContactCompanyName;

    private String buyerContactName;
    private String buyerJobTitle;
    private String buyerContactCompanyName;
    private String buyerContactCompanyLogo;
    private String buyerContactCompanyOrUserEmailAddress;
    private String buyerContactCompanyOrUserPhone;
    private String buyerCompanyName;

    private String hotelsCount;

    private final InternetAddress receiver;
    private final String emailSubject;

    HotelRfpBidReceivedForChainEmailTemplate(HotelRfpBidQueryViewReader bidReader, String invitationToken, int hotelsCount) {
        this.receiver = EmailTemplateTools.createInternetAddress(
                bidReader.getSupplierContactEmailAddress(),
                bidReader.getSupplierContactFullName()
        );
        this.emailSubject =  String.format("New RFP Received from \"%s\"", bidReader.getBuyerContactCompanyName());

        this.invitationToken = invitationToken;
        rfpName = bidReader.getRfpName();
        rfpDueDate = bidReader.getDueDate();
        programYear = String.valueOf(bidReader.getProgramYear());

        receiverFirstName = bidReader.getSupplierContactFirstName();
        supplierContactCompanyName = bidReader.getSupplierContactCompanyName();

        buyerContactName = bidReader.getBuyerContactFullName();
        buyerJobTitle = bidReader.getBuyerContactJobTitle();
        buyerContactCompanyName = bidReader.getBuyerContactCompanyName();
        buyerContactCompanyLogo = bidReader.getBuyerContactCompanyLogo();
        buyerContactCompanyOrUserEmailAddress = bidReader.getBuyerContactCompanyOrUserEmailAddress();
        buyerContactCompanyOrUserPhone = bidReader.getBuyerContactCompanyOrUserPhone();
        buyerCompanyName = bidReader.getBuyerCompanyName();

        this.hotelsCount = String.format("%s nominated hotel%s", String.valueOf(hotelsCount), hotelsCount == 1 ? "" : "s");
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
        model.put("DATE", DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(LocalDate.now()));
        model.put("DISPLAY_LOGO_AS", buyerContactCompanyLogo != null ? "block":"none");
        model.put("BUYER_CONTACT_COMPANY_LOGO", buyerContactCompanyLogo);
        model.put("RECEIVER_FIRST_NAME", receiverFirstName);
        model.put("BUYER_FULL_NAME", buyerContactName);
        model.put("BUYER_CONTACT_COMPANY_NAME", buyerContactCompanyName);
        model.put("SUPPLIER_CONTACT_COMPANY_NAME", supplierContactCompanyName);
        model.put("PROGRAM_YEAR", programYear);
        model.put("BUYER_COMPANY_NAME", buyerCompanyName);
        model.put("RFP_NAME", rfpName);
        model.put("RFP_DUE_DATE_MEDIUM", DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(rfpDueDate));
        model.put("BUYER_JOB_TITLE", buyerJobTitle);
        model.put("BUYER_EMAIL_ADDRESS", buyerContactCompanyOrUserEmailAddress);
        model.put("BUYER_PHONE", buyerContactCompanyOrUserPhone);
        model.put("HOTELS_COUNT", hotelsCount);

        return Collections.unmodifiableMap(model);
    }
}