package net.readybid.api.main.bid;

import net.readybid.auth.invitation.Invitation;
import net.readybid.email.Email;
import net.readybid.email.EmailService;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 4/10/2017.
 *
 */
@Service
public class BidEmailServiceImpl implements BidEmailService {

    private final BidEmailFactory bidEmailFactory;
    private final EmailService emailService;

    @Autowired
    public BidEmailServiceImpl(BidEmailFactory bidEmailFactory, EmailService emailService) {
        this.bidEmailFactory = bidEmailFactory;
        this.emailService = emailService;
    }

    @Override
    public void notifyContactOfFinalAgreementReceived(Invitation invitation, HotelRfpBid bid) {
        final Email email = bidEmailFactory.createFinalAgreementReceivedEmail(invitation, bid);
        emailService.send(email);
    }
}
