package net.readybid.app.interactors.rfp_hotel.gate;

import net.readybid.auth.account.core.Account;
import net.readybid.auth.user.User;

public interface HotelRfpStorageManager {
    void updateUserBasicDetails(User user);

    void updateUserEmailAddress(User user);

    void updateUserProfilePicture(User user);

    void updateBuyerLogo(String accountId, String logo);

    void updateBuyerBasicInformation(Account account);

    void enableChainSupport(String rfpId, String namCoverLetter);
}
