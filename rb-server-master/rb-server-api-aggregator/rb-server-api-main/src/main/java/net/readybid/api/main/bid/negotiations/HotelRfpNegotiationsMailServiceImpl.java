package net.readybid.api.main.bid.negotiations;

import net.readybid.app.interactors.rfp_hotel.gateway_dirty.BidRepository;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.core.UserAccountRepository;
import net.readybid.bidmanagerview.BidManagerViewSide;
import net.readybid.email.Email;
import net.readybid.email.EmailService;
import net.readybid.rfp.type.RfpType;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiation;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiations;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 8/2/2017.
 */
@Service
public class HotelRfpNegotiationsMailServiceImpl implements HotelRfpNegotiationsMailService {

    private final EmailService emailService;
    private final UserAccountRepository userAccountRepository;
    private final BidRepository bidRepository;
    private final HotelRfpNegotiationsMailFactory hotelRfpNegotiationsMailFactory;

    public HotelRfpNegotiationsMailServiceImpl(
            EmailService emailService,
            UserAccountRepository userAccountRepository,
            BidRepository bidRepository,
            HotelRfpNegotiationsMailFactory hotelRfpNegotiationsMailFactory
    ) {
        this.emailService = emailService;
        this.userAccountRepository = userAccountRepository;
        this.bidRepository = bidRepository;
        this.hotelRfpNegotiationsMailFactory = hotelRfpNegotiationsMailFactory;
    }

    @Override
    public void sendNewNegotiationEmail(HotelRfpNegotiations negotiations, AuthenticatedUser currentUser) {
        final HotelRfpBid bid = bidRepository.getBidById(negotiations.getBidId());
        Email email;

        UserAccount receiver = userAccountRepository.getById(negotiations.getLastCommunication().getFrom().getUserAccountId());
        if(BidManagerViewSide.determineSide(RfpType.HOTEL, receiver.getAccountType()).equals(BidManagerViewSide.SUPPLIER)){
            email = hotelRfpNegotiationsMailFactory.createNewNegotiationEmail(currentUser.getCurrentUserAccount(), receiver, bid);
            emailService.send(email);
        }

        if(!receiver.getId().equals(negotiations.getParties().getSupplier().getUserAccountId())){
            receiver = userAccountRepository.getById(negotiations.getParties().getSupplier().getUserAccountId());
            if(receiver != null && BidManagerViewSide.determineSide(RfpType.HOTEL, receiver.getAccountType()).equals(BidManagerViewSide.SUPPLIER)){
                email = hotelRfpNegotiationsMailFactory.createNewNegotiationEmail(currentUser.getCurrentUserAccount(), receiver, bid);
                emailService.send(email);
            }
        }
    }

    @Override
    public void sendNegotiationFinalizedEmail(HotelRfpNegotiations negotiations, HotelRfpNegotiation finalizedNegotiation, AuthenticatedUser currentUser) {
        final HotelRfpBid bid = bidRepository.getBidById(negotiations.getBidId());
        Email email;
        final HotelRfpNegotiation lastSupplierNegotiation = negotiations.getLastSupplierCommunication();
        UserAccount receiver = null;
        if(lastSupplierNegotiation != null){
            receiver = userAccountRepository.getById(lastSupplierNegotiation.getFrom().getUserAccountId());
            email = hotelRfpNegotiationsMailFactory.createNegotiationFinalizedEmail(currentUser.getCurrentUserAccount(), receiver, bid, negotiations.getFirstCommunication(), finalizedNegotiation);
            emailService.send(email);
        }

        if(receiver == null || !receiver.getId().equals(negotiations.getParties().getSupplier().getUserAccountId())){
            receiver = userAccountRepository.getById(negotiations.getParties().getSupplier().getUserAccountId());
            if(receiver != null && BidManagerViewSide.determineSide(RfpType.HOTEL, receiver.getAccountType()).equals(BidManagerViewSide.SUPPLIER)){
                email = hotelRfpNegotiationsMailFactory.createNegotiationFinalizedEmail(currentUser.getCurrentUserAccount(), receiver, bid, negotiations.getFirstCommunication(), finalizedNegotiation);
                emailService.send(email);
            }
        }
    }
}
