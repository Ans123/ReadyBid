package net.readybid.api.main.bid.negotiations;

import net.readybid.auth.useraccount.UserAccount;
import net.readybid.email.AbstractEmailFactory;
import net.readybid.email.Email;
import net.readybid.email.EmailCreationException;
import net.readybid.email.EmailService;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiation;
import net.readybid.templates.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;

/**
 * Created by DejanK on 8/2/2017.
 *
 */
@Service
public class HotelRfpNegotiationsEmailFactoryImpl extends AbstractEmailFactory implements HotelRfpNegotiationsMailFactory {

    @Autowired
    public HotelRfpNegotiationsEmailFactoryImpl(TemplateService templateService, EmailService emailService) {
        super(templateService, emailService);
    }

    @Override
    public Email createNewNegotiationEmail(UserAccount sender, UserAccount receiver, HotelRfpBid bid) {
        try {
            final Map<String, String> model = emailService.prepareModel();
            createNewNegotiationEmail(model, sender, receiver, bid);
            return  createEmail(receiver,
                    String.format("New negotiation request from %s", sender.getAccountName()),
                    model);
        } catch (Exception e){
            throw new EmailCreationException(e);
        }
    }

    @Override
    public Email createNegotiationFinalizedEmail(UserAccount sender, UserAccount receiver, HotelRfpBid bid, HotelRfpNegotiation initialNegotiation, HotelRfpNegotiation finalizedNegotiation) {
        try {
            final Map<String, String> model = emailService.prepareModel();
            createNegotiationFinalizedEmail(model, sender, receiver, bid, initialNegotiation, finalizedNegotiation);
            return createEmail(receiver,
                    String.format("Negotiation finalized by %s", sender.getAccountName()),
                    model, TemplateService.SIMPLE_TEMPLATE_WITHOUT_LINK);
        } catch (Exception e){
            throw new EmailCreationException(e);
        }
    }

    private void createNegotiationFinalizedEmail(Map<String, String> model, UserAccount sender, UserAccount receiver, HotelRfpBid bid, HotelRfpNegotiation initialNegotiation, HotelRfpNegotiation finalizedNegotiation)
            throws UnsupportedEncodingException {
        model.put("HEADER_TEXT", "");
        model.put("FIRST_NAME", receiver.getFirstName());
        model.put("MESSAGE",
                String.format("<p>This should conclude our negotiations. We look forward to a long and prosperous future with %s.</p>", receiver.getAccountName())
                        + String.format("<p>Thanks again and</p>")
                        + "<p>Yours truly,</p>"
                        + String.format("<p>%s<br />%s<br />%s<br /><a style=\"color: #16272B !important\" href=\"mailto:%s\" target=\"_blank\">%s</a></p>", sender.getFullName(), sender.getJobTitle(), sender.getAccountName(), sender.getAccountOrUserEmailAddress(), sender.getAccountOrUserEmailAddress())
        );
        model.put("MESSAGE_2", createNegotiationFinalizedTable(initialNegotiation, finalizedNegotiation));
        model.put("LINK_TEXT", "CLICK TO VIEW");
        model.put("LINK_URL", String.format("/bid-manager/%s?negotiations=%s&account=%s", receiver.getDefaultBidManagerView(), String.valueOf(bid.getId()), receiver.getAccountId()));
    }

    private String createNegotiationFinalizedTable(HotelRfpNegotiation initialNegotiation, HotelRfpNegotiation finalizedNegotiation) {
        // todo
        return "";
    }

    private void createNewNegotiationEmail(Map<String, String> model, UserAccount sender, UserAccount receiver, HotelRfpBid bid)
            throws UnsupportedEncodingException {

        model.put("HEADER_TEXT", "");
        model.put("FIRST_NAME", receiver.getFirstName());
        model.put("MESSAGE",
                String.format("<p>Please find our negotiation request relevant to %s. I sincerely hope that you can respond within reasonable time frame, no later than %s.</p>", bid.getRfpName(), DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(LocalDate.now().plusWeeks(1)))
        );
        model.put("MESSAGE_2",
                String.format(java.util.Locale.US, "<p>If you have any questions you can e mail me at <a style=\"color: #16272B !important\" href=\"mailto:%s\" target=\"_blank\">%s</a> or sign up for ReadyBid \"BidChat\", a feature which allows us to Chat Live on my Bid Manager.</p>", sender.getAccountOrUserEmailAddress(), sender.getAccountOrUserEmailAddress())
                        + "<p>Thanks so much</p>"
                        + String.format("<p>%s<br />%s<br />%s<br /><a style=\"color: #16272B !important\" href=\"mailto:%s\" target=\"_blank\">%s</a></p>", sender.getFullName(), sender.getJobTitle(), sender.getAccountName(), sender.getAccountOrUserEmailAddress(), sender.getAccountOrUserEmailAddress())
        );
        model.put("LINK_TEXT", "CLICK TO VIEW");
        model.put("LINK_URL", String.format("/bid-manager/%s?negotiations=%s&account=%s", receiver.getDefaultBidManagerView(), String.valueOf(bid.getId()), receiver.getAccountId()));
    }
}
