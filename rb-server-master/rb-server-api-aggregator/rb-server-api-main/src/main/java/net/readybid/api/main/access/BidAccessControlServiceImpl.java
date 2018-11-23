package net.readybid.api.main.access;

import net.readybid.app.entities.authorization.dirty.InvolvedAccounts;
import net.readybid.app.interactors.authentication.user.gate.CurrentUserProvider;
import net.readybid.app.interactors.rfp_hotel.gateway_dirty.BidRepository;
import net.readybid.auth.invitation.Invitation;
import net.readybid.auth.invitation.InvitationService;
import net.readybid.auth.permission.Permission;
import net.readybid.exceptions.NotAllowedException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by DejanK on 4/6/2017.
 *
 */
@Service
public class BidAccessControlServiceImpl extends AbstractAccessControlService implements BidAccessControlService {

    private final BidRepository bidRepository;
    private final InvitationService invitationService;

    @Autowired
    public BidAccessControlServiceImpl(
            BidRepository bidRepository,
            InvitationService invitationService,
            CurrentUserProvider currentUserProvider
    ) {
        super(currentUserProvider);
        this.bidRepository = bidRepository;
        this.invitationService = invitationService;
    }

    @Override
    public void readAsAny(String bidId) {
        check(bidId, (user, involvedCompanies) -> user.hasPermission(involvedCompanies.buyer, Permission.BUYER_READ) || user.hasPermission(involvedCompanies.supplier, Permission.SUPPLIER_READ));
    }

    @Override
    public void readAsAny(List<String> bidsIds) {
        check(bidsIds, (user, involvedCompanies) -> user.hasPermission(involvedCompanies.supplier, Permission.SUPPLIER_READ) || user.hasPermission(involvedCompanies.buyer, Permission.BUYER_READ));
    }

    @Override
    public void updateAsAny(String bidId) {
        check(bidId, (user, involvedCompanies) -> user.hasPermission(involvedCompanies.buyer, Permission.BUYER_UPDATE) || user.hasPermission(involvedCompanies.supplier, Permission.SUPPLIER_UPDATE));
    }

    @Override
    public void readAsBuyer(String bidId) {
        check(bidId, (user, involvedCompanies) -> user.hasPermission(involvedCompanies.buyer, Permission.BUYER_READ));
    }

    @Override
    public void readAsSupplier(String bidId) {
        check(bidId, (user, involvedCompanies) -> user.hasPermission(involvedCompanies.supplier, Permission.SUPPLIER_READ));
    }

    @Override
    public void updateAsSupplier(String bidId) {
        check(bidId, (user, involvedCompanies) -> user.hasPermission(involvedCompanies.supplier, Permission.SUPPLIER_UPDATE));
    }

    @Override
    public void updateAsSupplier(List<String> bidsIds) {
        check(bidsIds, (user, involvedCompanies) -> user.hasPermission(involvedCompanies.supplier, Permission.SUPPLIER_UPDATE));
    }

    @Override
    public ObjectId previewBidWithToken(String token) {
        final Invitation invitation = invitationService.getInvitation(token);
        if(invitation == null) throw new NotAllowedException();
        return invitation.getTargetId();
    }

    @Override
    public void updateAsBuyer(String bidId) {
        check(bidId, (user, involvedCompanies) -> user.hasPermission(involvedCompanies.buyer, Permission.BUYER_UPDATE));
    }

    @Override
    public void updateAsBuyer(List<String> bidsIds) {
        check(bidsIds, (user, involvedCompanies) -> user.hasPermission(involvedCompanies.buyer, Permission.BUYER_UPDATE));
    }

    @Override
    protected InvolvedAccounts getCompanies(String id) {
        return bidRepository.getInvolvedAccounts(id);
    }

    @Override
    protected List<InvolvedAccounts> getCompanies(List<String> ids) {
        return bidRepository.getInvolvedAccounts(ids);
    }
}
