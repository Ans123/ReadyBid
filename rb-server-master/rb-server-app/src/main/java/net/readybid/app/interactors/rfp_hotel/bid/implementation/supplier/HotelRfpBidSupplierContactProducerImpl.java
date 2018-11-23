package net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier;

import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.app.interactors.rfp_hotel.CreateHotelRfpSupplierContactRequest;
import net.readybid.app.interactors.rfp_hotel.HotelRfpSupplierContactFactory;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidSupplierContactProducer;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidSupplierContactLoader;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.account.core.AccountService;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.web.UserAccountService;
import net.readybid.user.BasicUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelRfpBidSupplierContactProducerImpl implements HotelRfpBidSupplierContactProducer {

    private final HotelRfpSupplierContactFactory supplierContactFactory;
    private final HotelRfpBidSupplierContactLoader loader;
    private final AccountService accountService;
    private final UserAccountService userAccountService;

    @Autowired
    public HotelRfpBidSupplierContactProducerImpl(
            HotelRfpSupplierContactFactory supplierContactFactory,
            HotelRfpBidSupplierContactLoader loader,
            AccountService accountService,
            UserAccountService userAccountService
    ) {
        this.supplierContactFactory = supplierContactFactory;
        this.loader = loader;
        this.accountService = accountService;
        this.userAccountService = userAccountService;
    }

    @Override
    public HotelRfpSupplierContact create(List<String> bidsIds, String userAccountId) {
        final UserAccount userAccount = userAccountService.getById(userAccountId);

        if(userAccount != null){
            final String entityId = loader.getEntityId(userAccount.getAccountType(), bidsIds);

            if(entityId != null){
                return isUserAccountMatchingBid(userAccount, entityId)
                        ? supplierContactFactory.create(userAccount) : null;
            }
        }

        return null;
    }

    private boolean isUserAccountMatchingBid(UserAccount userAccount, String entityId) {
        return entityId.equals(String.valueOf(userAccount.getEntityId()));
    }

    @Override
    public HotelRfpSupplierContact create(
            List<String> bidsIds,
            CreateHotelRfpSupplierContactRequest request,
            BasicUserDetails currentUser
    ) {
        Account account = null;

        final String entityId = loader.getEntityId(request.type, bidsIds);
        if(entityId != null) account = accountService.getOrCreateAccount(request.type, entityId, currentUser);
        return account == null ? null : supplierContactFactory.create(request, account);
    }
}
