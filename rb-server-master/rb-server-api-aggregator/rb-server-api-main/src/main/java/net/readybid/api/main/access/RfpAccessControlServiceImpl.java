package net.readybid.api.main.access;

import net.readybid.app.interactors.authentication.user.gate.CurrentUserProvider;
import net.readybid.app.interactors.rfp_hotel.gateway_dirty.RfpRepository;
import net.readybid.app.entities.authorization.dirty.InvolvedAccounts;
import net.readybid.auth.permission.Permission;
import net.readybid.auth.user.AuthenticatedUser;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mongodb.client.model.Filters.in;

/**
 * Created by DejanK on 4/6/2017.
 *
 */
@Service
public class RfpAccessControlServiceImpl extends AbstractAccessControlService implements RfpAccessControlService {

    private final RfpRepository rfpRepository;

    @Autowired
    public RfpAccessControlServiceImpl(RfpRepository rfpRepository, CurrentUserProvider currentUserProvider) {
        super(currentUserProvider);
        this.rfpRepository = rfpRepository;
    }

    @Override
    public void create(ObjectId forAccountId) {
        check((user, nullCompanies) -> user.hasPermission(forAccountId, Permission.BUYER_CREATE));
    }

    @Override
    public void read(String rfpId) {
        check(rfpId, (user, involvedCompanies) -> user.hasPermission(involvedCompanies.buyer, Permission.BUYER_CREATE));
    }

    @Override
    public void update(String rfpId) {
        check(rfpId, (user, involvedCompanies) -> user.hasPermission(involvedCompanies.buyer, Permission.BUYER_UPDATE));
    }

    @Override
    public void delete(String rfpId) {
        check(rfpId, (user, involvedCompanies) -> user.hasPermission(involvedCompanies.buyer, Permission.BUYER_DELETE));
    }

    @Override
    public Bson query() {
        final AuthenticatedUser user = getCurrentUser();
        return in("specifications.buyer.company.accountId", user.getAccountIdsWithPermission(Permission.BUYER_READ));
    }

    @Override
    public Document queryAsDocument() {
        final AuthenticatedUser user = getCurrentUser();
        return new Document("specifications.buyer.company.accountId", new Document("$in", user.getAccountIdsWithPermission(Permission.BUYER_READ)));
    }

    @Override
    protected InvolvedAccounts getCompanies(String id) {
        return rfpRepository.getInvolvedCompanies(id);
    }

    @Override
    protected List<InvolvedAccounts> getCompanies(List<String> ids) {
        return rfpRepository.getInvolvedCompanies(ids);
    }
}