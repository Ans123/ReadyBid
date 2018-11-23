package net.readybid.api.main.bid;

import net.readybid.auth.invitation.Invitation;
import net.readybid.email.AbstractEmailFactory;
import net.readybid.email.Email;
import net.readybid.email.EmailCreationException;
import net.readybid.email.EmailService;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.templates.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by DejanK on 4/10/2017.
 *
 */
@Service
public class BidEmailFactoryImpl extends AbstractEmailFactory implements BidEmailFactory {

    @Autowired
    public BidEmailFactoryImpl(TemplateService templateService, EmailService emailService) {
        super(templateService, emailService);
    }

    @Override
    public Email createFinalAgreementReceivedEmail(Invitation invitation, HotelRfpBid bid) {
        try {
            final Map<String, String> model = emailService.prepareModel();
            createFinalAgreementReceivedEmail(model, bid, invitation);
            return createEmail(invitation,
                    String.format("Final Agreement Received from \"%s\"", bid.getBuyerCompanyName()),
                    model);
        } catch (Exception e){
            throw new EmailCreationException(e);
        }
    }

    private void createFinalAgreementReceivedEmail(Map<String, String> model, HotelRfpBid bid, Invitation invitation) {
        model.put("HEADER_TEXT", "");
        model.put("FIRST_NAME", invitation.getFirstName());
        model.put("MESSAGE", String.format("<p>%s has been ACCEPTED into %s's %d Hotel Program.</p>", bid.getSupplierCompanyName(), bid.getBuyerCompanyName(), bid.getProgramYear()));
        model.put("MESSAGE_2", "");
        model.put("LINK_TEXT", "CLICK TO VIEW FINAL AGREEMENT");
        model.put("LINK_URL", String.format("/hotel-rfp/bids/%s/final-agreement/view?token=%s", bid.getId().toString(), invitation.getToken()));
    }
}
