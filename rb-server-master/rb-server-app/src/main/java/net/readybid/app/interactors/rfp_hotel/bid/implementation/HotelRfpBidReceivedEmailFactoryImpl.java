package net.readybid.app.interactors.rfp_hotel.bid.implementation;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.entities.core.ActionReport;
import net.readybid.app.entities.email.EmailTemplate;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryViewReader;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidReceivedEmailFactory;
import net.readybid.auth.invitation.Invitation;
import net.readybid.auth.invitation.InvitationService;
import net.readybid.user.BasicUserDetails;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;

@Service
public class HotelRfpBidReceivedEmailFactoryImpl implements HotelRfpBidReceivedEmailFactory {

    private final InvitationService invitationService;

    public HotelRfpBidReceivedEmailFactoryImpl(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @Override
    public List<EmailTemplate> createAll(List<ActionReport<HotelRfpBidQueryView>> actionReports, BasicUserDetails currentUser) {
        final List<EmailTemplate> emails = new ArrayList<>();
        final Map<String, List<HotelRfpBidQueryView>> bidsByChainRepsAndRfps = new HashMap<>();
        final HotelRfpBidQueryViewReader bidReader = new HotelRfpBidQueryViewReader();

        for(ActionReport<HotelRfpBidQueryView> ar : actionReports){
            bidReader.init(ar.tObject);
            if(bidReader.isSentToType(EntityType.HOTEL)){
                emails.add(createBidReceivedForHotelEmail(bidReader, currentUser));
            } else {
                final String chainRepAndRfpId = bidReader.getRfpId() + bidReader.getSupplierContactEmailAddress();
                if(!bidsByChainRepsAndRfps.containsKey(chainRepAndRfpId)){
                    bidsByChainRepsAndRfps.put(chainRepAndRfpId, new ArrayList<>());
                }
                bidsByChainRepsAndRfps.get(chainRepAndRfpId).add(bidReader.getView());
            }
        }

        for(List<HotelRfpBidQueryView> bids : bidsByChainRepsAndRfps.values()){
            emails.add(createBidReceivedForChainEmail(bids, currentUser));
        }

        return emails;
    }

    private EmailTemplate createBidReceivedForHotelEmail(HotelRfpBidQueryViewReader bidReader, BasicUserDetails currentUser) {
        final Date expiryDate = Date.from(bidReader.getProgramEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        final Invitation invitation = invitationService.newInvitation(bidReader.getSupplierContact(), bidReader.getId(), expiryDate, currentUser);
        return new HotelRfpBidReceivedForHotelEmailTemplate(bidReader, invitation.getToken());
    }

    private EmailTemplate createBidReceivedForChainEmail(List<HotelRfpBidQueryView> bids, BasicUserDetails currentUser) {
        final HotelRfpBidQueryViewReader bidReader = new HotelRfpBidQueryViewReader(bids.get(0));
        final Date expiryDate = Date.from(bidReader.getProgramEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        final Invitation invitation = invitationService.newInvitation(bidReader.getSupplierContact(), bidReader.getId(), expiryDate, currentUser);

        return new HotelRfpBidReceivedForChainEmailTemplate(bidReader, invitation.getToken(), bids.size());
    }
}
